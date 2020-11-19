package fr.epita.tf_idf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VectorizationLayer {
    public ArrayList<Token> vectorize(List<String> tokenListFromRawHTML) {
        ArrayList<Token> vector = new ArrayList<>();
        for (int i = 0; i < tokenListFromRawHTML.size(); i++) {
            String word = tokenListFromRawHTML.get(i);
            Optional<Token> token = vector.stream().filter(x -> x.word.equals(word)).findFirst();
            if (token.isEmpty())
                vector.add(new Token(word, 1f / tokenListFromRawHTML.size(), i));
            else {
                token.get().addIndex(i);
                token.get().setFrequency(((float) token.get().getCount()) / tokenListFromRawHTML.size());
            }
        }
        return vector;
    }
}