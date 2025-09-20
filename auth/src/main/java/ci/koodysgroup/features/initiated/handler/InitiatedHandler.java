package ci.koodysgroup.features.initiated.handler;

import ci.koodysgroup.domains.dtms.OtpDtm;
import ci.koodysgroup.domains.dtms.UserDtm;
import ci.koodysgroup.domains.dtms.UserOrOtpDtm;
import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Country;
import ci.koodysgroup.domains.entities.Otp;
import ci.koodysgroup.domains.entities.User;
import ci.koodysgroup.features.initiated.command.InitiatedCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.AccessRepository;
import ci.koodysgroup.repositories.CountryRepository;
import ci.koodysgroup.repositories.OtpRepository;
import ci.koodysgroup.utils.functions.GlobalFunction;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class InitiatedHandler implements CommandHandler<InitiatedCommand , CommandResponse<UserOrOtpDtm>> {

    private final CountryRepository country;
    private final OtpRepository otp;
    private  final AccessRepository access;





    @Override
    public CommandResponse<UserOrOtpDtm> handler(InitiatedCommand command) {

        try
        {
            Country country = this.country.findById(command.getCountry_id())
                    .orElseThrow(() -> new NoSuchElementException("The selected country does not exist, Please try again"));

            Optional<Access> access = this.access.findByLoginAndCountry(command.getGenerated_by() , country);

            if(access.isEmpty())
            {
                String code = "12345"; //  GlobalFunction.generatedOtpCode(5);

                Optional<Otp> otp = this.otp.findByGeneratedBy(command.getGenerated_by());

                if(otp.isPresent())
                {
                    Otp otp_value = otp.get();

                    if(otp_value.getCounter() == 3 && GlobalFunction.compareDate(otp_value.getUpdated_at()))
                    {
                        throw new RuntimeException("Sorry! Too many attempts for today, please try again later");
                    }

                    otp_value.setCode(code);
                    otp_value.setCounter(otp_value.getCounter()+1);

                    this.otp.save(otp_value);

                    return CommandResponse.success(UserOrOtpDtm.ofCode(OtpDtm.fromCodeDtm(otp_value)));

                }

                else
                {
                    Otp otp_saved = new Otp(code, command.getGenerated_by());
                    otp_saved = this.otp.save(otp_saved);

                    return CommandResponse.success(UserOrOtpDtm.ofCode(OtpDtm.fromCodeDtm(otp_saved)));
                }
            }

            else
            {
                Access access_value = access.get();

                if(access_value.isLocked_account())
                {
                    throw new IllegalStateException("Your account is locked, Please contact technical support for assistance");
                }

                access_value.setConnected("pending");
                this.access.save(access_value);

                User user = access_value.getUser();

                return CommandResponse.success(UserOrOtpDtm.ofUser(UserDtm.fromUserDtm(user)));
            }
        } catch (RuntimeException e) {
            return CommandResponse.error(e.getMessage() ,  e.getMessage().contains("not exist") ? "not_found" : "conflict");
        }

    }
}
