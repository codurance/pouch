package io.codurance.pouch;

import io.codurance.pouch.domain.Entity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.AfterLoadEvent;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Field;

@Configuration
@EnableJdbcRepositories
public class PouchApiConfig extends JdbcConfiguration {

    @Bean
    NamedParameterJdbcOperations operations() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/api");
    }

    @Bean
    public ApplicationListener<AfterLoadEvent> afterLoadEvent() throws NoSuchFieldException {

        Field isExistingField = Entity.class.getDeclaredField("isExisting");

        return event -> {
            var entity = event.getEntity();

            if(!(entity instanceof Entity))
                return;

            setAsExisting(entity, isExistingField);
        };
    }

    private void setAsExisting(Object entity, Field isExistingField) {
        try {
            isExistingField.setAccessible(true);
            isExistingField.setBoolean(entity, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
