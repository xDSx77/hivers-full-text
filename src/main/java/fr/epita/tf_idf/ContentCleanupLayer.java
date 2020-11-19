package fr.epita.tf_idf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ContentCleanupLayer {
    public String cleanupHTML(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }
}
