package ci.koodysgroup.features.forgetfulness.handler;


import ci.koodysgroup.domains.dtms.OtpDtm;
import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Country;
import ci.koodysgroup.domains.entities.Otp;
import ci.koodysgroup.features.forgetfulness.command.ForgetfulnessCommand;
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
public class ForgetfulnessHandler implements CommandHandler<ForgetfulnessCommand , CommandResponse<OtpDtm>> {

    private final AccessRepository repository;
    private final OtpRepository otpRepository;
    private final CountryRepository countryRepository;

    @Override
    public CommandResponse<OtpDtm> handler(ForgetfulnessCommand command) {
        try{

            Country country = this.countryRepository.findById(command.getCountryId())
                    .orElseThrow(() -> new NoSuchElementException("The selected country does not exist, Please try again ."));

            Optional<Access> access = this.repository.findByLoginAndCountry(command.getLogin() , country);

            if(access.isPresent())
            {
                String code = "12345"; //  GlobalFunction.generatedOtpCode(5);

                Otp otp = this.otpRepository.findByGeneratedBy(command.getLogin())
                        .map(dbOtp -> {
                            if (dbOtp.getCounter() == 3 && GlobalFunction.compareDate(dbOtp.getUpdatedAt())) {
                                throw new RuntimeException(
                                        "Sorry! Too many attempts for today, please try again later."
                                );
                            }

                            dbOtp.setCode(code);
                            dbOtp.setCounter(dbOtp.getCounter() + 1);
                            return this.otpRepository.save(dbOtp);
                        })
                                .orElseGet(() -> {
                                    Otp newOtp = new Otp(code, command.getLogin());
                                    return this.otpRepository.save(newOtp);
                                });

                return CommandResponse.success(OtpDtm.fromCodeDtm(otp));

            }
            else {
                return CommandResponse.error("Sorry, but your username does not appear to have an associated account . Please check it .","not_found");
            }

        }catch (Exception ex){
            return CommandResponse.error(ex.getMessage(),  ex.getMessage().contains("many attempts") ? "conflict" : "not_found");
        }
    }
}
