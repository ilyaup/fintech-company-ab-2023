create sequence client_id_num_seq;

create table Client
(
    id varchar(20) primary key default 'client_'||nextval('client_id_num_seq')::VARCHAR,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(20) not null,
    salary NUMERIC(17, 2) not null constraint not_negative_salary check (salary > 0)
);


create sequence application_id_num_seq;

create table Application
(
    id varchar(20) primary key default 'application_'||nextval('application_id_num_seq')::VARCHAR,
    client_id varchar(20) not null,
    requested_disbursement_amount numeric(17, 2),
    status varchar(20) not null,
    constraint not_negative_requested_disbursement_amount check (requested_disbursement_amount > 0),

    constraint FK_Application_Client
        foreign key(client_id)
            references Client(id)
);