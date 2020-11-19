package fr.epita.tf_idf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IndexerTest {

    @Test
    public void singleDocumentKeyIndexationTest(){

        final String document = "red green blue";
        final ArrayList<Token> tokenList = new ArrayList<>();

        tokenList.add(new Token("red",0.33f,0));
        tokenList.add(new Token("green",0.33f,1));
        tokenList.add(new Token("blue",0.33f,2));

        Indexer indexer = new Indexer();
        indexer.index(tokenList,document);

        Assertions.assertTrue(indexer.keywordToDocumentMap.containsKey("red"));
        Assertions.assertTrue(indexer.keywordToDocumentMap.containsKey("green"));
        Assertions.assertTrue(indexer.keywordToDocumentMap.containsKey("blue"));
    }

    @Test
    public void singleDocumentDocumentIndexationTest(){

        final String document = "red green blue";
        final ArrayList<Token> tokenList = new ArrayList<>();

        tokenList.add(new Token("red",0.33f,0));
        tokenList.add(new Token("green",0.33f,1));
        tokenList.add(new Token("blue",0.33f,2));

        Indexer indexer = new Indexer();
        indexer.index(tokenList,document);

        Assertions.assertEquals(document, indexer.keywordToDocumentMap.get("red").get(0));
        Assertions.assertEquals(document, indexer.keywordToDocumentMap.get("green").get(0));
        Assertions.assertEquals(document, indexer.keywordToDocumentMap.get("blue").get(0));
    }

    @Test
    public void mutlipleDocumentDocumentIndexationTest(){

        final String document1 = "red green blue";
        final String document2 = "red blue yellow";

        final ArrayList<Token> tokenList1 = new ArrayList<>();
        tokenList1.add(new Token("red",0.33f,0));
        tokenList1.add(new Token("green",0.33f,1));
        tokenList1.add(new Token("blue",0.33f,2));

        final  ArrayList<Token> tokenList2 = new ArrayList<>();
        tokenList2.add(new Token("red",0.33f,0));
        tokenList2.add(new Token("blue",0.33f,1));
        tokenList2.add(new Token("yellow",0.33f,2));

        Indexer indexer = new Indexer();
        indexer.index(tokenList1,document1);
        indexer.index(tokenList2,document2);

        Assertions.assertTrue(indexer.keywordToDocumentMap.containsKey("red"));
        Assertions.assertEquals(2, indexer.keywordToDocumentMap.get("red").size());
        Assertions.assertTrue(indexer.keywordToDocumentMap.get("red").contains(document1));
        Assertions.assertTrue(indexer.keywordToDocumentMap.get("red").contains(document2));

        Assertions.assertTrue(indexer.keywordToDocumentMap.containsKey("yellow"));
        Assertions.assertEquals(1, indexer.keywordToDocumentMap.get("yellow").size());
        Assertions.assertTrue(indexer.keywordToDocumentMap.get("yellow").contains(document2));

    }
}