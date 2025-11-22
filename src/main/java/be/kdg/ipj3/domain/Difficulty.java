package be.kdg.ipj3.domain;

public enum Difficulty {
    EASY,
    HARD,
    IMPOSSIBLE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Difficulty fromString(String value) {
        try {
            return Difficulty.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid difficulty: " + value);
        }
    }
}
