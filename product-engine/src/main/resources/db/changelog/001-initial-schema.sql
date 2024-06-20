create table Product
(
    code                   serial primary key,
    min_term               int,
    max_term               int,
    min_principal_amount   numeric(17, 2),
    max_principal_amount   numeric(17, 2),
    min_interest           numeric(5, 2),
    max_interest           numeric(5, 2),
    min_origination_amount numeric(17, 2),
    max_origination_amount numeric(17, 2)
);

create table Agreement
(
    id                 serial primary key,
    product_code       int,
    client_id          varchar(20),
    interest           numeric(5, 2),
    term               int,
    principal_amount   numeric(17, 2),
    origination_amount numeric(17, 2),
    status             varchar(20),
    disbursement_date  DATE,
    next_payment_date  DATE,

    constraint FK_agreement_product
        foreign key (product_code)
            references Product (code)
);

create table Payment_Schedule
(
    id               serial primary key,
    agreement_number int,
    version          int,

    constraint FK_payment_schedule_agreement
        foreign key (agreement_number)
            references Agreement (id)
);

create table Scheduled_Payment
(
    id                  serial primary key,
    payment_schedule_id int,
    status              varchar(20),
    payment_date        DATE,
    period_payment      numeric(17, 2),
    interest_payment    numeric(17, 2),
    principal_payment   numeric(17, 2),
    period_number       int,

    constraint FK_scheduled_payment_payment_schedule
        foreign key (payment_schedule_id)
            references Payment_Schedule (id)
);

insert into Product (min_term, max_term, min_principal_amount, max_principal_amount, min_interest, max_interest,
                     min_origination_amount, max_origination_amount)
values (3, 24, 50000, 500000, 0.08, 0.15, 2000, 10000)
