package fr.epita.tf_idf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {

    final Map<String, List<String>> keywordToDocumentMap = new HashMap<String, List<String>>();

    public void index(ArrayList<Token> tokenVector, String document) {
        for (Token token: tokenVector) {

            List<String> documents;

            if (!keywordToDocumentMap.containsKey(token.word)){
                documents = new ArrayList<>();
                keywordToDocumentMap.put(token.word, documents);
            }
            else{
                documents = keywordToDocumentMap.get(token.word);
            }
            documents.add(document);
        }
        return;
    }

    public ArrayList<String> query(String queryString) {
        return null;
    }
}
