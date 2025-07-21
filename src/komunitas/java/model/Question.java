package komunitas.java.model;

public class Question {
    private String id;
    private String text;
    private int weight;
    private String category;
    
    public Question(String id, String text, int weight, String category) {
        this.id = id;
        this.text = text;
        this.weight = weight;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}