# 1. Introduction
- Flyway is a java open source library.
- Flyway updates a database from one version to a next using migrations. 
- We can write migrations either in SQL with database specific syntax or in Java for advanced database transformations.
---
# 2. Flyway Gradle Plugin
- Add class DBMigration in project
```java
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
            dbMigration.setStrictMode(false);
            dbMigration.generateMigration();
        }
    }
```
- Gradle

```java
    import org.flywaydb.gradle.task.FlywayMigrateTask
    plugins {
        id "org.flywaydb.flyway" version "5.0.7"
    }

```

```java 
    //  Define version and description increment when create models 
    def verGraphQl = '1.0.0'
    def description = "First add model"
```

```java
    // Load setting info from resource file
    Properties localBootRunProperties(env = String) {
        Properties p = new Properties()
        p.load(new FileInputStream(
                file(project.projectDir).absolutePath + "/src/main/resources/application-"+ env +".yml"))
        return p
    }
```
```java
    // Create task generate sql
    task generateSql(type: JavaExec) {
        main = "com.gnt.flyway.DBMigration"
        args(verGraphQl, description)
        classpath = sourceSets.main.runtimeClasspath
    }
```

```java
    // Create task flyway default
    flyway {
        def properties = localBootRunProperties("prod")
        def urlDb = properties.get("databaseUrl")
        def usernameDb = properties.get("username")
        def passwordDb = properties.get("password")
        url = urlDb
        user = usernameDb
        password = passwordDb
        placeholderReplacement = false
    }
```
We can create task for multiple environments setting
```java
    task flywayMigrateDev(type: FlywayMigrateTask) {
        def properties = localBootRunProperties("dev")
        def urlDb = properties.get("databaseUrl")
        def usernameDb = properties.get("username")
        def passwordDb = properties.get("password")
        url = urlDb
        user = usernameDb
        password = passwordDb
        placeholderReplacement = false
    }
```
---
# 3. How to run
- Step 1: View version executed in DB
    ```java
        gradlew flywayInfo
    ```
- Step 2: Execute task create SQL Query
    ``` java
        gradlew generateSql
    ```
- Step 3: Execute task create table to DB
     ``` java
        gradlew flywayMigrateDev
    ```
---
