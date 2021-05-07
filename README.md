# *Docker guide*
- - -
## Start postgres

To create and start the database enter the following command in the terminal in the root folder of the project 

```docker-compose up -d```

To stop service

```docker-compose stop```

Start service

```docker-compose start```

Stop and remove containers

```docker-compose down```

- Our database is available on the local computer under port 5433 and container port 5432

- - -
## *How to create Dao and Service for new Entities guide*
- - -

1. Each new Dao interface should extend generic interface ReadOnlyDao<K, T> or ReadWriteDao<K, T>. ReadWriteDao interface includes methods from ReadOnlyDao interface. 
2. Generic K is key for entity. Generic T is entity. For example: `ExampleDao extends ReadWriteDao<Long, Example>`.
3. Each Dao implementation class should extend generic abstract class ReadOnlyDaoImpl<K,T> or ReadWriteDaoImpl<K, T> and implement their own interface.
4. There are a few simple realization of methods in generic Dao. New methods should be created in Dao interface of current entity. 
5. Same rules for Service classes except one thing. Service implementation should create constructor super.

- - -
## This project uses Lombok
- - -