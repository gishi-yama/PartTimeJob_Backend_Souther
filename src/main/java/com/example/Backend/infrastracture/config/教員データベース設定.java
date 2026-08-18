package com.example.Backend.infrastracture.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class 教員データベース設定 {

    @Bean("教員データソース設定")
    @Primary
    @ConfigurationProperties("spring.datasource.kyouin")
    public DataSourceProperties 教員データソース設定() {
        return new DataSourceProperties();
    }

    @Bean("教員データソース作成")
    @Primary
    public HikariDataSource 教員データソース作成(
            @Qualifier("教員データソース設定") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("教員JdbcClient作成")
    @Primary
    public JdbcClient 教員JdbcClient作成(
            @Qualifier("教員データソース作成") DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }

    @Bean("教員データソース初期化")
    public DataSourceInitializer 教員データソース初期化(
            @Qualifier("教員データソース作成") DataSource dataSource) {
        var initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(new ClassPathResource("DataBase/kyouin.sql")));
        return initializer;
    }
}

