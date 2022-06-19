package model;

import services.IResponse;
import services.IResponseFactory;

public class NetServerResponseFactory implements IResponseFactory {
    @Override
    public IResponse newResponse(String title, String content, String dateTime) {
        return new NetServerResponse(title, content, dateTime);
    }
}
