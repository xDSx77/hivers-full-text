package fr.epita.tf_idf;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TokenizationLayer {

    private final List<String> stopWordList;
    private final Map<String, List<String>> synonymMap;

    public TokenizationLayer() throws FileNotFoundException{
        final BufferedReader stopWordListReader = new BufferedReader(new FileReader("stop-words-fr.txt"));
        this.stopWordList = new ArrayList<>();

        String currentLine;
        while (true) {
            try {
                if ((currentLine = stopWordListReader.readLine()) == null)
                    break;
                stopWordList.add(currentLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final BufferedReader synonymDictionaryReader = new BufferedReader(new FileReader("synonyms-dictionary-fr-v2.txt"));
        this.synonymMap = new TreeMap<>();

        while (true) {
            try {
                if ((currentLine = synonymDictionaryReader.readLine()) == null)
                    break;

                final List<String> wordAndSynonyms = Arrays.asList(currentLine.split(","));
                final String word = wordAndSynonyms.get(0);
                final List<String> synonyms = wordAndSynonyms.subList(1, wordAndSynonyms.size());

                synonymMap.put(word, synonyms);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> sliceInput(final String lowerCaseText) {
        List<String> tokens = new ArrayList<>();

        final String regex = "(\\p{IsLatin}|\\d)+";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(lowerCaseText);

        while (matcher.find()) {
            final String word = matcher.group();
            tokens.add(word);
        }
        return tokens;
    }

    private List<String> synonymReplacement(List<String> tokenList) {
        int i = 0;
        for (String token : tokenList) {
            for (Map.Entry<String, List<String>> wordWithSynonyms : synonymMap.entrySet()) {
                boolean break_ = false;
                String word = wordWithSynonyms.getKey();
                if (token.equals(word))
                    break;

                List<String> synonyms = wordWithSynonyms.getValue();
                for (String synonym : synonyms) {
                    if (token.equals(synonym)) {
                        tokenList.set(i, word);
                        break_ = true;
                        break;
                    }
                }
                if (break_) break;
            }
            i++;
        }
        return tokenList;
    }

    public List<String> tokenize(final String textFromURL) {
        if (textFromURL == null)
            return new ArrayList<>();

        // lowercase text
        final String lowerCaseText = textFromURL.toLowerCase();

        // Slice the input stream into tokens
        List<String> tokenList = sliceInput(lowerCaseText);

        // Suppress stop words
        tokenList = tokenList.stream()
                .filter(str -> !stopWordList.contains(str))
                .collect(Collectors.toList());

        // Apply stemming algorithm
        FrenchStemmer frenchStemmer = new FrenchStemmerImpl();
        tokenList.replaceAll(frenchStemmer::getStemmedWord);

        // Apply synonyms replacement
        return synonymReplacement(tokenList);
    }




}
