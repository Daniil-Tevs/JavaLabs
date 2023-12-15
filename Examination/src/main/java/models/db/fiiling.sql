use movies;

insert ignore into country (title)
values ('USA'),
       ('Great Britain'),
       ('Turkey'),
       ('Serbia');

insert ignore into genre (title)
values ('Drama'),
       ('Comedy'),
       ('Triller'),
       ('Horror'),
       ('Fantastic'),
       ('History');

insert ignore into mood (title)
values ('New Year'),
       ('Birthday'),
       ('Autumn'),
       ('Summer'),
       ('Spring'),
       ('Winter'),
       ('Halloween'),
       ('Christmas');

insert ignore into movies (title, description, duration, year, country_id)
values ('Megalodon',
        'Paul and his family are planning a holiday in a coastal paradise. To their surprise, the picturesque and friendly village is abandoned and destroyed. According to Aztec legends, the last megalodon, the Black Demon, lives in local waters. He zealously protects nature from human invasion, destroying everything in his path. Traveling to a coastal platform in search of answers, the family finds themselves trapped. And now Paul needs to find a way to save his loved ones.',
        100, 2000, 1),
       ('Home alone',
        'An American family leaves Chicago for Europe, but in the rush to get ready, clueless parents forget at home... one of their children. The young creature, however, is not lost and demonstrates miracles of ingenuity. And when robbers break into the house, they have to regret meeting the cute baby more than once.',
        200, 2004, 2),
       ('Magnificent century',
        'The plot is based on real events that occurred during the reign of Sultan Suleiman I, and tells about the period in the life of the girl Alexandra, who was captured by the Turks and named Hurrem. Later she became the first official wife of Suleiman, but for this she had to go through a difficult path...',
        400, 2015, 3),
       ('Sinister',
        'A detective author and his family move to a small town and settle in a house where a chilling tragedy unfolded a year ago - all the residents were killed. The writer accidentally finds video recordings that are the key to the mystery of the crime. But nothing comes for free: terrible things begin to happen in the house, and now the lives of his loved ones are under threat.',
        400, 2016, 4),
       ('Harry Potter',
        'The life of ten-year-old Harry Potter cannot be called sweet: his parents died when he was barely a year old, and from his uncle and aunt, who took the orphan into their upbringing, they only get pokes and slaps on the head. But on Harry eleventh birthday, everything changes. A strange guest who unexpectedly appears on the doorstep brings a letter from which the boy learns that he is in fact a wizard and is enrolled in a school of magic called Hogwarts. ',
        500, 2009, 2),
       ('The Walking Dead',
        'This an American post-apocalyptic television series developed by Frank Darabont and based on the comic book series of the same name created by Robert Kirkman, Tony Moore and Charlie Adlard. The plot centers on a small group of people trying to survive a zombie apocalypse.',
        1300, 2006, 2);

insert ignore into movie_genre(movie_id, genre_id)
values (1, 3),
       (1, 4),
       (1, 5),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 6),
       (4, 1),
       (4, 3),
       (4, 4),
       (5, 5),
       (6, 1),
       (6, 4);

insert ignore into movie_mood(movie_id, mood_id)
values (1, 4),
       (2, 1),
       (2, 6),
       (2, 8),
       (3, 3),
       (4, 3),
       (5, 2),
       (6, 7),
       (2, 5),
       (5, 3);


insert ignore into movie_season (season_number, movie_id)
values (1, 2),
       (1, 5),
       (2, 3),
       (3, 1),
       (4, 4),
       (4, 6);