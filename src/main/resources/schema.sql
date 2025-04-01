-- Таблица с постами
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  post_text text not null,
  likes integer default 0,
  tags varchar(256) not null default '',
  image bytea);

insert into posts(title, post_text) values ('1 POST', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt, lectus id luctus laoreet, ante libero suscipit nibh, quis tincidunt purus nibh vitae est. Proin a orci et ipsum accumsan porttitor ac non est. Ut tincidunt enim vel odio pharetra, consequat posuere tellus volutpat. Pellentesque lacinia mi vel turpis condimentum gravida. Maecenas viverra justo nisi, et rutrum dolor pellentesque nec. Curabitur fringilla tincidunt augue eu tristique. In nec pretium tellus, iaculis ullamcorper nibh.');
insert into posts(title, post_text, tags) values ('2 POST', 'TEXT', 'TAG1');
insert into posts(title, post_text, tags) values ('3 POST', '123546457', 'TAG2');

-- Таблица с комментариями
create table if not exists comments(
  id bigserial primary key,
  post_id bigserial,
  text varchar(512) not null,
  constraint fk_comments_posts
       foreign key (post_id)
       REFERENCES posts (id));