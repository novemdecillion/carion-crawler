create table account (
  username character varying(255),
  password character varying(255) not null,
  role character varying(255) not null,
  primary key (username)
);

insert into account (username, password, role) values
  ('admin', '$2a$10$BGLiZaqou.N2bhFfFBwW5OnOcS2SFEn2eb3a.x3pQ.d3LkCnmn91a', 'ADMIN');
