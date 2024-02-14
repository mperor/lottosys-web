package mapi.lotto;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Profile("test")
@Configuration
public class EmptyMigrationStrategyConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            Flyway noJavaMigrationFlyway = Flyway.configure()
                    .configuration(flyway.getConfiguration())
                    .javaMigrations(new JavaMigration[]{})
                    .javaMigrationClassProvider(() -> Collections.emptyList())
                    .load();

            noJavaMigrationFlyway.migrate();
        };
    }
}
