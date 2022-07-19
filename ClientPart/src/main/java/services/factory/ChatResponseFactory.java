package services.factory;

import model.response.ChatResponse;
import model.response.IResponse;

public class ChatResponseFactory implements IResponseFactory {
    @Override
    public IResponse newResponse(String title, String content, String dateTime) {
        return new ChatResponse(title, content, dateTime);
    }
}
