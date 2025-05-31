CREATE TABLE orders (
       id BIGSERIAL PRIMARY KEY,
       order_date timestamp(6) without time zone,
       customer_address varchar(255),
       customer_email varchar(55),
       customer_name varchar(255),
       customer_phone varchar(20),
       status INT
);