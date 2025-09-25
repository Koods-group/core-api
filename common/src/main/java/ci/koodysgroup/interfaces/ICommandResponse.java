package ci.koodysgroup.interfaces;

public interface ICommandResponse {
    String getCode();
    boolean isSuccess();
    String getTitle();
    String getMessage();
    Object getContent();
}
