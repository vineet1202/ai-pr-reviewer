package com.vineet.ai_code_reviewer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.vineet.ai_code_reviewer.security.GithubOAuth2UserService;
import com.vineet.ai_code_reviewer.util.OAuth2LoginSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            GithubOAuth2UserService githubOAuth2UserService,
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler)
            throws Exception {

        http
        	.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/api/review").permitAll()
                .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )

            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(githubOAuth2UserService)
                )
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(authenticationFailureHandler())
            )
            .exceptionHandling(exceptions -> exceptions
            	    .defaultAuthenticationEntryPointFor(
            	        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
            	        request -> request.getServletPath().startsWith("/api")
            	    )
            	)

            .logout(Customizer.withDefaults());

        return http.build();
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            log.error("OAuth2 login failed", exception);
            response.sendRedirect("/login?error");
        };
    }
}