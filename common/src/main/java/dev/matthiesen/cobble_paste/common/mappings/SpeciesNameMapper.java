package dev.matthiesen.cobble_paste.common.mappings;

public final class SpeciesNameMapper {
    private SpeciesNameMapper() {
    }

    public static String toCobblemon(String showdownSpecies) {
        if (showdownSpecies == null || showdownSpecies.isBlank()) {
            return "";
        }

        String normalized = showdownSpecies.trim();
        normalized = normalized.replace("’", "'");
        normalized = normalized.replace("♂", "m");
        normalized = normalized.replace("♀", "f");
        normalized = normalized.replace("Mr. Mime", "Mr Mime");
        normalized = normalized.replace("Mime Jr.", "Mime Jr");
        normalized = normalized.replace("Farfetch'd", "Farfetchd");
        normalized = normalized.replace("Nidoran♂", "Nidoran m");
        normalized = normalized.replace("Nidoran♀", "Nidoran f");
        normalized = normalized.replace("Porygon-Z", "Porygon Z");
        normalized = normalized.replace("Rotom-W", "Rotom W");
        normalized = normalized.replace("Rotom-F", "Rotom F");
        normalized = normalized.replace("Rotom-C", "Rotom C");
        normalized = normalized.replace("Rotom-H", "Rotom H");
        normalized = normalized.replace("Rotom-S", "Rotom S");
        normalized = normalized.replace("Mr. Rime", "Mr Rime");
        normalized = normalized.replace("Sirfetch'd", "Sirfetchd");
        return normalized;
    }

    public static String toShowdown(String cobblemonSpecies) {
        if (cobblemonSpecies == null || cobblemonSpecies.isBlank()) {
            return "";
        }

        String normalized = cobblemonSpecies.trim();
        normalized = normalized.replace("_", " ");
        normalized = normalized.replace("-", " ");
        normalized = normalized.replace("Mr Mime", "Mr. Mime");
        normalized = normalized.replace("Mime Jr", "Mime Jr.");
        normalized = normalized.replace("Farfetchd", "Farfetch'd");
        normalized = normalized.replace("Sirfetchd", "Sirfetch'd");
        normalized = normalized.replace("Nidoran m", "Nidoran♂");
        normalized = normalized.replace("Nidoran f", "Nidoran♀");
        normalized = normalized.replace("Mr Rime", "Mr. Rime");
        normalized = normalized.replace("Porygon Z", "Porygon-Z");
        normalized = normalized.replace("Rotom W", "Rotom-W");
        normalized = normalized.replace("Rotom F", "Rotom-F");
        normalized = normalized.replace("Rotom C", "Rotom-C");
        normalized = normalized.replace("Rotom H", "Rotom-H");
        normalized = normalized.replace("Rotom S", "Rotom-S");
        return normalized;
    }
}
