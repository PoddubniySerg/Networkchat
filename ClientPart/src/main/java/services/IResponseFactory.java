package services;

public interface IResponseFactory {

    IResponse newResponse(String title, String content, String dateTime);
}
