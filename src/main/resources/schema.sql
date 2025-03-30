-- Таблица с постами
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  post_text text not null,
  likes integer default 0,
  tags varchar(256) not null default '',
  image bytea);

--insert into posts(title, post_text) values ('1 Пост', 'текст текст текст');
--insert into posts(title, post_text) values ('2 Пост', 'ТЕКСТ');
--insert into posts(title, post_text) values ('3 Пост', '123546457');

-- Таблица с комментариями
create table if not exists comments(
  id bigserial primary key,
  post_id bigserial,
  text varchar(512) not null,
  constraint fk_comments_posts
       foreign key (post_id)
       REFERENCES posts (id));