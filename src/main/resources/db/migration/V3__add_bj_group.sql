CREATE TABLE public.bj_groups (
      id SERIAL PRIMARY KEY,
      group_name_ru character varying(50) NOT NULL,
      group_name_lat character varying(50) NOT NULL,
      group_id integer NOT NULL UNIQUE,
      group_order integer NOT NULL,
      active boolean DEFAULT true
);
-- Создаем отдельную SEQUENCE для поля `group_order`
CREATE SEQUENCE group_order_seq;

-- Устанавливаем дефолтное значение для `group_order`
ALTER TABLE public.bj_groups
    ALTER COLUMN group_order SET DEFAULT nextval('group_order_seq');

-- Опционально: связываем SEQUENCE с таблицей (чтобы она удалялась при DROP TABLE)
ALTER SEQUENCE group_order_seq OWNED BY public.bj_groups.group_order;

insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('серьги Люкс','sergi_lyuks',1418);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('кольца Люкс','kolca_lyuks',1419);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('кресты декоративные','kresty_dekorativnye',1420);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('браслеты кожаные','braslety_kozhannye',1421);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('серьги','sergi',1422);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('кольца','kolca',1423);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('колье, цепи','kole_cepi',1424);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('браслеты','braslety',1425);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('браслеты на ногу','braslety_na_nogu',1426);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('броши','broshi',1427);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('колье, подвески Люкс','kole_podveski_lyuks',1428);
insert into public.bj_groups (group_name_ru,group_name_lat,group_id) VALUES('коллекция "Дерзская"','kollekciya_derzskaya',1430);

