-- noinspection SqlNoDataSourceInspectionForFile

create sequence client_id_num_seq;

create table Client
(
    id         varchar(20) primary key default 'client_' || nextval('client_id_num_seq')::varchar,
    first_name varchar(20)    not null,
    last_name  varchar(20)    not null,
    email      varchar(20)    not null unique,
    salary     numeric(17, 2) not null
        constraint not_negative_salary check (salary > 0)
);


create sequence application_id_num_seq;

create type application_status as enum ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED', 'CANCELLED');

create table Application
(
    id                            varchar(20) primary key default 'application_' || nextval('application_id_num_seq')::varchar,
    client_id                     varchar(20)        not null,
    requested_disbursement_amount numeric(17, 2)     not null,
    status                        application_status not null,
    constraint not_negative_requested_disbursement_amount check (requested_disbursement_amount >= 0),

    constraint FK_Application_Client
        foreign key (client_id)
            references Client (id)
);

insert into Client (first_name, last_name, email, salary)
values ('lol', 'lolovich', 'lul@yandex.ru', 50000);