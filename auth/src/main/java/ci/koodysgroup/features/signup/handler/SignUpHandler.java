package ci.koodysgroup.features.signup.handler;


import ci.koodysgroup.domains.dtms.AccessDtm;
import ci.koodysgroup.domains.dtms.UserDtm;
import ci.koodysgroup.domains.entities.*;
import ci.koodysgroup.features.signup.command.SignUpCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.*;
import ci.koodysgroup.service.JwtService;
import ci.koodysgroup.utils.response.CommandResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
public class SignUpHandler implements CommandHandler<SignUpCommand, CommandResponse<AccessDtm>> {

    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final CountryRepository countryRepository;


    @Autowired
    JwtService service;


    @Override
    public CommandResponse<AccessDtm> handler(SignUpCommand command) {

            Otp otp = this.otpRepository.findById(command.getOtpId())
                    .orElseThrow(() -> new NoSuchElementException("Sorry ! You are not authorised to perform this action ."));

            if(otp.isConsumed())
            {
                User user = new User(command.getCivility(),command.getName(),command.getSurname(),otp.getGeneratedBy());

                user = this.userRepository.save(user);

                Optional<Role> role = roleRepository.findByAlias("user");
                Optional<Country> country = countryRepository.findById(command.getCountryId());

                if(role.isEmpty() || country.isEmpty())
                {
                    return CommandResponse.error("An error occurred during your registration. Please contact technical support ." , "error");
                }

                Access access = new Access(command.getLogin(),user,passwordEncoder.encode(command.getPassword()),role.get(),country.get());
                access.setConnected("connected");

                access = accessRepository.save(access);


                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(access.getLogin(),command.getPassword()));

                UserDetails details = (UserDetails) authentication.getPrincipal();

                assert details != null;
                String bearer = service.generateToken(details.getUsername());

                Token token = new Token(LocalDateTime.now().plusHours(24),access,bearer);
                tokenRepository.save(token);

                this.otpRepository.delete(otp);

                return CommandResponse.success(AccessDtm.fromAccessDtm(access , token.getToken()));
            }

            else
            {
                return CommandResponse.error("Please validate your telephone number before registering ." , "conflict");
            }

    }
}
