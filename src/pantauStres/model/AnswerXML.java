package pantauStres.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("answer")
public class AnswerXML {

    @XStreamImplicit
    private ArrayList<Answer> results = new ArrayList<>();

    public ArrayList<Answer> getAnswers() {
        return results;
    }

    public void setAnswers(ArrayList<Answer> results) {
        this.results = results;
    }
}

