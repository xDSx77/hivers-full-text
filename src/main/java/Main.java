import fr.epita.tf_idf.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static void IndexRawHTML(Indexer indexer, String rawHTML)
    {
        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();

        String textFromRawHTML = contentCleanupLayer.cleanupHTML(rawHTML);

        TokenizationLayer tokenizationLayer;
        try {
            tokenizationLayer = new TokenizationLayer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<String> tokenListFromRawHTML = tokenizationLayer.tokenize(textFromRawHTML);

        VectorizationLayer vectorConversionLayer = new VectorizationLayer();

        ArrayList<Token> tokenVectorFromRawHTML = vectorConversionLayer.vectorize(tokenListFromRawHTML);


        indexer.index(tokenVectorFromRawHTML, textFromRawHTML);
    }

    static void Index(Indexer indexer, BufferedReader reader)
    {
        boolean done = false;

        do {
            try {
                System.out.println("Select indexation method:");
                System.out.println("- HTML: type HTML line:");
                System.out.println("- URL: from URL");
                switch (reader.readLine()) {
                    case "HTML":
                        System.out.println("Enter HTML line:");
                        IndexRawHTML(indexer, reader.readLine());
                        done = true;
                        break;
                    case "URL":
                        System.out.println("Enter URL:");
                        TransportLayer transportLayer = new TransportLayer();
                        String textFromURL = transportLayer.fromUrl(reader.readLine());
                        IndexRawHTML(indexer, textFromURL);
                        done = true;
                        break;
                    default:
                        break;
                }
            }
            catch (IOException e) {
                System.out.println("Error lol");
            }
        }
        while (!done);

    }


    public static void main(String[] args) {
        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        Indexer indexer = new Indexer();

        String rawHTML0 = "<div>bleu bleu bleu bleu bleue</div>";
        String rawHTML1 = "<div>le lapin bleu est en train de pêcher dans une rivière bleu</div>";
        String rawHTML2 = "<div>le chien bleu est en train de pêcher dans une rivière bleu</div>";
        String rawHTML3 = "<div>le chat bleu est en train de pêcher dans une rivière bleu</div>";
        String rawHTML4 = "<div>la rivière rouge est en train de pêcher dans une rivière rouge</div>";
        String rawHTML5 = "<div>le lapin rouge est en train de pêcher dans une rivière bleu</div>";
        String rawHTML6 = "<div><div><div>lapin lapin lapin lapin lapin la pain bleu bleu bleu</div></div></div>";

        IndexRawHTML(indexer, rawHTML0);
        IndexRawHTML(indexer, rawHTML1);
        IndexRawHTML(indexer, rawHTML2);
        IndexRawHTML(indexer, rawHTML3);
        IndexRawHTML(indexer, rawHTML4);
        IndexRawHTML(indexer, rawHTML5);
        IndexRawHTML(indexer, rawHTML6);

        while (true){
            try {
                System.out.println("Enter command:");
                System.out.println("- index: index HTML");
                System.out.println("- query: search into indexed documents");

                // Reading data using readLine
                switch (reader.readLine()) {
                    case "index":
                        Index(indexer, reader);
                        break;
                    case "query":
                        System.out.println("Enter query:");
                        List<String> documentTexts = indexer.query(reader.readLine());
                        for (String documentText : documentTexts) {
                            System.out.println(documentText);
                        }
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
            catch (IOException e)
            {
                System.out.println("Unknown command");
            }
            System.out.println("\n\n");
        }
    }

}
