package fr.epita.tf_idf;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenizationLayer {

    private final String stopWordFilePath;
    private BufferedReader stopWordFileReader = null;

    public TokenizationLayer(String stopWordFilePath) {
        this.stopWordFilePath = stopWordFilePath;
        BufferedReader reader = null;
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

        List<String> tokenList = new ArrayList<>(Arrays.asList(textFromURL.split(" ")));
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
        List<String> filteredList = tokenList.stream()
                .filter(stopWordList::contains)
                .collect(Collectors.toList());

        return tokenList;
    }
}
