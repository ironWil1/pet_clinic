**Table of Contents**

[[_TOC_]]

# Ветклиника
## Немного описания. 

Проект ветклиники - это бек сервер приложения, работающего в втеклинике. В разрабатываемый/планируемый функционал входят:   

1. Личный кабинет (далее ЛК) клинента  
1. ЛК доктора  
1. ЛК админа  
1. ЛК контент менеджера  
1. Форум  
1. Интеграция с соцсетями и менеджерами

#### Функионал клиента. 

1. Вести карточку домашего животного (электронный ветпаспорт)  
1. Просматривать календарь приема и записывать питомца на прием к конкретному или дежурному доктору. 
1. Заводить электронный адресник и использовать страница поиска пропавших животного  
1. Оставлять отзывы докторам
1. Создавать топики на форуме и отвечать в созданных и открытых топиках
1. Подписываться на уведомления о новостях и акциях

#### Функционал доктора. 

1. Создавать/редактировать/удалять записи о проведенных процедурах и медосмотрах
1. Просматривать информацию об истории болезни питомца  
1. Назначать и изченять программу лечения питомца

#### Функионал админа

1. создавать/редактировать/удалять учетные записи на платформе  
1. создавать/редактировать/удалять комментарии пользователей
1. создавать/редактировать/удалять/закрывать топики на форуме

#### Функионал контекнт менеджера

1. наполнение бд сайта конктентом 

(описание будет дополнятся со временем)


## Используемые технологии  

На данный момент в проекте используются следующий стек: 

1. Spring boot 
1. Spring web
1. Spring security
1. PostgreSQL
1. JWT
1. swaqgger
1. Docker
1. gitlab ci/cd  

При необходимости и желании стек можно и нужно расширять, главное чтоб для этого были причины и не было повторяющегося функционала  



## Порядок работы  

