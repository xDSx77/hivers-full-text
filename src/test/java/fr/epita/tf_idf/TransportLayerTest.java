package fr.epita.tf_idf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TransportLayerTest {
    @Test
    void testFromUrl() {
        final var testUrl = "https://www.20minutes.fr/monde/2910287-20201117-spacex-assiste-naissance-nouvelle-economie-vols-habites";
        final var tLayer = new TransportLayer();
        try {
            Assertions.assertNotNull(tLayer.fromUrl(testUrl));
        } catch (IOException e) {
            Assertions.assertNotNull(e);
        }
    }
}