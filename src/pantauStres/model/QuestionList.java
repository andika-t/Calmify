package pantauStres.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;

@XStreamAlias("questions")
public class QuestionList {

    @XStreamImplicit
    private ArrayList<Question> listPertanyaan = new ArrayList<>();

    public ArrayList<Question> getListPertanyaan() {
        return listPertanyaan;
    }

    public void setListPertanyaan(ArrayList<Question> listPertanyaan) {
        this.listPertanyaan = listPertanyaan;
    }
}