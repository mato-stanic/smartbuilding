CREATE SEQUENCE apartment_cron_seq
START WITH 100
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE public.apartment_cron
(
    id BIGINT PRIMARY KEY,
    apartment_id BIGINT,
    time VARCHAR(255),
    action VARCHAR(255),
    CONSTRAINT apartment_cron_apartment_id_fk FOREIGN KEY (apartment_id) REFERENCES apartments (id)
);

CREATE TABLE public.apartment_cron_days
(
  apartment_cron_id bigint,
  day VARCHAR(255),
FOREIGN KEY (apartment_cron_id) REFERENCES apartment_cron (id)
);

ALTER TABLE public.apartment_cron ADD room VARCHAR(255) NULL;

ALTER TABLE apartment_layout OWNER TO m2stanic;
ALTER TABLE apartment_cron OWNER TO m2stanic;
ALTER TABLE apartment_cron_days OWNER TO m2stanic;