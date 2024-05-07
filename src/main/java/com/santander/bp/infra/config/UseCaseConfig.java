package com.santander.bp.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.santander.bp.domain.AppArsenalProvider;
import com.santander.bp.domain.usecase.AppArsenalUseCase;

/** Use case config. */
@Configuration
public class UseCaseConfig {

    /**
     * App arsenal use case.
     *
     * @param appArsenalProvider {@link AppArsenalProvider}
     * @return {@link AppArsenalUseCase}
     */
    @Bean
    public AppArsenalUseCase appArsenalUseCase(AppArsenalProvider appArsenalProvider) {
        return new AppArsenalUseCase(appArsenalProvider);
    }
}