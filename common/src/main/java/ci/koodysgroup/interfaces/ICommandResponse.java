package ci.koodysgroup.interfaces;

public interface ICommandResponse {
    String getCode();
    boolean isSuccess();
    String getMessage();
    Object getContent();
}
