package pantauStres.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;

@XStreamAlias("answers")
public class AnswerList {

    @XStreamImplicit
    private ArrayList<Answer> listJawaban = new ArrayList<>();

    public ArrayList<Answer> getListJawaban() {
        return listJawaban;
    }

    public void setListJawaban(ArrayList<Answer> listJawaban) {
        this.listJawaban = listJawaban;
    }
}