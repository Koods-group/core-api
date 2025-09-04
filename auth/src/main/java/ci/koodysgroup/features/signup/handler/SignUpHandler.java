package ci.koodysgroup.features.signup.handler;


import ci.koodysgroup.domains.dtms.UserDtm;
import ci.koodysgroup.domains.entities.Otp;
import ci.koodysgroup.domains.entities.User;
import ci.koodysgroup.features.signup.command.SignUpCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.OtpRepository;
import ci.koodysgroup.repositories.UserRepository;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class SignUpHandler implements CommandHandler<SignUpCommand, CommandResponse<UserDtm>> {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;


    @Override
    public CommandResponse<UserDtm> handler(SignUpCommand command) {
        try{

            Otp otp = this.otpRepository.findById(command.getOtp_id())
                    .orElseThrow(() -> new NoSuchElementException("Sorry! You are not authorised to perform this action"));

            if(otp.isConsumed())
            {
                User user = new User(command.getCivility(),command.getName(),command.getSurname(),otp.getGeneratedBy());

                user = this.userRepository.save(user);

                this.otpRepository.delete(otp);

                return CommandResponse.success(UserDtm.fromUserDtm(user));
            }

            else
            {
                return CommandResponse.error("Please validate your telephone number before registering" , "conflict");
            }

        }catch (Exception e)
        {
            return CommandResponse.error(e.getMessage() ,  "not_found");
        }
    }
}
