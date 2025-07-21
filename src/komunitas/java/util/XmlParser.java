package komunitas.java.util;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class XmlParser<T> {
    private final Class<T> type;
    
    public XmlParser(Class<T> type) {
        this.type = type;
    }

    public void marshal(List<T> data, String filePath, String rootElementName, String itemElementName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement(rootElementName);
            doc.appendChild(rootElement);

            for (T item : data) {
                Element itemElement = doc.createElement(itemElementName);
                
                // Serialize each field
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Element fieldElement = doc.createElement(field.getName());
                    Object value = field.get(item);
                    fieldElement.setTextContent(value != null ? value.toString() : "");
                    itemElement.appendChild(fieldElement);
                }
                
                rootElement.appendChild(itemElement);
            }

            // Write to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException("Error marshalling XML", e);
        }
    }

    public List<T> unmarshal(String filePath, String itemElementName) {
        List<T> result = new ArrayList<>();
        
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return result;
            }
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName(itemElementName);
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    result.add(parseItem((Element) node));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error unmarshalling XML", e);
        }
        
        return result;
    }
    
    private T parseItem(Element element) throws Exception {
        // Try to create instance using no-arg constructor first
        T instance;
        try {
            Constructor<T> noArgConstructor = type.getDeclaredConstructor();
            noArgConstructor.setAccessible(true);
            instance = noArgConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            // If no-arg constructor doesn't exist, try to find any constructor
            Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();
            if (constructors.length == 0) {
                throw new RuntimeException("No constructors found for " + type.getName());
            }
            
            // Use first constructor and provide default values
            Constructor<T> constructor = constructors[0];
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == boolean.class) {
                    params[i] = false;
                } else if (paramTypes[i] == int.class) {
                    params[i] = 0;
                } else {
                    params[i] = null;
                }
            }
            
            instance = constructor.newInstance(params);
        }
        
        // Set all fields from XML
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            NodeList fieldNodes = element.getElementsByTagName(field.getName());
            if (fieldNodes.getLength() > 0) {
                String value = fieldNodes.item(0).getTextContent();
                
                try {
                    if (field.getType() == String.class) {
                        field.set(instance, value);
                    } else if (field.getType() == LocalDate.class) {
                        if (!value.isEmpty()) {
                            field.set(instance, LocalDate.parse(value));
                        }
                    } else if (field.getType() == LocalDateTime.class) {
                        // Fix untuk LocalDateTime - cek empty dan handle dengan benar
                        if (!value.isEmpty() && !value.equals("null")) {
                            try {
                                field.set(instance, LocalDateTime.parse(value));
                            } catch (Exception dateError) {
                                // Jika parsing gagal, set ke current time
                                System.err.println("Warning: Failed to parse LocalDateTime '" + value + "', using current time");
                                field.set(instance, LocalDateTime.now());
                            }
                        } else {
                            // Jika kosong, set ke current time
                            field.set(instance, LocalDateTime.now());
                        }
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        field.set(instance, Boolean.parseBoolean(value));
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        if (!value.isEmpty()) {
                            field.set(instance, Integer.parseInt(value));
                        }
                    }
                } catch (Exception fieldError) {
                    System.err.println("Warning: Failed to set field " + field.getName() + " with value '" + value + "': " + fieldError.getMessage());
                    // Continue dengan field lain jika ada error
                }
            }
        }
        
        return instance;
    }
}