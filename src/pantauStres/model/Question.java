package pantauStres.model;

public class Question {
    private int id;
    private String pertanyaan;
    private int skor;
    private String kategori;

    public Question(int id, String pertanyaan, int skor, String kategori) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.skor = skor;
        this.kategori = kategori;
    }

    public String getKategori(){
        return kategori;
    }

    public void setKategori(String kategori){
        this.kategori = kategori;
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
