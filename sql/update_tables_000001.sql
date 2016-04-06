ALTER TABLE public.message DROP CONSTRAINT message_recipient_id_fkey;
ALTER TABLE public.users DROP CONSTRAINT fk_camlvklbluxpcvlps0s8vs8wp;
ALTER TABLE public.organization RENAME TO apartments;
ALTER TABLE public.message
ADD CONSTRAINT message_recipient_id_fkey
FOREIGN KEY (recipient_id) REFERENCES apartments (id);
ALTER TABLE public.users
ADD CONSTRAINT fk_camlvklbluxpcvlps0s8vs8wp
FOREIGN KEY (apartment_id) REFERENCES apartments (id);

CREATE TABLE public.apartment_layout
(
    id BIGINT PRIMARY KEY,
    living_room BOOLEAN,
    hallway BOOLEAN,
    kitchen BOOLEAN,
    bathroom BOOLEAN,
    bedroom BOOLEAN,
    apartment_id BIGINT
);

ALTER TABLE public.apartment_layout
ADD CONSTRAINT apartment_layout_apartment_id_fk
FOREIGN KEY (apartment_id) REFERENCES apartments (id);

CREATE SEQUENCE apartment_layout_seq
START WITH 100
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

insert INTO apartment_layout (id, living_room, hallway, kitchen, bathroom, bedroom, apartment_id)
    VALUES (nextval('apartment_layout_seq'), FALSE, FALSE, FALSE, FALSE, FALSE, 103);

insert INTO apartment_layout (id, living_room, hallway, kitchen, bathroom, bedroom, apartment_id)
    VALUES (nextval('apartment_layout_seq'), FALSE, FALSE, FALSE, FALSE, FALSE, 104);

insert INTO apartment_layout (id, living_room, hallway, kitchen, bathroom, bedroom, apartment_id)
    VALUES (nextval('apartment_layout_seq'), FALSE, FALSE, FALSE, FALSE, FALSE, 105);
