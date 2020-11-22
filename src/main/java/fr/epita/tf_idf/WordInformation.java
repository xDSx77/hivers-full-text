package fr.epita.tf_idf;

public class WordInformation {

    final private String word;
    private String newWord;
    private String regionSecond = "";
    private String regionFirst = "";
    private String region = "";


    public WordInformation(String word) {
        assert word != null;
        this.word = word.toLowerCase();
        this.newWord = word;
    }

    public String getRegionSecond() {
        return regionSecond;
    }

    public void setRegionSecond(String regionSecond) {
        this.regionSecond = regionSecond;
    }

    public String getRegionFirst() {
        return regionFirst;
    }

    public void setRegionFirst(String regionFirst) {
        this.regionFirst = regionFirst;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }

    public String getWord() {
        return this.word ;
    }


}
