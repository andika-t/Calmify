package pantauStres.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import pantauStres.model.Answer;
import pantauStres.model.AnswerList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AnswerModel {
    private static final String FILE_PATH = "data/answers.xml";

    public ArrayList<Answer> getAnswers() {
    XStream xstream = new XStream(new StaxDriver());
    xstream.processAnnotations(AnswerList.class);
    xstream.alias("answer", Answer.class);
    xstream.addPermission(NoTypePermission.NONE);
    xstream.allowTypes(new Class[]{AnswerList.class, Answer.class});
    
    File file = new File(FILE_PATH);
    if (file.exists()) {
        try (FileReader reader = new FileReader(file)) {
            AnswerList list = (AnswerList) xstream.fromXML(reader);
            if (list != null && list.getListJawaban() != null) {
                // Jika berhasil, kembalikan list dari file
                return list.getListJawaban();
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data jawaban: " + e.getMessage());
        }
    }
    return new ArrayList<>();
}

    public void addAnswer(Answer answer) {
        ArrayList<Answer> currentAnswers = getAnswers();
        currentAnswers.add(answer);
        
        AnswerList answerList = new AnswerList();
        answerList.setListJawaban(currentAnswers);
        processXML(answerList);
    }

    public void deleteAnswer(String id) {
        ArrayList<Answer> currentAnswers = getAnswers();
        currentAnswers.removeIf(a -> a.getId().equals(id));
        
        AnswerList answerList = new AnswerList();
        answerList.setListJawaban(currentAnswers);
        processXML(answerList);
    }

    public void deleteAnswersByUsername(String username) {
        ArrayList<Answer> currentAnswers = getAnswers();
        boolean removed = currentAnswers.removeIf(answer -> username.equals(answer.getUsername()));
        
        if (removed) {
            System.out.println("Menghapus data asesmen untuk pengguna: " + username);
            AnswerList answerList = new AnswerList();
            answerList.setListJawaban(currentAnswers);
            processXML(answerList);
        } else {
            System.out.println("Tidak ada data asesmen yang ditemukan untuk pengguna: " + username);
        }
    }

    private void processXML(AnswerList list) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(AnswerList.class);
        xstream.alias("answer", Answer.class);
        
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                xstream.toXML(list, fos);
            }
        } catch (IOException e) {
            System.err.println("Perhatian: Gagal menulis data jawaban. " + e.getMessage());
        }
    }
}