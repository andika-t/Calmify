package pantauStres.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import pantauStres.model.Answer;
import pantauStres.model.AnswerXML;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AnswerManager {

    private static final String FILENAME = "src/data/answers.xml";
    private ArrayList<Answer> data; // Daftar jawaban yang dikelola di memori
    private AnswerXML resultHistory;
    private XStream xstream;

    public AnswerManager() {
        // Setup XStream sekali di konstruktor
        xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(AnswerXML.class);
        xstream.processAnnotations(Answer.class);

        xstream.alias("answers",AnswerXML.class);
        xstream.alias("answer",Answer.class);
        
        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[]{AnswerXML.class, Answer.class});

        resultHistory = new AnswerXML();
        this.data = loadAnswersFromFile(); // Muat data saat inisialisasi
    }

    private ArrayList<Answer> loadAnswersFromFile() {
        try (FileReader reader = new FileReader(FILENAME)) {
            AnswerXML bank = (AnswerXML) xstream.fromXML(reader);
            if (bank != null && bank.getAnswers() != null) {
                return bank.getAnswers();
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file " + FILENAME + ": " + e.getMessage() + ". Memulai dengan list kosong.");
        }
        return new ArrayList<>();
    }

    // Mengembalikan data yang sudah ada di memori
    public ArrayList<Answer> getAnswers() {
        return data;
    }

    public void tambahData(Answer answer) {
        data.add(answer);
        saveDataToFile(); // Simpan perubahan
    }

    public void hapusData(String id) {
        // PERBAIKAN: Operasi dilakukan pada list 'data' milik kelas
        data.removeIf(a -> a.getId().equals(id));
        saveDataToFile(); // Simpan perubahan
    }

    public void editData(int index, Answer answer) {
        if (index >= 0 && index < data.size()) {
            data.set(index, answer);
            saveDataToFile(); // Simpan perubahan
        }
    }

    private void saveDataToFile() {
        resultHistory.setAnswers(data);
        try (FileOutputStream fos = new FileOutputStream(FILENAME)) {
            xstream.toXML(resultHistory, fos);
        } catch (IOException e) {
            System.err.println("Gagal menulis ke file " + FILENAME + ": " + e.getMessage());
        }
    }
}