package com.gnt.flyway;

import io.ebean.annotation.Platform;

import java.io.IOException;

public class DBMigration {

    public static void main(String[] args) throws IOException {
        System.setProperty("ddl.migration.generate", "true");
        if (args.length > 0) {
            System.setProperty("ddl.migration.version", args[0]);
        }
        if (args.length > 1) {
            System.setProperty("ddl.migration.name", args[1]);
        } else {
            System.setProperty("ddl.migration.name", "Change model");
        }
        io.ebean.dbmigration.DbMigration dbMigration = io.ebean.dbmigration.DbMigration.create();
        dbMigration.setPlatform(Platform.MYSQL);
        // dbMigration.setGeneratePendingDrop("V1.0.1__First_add_model.model.xml");
        dbMigration.setStrictMode(false);
        dbMigration.generateMigration();
    }
}
