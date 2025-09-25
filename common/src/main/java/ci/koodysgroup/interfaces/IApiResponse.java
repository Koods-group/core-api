package ci.koodysgroup.interfaces;

public interface IApiResponse {
    Object getContent();
    String getTitle();
    String getMessage();
    boolean isSuccess();
    Object getErrors();
    String getCode();
}
