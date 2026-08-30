package dev.matthiesen.cobble_paste.common.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokePasteApiClientTest {
    @Test
    void extractsIdFromSupportedInputs() {
        assertEquals("0123456789abcdef", PokePasteApiClient.extractPasteId("0123456789abcdef"));
        assertEquals(
                "0123456789abcdef",
                PokePasteApiClient.extractPasteId("https://pokepast.es/0123456789abcdef/raw")
        );
    }

    @Test
    void returnsEmptyIdWhenInputDoesNotContainOne() {
        assertEquals("", PokePasteApiClient.extractPasteId("https://example.com/a-paste"));
    }
}
