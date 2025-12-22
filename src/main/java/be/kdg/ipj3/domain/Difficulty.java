package be.kdg.ipj3.domain;

public enum Difficulty {
    EASY,
    HARD,
    IMPOSSIBLE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String getDisplayName() {
        return switch (this) {
            case EASY -> "Easy";
            case HARD -> "Medium";
            case IMPOSSIBLE -> "Hard";
        };
    }
}
