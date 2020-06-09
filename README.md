# todo-service 

![Java CI](https://github.com/cwansart/testcontainers-university/workflows/Java%20CI/badge.svg)

## Run locally

### One time configuration

To run the application locally download the current [Open Liberty](https://openliberty.io/downloads/#runtime_releases) -- _All GA Features_ is usually a good choice for development. Extract the zip somewhere, for example in your home folder.

Edit the _server.xml_ which can be found in the folder _wlp/usr/servers/defaultServer/server.xml_ and add the following lines:

```
<dataSource id="todoListDS" jndiName="jdbc/TodoListDS" type="javax.sql.ConnectionPoolDataSource">
  <jdbcDriver id="postgresql-driver" javax.sql.XADataSource="org.postgresql.xa.PGXADataSource" javax.sql.DataSource="org.postgresql.ds.PGDataSource" javax.sql.ConnectionPoolDataSource="org.postgresql.ds.PGConnectionPoolDataSource" libraryRef="postgresql-library" />
  <properties.postgresql serverName="localhost" portNumber="5432" databaseName="postgres" user="postgres" password="postgres" />
</dataSource>
<library id="postgresql-library">
  <fileset id="PostgreSQLFileset" dir="${shared.resource.dir}" includes="*.jar" />
</library>
```

Also check the `featureManager` at the top. Make sure it has the entry `<feature>javaee-8.0</feature>`.

After editing the server.xml file you need to download the PostgreSQL driver from [their website](https://jdbc.postgresql.org/download.html). Get the latest JDBC driver and save it in the wlp subfolder _wlp/usr/shared/resources_. You may need to create shared and its subfolders.

### Starting the server

After you configured the server you can run the application either manually by starting Open Liberty and putting your _war_ inside the dropins folder or by deploying it via your IDE.

Before you can access your application, you need to make sure the database is running. To do so start Docker and run:

```bash
$ docker-compose -f docker-compose.db.yml up
```

Now you can deploy the app and use it.
