package services.factory;

import model.response.IResponse;

public interface IResponseFactory {

    IResponse newResponse(String title, String content, String dateTime);
}
