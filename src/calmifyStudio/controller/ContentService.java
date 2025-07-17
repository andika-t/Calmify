package calmifyStudio.controller;

import calmifyStudio.model.Content;
import calmifyStudio.model.XmlContentInterface;

import java.util.List;
import java.util.Optional;
public class ContentService {

    private final XmlContentInterface contentDao;

    public ContentService(XmlContentInterface contentDao) {
        this.contentDao = contentDao;
    }

    public boolean addContent(Content content) {
        if (content.getTitle() == null || content.getTitle().trim().isEmpty()) {
            System.err.println("Judul konten tidak boleh kosong.");
            return false;
        }
        return contentDao.addContent(content);
    }

    public boolean updateContent(Content content) {
        if (content.getId() == null || content.getId().trim().isEmpty()) {
            System.err.println("ID konten tidak boleh kosong untuk pembaruan.");
            return false;
        }
        if (content.getTitle() == null || content.getTitle().trim().isEmpty()) {
            System.err.println("Judul konten tidak boleh kosong.");
            return false;
        }
        return contentDao.updateContent(content);
    }

    public boolean deleteContent(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.err.println("ID konten tidak boleh kosong untuk penghapusan.");
            return false;
        }
        return contentDao.deleteContent(id);
    }

    public List<Content> getAllContents() {
        return contentDao.getAllContents();
    }

    public Optional<Content> getContentById(String id) {
        return contentDao.getContentById(id);
    }
}
