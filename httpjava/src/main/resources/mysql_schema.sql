drop database if exists technologies;
create database technologies;

create table technologies.genres (
  id int auto_increment primary key,
  name varchar (255) unique
);

create table technologies.authors (
  id int auto_increment primary key,
  name varchar (255) unique
);

create table technologies.books (
  id int auto_increment primary key,
  author_id int not null,
  genre_id int not null,
  name varchar (255)
);

insert into technologies.genres (id, name) values (1, 'Horror');
insert into technologies.genres (id, name) values (2, 'Science Fiction');
insert into technologies.genres (id, name) values (3, 'Fantasy');

insert into technologies.authors (id, name) values (1, 'Mary Shelley');
insert into technologies.authors (id, name) values (2, 'H.G. Wells');
insert into technologies.authors (id, name) values (3, 'J.R.R. Tolkien');
insert into technologies.authors (id, name) values (4, 'Stephen King');
insert into technologies.authors (id, name) values (5, 'Philip K. Dick');
insert into technologies.authors (id, name) values (6, 'George R.R. Martin');

insert into technologies.books (id, author_id, genre_id, name) values (1, 1, 1, 'Frankenstein');
insert into technologies.books (id, author_id, genre_id, name) values (2, 2, 2, 'The Time Machine');
insert into technologies.books (id, author_id, genre_id, name) values (3, 2, 2, 'The War of the Worlds');
insert into technologies.books (id, author_id, genre_id, name) values (4, 2, 2, 'The Invisible Man');
insert into technologies.books (id, author_id, genre_id, name) values (5, 3, 3, 'The Fellowship of the Ring');
insert into technologies.books (id, author_id, genre_id, name) values (6, 3, 3, 'The Two Towers');
insert into technologies.books (id, author_id, genre_id, name) values (7, 3, 3, 'The Return of the King');
insert into technologies.books (id, author_id, genre_id, name) values (8, 4, 1, 'It');
insert into technologies.books (id, author_id, genre_id, name) values (9, 4, 1, 'The Shining');
insert into technologies.books (id, author_id, genre_id, name) values (10, 4, 3, 'The Gunslinger');
insert into technologies.books (id, author_id, genre_id, name) values (11, 5, 2, 'Do Androids Dream of Electric Sheep?');
insert into technologies.books (id, author_id, genre_id, name) values (12, 5, 2, 'A Scanner Darkly');
insert into technologies.books (id, author_id, genre_id, name) values (13, 6, 3, 'A Game of Thrones');
insert into technologies.books (id, author_id, genre_id, name) values (14, 6, 3, 'A Clash of Kings');
insert into technologies.books (id, author_id, genre_id, name) values (15, 6, 3, 'A Storm of Swords');
insert into technologies.books (id, author_id, genre_id, name) values (16, 6, 3, 'A Feast for Crows');
insert into technologies.books (id, author_id, genre_id, name) values (17, 6, 3, 'A Dance with Dragons');
insert into technologies.books (id, author_id, genre_id, name) values (18, 6, 2, 'Nightflyers');