Для управления версиями кода проекта используется Gitlab.  
Для управления задачами так же используется доска Gitlab (Issues -> Boards)  
Рабочие задачи находятся на доске [Development](https://git.kata.academy/Alexander_Shcherbakov/pet_clinic/-/boards/20): 

1. Колонки To Do * - содержат задачи, готовые для выполнения. 
1. Колонка Doing - содержит задачи, которые находятся в работе в данный момент (не забываем переносить их сюда)  
1. Колонка Need pre-review - задачи выполненые, должны проверяться двумя студентами. После каждой проверки необходимо поставить лайк или дизлайк. Если задача выполненна не верно, то необходимо пренести её в In Progress. Задача должна набрать 2 лайка, для переноса в "Need Review".
1. Колонка "Need Review" - задачи выполненые, проверенные, ожидают ревью ментора.

Задача должна пройти ревью одного из членов команды и ревью лида. Кидайте в общий чат ссылку на карточку, которая требует ревью и сообщение о том, что задача ожидает ревью.
При проведении ревью - делитесь опытом и давайте советы коллеге, от вас не требуется "забраковать" задачу.

На доске [Info](https://git.kata.academy/Alexander_Shcherbakov/pet_clinic/-/boards/85) находится полезная информация: 

1. Колонки Open/Close - все открытые и закрыте карточки
1. Колонка INFO - карточки с полезной информацией, с ними необходимо ознакомиться
1. Колонка In Development - тут находятся карточки на этапе разработки ТЗ 

Правила решения задач описаны [тут](https://git.kata.academy/Alexander_Shcherbakov/pet_clinic/-/issues/6)  
Кодстайл описан [тут](https://git.kata.academy/Alexander_Shcherbakov/pet_clinic/-/issues/23)   


### Gitflow

Используем [git-flow](https://nvie.com/posts/a-successful-git-branching-model/), свою ветку создаем от master ветки (за редким исключением, когда одну задачу делим на подзадачи)  

Коммитим в репозиторий [атомарно](https://sashasushko.tumblr.com/post/174690191358/good-commit)  

Один коммит - одно изменение.
Один Merge Request - одна задача.

Ветку создаём с префиксом номера карточки задачи. 

## Структура проекта  

Код поделен на несколько модулей: 
 
1. model - содержит сущности и дто
1. dao (abstract + impl) - содержит дао слой
1. service (abstract + impl) - содержит бизнеслогику
1. security - содержит все, что связанно с сесурити
1. web - все, что связано с веб (контроллеры, контроллер эдвайсы и тд)
1. discord - модуль для интеграции с дискордом (в перспективе будет вынесен в отдельный сервис)
 
# Docker Guide
### Start Postgres

To create and start the database enter the following command in the terminal in the docker-for-local folder of the project

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
6. Тестирование у нас производится с использованием библиотеки testcontainers для поднятия контейнера с базой под тесты

# Database Diagram

To change login via google-account petclinic.vet24@gmail.com

Last update 20.05.2021 12:50

https://dbdiagram.io/d/60a62c9db29a09603d15bc72

<!--
[//]: # ()
[//]: # (# Sonarqube Guide)

[//]: # ()
[//]: # (Для работы с Sonarqube в Idea нужно подключить плагин Sonarlint.)

[//]: # (1. Идём в настройки IDEA и в разделе Plugins находим плагин SonarLint и устанавливаем его.)

[//]: # (2. Открываем панель плагина, кликаем по иконке "Configure SonarLint".)

[//]: # (3. Далее "Configure the connection...", добавляем новый &#40;SonarQube, не SonarCloud&#41;. URL: http://91.241.64.154:9000/)

[//]: # (4. Выбираем авторизацию по логину и паролю, вводим. Ok. Finish.)

[//]: # (5. Из выпадающего списка выбираем проект "pet_clinic". В Project key указываем pet_clinic. Ok.)

[//]: # (6. Если не получилось - гайд с картинками: https://habr.com/ru/company/krista/blog/469963/)

[//]: # ()
[//]: # (# Sonarqube. Поднять контейнер на сервере с нуля.)

[//]: # ()
[//]: # (1. Открыть командную строку в ОС и ввести ssh john@91.241.64.154, после запроса пароля указать r43naj5QV2)

[//]: # (2. Ввести команду sudo su и указать пароль r43naj5QV2)

[//]: # (3. Проверить есть ли конфигурационный файл docker-compose.yml на сервере командой **cd /sonarqube** , далее **ll**.)

[//]: # (4. Если файла нет, то ввести команду **nano docker-compose.yml** и скопировать содержимое файла docker-compose.yml из директории devops/sonarqube проекта, после чего сохранить файл на сервере.)

[//]: # (5. Проверить наличие директорий для sonarqube и postgresql по аналогии с п.3:)

[//]: # (   /data/docker-volume/sonarqube1/data)

[//]: # (   /data/docker-volume/sonarqube1/extensions)

[//]: # (   /data/docker-volume/sonarqube1/logs)

[//]: # (   /data/docker-volume/postgres_sonar)

[//]: # (   Если их нет, то создать командой **mkdir /названия/нужных/директорий**)

[//]: # (   И скопировать плагины сюда /data/docker-volume/sonarqube1/extensions из директории /sonarqube/plugins командой **cp -r /sonarqube/plugins /data/docker-volume/sonarqube1/extensions/**)

[//]: # (7. Запустить контейнеры командой **docker-compose up -d**)

[//]: # (8. В браузере перейти по http://91.241.64.154:9000/ и залогиниться логин/пароль admin)

[//]: # (9. Сменить пароль админу на qwertyASDFGH123456 , также добавить пользователя:)

[//]: # (   login - petclinic.vet24@gmail.com)

[//]: # (   password - MFbeSVsb)

[//]: # (10. Создать проект с project key и display name ==> pet_clinic)

[//]: # (11. Сгенерировать токен по названию pet_clinic)

[//]: # (12. Открыть файл .gitlab-ci.yml &#40;с браузером закончили, файл лежит в склонированном в вашу IntelliJ IDEA проекте&#41; и указать в значении переменной SONAR_PROJECT_KEY токен.)

[//]: # (13. Готово, всё работает.)

[//]: # (# Flyway Guide)

[//]: # ()
[//]: # (Flyway - это инструмент для миграции скриптов в формате sql,)

[//]: # (расположенных в pet_clinic\web\src\main\resources\db\migration)

[//]: # (В данном случае с их помощью создаются таблицы по всем сущностям базы данных,)

[//]: # (что позволит легко создать ту же самую структуру БД при переносе на другой сервер.)

[//]: # (Правила написания миграций: https://flywaydb.org/documentation/concepts/migrations)-->


# Профили

1. Для локальной разработки профиль: local. 
2. Для запущенного сервера: prod.
3. Для тестирования в среде разработки: test.
4. Для прогона тестов на сервере: testprod.

Для указания профиля тестам необходимо:

1. В боковом меню открываем дерево проекта, выбираем JUnit тест и правой кнопкой мыши вызываем выпадающее меню.
2. В меню выбираем пункт "More Run/Debug" -> "Modify Run Configuration..."
3. Открывается окно создания конфигурации.
4. В разделе "Build and run" выбираем поле ввода аргументов командной строки "VM options" 
   и добавляем к ключу "-ea" ключ "-Dspring.profiles.active=test".
   (Получится "-ea -Dspring.profiles.active=test")
5. Нажимаем "Apply" и "OK". В результате профиль изменится и тест будет доступен для запуска в панели навигации.

Для смены профиля при запуске приложения, выбираем в дереве проекта com.vet24.web.PetClinicApplication
и правой кнопкой мыши вызываем выпадающее меню.

1. В выпадающем меню выбираем пункт "Edit Configurations..."
2. В VM Options добавляем -Dspring.profiles.active=local либо в поле "Active profiles" пишем имя профиля "local".
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
