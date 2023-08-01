--DROP TABLE IF EXISTS authorities;
--DROP TABLE IF EXISTS users;

create table if not exists users(
	username varchar(100) not null primary key,
	password varchar(100) not null,
	enabled boolean not null
);

create table if not exists  authorities (
	username varchar(100) not null,
	authority varchar(100) not null,
	primary key (username, authority),
	constraint fk_authorities_users foreign key(username) references users(username)
);