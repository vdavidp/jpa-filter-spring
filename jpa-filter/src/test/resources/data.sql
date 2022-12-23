insert into language (name) values
('English'), ('Spanish'), ('French');

insert into article (title, main_topic, starts, language_id) values
('Article 1', 'Health', 4, 1),
('Article 2', 'Work', 3, 2),
('Article 3', 'Love', 5, 3),
('Article X', 'Article X', 2, 1);

insert into comment (author, keyed, article_id) values
('david', 't1', 1),
('fabi', 't2', 2),
('grace', 't3', 3);

insert into dummy (id, integer_value, long_value, big_integer_value, float_value,
double_value, big_decimal_value, boolean_value, util_date, uuid) values
(1, 22, 88, 221, 94.221, 9.123, 443.22, true, '2020-10-21', '7b064b40-230d-42a5-91d0-8e0fcee8244b'),
(2, 12, 10, 998, 1.333, 4.192, 39.22, false, '2010-05-11', '2a9951bc-c197-450b-883c-bd4af8b6d0c3'),
(3, 1, 2, 3, 4.0, null, null, false, '2020-09-15', '46d0caeb-3488-4abd-8e6c-255a10e7aee6');