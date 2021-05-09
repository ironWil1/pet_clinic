**Table of Contents**

[[_TOC_]]

# Docker Guide
### Start Postgres

To create and start the database enter the following command in the terminal in the root folder of the project

```docker-compose up -d```

To stop service

```docker-compose stop```

Start service

```docker-compose start```

Stop and remove containers

```docker-compose down```

- Our database is available on the local computer under port 5433 and container port 5432


# Dao and Service Guide
### How to create Dao and Service for new Entities


1. Each new Dao interface should extend generic interface ReadOnlyDao<K, T> or ReadWriteDao<K, T>. ReadWriteDao interface includes methods from ReadOnlyDao interface.
2. Generic K is key for entity. Generic T is entity. For example: `ExampleDao extends ReadWriteDao<Long, Example>`.
3. Each Dao implementation class should extend generic abstract class ReadOnlyDaoImpl<K,T> or ReadWriteDaoImpl<K, T> and implement their own interface.
4. There are a few simple realization of methods in generic Dao. New methods should be created in Dao interface of current entity.
5. Same rules for Service classes except one thing. Service implementation should create constructor super.

# Lombok Guide
### Use Lombok annotations to generate code
Don't write boilerplate code. Use [Lombok](https://javarush.ru/groups/posts/2753-biblioteka-lombok) annotations to generate it for you.

```text
@NoArgsConstructor // Generates constructor that take no arguments.
@AllArgsConstructor // Generates constructor that take one argument per final / non-nullfield.
@RequiredArgsConstructor // Generates constructor that take one argument for every field.
@Getter // Never write public int getFoo() {return foo;} again.
@Setter // Never write setters again.
@EqualsAndHashCode // Generates hashCode and equals implementations from the fields of your object.
@ToString // Generates a toString.
@Data // Combines @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
```


# Swagger Guide
### Interact with APIs using browser
http://localhost:8080/swagger-ui.html

### Generate API documentation based on annotations
Quick and easy way to document your APIs.
[Live demos](https://github.com/springdoc/springdoc-openapi-demos) with source code.