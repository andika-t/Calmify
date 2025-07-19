package pantauStres.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import pantauStres.model.Question;
import pantauStres.model.QuestionList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionModel {
    private static final String FILE_PATH = "data/questions.xml";

    // Metode ini membaca file setiap kali dipanggil, sesuai pola ModelXML.
    public ArrayList<Question> getQuestions() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(QuestionList.class);
        xstream.alias("question", Question.class);
        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[]{QuestionList.class, Question.class});

        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                QuestionList list = (QuestionList) xstream.fromXML(reader);
                if (list != null) {
                    return list.getListPertanyaan();
                }
            } catch (Exception e) {
                System.err.println("Gagal memuat data pertanyaan: " + e.getMessage());
            }
        }
        return new ArrayList<>(); // Kembalikan list kosong jika file tidak ada atau error
    }
    
    // Metode ini membaca data, mengubahnya, lalu menulis ulang semuanya ke file.
    public void addQuestion(Question question) {
        ArrayList<Question> currentQuestions = getQuestions();
        currentQuestions.add(question);
        
        QuestionList questionList = new QuestionList();
        questionList.setListPertanyaan(currentQuestions);
        processXML(questionList);
    }

    public void updateQuestion(int index, Question question) {
        ArrayList<Question> currentQuestions = getQuestions();
        if (index >= 0 && index < currentQuestions.size()) {
            currentQuestions.set(index, question);
            
            QuestionList questionList = new QuestionList();
            questionList.setListPertanyaan(currentQuestions);
            processXML(questionList);
        }
    }

    public void deleteQuestion(int id) {
        ArrayList<Question> currentQuestions = getQuestions();
        currentQuestions.removeIf(q -> q.getId() == id);
        
        QuestionList questionList = new QuestionList();
        questionList.setListPertanyaan(currentQuestions);
        processXML(questionList);
    }
    
    // Metode privat untuk menulis ke file, sama seperti di ModelXML.
    private void processXML(QuestionList list) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(QuestionList.class);
        xstream.alias("question", Question.class);

        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // Buat folder 'data' jika belum ada
            try (FileOutputStream fos = new FileOutputStream(file)) {
                xstream.toXML(list, fos);
            }
        } catch (IOException e) {
            System.err.println("Perhatian: Gagal menulis data pertanyaan. " + e.getMessage());
        }
    }
}