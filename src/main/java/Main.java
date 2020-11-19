import fr.epita.tf_idf.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rawHTML = "<div>the blue rabbit is fishing in a blue river</div>";
        String url = "";

        TransportLayer transportLayer = new TransportLayer();
        String htmlFromURL = null;
        try {
            htmlFromURL = transportLayer.fromUrl(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();

        String textFromURL = contentCleanupLayer.cleanupHTML(htmlFromURL);
        String textFromRawHTML = contentCleanupLayer.cleanupHTML(rawHTML);

        TokenizationLayer tokenizationLayer = new TokenizationLayer();

        ArrayList<String> tokenListFromURL = tokenizationLayer.tokenize(textFromURL);
        ArrayList<String> tokenListFromRawHTML = tokenizationLayer.tokenize(textFromRawHTML);

        VectorizationLayer vectorConversionLayer = new VectorizationLayer();

        ArrayList<Token> tokenVectorFromURL = vectorConversionLayer.vectorize(tokenListFromURL);
        ArrayList<Token> tokenVectorFromRawHTML = vectorConversionLayer.vectorize(tokenListFromRawHTML);

        Indexer indexer = new Indexer();

        indexer.index(tokenVectorFromURL, textFromURL);
        indexer.index(tokenVectorFromRawHTML, textFromRawHTML);

        String queryString = "the blue";

        ArrayList<String> documents = indexer.query(queryString);
    }
}
