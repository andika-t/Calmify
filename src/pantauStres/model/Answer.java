package pantauStres.model;

public class Answer {
    private String username;
    private String id;
    private String waktuTes;
    private String interpretasi;
    private boolean shareData;
    private int skorMood;
    private int skorKualitas;

    public Answer() {
    }

    public Answer(String id, String username, String waktuTes, String interpretasi, boolean shareData, int skorMood, int skorKualitas) {
        this.id = id;
        this.username = username;
        this.waktuTes = waktuTes;
        this.interpretasi = interpretasi;
        this.shareData = shareData;
        this.skorMood = skorMood;
        this.skorKualitas = skorKualitas;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaktuTes() {
        return waktuTes;
    }

    public void setWaktuTes(String waktuTes) {
        this.waktuTes = waktuTes;
    }

    public String getInterpretasi() {
        return interpretasi;
    }

    public void setInterpretasi(String interpretasi) {
        this.interpretasi = interpretasi;
    }

    public boolean isShareData() {
        return shareData;
    }

    public void setShareData(boolean shareData) {
        this.shareData = shareData;
    }

    public int getSkorMood() {
        return skorMood;
    }

    public void setSkorMood(int skorMood) {
        this.skorMood = skorMood;
    }

    public int getSkorKualitas() {
        return skorKualitas;
    }

    public void setSkorKualitas(int skorKualitas) {
        this.skorKualitas = skorKualitas;
    }

}
