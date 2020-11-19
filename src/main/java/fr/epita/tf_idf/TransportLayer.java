package fr.epita.tf_idf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransportLayer {
    public String fromUrl(final String url) throws IOException {
        final var urlObj = new URL(url);
        final var con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("GET");
        final var in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }


}
