package ci.koodysgroup.service;

import ci.koodysgroup.repositories.AccessRepository;
import ci.koodysgroup.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    TokenRepository repository;

    @Autowired
    AccessRepository accessRepository;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }

        final String token = header.split(" ")[1].trim();

        repository.findByToken(token).ifPresent(dbToken -> {
            accessRepository.findById(dbToken.getAccess().getId()).ifPresent( access -> {
                access.setConnected("unconnected");
                accessRepository.save(access);
            });
            repository.delete(dbToken);
        });

    }
}
