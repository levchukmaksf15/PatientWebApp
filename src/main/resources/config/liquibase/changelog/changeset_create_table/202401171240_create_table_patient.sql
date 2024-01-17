CREATE TABLE patient
(
    id                  UUID            PRIMARY KEY,
    name                varchar(30)     NOT NULL,
    birth_date          date            NOT NULL,
    last_temperature    numeric(19, 1),
    last_pulse          integer,
    is_discharged       boolean         NOT NULL DEFAULT TRUE,

    CONSTRAINT order_date_unique UNIQUE (name)
)