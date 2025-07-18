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
    private ArrayList<Question> data = this.getQuestions();
    private QuestionXML questionBank = new QuestionXML();

    public ArrayList<Question> getQuestions() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(QuestionXML.class);
        xstream.alias("question", Question.class);

        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[] { QuestionXML.class, Question.class });

        ArrayList<Question> data = new ArrayList<>();

        try (FileReader reader = new FileReader(FILENAME)) {
            QuestionXML myData = (QuestionXML) xstream.fromXML(reader);
            for (int i = 0; i < myData.getQuestions().size(); i++) {
                Question question = myData.getQuestions().get(i);
                data.add(new Question(question.getId(), question.getPertanyaan(), question.getSkor()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    

    public void tambahData(Question question) {
        data.add(question);
        questionBank.setQuestions(data);
        processXML(questionBank);
    }

    public void hapusData(int id) {
        ArrayList<Question> data = this.getQuestions();
        data.removeIf(question -> question.getId() == id);
        questionBank.setQuestions(data);
        processXML(questionBank);
    }

    public void editData(int index, Question questionEdit) {
        data.set(index, questionEdit);
        questionBank.setQuestions(data);
        processXML(questionBank);
    }

    private void processXML(QuestionXML question) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(QuestionXML.class);
        xstream.alias("question", Question.class);

        try (FileOutputStream fos = new FileOutputStream(FILENAME)) {
            xstream.toXML(question, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}