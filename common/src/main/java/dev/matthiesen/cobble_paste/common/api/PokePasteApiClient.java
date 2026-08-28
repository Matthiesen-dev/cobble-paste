package dev.matthiesen.cobble_paste.common.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PokePasteApiClient {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Pattern POKEPASTE_ID = Pattern.compile("(?:https?://)?pokepast\\.es/([0-9a-fA-F]{16})(?:/.*)?$");

    private PokePasteApiClient() {
    }

    private static HttpRequest.Builder getRequestBuilder(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("User-Agent", "CobblePaste/1.0 (https://github.com/matthiesen-dev/cobble-paste)");
    }

    public static CompletableFuture<String> fetchRawPaste(String urlOrId) {
        String targetUrl = normalizeToPasteUrl(urlOrId);
        HttpRequest request = getRequestBuilder(targetUrl + "/raw")
                .GET()
                .build();
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 400) {
                        throw new IllegalStateException("Failed to fetch Pokepaste: HTTP " + response.statusCode());
                    }
                    return response.body();
                });
    }

    public static CompletableFuture<String> createPaste(String pasteText, String title, String author) {
        StringBuilder body = new StringBuilder();
        body.append("paste=").append(URLEncoder.encode(pasteText, StandardCharsets.UTF_8));
        if (title != null && !title.isBlank()) {
            body.append("&title=").append(URLEncoder.encode(title, StandardCharsets.UTF_8));
        }
        if (author != null && !author.isBlank()) {
            body.append("&author=").append(URLEncoder.encode(author, StandardCharsets.UTF_8));
        }

        HttpRequest request = getRequestBuilder("https://pokepast.es/create")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    String location = response.headers().firstValue("Location").orElse("");
                    if (response.statusCode() == 303 && !location.isBlank()) {
                        return "https://pokepast.es" + location;
                    }
                    if (response.statusCode() >= 300 && response.statusCode() < 400) {
                        String redirect = response.headers().firstValue("Location").orElse("");
                        if (!redirect.isBlank()) {
                            return "https://pokepast.es" + redirect;
                        }
                    }
                    throw new IllegalStateException("pokepast.es rejected the paste submission: HTTP " + response.statusCode());
                });
    }

    public static String normalizeToPasteUrl(String urlOrId) {
        if (urlOrId == null || urlOrId.isBlank()) {
            throw new IllegalArgumentException("Pokepaste URL or ID is required.");
        }

        String trimmed = urlOrId.trim();
        Matcher matcher = POKEPASTE_ID.matcher(trimmed);
        if (matcher.find()) {
            return "https://pokepast.es/" + matcher.group(1);
        }

        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed.replaceAll("/raw$", "").replaceAll("/$", "");
        }

        return "https://pokepast.es/" + trimmed;
    }
}
