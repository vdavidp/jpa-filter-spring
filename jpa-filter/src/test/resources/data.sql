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

insert into dummy (id, integer_value, long_value, big_integer_value, float_value,
double_value, big_decimal_value) values
(1, 22, 88, 221, 94.221, 9.123, 443.22),
(2, 12, 10, 998, 1.333, 4.192, 39.22);