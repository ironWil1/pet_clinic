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

### 4. Настройки уведомлений
1. в профиль поля указанные ниже
1. посмотреть все места, где редактируется профиль, на то, что новые поля там теперь тоже тестируются

```
boolean discordNotify // false - дефолтное значение
boolean emailNotify // false - дефолтное значение
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


 ## Отправка сообщений об опубликованных новостях  
 1. Добавить в сущность News связь с DiscrodMessage
 1. Создать Маппер, который будет предобразовывать новость в сообщение для дискорда
 1. при переводе новости в статус опубликованной отправлять сообщение в дискордканал (создавать новую сущность DiscordMessage)
 1. При редактировании новости редиктирование сообщение в дискорде
 1. При удалении новости или при снаятии с публикации удалть сообщение из дискорда (Удалять DIscordMessage)



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

##  Дозировка препаратов
1. Создать модель для дозировки препаратов.
2. создать дао + сервис
3. связь с препаратом делаем oneToMany (одна дозировка может быть связана только с одним препаратом)
4. добавить связь в препараты  
5. проверить что все тесты не перестали работать

```

public class Dosage {
    private Long id;
    private Integer dosageSize; //добавь коммент что именно описывает это поле
    private dosageForm form; // и тут
}

enum DosageForm {
    DROPS, PILLS;
}
```

### Api работы с дозировкой
1. добавить в medicineConntoller api работы с дозировкой
1. При добавлении новой дозировки делать проверку, что для  данного препарата такой дозировки (тип + доза) не было  

```
GET /api/manager/medicine/{medicineId}/dosage -> List<DosageResponseDto>
POST List<DosageRequstDto> -> /api/manager/medicine/{medicineId}/dose -> Void //добавление новых дозировок
DELETE /api/manager/medicine/{medicineId}/dosage/{doseId} -> Void

class DosageRequstDto {
	Integer dosageSize; //not Null
	dosageForm form; //not Null
}

class DosageResponseDto {
	Long id
	Integer dosageSize 
	dosageForm form 
}

``` 

# Аутентификация

### Получение текущего пользователя

1. Добавить эндпоинт ``` GET /api/auth/getCurrent -> AuthResponse ```
2. эндпоинт должен быть доступен только аутентифицированным пользователям  
3. Токен берется из хидера и добавляется в дто  

# Репродукция  

## Client  

### Получение всех записей о репродукции  
1. создать эндпоинт ``` GET /api/client/pet/{petId}/reproduction -> List<ReproductionDto>```  

# User  

## Рефактор  

### Избавляемся от наследования в моделях User  
1. Сделать класс User не абстрактным  
1. Перенести поля из наследников в класс User
1. Удалить всех наследников  
1. Исправить весь застрагиваемый код  
1. Исправить все затрагиваемые тесты

# Pet

## AppearanceController
1. Создать таблицу, которая будет заполнена породами для каждого типа животных (у нас сейчас есть только собака и кошка) - сочетание тип порода должны быть уникальными
1. создать дао и сервис
1. Создать эндпоинт для получения списка пород из нашей бд. text - поле по которому происходит поиск совпадений в таблице (для поиска использовать триграммы в postgres). petType - тип животного, среди пород которых надо искать. Если не указан - искать среди всех. Если и text и petType пустые, то отправлять пустой список
```
GET /api/appearance/breed?petType(необязательный параметр)&text(необязательный параметр) -> List<String>
```  


1. Создать таблицу, содержащую список возможных окрасов животных (каждый цвет - уникальный).
1. создать дао и сервис
1. Создать эндпоинт получения списка окрасов, text - поле по которому происходит поиск совпадений в таблице (для поиска использовать триграммы в postgres). Если text - пустой, то отдавать пустой список
```
GET /api/appearance/color?text(необязательный параметр) -> List<String>
```

## AppearanceManagerController  
  
### AppearanceManagerController color
1. Создать круд контроллер для добавления цветов в базу
2. исключения при добавлении существующих записей игнорировать

```
GET /api/manager/appearance/color?text(необязательный параметр) -> List<String>
POST List<String> -> /api/manager/appearance/color -> Void
DELETE List<String> - > /api/manager/appearance/color -> Void
```

### AppearanceManagerController breed
1. Создать круд контроллер для добавления пород в базу
2. исключения при добавлении существующих записей игнорировать

```
GET /api/manager/appearance/breed?petType(необязательный параметр)&text(необязательный параметр) -> List<String> -> List<String> ()
POST List<String> -> /api/manager/appearance/breed?petType(обязательный параметр) -> Void
DELETE List<String> - > /api/manager/appearance/breed?petType(обязательный параметр) -> Void
```

## PetController -> PetClientController
1. переименовать и переписать контроллер.
1. при запросе Put обновлять в сущности только те поля, которые не null в дто
1. Удалить все остальные методы из контроллера
1. при создании или изменении проверять что breed и color представлены в нашей базу (задача выше)
1. Избавиться от AbstractNewPetDto наследников и мапперов

  ```
  GET api/client/pet -> List<PetResponseDto>
  GET api/client/pet/{petId} -> PetResponseDto
  POST PetRequestDto_Post -> api/client/pet/{petId} -> PetResponseDto
  PUT PetRequestDto_Put -> api/client/pet/{petId} -> PetResponseDto (переименовать PetDto)
  DELETE api/client/pet/{petId} -> Void
  ```

  ```
    PetRequestDto_Post {
      name // notNull
      avatar // nullable
      birthDay // notNull
      petType // notNull
      breed // notNull
      gender // notNull
      color // notNull
      size // nullable
      weight // nullable
      desription // nullable
    }
  ```

  ```
    PetRequestDto_Put {
      name // nullable
      avatar // nullable
      birthDay // nullable
      petType // nullable
      breed // nullable
      gender // nullable
      color // nullable
      size // nullable
      weight // nullable
      desription // nullable
    }
  ```

  ```
  public class PetResponseDto {
      private Long id;
      private String name;
      private String avatar;
      private LocalDate birthDay;
      private PetType petType;
  }
  ```

  # Запись на прием

  ## получение календаря доступных записей на неделю
  1. создать эндпоинт полчение доступных слотов для записи на неделю, при чем - если doctorId не указан, то смотрится среди всех докторов сразу. Если date не указан, то присылать календарь на текущую неделю, если указан, то на неделю, к которой относится дата. Все прошедшие дни присылать как без доступных записей
  1. Условием доступности доктора является то, что доктор в этот день не в отпуске, не на больничном, не на выходном, в это время он на соответствующей смене и в это время у него нет других записей
  1. расписание можно давать только на текущую и следующую неделю, если дата - из прошлого, или дальше чем следующая неделя - отправлять 400 статус
  ```
  GET /api/client/appointment?doctorId(необязательный)&date(необязательный) -> AppointmentCallendarDto
  ```
  ```
  AppointmentCallendarDto {
    List<AppointmentCallendarElementDto> days;
  }
  ```
  ```
  AppointmentCallendarElementDto {
    date;
    List<AppointmentDayElementDto> appointments; один запись длится один час и начинается в начале каждого часа
  }
  ```
  ```
  AppointmentDayELementDto {
    time // формат hh:mm
    isAvailable // доступен слот или нет
  }
  ```

# Scheduler

## TaskScheduler 
1. Создать TaskScheduler (интерфейс + реализацию)
2. Таск шедулер будет иметь методы, которые вызываются по расписанию.
3. маски для Cron вынести в application.properties
4. добавить запуск балансировки расписания докторов по расписанию

## DoctorScheduleBalancer
1. Создать DoctorScheduleBalancer (интерфейс + реализацию)
1. Перенести реализацию балансироваки из DoctorScheduleBalanceUtil в балансер, а DoctorScheduleBalanceUtil - удалить
1. убрать вызов метода по расписанию из балансера
1. убрать "очистки коллекций"
1. Разобраться в алгоритме балансировки и декомпозировать метод balance()


```
interface DoctorScheduleBalancer {
 void balance();
}

