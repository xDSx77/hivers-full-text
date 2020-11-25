package fr.epita.tf_idf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GlobalTest {


    void IndexRawHTML(Indexer indexer, String rawHTML)
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

    void Init(Indexer indexer){
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
    }


    @Test
    void TestBleu() {

        String queryString = "le bleu";
        Indexer indexer = new Indexer();
        Init(indexer);
        List<String> documents = indexer.query(queryString);

        queryString = "";
    }

    @Test
    void TestLapinBleu() {

        String queryString = "lapin bleu";
        Indexer indexer = new Indexer();
        Init(indexer);
        List<String> documents = indexer.query(queryString);

        queryString = "";
    }

    @Test
    void TestRouge() {

        String queryString = "rouge";
        Indexer indexer = new Indexer();
        Init(indexer);
        List<String> documents = indexer.query(queryString);

        queryString = "";
    }

    @Test
    void TestLapin() {

        String queryString = "lapin";
        Indexer indexer = new Indexer();
        Init(indexer);
        List<String> documents = indexer.query(queryString);

        queryString = "";
    }

    @Test
    void TestRiviereRouge() {

        String queryString = "rivière rouge";
        Indexer indexer = new Indexer();
        Init(indexer);
        List<String> documents = indexer.query(queryString);

        queryString = "";
    }
}
