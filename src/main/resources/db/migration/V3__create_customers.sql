CREATE TABLE public.customers
(
    id       int NOT NULL PRIMARY KEY,
    email    varchar(250) NOT NULL,
    password varchar(250) NOT NULL,
    discount float
);

CREATE SEQUENCE customers_code_seq;

INSERT INTO customers VALUES(1,'onalex911@gmail.com','$2a$12$mYKtaLUYD8fNDvhDNpsKxOSMipG8Q6vIwIdaCqSh.uLNhd8tH2GW2',0);

SELECT setval('customers_code_seq', (SELECT MAX(id) FROM customers));
ALTER TABLE customers ALTER COLUMN id SET DEFAULT nextval('customers_code_seq');

CREATE TABLE public.rekvizitu_schet
(
    id                      bigint NOT NULL PRIMARY KEY, -- без автоинкремента на этапе создания
    vid                     int          DEFAULT NULL,
    nds                     int          DEFAULT NULL,
    poluchatel              varchar(250) DEFAULT NULL,
    inn                     varchar(20)  DEFAULT NULL,
    kpp                     varchar(20)  DEFAULT NULL,
    bank                    varchar(250) DEFAULT NULL,
    bik                     varchar(20)  DEFAULT NULL,
    kor_schet               varchar(250) DEFAULT NULL,
    raschetnuy_schet        varchar(250) DEFAULT NULL,
    postavshik              varchar(250) DEFAULT NULL,
    postavshik_adres        varchar(250) DEFAULT NULL,
    postavshik_telefon      varchar(250) DEFAULT NULL,
    gruzootpravitel         varchar(250) DEFAULT NULL,
    gruzootpravitel_adres   varchar(250) DEFAULT NULL,
    gruzootpravitel_telefon varchar(250) DEFAULT NULL,
    pechat                  varchar(250) DEFAULT NULL,
    rukovoditel             varchar(250) DEFAULT NULL,
    buhgalter               varchar(250) DEFAULT NULL,
    comment                 text,
    customer                int
);

-- Создаём последовательность, но не связываем её с таблицей пока
CREATE SEQUENCE rekvizitu_schet_code_seq;

INSERT INTO rekvizitu_schet
VALUES (1, 1, 1,
        'Индивидуальный предприниматель Нарольский Анатолий Евгеньевич',
        '591102207906',
        '',
        'Дальневосточный банк ПАО Сбербанк г.Хабаровск',
        '040813608',
        '30101810600000000608',
        '40802810270000028866',
        'Индивидуальный предприниматель Нарольский Анатолий Евгеньевич',
        '680009, Россия, г.Хабаровск, ул.Промышленная, д.20, литера \"Б\"',
        '(4212)45-04-11, 8-914-197-57-19',
        'Индивидуальный предприниматель Нарольский Анатолий Евгеньевич',
        '680009, Россия, г.Хабаровск, ул.Промышленная, д.20, литера \"Б\"',
        '(4212)450-411, 8-914-197-57-19',
        '',
        'Нарольская Виктория Юрьевна', '', 'Счет действителен в течение 3-х рабочих дней',1);

SELECT setval('rekvizitu_schet_code_seq', (SELECT MAX(id) FROM rekvizitu_schet));
ALTER TABLE rekvizitu_schet ALTER COLUMN id SET DEFAULT nextval('rekvizitu_schet_code_seq');




