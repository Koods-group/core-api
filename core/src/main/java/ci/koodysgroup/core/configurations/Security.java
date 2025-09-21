package ci.koodysgroup.core.configurations;


import ci.koodysgroup.service.LogoutService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    JwtAuthentificationFilter filter;

    @Autowired
    LogoutService service;

    @Autowired
    AuthenticationProvider provider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/resource/**",
                                "api/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authEx) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.setCharacterEncoding("UTF-8");
                            res.getWriter().write("""
                            {
                              "code": "unauthorized",
                              "message": "%s",
                              "success":false
                            }
                        """.formatted(authEx.getMessage()));
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/sign-out")
                        .logoutRequestMatcher(request ->
                                "DELETE".equals(request.getMethod()) && "/api/auth/sign-out".equals(request.getRequestURI())
                        )
                        .addLogoutHandler(service)
                        .logoutSuccessHandler((request, response, authentication) ->
                                {
                                    SecurityContextHolder.clearContext();
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    response.getWriter().write("""
                                        {
                                          "code": "success",
                                          "message": "You have been successfully logged out, We look forward to seeing you again .",
                                          "success":true
                                        }
                                    """);
                                }
                        )
                )
                .build();
    }
}
