package com.spring.spring6webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig implements WebClientCustomizer {

    private final String rootUrl;
    private final ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager;

    public WebClientConfig(@Value("${webclient.url}") String rootUrl,
                           ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        this.rootUrl = rootUrl;
        this.reactiveOAuth2AuthorizedClientManager = reactiveOAuth2AuthorizedClientManager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction serverOAuth2AuthorizedClientExchangeFilterFunction
                = new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
        serverOAuth2AuthorizedClientExchangeFilterFunction.setDefaultClientRegistrationId("springauth");
        webClientBuilder
                .filter(serverOAuth2AuthorizedClientExchangeFilterFunction)
                .baseUrl(rootUrl);
    }
}
