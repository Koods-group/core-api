package ci.koodysgroup.features.signin.handler;

import ci.koodysgroup.domains.dtms.OtpDtm;
import ci.koodysgroup.domains.entities.Otp;
import ci.koodysgroup.features.signin.command.ValidatedOtpCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.OtpRepository;
import ci.koodysgroup.utils.functions.GlobalFunction;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;


@Component
@AllArgsConstructor
public class ValidatedOtpHandler implements CommandHandler<ValidatedOtpCommand , CommandResponse<OtpDtm>> {

    private final OtpRepository repository;

    @Override
    public CommandResponse<OtpDtm> handler(ValidatedOtpCommand command) {
        try{

            Otp otp = this.repository.findById(command.getOtp_id())
                    .orElseThrow(() -> new NoSuchElementException("Sorry! You have no pending validations"));

            boolean time_elapsed = GlobalFunction.elapsedTime(otp.getUpdated_at() , 45);

            if(time_elapsed)
            {
                throw new RuntimeException("Validation time limit has expired, Please try again with a new code.");
            }

            if(Objects.equals(otp.getCode(), command.getCode()) && Objects.equals(otp.getGeneratedBy() , command.getGenerated_by()))
            {

                otp.setConsumed(true);
                this.repository.save(otp);

                return CommandResponse.success(OtpDtm.fromCodeDtm(otp));
            }

            else
            {
                return CommandResponse.error("The code entered is invalid, please try again","bad_request");
            }


        }catch (Exception e){
            return CommandResponse.error(e.getMessage() ,  e.getMessage().contains("expired") ? "conflict" : "not_found");
        }
    }
}
