package fr.epita.tf_idf;
import java.util.*;
import java.util.stream.Collectors;

public class Indexer {

    final Map<String, List<Pair<String, List<Token>>>> keywordToDocumentMap = new HashMap<>();

    public void index(ArrayList<Token> tokenVector, String document) {
        for (Token token: tokenVector) {
            List<Pair<String, List<Token>>> documents;
            if (!keywordToDocumentMap.containsKey(token.word)){
                documents = new ArrayList<>();
                keywordToDocumentMap.put(token.word, documents);
            }
            else
                documents = keywordToDocumentMap.get(token.word);
            documents.add(new Pair<>(document, tokenVector));
        }
    }

    private double computeIDF(int corpusSize, int matchingDocs) {
        return Math.log((double)corpusSize / (1 + (double)matchingDocs));
    }


    private ArrayList<Token> vectorize(String queryString) {
        TokenizationLayer tokenizationLayer = new TokenizationLayer();
        List<String> tokenList = tokenizationLayer.tokenize(queryString);

        VectorizationLayer vectorConversionLayer = new VectorizationLayer();
        return vectorConversionLayer.vectorize(tokenList);
    }

    private double computeSimilarity(List<Double> v1, List<Double> v2) {
        double dotProduct = 0;
        double normv1 = 0;
        double normv2 = 0;
        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normv1 += v1.get(i) * v1.get(i);
            normv2 += v2.get(i) * v2.get(i);
        }
        normv1 = Math.sqrt(normv1);
        normv2 = Math.sqrt(normv2);

        return Math.acos(dotProduct / (normv1 * normv2));
    }

    private List<Double> TFIDFVector(List<Token> queryTokens, List<Token> documentTokens, int corpusSize) {
        List<Double> vector = new ArrayList<>();
        double norm = 0;
        for (Token token : queryTokens) {
            float frequency = documentTokens
                    .stream()
                    .filter(x -> x.word.equals(token.word))
                    .findFirst()
                    .map(value -> value.frequency)
                    .orElse(0F);
            double TFIDF = frequency * computeIDF(corpusSize, keywordToDocumentMap.get(token.word).size());
            vector.add(TFIDF);
            norm += TFIDF * TFIDF;
        }
        norm = Math.sqrt(norm);
        for (int i = 0; i < vector.size(); i++)
            vector.set(i, vector.get(i) / norm);
        return vector;
    }

    public List<String> query(String queryString) {
        Set<Pair<String, List<Token>>> documents = new HashSet<>();
        ArrayList<Token> queryTokens = vectorize(queryString);
        for (Token queryToken : queryTokens) {
            List<Pair<String, List<Token>>> documentsResult = keywordToDocumentMap.get(queryToken.word);
            if (documentsResult != null)
                documents.addAll(documentsResult);
        }

        List<Double> queryTFIDF = TFIDFVector(queryTokens, queryTokens,documents.size());

        List<Pair<String, Double>> sortedDocuments = new ArrayList<>();
        for (Pair<String, List<Token>> document : documents) {
            List<Double> documentVector = TFIDFVector(queryTokens, document.item2,documents.size());

            double cosineSimilarity = computeSimilarity(documentVector, queryTFIDF);
            sortedDocuments.add(new Pair<>(document.item1, cosineSimilarity));
        }

        sortedDocuments.sort((x, y) -> (int)(y.item2 - x.item2));
        return sortedDocuments.stream().map(x -> x.item1).collect(Collectors.toList());
    }
}