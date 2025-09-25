package ci.koodysgroup.features.resetpassword.handler;

import ci.koodysgroup.domains.dtms.UserDtm;
import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Country;
import ci.koodysgroup.domains.entities.Otp;
import ci.koodysgroup.features.resetpassword.command.ResetPasswordCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.AccessRepository;
import ci.koodysgroup.repositories.CountryRepository;
import ci.koodysgroup.repositories.OtpRepository;
import ci.koodysgroup.service.JwtService;
import ci.koodysgroup.utils.functions.GlobalFunction;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ResetPasswordHanlder implements CommandHandler<ResetPasswordCommand , CommandResponse<UserDtm>> {

    private final AccessRepository repository;
    private final OtpRepository otpRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public CommandResponse<UserDtm> handler(ResetPasswordCommand command) {

        try {

            Otp otp = this.otpRepository.findById(command.getOtpId())
                    .orElseThrow(() -> new NoSuchElementException("Sorry! You have no pending validations ."));

            boolean time_elapsed = GlobalFunction.elapsedTime(otp.getUpdatedAt() , 45);

            if(time_elapsed)
            {
                throw new RuntimeException("Validation time limit has expired, Please try again with a new code .");
            }

            if(!otp.isConsumed())
            {
                return CommandResponse.error("Identity not confirmed", "Sorry, you must enter the verification code for validation, then return here ." , "conflict");
            }

            Country country = this.countryRepository.findById(command.getCountryId())
                    .orElseThrow(() -> new NoSuchElementException("The selected country does not exist, Please try again ."));

            Optional<Access> access = this.repository.findByLoginAndCountry(command.getLogin() , country);

            access.ifPresentOrElse(value -> {
                value.setPassword(this.passwordEncoder.encode(command.getPassorwd()));
                repository.save(value);
            } , ()  -> {
                throw new NoSuchElementException(" Sorry ! You are not authorised to perform this action .");
            } ) ;

            otpRepository.delete(otp);

            return CommandResponse.success("Action completed", "Great, your password has been successfully reset ." , UserDtm.fromUserDtm(access.get().getUser()),"success");

        }catch (Exception ex){
            return CommandResponse.error("Error", ex.getMessage(), ex.getMessage().contains("expired") ? "conflict" : "not_found");
        }

    }
}
