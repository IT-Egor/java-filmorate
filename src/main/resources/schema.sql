-- пользователи
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(50),
    birthday DATE NOT NULL
);

-- друзья
CREATE TABLE IF NOT EXISTS friends (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (user_id, friend_id)
);

-- рейтинги
CREATE TABLE IF NOT EXISTS ratings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- фильмы
CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    rating_id BIGINT REFERENCES ratings(id)
);

-- лайки
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (film_id, user_id)
);

-- жанры
CREATE TABLE IF NOT EXISTS genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- жанры фильмов
CREATE TABLE IF NOT EXISTS film_genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id) ON DELETE CASCADE,
    UNIQUE (film_id, genre_id)
);

-- отзывы на фильмы
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    content VARCHAR NOT NULL,
    type BOOLEAN,
    UNIQUE (film_id, user_id)
);

-- оценка на отзывы
CREATE TABLE IF NOT EXISTS like_reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    review_id BIGINT REFERENCES reviews(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    like_review INT NOT NULL,
    UNIQUE (review_id, user_id)
);

--лента событий
CREATE TABLE IF NOT EXISTS feed
(
    event_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    timestamp  long        not null,
    event_type varchar(10) not null,
    operation  varchar(10) not null,
    entity_id  long         not null
);