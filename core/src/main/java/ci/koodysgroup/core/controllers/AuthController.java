package ci.koodysgroup.core.controllers;


import ci.koodysgroup.core.contexts.auth.InitiatedContext;
import ci.koodysgroup.core.contexts.auth.SignUpContext;
import ci.koodysgroup.core.contexts.auth.ValidatedContext;
import ci.koodysgroup.domains.dtms.OtpDtm;
import ci.koodysgroup.domains.dtms.UserDtm;
import ci.koodysgroup.domains.dtms.UserOrOtpDtm;
import ci.koodysgroup.features.initiated.command.InitiatedCommand;
import ci.koodysgroup.features.signin.command.ValidatedOtpCommand;
import ci.koodysgroup.features.signup.command.SignUpCommand;
import ci.koodysgroup.interfaces.bus.CommandBus;
import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import ci.koodysgroup.utils.response.CommandResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final CommandBus bus;

    public AuthController(CommandBus bus)
    {
        this.bus = bus;
    }


    @PostMapping("sign-up")
    public ResponseEntity<ApiResponse<UserDtm>> signUp(@RequestBody SignUpContext context)
    {
        SignUpCommand command = new SignUpCommand(context.getOtpId(),context.getCivility(),context.getName(),context.getSurname(),context.getLogin());

        CommandResponse<UserDtm> response = this.bus.send(command);
        if(!response.isSuccess())
        {
            return switch (response.getCode()) {
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                case "conflict" -> ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseUtil.conflict(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError());
            };
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.ok(response.getContent(),"A validation code has been sent to you by text message to continue the process","success"));
    }


    @PostMapping("initiated")
    public ResponseEntity<ApiResponse<UserOrOtpDtm>> initiated(@RequestBody InitiatedContext context)
    {
        InitiatedCommand command = new InitiatedCommand(context.getCountryId(),context.getGeneratedBy());
        CommandResponse<UserOrOtpDtm> response = this.bus.send(command);

        if(!response.isSuccess())
        {
            return switch (response.getCode()) {
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                case "conflict" -> ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseUtil.conflict(response.getMessage()));
                case "unauthorized" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseUtil.unauthorized(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError());
            };
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.ok(response.getContent(),"A validation code has been sent to you by text message to continue the process","success"));
    }


    @PostMapping("validated-otp")
    public ResponseEntity<ApiResponse<OtpDtm>> validated(@RequestBody ValidatedContext context)
    {
        ValidatedOtpCommand command = new ValidatedOtpCommand(context.getOtpId(),context.getCode(),context.getGeneratedBy());
        CommandResponse<OtpDtm> response = this.bus.send(command);

        if(!response.isSuccess())
        {
            return switch (response.getCode()) {
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                case "bad_request" -> ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseUtil.badRequest(response.getMessage()));
                case "conflict" -> ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseUtil.conflict(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError());
            };
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseUtil.ok(response.getContent() , "Code validated, your account is now activated", "success"));
    }

}
