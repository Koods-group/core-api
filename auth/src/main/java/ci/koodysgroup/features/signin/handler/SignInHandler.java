package ci.koodysgroup.features.signin.handler;

import ci.koodysgroup.domains.dtms.AccessDtm;
import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Token;
import ci.koodysgroup.features.signin.command.SignInCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.AccessRepository;
import ci.koodysgroup.repositories.TokenRepository;
import ci.koodysgroup.service.JwtService;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class SignInHandler implements CommandHandler<SignInCommand , CommandResponse<AccessDtm>> {

    private final AccessRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    @Autowired
    JwtService service;


    @Override
    public CommandResponse<AccessDtm> handler(SignInCommand command) {
        try{

            Access access = repository.findByLogin(command.getLogin())
                    .orElseThrow(() -> new NoSuchElementException(" Sorry ! You are not authorised to perform this action ."));

            if(Objects.equals(access.getConnected(), "connected")){
                return CommandResponse.error("Existing connection", "Sorry! you appear to be logged in on another device, Please log out of that device ." , "conflict");
            }

            if(!Objects.equals(access.getConnected() , "pending")){
                return CommandResponse.error("Connection not initiated", "You do not have a pending connection, we cannot proceed with your request ." , "conflict");
            }

            try{
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(access.getLogin(),command.getPassword()));

                UserDetails details = (UserDetails) authentication.getPrincipal();

                access.setConnected("connected");
                access = repository.save(access);

                assert details != null;
                String bearer = service.generateToken(details.getUsername());

                Token token = new Token(LocalDateTime.now().plusHours(24),access,bearer);
                tokenRepository.save(token);

                return CommandResponse.success("Logged-in user",AccessDtm.fromAccessDtm(access , token.getToken()));

            }catch(BadCredentialsException ex){
                return CommandResponse.error("Invalid access", "Sorry! your credentials do not match, Please try again with valid credentials ." , "not_found");
            }


        }catch (Exception e){
            return CommandResponse.error("Error", e.getMessage() , "not_found");
        }
    }
}
