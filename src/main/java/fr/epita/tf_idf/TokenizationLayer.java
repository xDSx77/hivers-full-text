package fr.epita.tf_idf;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TokenizationLayer {

    private final String stopWordFilePath;
    private BufferedReader stopWordFileReader = null;

    public TokenizationLayer(final String stopWordFilePath) {
        this.stopWordFilePath = stopWordFilePath;
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(stopWordFilePath));
            this.stopWordFileReader = reader;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TokenizationLayer() {
        this.stopWordFilePath = null;
    }

    public List<String> tokenize(final String textFromURL) {
        if (textFromURL == null)
            return new ArrayList<>();
        final String lowerCaseText = textFromURL.toLowerCase();

        List<String> tokenList = new ArrayList<>();

        final String regex = "[0-9A-Za-zÀ-ÖØ-öø-ÿ]+";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(lowerCaseText);
        while (matcher.find()) {
            String word = matcher.group();
            tokenList.add(word);
        }

        List<String> stopWordList = new ArrayList<>();
        if (stopWordFileReader != null) {
            String currentLine;
            while (true) {
                try {
                    if ((currentLine = stopWordFileReader.readLine()) == null)
                        break;
                    stopWordList.add(currentLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tokenList.stream()
                .filter(str -> !stopWordList.contains(str))
                .collect(Collectors.toList());
    }
}
