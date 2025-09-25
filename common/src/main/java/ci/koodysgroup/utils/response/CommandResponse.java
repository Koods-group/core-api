package ci.koodysgroup.utils.response;

import ci.koodysgroup.interfaces.ICommandResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CommandResponse<T> implements ICommandResponse {
    private boolean success;
    private String title;
    private String message;
    private T content;
    private String code;


    public static <T> CommandResponse<T> success(String title, T content) {
        return new CommandResponse<>(true,title, "Operation successful", content, "success");
    }

    public static <T> CommandResponse<T> success(String title, String message, T content, String code) {
        return new CommandResponse<>(true,title, message, content, code);
    }

    public static <T> CommandResponse<T> error(String title, String message , String code) {
        return new CommandResponse<>(false, title, message, null, code);
    }

}
