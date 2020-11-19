package fr.epita.tf_idf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContentCleanupLayerTest {

    @Test
    public void testSimpleDivCleanUp() {
        final String html = "<div>this is a simple string</div>";
        final String text = "this is a simple string";

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();
        Assertions.assertEquals(text, contentCleanupLayer.cleanupHTML(html));
    }

    @Test
    public void testPandDivSimpleCleanUp() {
        final String html = "<p>this is</p><div> a simple string</div>";
        final String text = "this is a simple string";

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();
        Assertions.assertEquals(text, contentCleanupLayer.cleanupHTML(html));
    }

    @Test
    public void testSimpleHTMLPage() {
        final String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>this is</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>a\n" +
                "<p> simple string</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        final String text = "this is a simple string";

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();
        Assertions.assertEquals(text, contentCleanupLayer.cleanupHTML(html));
    }

    @Test
    public void testHTMLWithJSPage() {
        final String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>this is</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>a\n" +
                "<p> simple</p>\n" +
                "\n" +
                "<button type=\"button\"\n" +
                "onclick=\"document.getElementById('demo').innerHTML = Date()\">\n" +
                " string</button>\n" +
                "</body>\n" +
                "</html>";

        final String text = "this is a simple string";

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();
        Assertions.assertEquals(text, contentCleanupLayer.cleanupHTML(html));
    }

    @Test
    public void testHTMLWithJSscriptTag() {
        final String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>this is</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>a\n" +
                "<p> simple string</p>\n" +
                "\n" +
                "</body>\n" +
                "<script type=\"text/javascript\">\n" +
                "console.log(\"ignored content\"\n" +
                "</script>\n" +
                "</html>";

        final String text = "this is a simple string";

        ContentCleanupLayer contentCleanupLayer = new ContentCleanupLayer();
        Assertions.assertEquals(text, contentCleanupLayer.cleanupHTML(html));
    }
}