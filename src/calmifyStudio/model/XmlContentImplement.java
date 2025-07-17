package calmifyStudio.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class XmlContentImplement implements XmlContentInterface {

    private final String filePath;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;

    public XmlContentImplement(String filePath) {
        this.filePath = filePath;
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            initializeXmlFile();
        } catch (ParserConfigurationException e) {
            System.err.println("Kesalahan konfigurasi parser XML: " + e.getMessage());
        }
    }

    private void initializeXmlFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("contents");
                doc.appendChild(rootElement);
                saveXmlFile(doc);
            } catch (TransformerException e) {
                System.err.println("Kesalahan saat menginisialisasi file XML: " + e.getMessage());
            }
        }
    }

    private Document loadXmlFile() throws IOException, org.xml.sax.SAXException {
        File file = new File(filePath);
        if (!file.exists()) {
            initializeXmlFile(); 
        }
        return dBuilder.parse(file);
    }

    private void saveXmlFile(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Indentasi untuk keterbacaan
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    @Override
    public boolean addContent(Content content) {
        try {
            Document doc = loadXmlFile();
            Element rootElement = doc.getDocumentElement();

            if (content.getId() == null || content.getId().isEmpty()) {
                content.setId(UUID.randomUUID().toString());
            }

            Element contentElement = doc.createElement("content");
            contentElement.setAttribute("id", content.getId());

            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode(content.getTitle()));
            contentElement.appendChild(title);

            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(content.getDescription()));
            contentElement.appendChild(description);

            Element contentLink = doc.createElement("contentLink");
            contentLink.appendChild(doc.createTextNode(content.getContentLink()));
            contentElement.appendChild(contentLink);

            rootElement.appendChild(contentElement);
            saveXmlFile(doc);
            return true;
        } catch (IOException | org.xml.sax.SAXException | TransformerException e) {
            System.err.println("Kesalahan saat menambahkan konten: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateContent(Content content) {
        try {
            Document doc = loadXmlFile();
            NodeList nodeList = doc.getElementsByTagName("content");
            boolean found = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getAttribute("id").equals(content.getId())) {
                        element.getElementsByTagName("title").item(0).setTextContent(content.getTitle());
                        element.getElementsByTagName("description").item(0).setTextContent(content.getDescription());
                        element.getElementsByTagName("contentLink").item(0).setTextContent(content.getContentLink());
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                saveXmlFile(doc);
                return true;
            }
            return false;
        } catch (IOException | org.xml.sax.SAXException | TransformerException e) {
            System.err.println("Kesalahan saat memperbarui konten: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteContent(String id) {
        try {
            Document doc = loadXmlFile();
            NodeList nodeList = doc.getElementsByTagName("content");
            boolean found = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getAttribute("id").equals(id)) {
                        element.getParentNode().removeChild(element);
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                saveXmlFile(doc);
                return true;
            }
            return false;
        } catch (IOException | org.xml.sax.SAXException | TransformerException e) {
            System.err.println("Kesalahan saat menghapus konten: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Content> getAllContents() {
        List<Content> contents = new ArrayList<>();
        try {
            Document doc = loadXmlFile();
            NodeList nodeList = doc.getElementsByTagName("content");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String contentLink = element.getElementsByTagName("contentLink").item(0).getTextContent();
                    contents.add(new Content(id, title, description, contentLink));
                }
            }
        } catch (IOException | org.xml.sax.SAXException e) {
            System.err.println("Kesalahan saat mengambil semua konten: " + e.getMessage());
        }
        return contents;
    }

    @Override
    public Optional<Content> getContentById(String id) {
        try {
            Document doc = loadXmlFile();
            NodeList nodeList = doc.getElementsByTagName("content");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getAttribute("id").equals(id)) {
                        String title = element.getElementsByTagName("title").item(0).getTextContent();
                        String description = element.getElementsByTagName("description").item(0).getTextContent();
                        String contentLink = element.getElementsByTagName("contentLink").item(0).getTextContent();
                        return Optional.of(new Content(id, title, description, contentLink));
                    }
                }
            }
        } catch (IOException | org.xml.sax.SAXException e) {
            System.err.println("Kesalahan saat mengambil konten berdasarkan ID: " + e.getMessage());
        }
        return Optional.empty();
    }
}
