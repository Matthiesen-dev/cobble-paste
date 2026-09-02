package dev.matthiesen.cobble_paste.common.formats;

import com.cobblemon.mod.common.pokemon.Gender;

public enum ShowdownGender {
    MALE("M"),
    FEMALE("F"),
    GENDERLESS("N");

    private final String showdownString;

    ShowdownGender(String code) {
        this.showdownString = code;
    }

    public String getShowdownString() {
        return showdownString;
    }

    public static ShowdownGender fromGender(Gender gender) {
        if (gender == null) {
            return null;
        }
        return switch (gender) {
            case MALE -> ShowdownGender.MALE;
            case FEMALE -> ShowdownGender.FEMALE;
            case GENDERLESS -> ShowdownGender.GENDERLESS;
        };
    }

    public static ShowdownGender fromString(String rawGenderText) {
        ShowdownGender gender = null;
        if (rawGenderText != null && !rawGenderText.isBlank()) {
            String normalized = rawGenderText.trim();
            if (normalized.equalsIgnoreCase("m") || normalized.equalsIgnoreCase("male")) {
                gender = ShowdownGender.MALE;
            } else if (normalized.equalsIgnoreCase("f") || normalized.equalsIgnoreCase("female")) {
                gender = ShowdownGender.FEMALE;
            } else if (normalized.equalsIgnoreCase("n") || normalized.equalsIgnoreCase("genderless")) {
                gender = ShowdownGender.GENDERLESS;
            }
        }
        return gender;
    }
}
