package model;

public enum CommandsList {

    EXIT("/exit"),
    REGISTRATION("/registration"),
    AUTHORIZATION("/authorization"),
    INFO_CONTENT("/info"),
    MESSAGE("/message"),
    VALID_COMMAND("/ok");

    private final String description;

    CommandsList(String description) {
        this.description = description;
    }

    public String command() {
        return this.description;
    }
}
