package pantauStres.model;

public enum Score {
    RENDAH("Rendah"),
    SEDANG("Sedang"),
    TINGGI("Tinggi");

    private final String tingkatTampilan;

    Score(String tingkatTampilan) {
        this.tingkatTampilan = tingkatTampilan;
    }

    public String getDisplayValue() {
        return tingkatTampilan;
    }

    public static Score fromString(String teks) {
        for (Score tingkat : Score.values()) {
            if (tingkat.tingkatTampilan.equalsIgnoreCase(teks)) {
                return tingkat;
            }
        }
        throw new IllegalArgumentException("Tingkatan tidak ada");
    }
}
