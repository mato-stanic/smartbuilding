--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: action_history; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE action_history (
    action_type character varying(63) NOT NULL,
    id bigint NOT NULL,
    "time" timestamp without time zone,
    user_id bigint,
    user_name character varying(255),
    user_apartment_name character varying(255),
    user_apartment_id bigint,
    ref_type character varying(255),
    ref_id bigint,
    ref_name character varying(255)
);


ALTER TABLE action_history OWNER TO m2stanic;

--
-- Name: company_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE company_seq
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE company_seq OWNER TO m2stanic;

--
-- Name: message; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE message (
    id bigint NOT NULL,
    body character varying(4096),
    title character varying(512),
    recipient_id bigint,
    sender_id bigint,
    read boolean,
    sending_time timestamp without time zone
);


ALTER TABLE message OWNER TO m2stanic;

--
-- Name: message_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE message_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE message_seq OWNER TO m2stanic;

--
-- Name: operator_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE operator_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE operator_seq OWNER TO m2stanic;

--
-- Name: organization; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE organization (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    user_type character varying(255)
);


ALTER TABLE organization OWNER TO m2stanic;

--
-- Name: role_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE role_seq OWNER TO m2stanic;

--
-- Name: user_profile_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE user_profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_profile_seq OWNER TO m2stanic;

--
-- Name: user_role; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE user_role (
    id bigint NOT NULL,
    description character varying(255),
    displayname character varying(255),
    name character varying(255),
    scope character varying(255)
);


ALTER TABLE user_role OWNER TO m2stanic;

--
-- Name: user_role_perms; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE user_role_perms (
    role_id bigint NOT NULL,
    permission character varying(255) NOT NULL
);


ALTER TABLE user_role_perms OWNER TO m2stanic;

--
-- Name: user_seq; Type: SEQUENCE; Schema: public; Owner: m2stanic
--

CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_seq OWNER TO m2stanic;

--
-- Name: users; Type: TABLE; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE TABLE users (
    id bigint NOT NULL,
    active boolean,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    role_id bigint,
    apartment_id bigint,
    last_login timestamp without time zone
);


ALTER TABLE users OWNER TO m2stanic;

--
-- Data for Name: action_history; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY action_history (action_type, id, "time", user_id, user_name, user_apartment_name, user_apartment_id, ref_type, ref_id, ref_name) FROM stdin;
APP_USER_UPDATED	2777	2016-04-04 21:48:07.188	1	Administrator	ADMIN	102	APP_USER	1	Administrator
\.


--
-- Name: company_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('company_seq', 129, true);

--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY message (id, body, title, recipient_id, sender_id, read, sending_time) FROM stdin;
\.


--
-- Name: message_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('message_seq', 5, true);


--
-- Name: operator_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('operator_seq', 7, true);


--
-- Data for Name: organization; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY organization (id, name, user_type) FROM stdin;
102	ADMIN	ADMIN
104	Stan 1b	TENANT
105	Stan 2a	TENANT
103	Stan 1a	TENANT
\.




--
-- Name: role_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('role_seq', 3, true);

--
-- Name: user_profile_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('user_profile_seq', 11, true);


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY user_role (id, description, displayname, name, scope) FROM stdin;
1	Can administer tariffs and other entities	Administrator	admin	ADMIN
3	Can administer tariffs and other entities	Tenant	tenant	TENANT
\.


--
-- Data for Name: user_role_perms; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY user_role_perms (role_id, permission) FROM stdin;
1	ACCESS_ADMIN_CONSOLE
1	MANAGE_USERS
1	MANAGE_OPERATORS
3	ACCESS_ADMIN_CONSOLE
\.


--
-- Name: user_seq; Type: SEQUENCE SET; Schema: public; Owner: m2stanic
--

SELECT pg_catalog.setval('user_seq', 2777, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: m2stanic
--

COPY users (id, active, email, first_name, last_name, password, username, role_id, apartment_id, last_login) FROM stdin;
2776	t	nesta@nafas.ma	nesta	nesta	nesta	nesta	3	103	\N
1	t	admin@admin.dee	Administrator		m2stanic	admin	1	102	2016-04-04 21:47:58.168
2772	t	mato.stanic@gmail.com	John	Doe	testtest	testApt1	3	103	2016-04-04 20:41:53.513
2775	t	gdfgdsf@m	gfdgfsd	gdsfgsdf	fgasdgd	fdsafas	3	104	\N
2773	t	fgsa@mfdsa.net	oigsda	oiigj	toiasj	testApt2a	3	105	2016-04-04 20:24:56.561
2774	t	oijgasoi@mail.com	jgiao	oigja	šasšj	stan1b	3	104	2016-04-04 20:26:07.869
\.


--
-- Name: action_history_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY action_history
    ADD CONSTRAINT action_history_pkey PRIMARY KEY (id);


--
-- Name: message_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: organization_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (id);


--
-- Name: uk_g7g1uamub9pk8id4d1a3tntg; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT uk_g7g1uamub9pk8id4d1a3tntg UNIQUE (displayname);


--
-- Name: uk_lnth8w122wgy7grrjlu8hjmuu; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT uk_lnth8w122wgy7grrjlu8hjmuu UNIQUE (name);


--
-- Name: uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: user_role_perms_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY user_role_perms
    ADD CONSTRAINT user_role_perms_pkey PRIMARY KEY (role_id, permission);


--
-- Name: user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: m2stanic; Tablespace:
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: unique_organization_name; Type: INDEX; Schema: public; Owner: m2stanic; Tablespace:
--

CREATE UNIQUE INDEX unique_organization_name ON organization USING btree (name);


--
-- Name: fk_ca75maa8akshrjg8l7p0hn2pn; Type: FK CONSTRAINT; Schema: public; Owner: m2stanic
--

ALTER TABLE ONLY user_role_perms
    ADD CONSTRAINT fk_ca75maa8akshrjg8l7p0hn2pn FOREIGN KEY (role_id) REFERENCES user_role(id);


--
-- Name: fk_camlvklbluxpcvlps0s8vs8wp; Type: FK CONSTRAINT; Schema: public; Owner: m2stanic
--

ALTER TABLE ONLY users
    ADD CONSTRAINT fk_camlvklbluxpcvlps0s8vs8wp FOREIGN KEY (apartment_id) REFERENCES organization(id);


--
-- Name: fk_krvotbtiqhudlkamvlpaqus0t; Type: FK CONSTRAINT; Schema: public; Owner: m2stanic
--

ALTER TABLE ONLY users
    ADD CONSTRAINT fk_krvotbtiqhudlkamvlpaqus0t FOREIGN KEY (role_id) REFERENCES user_role(id);


--
-- Name: message_recipient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: m2stanic
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_recipient_id_fkey FOREIGN KEY (recipient_id) REFERENCES organization(id);


--
-- Name: message_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: m2stanic
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

