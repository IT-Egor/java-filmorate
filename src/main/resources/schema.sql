-- пользователи
CREATE TABLE IF NOT EXISTS users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(50),
    birthday DATE NOT NULL
);

-- друзья
CREATE TABLE IF NOT EXISTS friends (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT REFERENCES users(id),
    friend_id INT REFERENCES users(id),
    is_approved BOOLEAN NOT NULL
);

-- фильмы
CREATE TABLE IF NOT EXISTS films (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL
);

-- лайки
CREATE TABLE IF NOT EXISTS likes (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES films(id),
    user_id INT REFERENCES users(id)
);

-- жанры
CREATE TABLE IF NOT EXISTS genres (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- жанры фильмов
CREATE TABLE IF NOT EXISTS film_genres (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES films(id),
    genre_id INT REFERENCES genres(id)
);

-- рейтинги
CREATE TABLE IF NOT EXISTS ratings (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- рейтинги фильмов
CREATE TABLE IF NOT EXISTS film_ratings (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES films(id),
    rating_id INT REFERENCES ratings(id)
);