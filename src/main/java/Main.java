import fr.epita.tf_idf.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String rawHTML = "<div>le lapin bleu est en train de pêcher dans une rivière bleue</div>";
        String url = "https://www.20minutes.fr/monde/2910287-20201117-spacex-assiste-naissance-nouvelle-economie-vols-habites";

        TransportLayer transportLayer = new TransportLayer();
        String htmlFromURL;
        try {
            htmlFromURL = transportLayer.fromUrl(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();

        String textFromURL = contentCleanupLayer.cleanupHTML(htmlFromURL);
        String textFromRawHTML = contentCleanupLayer.cleanupHTML(rawHTML);

        TokenizationLayer tokenizationLayer;
        try {
            tokenizationLayer = new TokenizationLayer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<String> tokenListFromURL = tokenizationLayer.tokenize(textFromURL);
        List<String> tokenListFromRawHTML = tokenizationLayer.tokenize(textFromRawHTML);

        VectorizationLayer vectorConversionLayer = new VectorizationLayer();

        ArrayList<Token> tokenVectorFromURL = vectorConversionLayer.vectorize(tokenListFromURL);
        ArrayList<Token> tokenVectorFromRawHTML = vectorConversionLayer.vectorize(tokenListFromRawHTML);

        Indexer indexer = new Indexer();

        indexer.index(tokenVectorFromURL, textFromURL);
        indexer.index(tokenVectorFromRawHTML, textFromRawHTML);

        String queryString = "le bleu";

        List<String> documents = indexer.query(queryString);
    }
}
