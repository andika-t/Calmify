package calmifyStudio.model;

import java.util.List;
import java.util.Optional;

public interface XmlContentInterface {

    boolean addContent(Content content);

    boolean updateContent(Content content);

    boolean deleteContent(String id);

    List<Content> getAllContents();

    Optional<Content> getContentById(String id);
}