```

# Notification

## Reminder

1. Создать Интерфейс Reminder;
1. Создать абстрактный класс Remind;
  

```  
interface RemindSender {
	void send(Remind remind) // получает нужный тип уведомления и отправляет 
	void send(List<Remind> reminds)
}

```  

```
abstract class Remind<T> {
	T content;
	String reciver; // тут будет идентификатор, куда посылать ведомление. Например, если это EmailRemind то тут будет email. Для дискорда - дискордайди
	RemindType type;
}
```
```
enum RemindType {
	EMAIL, DISCORD;
}
```

## EmailReminder

1. Создать EmailReminder - релизацию Reminder;
1. Создать ```EmailRemind extends Remind<String>```;
1. в EmailReminder метод send должен принимать EmailRemind и отправлять электронное сообщение используя метод sendMultipartHtmlMessage в MailService

## RemindConverter
1. создать интерфейс RemindConverter
1. создать реализацию EmailRemindConverter, который будет конвертировать уведомление в EmailRemind

```
RemindConverter {
Remind convert(UserNotification notification);
}
```

## RemindService
1. добавить в RemindSender
1. создать RemindService (с реализацией)
1. RemindService - должен собирать в себе все RemindSender, и RemindConverter
1. remindAllNotification() - должен смотреть в базе все уведомления, события которых наступают завтра и отправлять пользователям напоминания, и отправлять уведомления (на данный момент на email) через соответствующий RemindSender


```
class ReminService {
void remindAllNotification();
}
```



# Админка
## AdminUserController

1. создать CRUD контроллер для создания пользователей 
1. при создании пользователя для него должен создавать и профиль
1. При изменении пользователя менять только не записи, которые не null. 
1. CLIENT должен создаваться как UNVERIFIED_CLIENT, со всей сопуствтующей логикой (как при регистрации) - создание токена, отправка письма со ссылкой подтверждения почты. Админы доктора и клиенты создаются без этого. 
1. AdminDoctorController удалить + удалить все мапперы и дто, которые в нем использовались (если они больше нигде не используются)


```
GET /api/amind/user -> UserResponseDto
GET /api/amind/user/{id} -> List<UserResponseDto>
POST UserRequestDto -> /api/amind/user -> UserResponseDto
PUT UserRequestDto -> /api/amind/user/{id} -> UserResponseDto
DELETE /api/amind/user/{id} -> Void
```

```
class UserRequestDto {
   String email ; //notNull
   RoleNameEnum role //notNull
   password // notNull
   String firstName; //nullable
   String lastName; //nullable
   LocalDate birthDate; //nullable
   String discordId; //nullable
   String telegramId; // nullable
   String avatarUrl; // nullable
}

class UserResponseDto {
  Long id
  String email 
  RoleNameEnum role
  String password 
  ProfileDto profile;
}
```