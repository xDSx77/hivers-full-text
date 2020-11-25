package fr.epita.tf_idf;
import javax.print.Doc;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Indexer {

    public class Document
    {
        public final String text;
        public final List<Token> tokenList;

        public Document(String text, List<Token> tokenList) {
            this.text = text;
            this.tokenList = tokenList;
        }
    }

    final Map<String, List<Document>> keywordToDocumentMap = new HashMap<>();

    public void index(ArrayList<Token> tokenVector, String documentText) {

        List<Token> tokenList = new ArrayList<>();
        Document document = new Document(documentText, tokenList);

        for (Token token: tokenVector) {
            List<Document> documents;
            if (!keywordToDocumentMap.containsKey(token.word)){
                documents = new ArrayList<>();
                keywordToDocumentMap.put(token.word, documents);
            }
            else
                documents = keywordToDocumentMap.get(token.word);
            tokenList.add(token);
            documents.add(document);
        }
    }

    private double computeIDF(int corpusSize, int matchingDocs) {
        return Math.log((double)corpusSize / (1 + (double)matchingDocs)) + 1;
    }


    private ArrayList<Token> vectorize(String queryString) {
        TokenizationLayer tokenizationLayer = null;
        try {
            tokenizationLayer = new TokenizationLayer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

    private List<Double> TFIDFVector(Set<Token> allTokens, List<Token> documentTokens, int corpusSize) {
        List<Double> vector = new ArrayList<>();
        double norm = 0;
        for (Token token :  allTokens) {
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
        Set<Document> documents = new HashSet<>();
        ArrayList<Token> queryTokens = vectorize(queryString);
        for (Token queryToken : queryTokens) {
            List<Document> documentsResult = keywordToDocumentMap.get(queryToken.word);
            if (documentsResult != null)
                documents.addAll(documentsResult);
        }

        List<Pair<Document, Double>> sortedDocuments = new ArrayList<>();
        for (Document document : documents) {

            Set<Token> allTokens = new HashSet<>();
            allTokens.addAll(document.tokenList);
            allTokens.addAll(queryTokens);

            List<Double> queryTFIDF = TFIDFVector(allTokens, queryTokens,documents.size());
            List<Double> documentVector = TFIDFVector(allTokens, document.tokenList,documents.size());
            double cosineSimilarity = computeSimilarity(documentVector, queryTFIDF);
            sortedDocuments.add(new Pair<>(document, cosineSimilarity));
        }

        sortedDocuments.sort((x, y) -> {
            if (y.item2 - x.item2 > 0)
                return -1;
            else if (y.item2 - x.item2 < 0)
                return  1;
            return 0;
        });
        return sortedDocuments.stream().map(x -> x.item1.text).collect(Collectors.toList());
    }
}