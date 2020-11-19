import fr.epita.tf_idf.*;

import javax.security.auth.callback.LanguageCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

public class Main {
    public static void main(String[] args) {
        String rawHTML = "<div>the blue rabbit is fishing in a blue river</div>";
        String url = "";

        TransportLayer transportLayer = new TransportLayer();
        String htmlFromURL = transportLayer.fromUrl(url);

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

        indexer.index(tokenVectorFromURL);

        String queryString = "the blue";

        ArrayList<String> documents = indexer.query(queryString);
    }
}
