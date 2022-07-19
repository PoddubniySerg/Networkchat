package model.response;

public class ChatResponse implements IResponse {

    private final String title;

    private final String content;

    private final String localDateTime;

    public ChatResponse(String title, String content, String localDateTime) {
        this.title = title;
        this.content = content;
        this.localDateTime = localDateTime;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getDateTime() {
        return this.localDateTime;
    }

    @Override
    public String toString() {
        return this.title + "\n" + this.content + "\n" + this.localDateTime + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatResponse)) return false;
        ChatResponse response = (ChatResponse) obj;
        return this.title.equals(response.getTitle())
                && this.content.equals(response.getContent())
                && this.localDateTime.equals(response.getDateTime());
    }
}
