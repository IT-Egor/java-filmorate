INSERT INTO ratings (name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

INSERT INTO genres (name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

INSERT INTO films (name, description, release_date, duration, rating_id)
VALUES
    ('Фильм 1', 'Описание фильма 1', '2020-01-01', 120, 1),
    ('Фильм 2', 'Описание фильма 2', '2019-06-15', 90, 2),
    ('Фильм 3', 'Описание фильма 3', '2018-03-20', 150, 3);

INSERT INTO users (email, login, name, birthday)
VALUES
    ('user1@example.com', 'user1', 'Иван Иванов', '1990-01-01'),
    ('user2@example.com', 'user2', 'Мария Петрова', '1985-06-15'),
    ('user3@example.com', 'user3', 'Сергей Сидоров', '1970-03-20');

INSERT INTO likes (film_id, user_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 2),
    (3, 3),
    (3, 1);

INSERT INTO film_genres (film_id, genre_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3);

INSERT INTO friends (user_id, friend_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 2);

MERGE INTO event_type (id, name) KEY(id)
VALUES
(1, 'LIKE'),
(2, 'REVIEW'),
(3, 'FRIEND');

MERGE INTO event_operation (id, name) KEY(id)
VALUES
(1, 'REMOVE'),
(2, 'ADD'),
(3, 'UPDATE');