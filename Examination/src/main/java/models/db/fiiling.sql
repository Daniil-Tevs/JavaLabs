use movies;

insert ignore into movies (title, description, duration)
values ('movie1', 'descriiiiiiption1', 100),
       ('movie2', 'descriiiiiiption2', 200),
       ('movie3', 'descriiiiiiption3', 400),
       ('movie4', 'descriiiiiiption4', 400),
       ('movie5', 'descriiiiiiption5', 500),
       ('movie6', 'descriiiiiiption6', 600);



insert ignore into genre (title)
values ('drama'),
       ('comedy');

insert ignore into movie_genre(movie_id, genre_id)
values (1, 1),
       (1, 2),
       (2, 1);
