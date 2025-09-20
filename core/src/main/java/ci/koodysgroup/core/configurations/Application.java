package ci.koodysgroup.core.configurations;

import ci.koodysgroup.domains.entities.Country;
import ci.koodysgroup.repositories.AccessRepository;
import ci.koodysgroup.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Configuration
public class Application {

    @Autowired
    AccessRepository repository;

    @Autowired
    CountryRepository countryRepository;

    @Bean
    public UserDetailsService userDetailsService() {

            return login -> repository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Your session may have expired. Please log in again ."
                    ));
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService());
        authProvider.setPasswordEncoder(this.passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }

}
