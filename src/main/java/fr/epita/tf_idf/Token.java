package fr.epita.tf_idf;

import java.util.ArrayList;
import java.util.List;

public class Token {
    final String word;
    float frequency;
    final List<Integer> indexes;

    public Token(String word, Float frequency, List<Integer> indexes) {
        this.word = word;
        this.frequency = frequency;
        this.indexes = indexes;
    }

    public Token(String word, Float frequency, int index) {
        this.word = word;
        this.frequency = frequency;
        indexes = new ArrayList<>();
        indexes.add(index);
    }

    public void addIndex(int index) {
        indexes.add(index);
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public int getCount() {
        return indexes.size();
    }
}