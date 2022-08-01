# "Новости"
## Модель  

```
class News {
  long id
  NewsType type
  String content
  String title
  boolean isImportant
  boolean published //false по умолчанию
  LocalDateTime endTime
  List<String> pictures //массив адресов картинок связанных с новостью
}
```  
### 1. рефактор модели

## Клиент
### 1 Контроллер получения новостей.   
```
GET /api/client/news -> List<ClientNewsResponseDto>
```
1. Создать контроллер ClientNewsRestController
2. контроллер возвращает список предстоящих новостей, у который published = true.
3. Получение дто осуществляем одни запросом в бд. этот запрос добавить в NewsDao (NewsService соответственно)  

```
class ClientNewsResponseDto {
  id,
  title,
  type,
  content
}
```

## Менеджер
### 1 Круд контроллер новостей  
1. ManagerNewsController
1. получение, создание и изменение новостей
1. Методы принимают ManagerNewsRequestDto, а отдают ManagerNewsResponseDto


```
/api/manager/news
```

```
class ManagerNewsRequestDto{
  title
  content
  type
  isImportant
  endTime // проверка даты, что она будет в будущем
}
```

```
class ManagerNewsResponseDto {
  id
  title
  content
  type
  isImportant
  endTime
  published
  pictures
}
```
### 2 Публикация новостей
1. добавить в контроллер метод изменение публикации новости
1. контроллер принимает в теле массив айдишников, новости которых надо сделать опубликованными.
1. В ответ возвращается мапа, в которой указаны айдшники новостей, которые нельзя/не получилось опубликовать. в значении мапы указать причину, по которой новость не опубликована. Новость нельзя опубликовать по след причинам:
  - новость не существует
  -  endData новости уже прошла

```
PUT List<Long> /api/manager/news/publish -> Map<Long, String>
```
### 3 Снятие новости с публикации
1. добавить в контроллер метод изменение статуса опубликованности новости
1. контроллер принимает в теле массив айдишников, новости которых надо снять с публикации.
1. В ответ возвращается мапа, в которой указаны айдшники новостей, которые нельзя/не получилось снять с публикации. в значении мапы указать причину, по которой новость не снята с публикации. Новость нельзя снять с публикации:
  - новость не существует
  -  endData новости уже прошла

```
PUT List<Long> /api/manager/news/unpublish -> Map<Long, String>
```

### 4 добавление картинок к новости
1. добавить эндпоинт, сохраняющий список картинок для новости

```
PUT List<String> /api/manager/news/{id}/pictures/ -> Void
```

# "Контактные данные питомца"
## Модель
Сущность создается автоматически при создании питомца, информация для заполнения берется из сущности клиента, создается уникальный код
```
class PetContact {
    Long id;
    String ownerName;
    String address;
    Long phone;
    String description; //сообщение нашедшему
    String code; //сделать неизменным
    private Pet pet // oneToOne;
```
### 1. Рефактор модели
1. Исправить модель и связь с питомцем, сделать связь OneToOne

## Клиент  
### 1. Контактные данные
1. создать контроллер (PetContactController) для контактных данных питомца, при чем  
  - code не должен изменяться  

```
GET /api/client/pet/contact?petId -> PetContactResponseDto
```
```
PUT PetContactDto -> /api/client/pet/contact?petId -> PetContactDto
```  
```
  class PetContactDto {
    String ownerName;
    String address;
    Long phone;
    String description;
  }
```
### 2. QR-code

1. Исправить логику генерации qr-кода. Этот код должен содержать абсолютный путь (не относительный) в эндпоинту http://{хост приложения}/petfound?{code}
2. Перенести этот метод в PetContactController

```
GET /api/client/pet/contact/qr?{petId(обязательный)} -> возвращает картинку с qr кодом
```
# Находка питомца
## Модель  
```
public class PetFound {
    private Long id;
    private String latitude;
    private String longitude;
    private String message;
    private Pet pet;
    private LocalDateTime foundDate // дата создания сущности, заполняется присохранении
```  
### 1. рефактор модели  

### 2. Контактная информация нашедшему питомца  
1. эндпоинт получения контактной информации  
```
GET /api/petfound?{code} (обязательный параметр) -> PetContactDto
```  
2. Сохранение сущности petFound + отправка сообщения
```
POST PetFoundDto -> /api/petfound?{code} -> Void
```
```
public class PetFoundDto {
    private String latitude;
    private String longitude;
    private String text;
}
```
3. Сделать ```/api/petfound``` доступным для всех
<!-- //TODO добавить функционал яндекс карт на фронт + доработать хранение координат/адреса -->

### 3. История находок питомца
1. в PetFoundClientController создать эндпоинт получения истории находок питомца  
```
GET /api/client/petfound?petId -> List<PetFoundClientDto> (сортировка по дате)
```
```  
public class PetFoundClientDto {
    private Long id;
    private String latitude;
    private String longitude;
    private String text;
    private LocalDateTime foundDate;
}
```    

# Профиль пользователя

## Модель

