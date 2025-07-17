package pantauStres.model;

public class Question {
    private int id;
    private String pertanyaan;
    private int skor;

    public Question(int id, String pertanyaan, int skor) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.skor = skor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public int getSkor() {
        return skor;
    }

    public void setSkor(int skor) {
        this.skor = skor;
    }

}
