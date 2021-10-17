alter table searched_page alter column create_at set not null;

create table search_keyword (
  keyword character varying(255),
  searched_at date,
  primary key (keyword)
);

delete from state where key = 'searchedDate';
insert into search_keyword (keyword) values ('oriental consultants'), ('オリエンタル コンサルタンツ');

drop table crawled_page;
create table crawled_page(
  url varchar(2047),
  status character varying(255),
  note text,
  exclude boolean not null default false,

  searched_at timestamp with time zone not null,
  crawled_at timestamp with time zone not null,
  primary key (url)
);
