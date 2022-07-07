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
1. рефактор модели

## Клиент
### 1 Контроллер получения новостей.   
```
GET /api/client/news -> List<ClientNewsResponseDto>
```
1. Создать контроллер ClientNewsRestController
2. контроллер возвращает список новостей, у который published = true.
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
    String сode; //сделать неизменным
    private Pet pet // oneToOne;
```
### 1. Рефактор модели
1. Исправить модель и связь с питомцем, сделать связь OneToOne

## Клиент  
### 1. Контактные данные
1. создать контроллер (PetContactController) для контактных данных питомца, при чем  
  - petCode не должен изменяться  

```
GET /api/client/pet/contact?petId -> PetContactResponseDto
```
```
PUT PetContactDto -> /api/client/ -> PetContactDto
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

1. Исправить логику генерации qr-кода. Этот код должен содержать абсолютный путь (не относительный) в эндпоинту http://{хост приложения}/petfound?{petcode}
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
1. рефактор модели  

## Контактная информация нашедшему питомца  
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
//TODO добавить функционал яндекс карт на фронт + доработать хранение координат/адреса

## Клиент
1. в PetFoundClientController создать эндпоинт получения истории находок питомца  
``` GET /api/client/petfound?petId -> List<PetFoundClientDto> (сортировка по дате) ```

```
public class PetFoundClientDto {
    private Long id;
    private String latitude;
    private String longitude;
    private String text;
    private LocalDateTime foundDate;
}
```  
