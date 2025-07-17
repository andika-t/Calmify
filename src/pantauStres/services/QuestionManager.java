package pantauStres.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import pantauStres.model.Question;
import pantauStres.model.QuestionXML;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionManager {

    private static final String FILENAME = "src/data/questions.xml";
    private ArrayList<Question> data;
    private QuestionXML questionBank;
    private XStream xstream;

    public QuestionManager() {
        xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(QuestionXML.class);
        xstream.alias("question", Question.class);

        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[]{QuestionXML.class, Question.class});

        questionBank = new QuestionXML();
        this.data = loadQuestionsFromFile(); // Muat data saat inisialisasi
    }

    private ArrayList<Question> loadQuestionsFromFile() {
        try (FileReader reader = new FileReader(FILENAME)) {
            QuestionXML bank = (QuestionXML) xstream.fromXML(reader);
            if (bank != null && bank.getQuestions() != null) {
                return bank.getQuestions();
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file " + FILENAME + ": " + e.getMessage() + ". Memulai dengan list kosong.");
        }
        return new ArrayList<>();
    }

    // Mengembalikan data yang sudah ada di memori
    public ArrayList<Question> getQuestions() {
        return data;
    }

    public void tambahData(Question question) {
        data.add(question);
        saveDataToFile(); // Simpan perubahan ke XML
    }

    public void hapusData(int id) {
        // Operasi dilakukan pada list 'data' milik kelas
        data.removeIf(q -> q.getId() == id);
        saveDataToFile(); // Simpan perubahan ke XML
    }

    public void editData(int index, Question questionEdit) {
        if (index >= 0 && index < data.size()) {
            data.set(index, questionEdit);
            saveDataToFile(); // Simpan perubahan ke XML
        }
    }

    private void saveDataToFile() {
        questionBank.setQuestions(data);
        try (FileOutputStream fos = new FileOutputStream(FILENAME)) {
            xstream.toXML(questionBank, fos);
        } catch (IOException e) {
            System.err.println("Gagal menulis ke file " + FILENAME + ": " + e.getMessage());
        }
    }
}