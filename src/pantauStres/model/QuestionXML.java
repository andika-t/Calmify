package pantauStres.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("questions")
public class QuestionXML{
    
    @XStreamImplicit
    private ArrayList<Question> questions = new ArrayList<>();

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}