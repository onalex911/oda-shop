CREATE TABLE public.gruptov (
                         code integer NOT NULL PRIMARY KEY,  -- без автоинкремента на этапе создания
                         purl varchar(250),
                         flpurl integer DEFAULT NULL,
                         code1 varchar(250),
                         nomer integer DEFAULT NULL,
                         vid integer DEFAULT NULL,
                         cena integer DEFAULT NULL,
                         skidka numeric(10,2) DEFAULT NULL,
                         zakaz integer DEFAULT NULL,
                         ost integer NOT NULL DEFAULT 0,
                         blok integer DEFAULT NULL,
                         rod varchar(250),
                         name varchar(250),
                         pic varchar(250),
                         menu1 integer DEFAULT NULL,
                         menu2 integer DEFAULT NULL,
                         flmenu varchar(250),
                         top varchar(250),
                         maingrup varchar(250),
                         razmerpic varchar(250),
                         comment text,
                         keywords varchar(250),
                         description text,
                         dop varchar(250)
);

-- Создаём последовательность, но не связываем её с таблицей пока
CREATE SEQUENCE gruptov_code_seq;



CREATE TABLE public.tovar (
                       code integer PRIMARY KEY,
                       purl varchar(250),
                       flpurl integer DEFAULT NULL,
                       code1 varchar(250),
                       nomer integer DEFAULT NULL,
                       gruptov integer DEFAULT NULL,
                       name varchar(250),
                       pic varchar(250),
                       bpic varchar(250),
                       ostatok integer DEFAULT NULL,
                       cena numeric(10,2) DEFAULT NULL,
                       cena1 numeric(10,2) DEFAULT NULL,
                       ves numeric(10,3) DEFAULT NULL,
                       blok integer DEFAULT NULL,
                       data varchar(10),
                       data_red varchar(250),
                       top varchar(250),
                       dop_parametru varchar(250),
                       link varchar(250),
                       prosmotru integer DEFAULT NULL,
                       comment text,
                       comment1 text,
                       dop varchar(250)
);


CREATE SEQUENCE tovar_code_seq;
