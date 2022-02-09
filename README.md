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
- **Port for test database is 5434.**


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

```
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
### Interact with our APIs using browser
http://localhost:8080/swagger-ui.html

### Generate API documentation based on annotations
Quick and easy way to document your APIs.

[Source code](https://github.com/springdoc/springdoc-openapi-demos/blob/master/springdoc-openapi-spring-boot-2-webmvc/src/main/java/org/springdoc/demo/app2/api/UserApi.java) — check what annotations are used



# Integration Test Guide

1. IT class should extend ControllerAbstractIntegrationTest.class
2. IT class should be annotated with @DBRider.
3. Test method should use @DataSet for generation database content.  
4. @DataSet could be with [possible values](https://database-rider.github.io/getting-started/#configuration). 
5. For more information check [official Database Rider documentation](https://database-rider.github.io/database-rider/1.23.0/documentation.html)

# Database Diagram

To change login via google-account petclinic.vet24@gmail.com

Last update 20.05.2021 12:50

https://dbdiagram.io/d/60a62c9db29a09603d15bc72

# Sonarqube Guide

Для работы с Sonarqube в Idea нужно подключить плагин Sonarlint.
1. Идём в настройки IDEA и в разделе Plugins находим плагин SonarLint и устанавливаем его.
2. Открываем панель плагина, кликаем по иконке "Configure SonarLint".
3. Далее "Configure the connection...", добавляем новый (SonarQube, не SonarCloud). URL: http://91.241.64.154:9000/
4. Выбираем авторизацию по логину и паролю, вводим. Ok. Finish.
5. Из выпадающего списка выбираем проект "pet_clinic". В Project key указываем pet_clinic. Ok.
6. Если не получилось - гайд с картинками: https://habr.com/ru/company/krista/blog/469963/

# Sonarqube. Поднять контейнер на сервере с нуля.

1. Открыть командную строку в ОС и ввести ssh john@91.241.64.154, после запроса пароля указать r43naj5QV2
2. Ввести команду sudo su и указать пароль r43naj5QV2
3. Проверить есть ли конфигурационный файл docker-compose.yml на сервере командой **cd /sonarqube** , далее **ll**.
4. Если файла нет, то ввести команду **nano docker-compose.yml** и скопировать содержимое файла docker-compose.yml из директории devops/sonarqube проекта, после чего сохранить файл на сервере.
5. Проверить наличие директорий для sonarqube и postgresql по аналогии с п.3:
   /data/docker-volume/sonarqube1/data
   /data/docker-volume/sonarqube1/extensions
   /data/docker-volume/sonarqube1/logs
   /data/docker-volume/postgres_sonar
   Если их нет, то создать командой **mkdir /названия/нужных/директорий**
   И скопировать плагины сюда /data/docker-volume/sonarqube1/extensions из директории /sonarqube/plugins командой **cp -r /sonarqube/plugins /data/docker-volume/sonarqube1/extensions/**
7. Запустить контейнеры командой **docker-compose up -d**
8. В браузере перейти по http://91.241.64.154:9000/ и залогиниться логин/пароль admin
9. Сменить пароль админу на qwertyASDFGH123456 , также добавить пользователя:
   login - petclinic.vet24@gmail.com
   password - MFbeSVsb
10. Создать проект с project key и display name ==> pet_clinic
11. Сгенерировать токен по названию pet_clinic
12. Открыть файл .gitlab-ci.yml (с браузером закончили, файл лежит в склонированном в вашу IntelliJ IDEA проекте) и указать в значении переменной SONAR_PROJECT_KEY токен.
13. Готово, всё работает.

# Flyway Guide

Flyway - это инструмент для миграции скриптов в формате sql,
расположенных в pet_clinic\web\src\main\resources\db\migration
В данном случае с их помощью создаются таблицы по всем сущностям базы данных,
что позволит легко создать ту же самую структуру БД при переносе на другой сервер.
Правила написания миграций: https://flywaydb.org/documentation/concepts/migrations

# Профили

1. Используется по умолчанию: local.
2. Для запущенного сервера: prod.
3. Для тестирования в среде разработки: test.
4. Для прогона тестов на сервере: testprod.

Для изменения профиля "local" на профиль "test" необходимо выполнить следующие действия:

1. В боковом меню открываем дерево проекта, выбираем JUnit тест и правой кнопкой мыши вызываем выпадающее меню.
2. В меню выбираем пункт "More Run/Debug" -> "Modify Run Configuration..."
3. Открывается окно создания конфигурации.
4. В разделе "Build and run" выбираем поле ввода аргументов командной строки "VM options" 
   и добавляем к ключу "-ea" ключ "-Dspring.profiles.active=test".
   (Получится "-ea -Dspring.profiles.active=test")
5. Нажимаем "Apply" и "OK". В результате профиль изменится и тест будет доступен для запуска в панели навигации.

Для смены профиля при запуске сервера, выбираем в дереве проекта com.vet24.web.PetClinicApplication
и правой кнопкой мыши вызываем выпадающее меню.

1. В выпадающем меню выбираем пункт "Edit Configurations..."
2. В поле "Active profiles" пишем имя профиля "testprod".
3. Нажимаем "Apply" и "OK".

# JsonView

Работа с JsonView происходит в классе DTO и контроллере использующем его.

Игнорирование поля(ей) будет в одном методе контроллера или методы будут игнорировать одни и те же поля:
1. Пометить игнорируемые поля DTO аннотацией @JsonView(View.Ignore.class)
2. Пометить необходимый метод контроллера аннотацией @JsonView(View.Public.class)

Игнорирование поля(ей) будет в нескольких методах контроллера и необходимо игнорировать разные поля:
1. Пометить (при необходимости) игнорируемые во всех методах контроллера поля DTO аннотацией 
@JsonView(View.Ignore.class)
2. Для каждого метода контроллера пометить необходимые ему поля аннотацией @JsonView(View.Public{number}.class). 
Этой же аннотацией пометить сам метод. При необходимости создать в классе View классы аналогичным образом

Методы контроллера не содержащие аннотацию @JsonView будут использовать все поля DTO
