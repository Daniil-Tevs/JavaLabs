create database if not exists movies;

create table if not exists movies.movies
(
    id          int          not null auto_increment,
    title       varchar(510) not null,
    description text         not null,
    duration    int,

    primary key (id)
);

create table if not exists movies.genre
(
    id    int not null auto_increment,
    title varchar(510),
    primary key (id)
);

create table if not exists movies.movie_genre
(
    id       int not null auto_increment,
    movie_id int not null,
    genre_id int not null,
    primary key (id),
    foreign key fk_movie_id (movie_id) references movies (id),
    foreign key fk_genre_id (genre_id) references genre (id)
);