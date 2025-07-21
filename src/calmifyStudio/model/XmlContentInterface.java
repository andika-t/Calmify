package calmifyStudio.model;

import java.util.Optional;

import calmifyStudio.util.ArrayList;

public interface XmlContentInterface {

    boolean addContent(Content content);

    boolean updateContent(Content content);

    boolean deleteContent(String id);

    ArrayList<Content> getAllContents(); 

    Optional<Content> getContentById(String id);
}
