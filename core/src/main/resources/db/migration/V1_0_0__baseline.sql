create table state (
  key character varying(255),
  value character varying(255),
  primary key (key)
);

create table searched_page (
  url character varying(2047),
  create_at timestamp with time zone,
  primary key (url)
);

create table crawled_page(
  url varchar(2047),
  html text,
  text text,
  seen timestamp with time zone not null,
  primary key (url)
);
