create table addresses (id bigint not null auto_increment, street varchar(255), primary key (id));
create table phones (id bigint not null auto_increment, number varchar(255), user_id bigint, primary key (id));
create table users (id bigint not null auto_increment, name varchar(255), address_id bigint, primary key (id));
alter table phones add constraint users_fk foreign key (user_id) references users;
alter table users add constraint address_fk foreign key (address_id) references addresses;