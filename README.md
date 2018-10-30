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
    // Before running generate sql command must increase version graphql number 
    // EX:  def verGraphQl = '1.0.0' to def verGraphQl = '1.0.1' 
    // EX:  def description = "First add model" to def description = "Change model ..."
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
## a. Normal generate sql:
- <b>Step 1: </b> View version executed in DB
```java
   gradlew flywayInfo
```
- <b> Step 2: </b>

     Before running generate sql command must increase version graphql number <br/>
     EX:  def verGraphQl = '1.0.0' to def verGraphQl = '1.0.1' <br/>
     EX:  def description = "First add model" to def description = "Change model ..."
     ```java
     def verGraphQl = '1.0.0'
     def description = "First add model"
     ```
- Step 3: Generate sql with command  
  ``` java
        gradlew generateSql
    ```
- Step 3: Execute task create table to Migrate Sql from local to server DB
     ``` java
        gradlew flywayMigrateDev
    ```
## b : Generate peding drop column name 

- Step 1: Add generate peding drop to  class DBMigration
```java
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
        // Add version pedding drop
        dbMigration.setGeneratePendingDrop("V1.0.1__First_add_model.model.xml");
        dbMigration.generateMigration();
    }
```
- Step 2: Do all step in [## a. Normal generate sql](#a-normal-generate-sql)
    
---
