# java-filmorate
Template repository for Filmorate project.

#Схема 

![Image alt](https://github.com/Sardiand/java-filmorate/blob/main/diagram.png)

#Примеры запросов

* `Создание` фильма:

```SQL
INSERT INTO films (name,
                   description,
                   release_date,
                   duration,
                   mpa_rating_id)
VALUES (?, ?, ?, ?, ?);
```

* `Обновление` фильма:

```SQL
UPDATE
    films
SET name                = ?,
    description         = ?,
    release_date        = ?,
    duration		= ?,
    mpa_rating_id       = ?
WHERE film_id = ?;