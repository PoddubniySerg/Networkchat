package model;

import services.IResponse;
import services.IResponseFactory;

public class ChatResponseFactory implements IResponseFactory {
    @Override
    public IResponse newResponse(String title, String content, String dateTime) {
        return new ChatResponse(title, content, dateTime);
    }
}
