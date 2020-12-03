package ru.otus.services.impl;

import org.flywaydb.core.Flyway;
import ru.otus.components.DbConfig;
import ru.otus.services.MigrationService;

public class FlywayMigrationServiceImpl implements MigrationService {
    private final Flyway flyway;

    public FlywayMigrationServiceImpl(String path, DbConfig dbConfig) {
        this.flyway = Flyway.configure()
                .dataSource(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword())
                .locations(path)
                .load();
    }

    @Override
    public void migrate() {
        flyway.migrate();
    }

}
