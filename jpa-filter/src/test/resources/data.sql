insert into language (name) values
('English'), ('Spanish'), ('French');

insert into article (title, main_topic, language_id) values
('Article 1', 'Health', 1),
('Article 2', 'Work', 2),
('Article 3', 'Love', 3),
('Article X', 'Article X', 1);

insert into comment (author, key, article_id) values
('david', 't1', 1),
('fabi', 't2', 2),
('grace', 't3', 3);

--insert into tag (name, article_id) values
--('jpa', 1),
--('spring', 2),
--('vip', 3);