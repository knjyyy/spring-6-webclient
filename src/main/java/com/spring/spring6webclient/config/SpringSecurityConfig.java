package com.spring.spring6webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@Configuration
public class SpringSecurityConfig {
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService) {
        ReactiveOAuth2AuthorizedClientProvider reactiveOAuth2AuthorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager
                authorizedClientServiceReactiveOAuth2AuthorizedClientManager
                = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager
                (reactiveClientRegistrationRepository, reactiveOAuth2AuthorizedClientService);
        authorizedClientServiceReactiveOAuth2AuthorizedClientManager
                .setAuthorizedClientProvider(reactiveOAuth2AuthorizedClientProvider);
        return authorizedClientServiceReactiveOAuth2AuthorizedClientManager;
    }
}
