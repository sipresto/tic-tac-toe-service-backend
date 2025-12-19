package be.kdg.ipj3.domain;

public record Player(
        UserId id,
        boolean isHuman,
        String name
) {
    public static Player human(String id, String name) {
        return new Player(new UserId(id), true, name);
    }

    public static Player ai(Difficulty difficulty) {
        String name = "Bot (" + difficulty + ")";
        return new Player(new UserId(name), false, name);
    }
}