### 1. Создание модели  
1. Создать сущность + дао + сервис  
1. В TestDataInitialiser добавить создание по одщной сущности на каждого user  
```
class Profile{
  User user; //onetToOne + MapsId
  String avatarUrl;
  string firstName;
  string lastName;
  LocalDate birthDate;
  string discordId;
  string telegramId;
}
```  

### 2. Рефактор сущности User  
1. Удалить из User все поля, которые есть в сущности Profile  
1. Поправить все мапперы, которые превращали User (и наследников) на использование данных из Profile
1. Поправить все упавшие тесты (не закомментировать а именно исправить, чтобы они работали)  
1. Удалить из существующих контроллеров методы, изменяющие поля Profile (особое внимание ClientController)  

### 3. Создание контроллера UserProfileController  
1. контроллер для работы с сущностью Profile  
```
GET /api/user/profile -> ProfileDto
PUT ProfileDto /api/user/profile -> Void
```

# Интеграция с discord

## DiscordModule

1. Создать мавен модуль discord под интеграцию с дискордом.
2. создать в этом модуле проект для работы с вебхуками дискорда (нам поднадобится spring boot web + lombok + feign client)  
3. Создать иерархию папок внтури com.vet42.discord  
```  
service  
model  
feign  
```
в каждый пакет положить по пустому гит файлу, чтобы структуру можно было в репу добавить

## Feign client and dto  
1. создать дто классы, которые будут представлять из себя отправляемые в дискорд сообщения. Их структуру взять из discordApi
2. создать феигн клиент, который будет отправлять сообщения в канал в дискорде (используя выше созданные дто)
3. использовать вебхук для тестов:
```
https://discord.com/api/webhooks/993487572003213342/LV3qfF2IcKhsKIQQrv4TPD6w180ALKTXJh0gmJrlO1pg1JLfM1NRzLb3rl1VaQSOKIRG
```  



 ## Сущность сохраненного в дискорде сообщения

 1. Создать модель DiscordMessage + дао
```
class DiscordMessage{
  id, //id в базе
  discordMsgId // id сообщения в дискорде (привязывается при отправке сообщения в чат) не может быть пустым
}
```  

## Сервис отправки сообщений в дискорд
1. Сервис DiscordMessageService должен иметь 3 метода:  
 - sendMessage(MessageDto) -> DiscordMessage - метод отправляет сообщение в чат, сохраняет сущность в базу и возвращает эту сущность)  
 - deleteMessage(discordMessageId) - метод удаляет сообщение из дискорда и удаляет его из базы  
 - editMessage(discordMessageId, MessageDto) - изменяет сообщение в дискорде.  
   

<!--
Для привязки учетной записи дискорда к профилю сделаем следующее, создадим персональный код для пользователя и отдадим ему код с инструкцией о том, куда в дискорде этот код отправить. Бот, слушающий ивенты в дискорде получит код и сохранит discordId в профиль. После этого дискорд токен будет удален.

## DiscordToken

1. Модель
```
class DiscordToken {
  profile;
  string token;
}
```
2. Контроллер генерации токена
```
GET /api/user/profile/discord -> String
```
 -->
# Медосмотр

## Рефактор контроллера:  
1. Перенести всю бизнеслогику в ClinicalExaminationService, в контроллере оставить только валидацию
2. перевести сваггер на русский язык
3. контроллер сделать согласно следующему апи:  
```
POST ClinicalExaminationRequestDto -> /api/doctor/exam?petId={petId}
GET /api/doctor/exam?petId={petId} -> List<ClinicalExaminationResponseDto>  
GET /api/doctor/exam/{examId} -> ClinicalExaminationResponseDto
PUT ClinicalExaminationRequestDto -> /api/doctor/exam/{examId} -> Void
DELETE /api/doctor/exam/{examId} -> Void
```
```
ClinicalExaminationRequestDto {
  Double weight;
  Boolean isCanMove;
  String text;
}
```
```
ClinicalExaminationResponseDto {
   Long id;
   Long petId;
   Double weight;
   Boolean isCanMove;
   String text;
}
```
4. При добавлении нового  медосмотра обновлять данные о весе питомца
5. При изменении последнего медосмотра для питомца обновлять данные о весе питомца

# Препараты

## Управление препаратами менеджером

1. Исправить контроллер MedicineController:
```
GET api/manager/medicine?manufactureName(не обязательный)&name(не обязательный)&searchText(не обязательный) -> List<MedicineResponseDto> (объединить его с /search)
GET api/manager/medicine/{id} -> MedicineResponseDto
DELETE api/manager/medicine/{id} -> Void
PUT MedicineRequestDto -> api/manager/medicine/{id} -> MedicineResponseDto
POST MedicineRequestDto -> api/manager/medicine/ -> MedicineResponseDto

```
```
MedicineResponseDto {
  id,
  manufactureName,
  name,
  iconUrl,
  description,
  creationDateTime;
  lastUpdateDateTime;
  UserInfoDto createAuthor;
  UserInfoDto lastUpdateAuthor;
}
```
```
MedicineRequestDto {
  manufactureName, //notNull
  name, //notNull
  iconUrl, //notNull
  description, //notNull
}
```
2. методы по установке картинки  - удалить
3. тесты поправить
4. Сортировка выдачи списка препаратов осуществляется сперва по имени производителя, затем по названию препарата
