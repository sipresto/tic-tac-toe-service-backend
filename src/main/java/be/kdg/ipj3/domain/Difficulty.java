package be.kdg.ipj3.domain;

public enum Difficulty {
    EASY,
    HARD,
    IMPOSSIBLE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
