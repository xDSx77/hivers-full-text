package fr.epita.tf_idf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizationLayerTest {

    TokenizationLayer tokenizationLayer = new TokenizationLayer("stopwords-filter-fr.txt");

    @Test
    public void TestTokenization() {
        String raw = "Bonjour, je m'appelle Allan";
        List<String> tokenList = tokenizationLayer.tokenize(raw);
        Assertions.assertTrue(tokenList.contains("bonjour"));
        Assertions.assertTrue(tokenList.contains("appelle"));
        Assertions.assertTrue(tokenList.contains("allan"));
    }

    @Test
    public void TestTokenization2() {
        String raw = "Endal, né en décembre 1995 et mort le 13 mars 2009, est un chien d'assistance qui a vécu au Royaume-Uni. Adopté par Allen Parton, ancien militaire handicapé à la suite d'un accident d'automobile, ce retriever du Labrador a plusieurs exploits à son actif que les médias britanniques ont soulignés, comme utiliser un guichet automatique bancaire, charger un lave-linge et porter secours à son maître inconscient.";
        List<String> tokenList = tokenizationLayer.tokenize(raw);
        Assertions.assertTrue(tokenList.contains("1995"));
        Assertions.assertTrue(tokenList.contains("décembre"));
        Assertions.assertTrue(tokenList.contains("13"));
        Assertions.assertTrue(tokenList.contains("2009"));
        Assertions.assertTrue(tokenList.contains("vécu"));
        Assertions.assertTrue(tokenList.contains("uni"));
        Assertions.assertTrue(tokenList.contains("maître"));
    }
}