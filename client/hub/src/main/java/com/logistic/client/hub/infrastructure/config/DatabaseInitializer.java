package com.logistic.client.hub.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializer {

  @Bean
  public CommandLineRunner loadData(DataSource dataSource) {
    return args -> {
      ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
          new ClassPathResource("db/migration/V1__hub_data.sql")
      );
      DatabasePopulatorUtils.execute(populator, dataSource);
    };
  }
}
