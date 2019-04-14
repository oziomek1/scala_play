--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1
-- Dumped by pg_dump version 11.1

-- Started on 2019-04-14 11:11:16 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 197 (class 1259 OID 16467)
-- Name: category; Type: TABLE; Schema: public; Owner: oziomek
--

CREATE TABLE public.category (
    "categoryID" integer NOT NULL,
    "categoryName" character varying(50) NOT NULL
);


ALTER TABLE public.category OWNER TO oziomek;

--
-- TOC entry 199 (class 1259 OID 16480)
-- Name: order; Type: TABLE; Schema: public; Owner: oziomek
--

CREATE TABLE public."order" (
    "orderID" integer NOT NULL,
    "orderUserID" integer NOT NULL,
    "orderAddress" character varying(50) NOT NULL,
    "orderDate" timestamp with time zone NOT NULL,
    "orderCity" character varying(50) NOT NULL,
    "orderCountry" character varying(50) NOT NULL,
    "orderTax" real NOT NULL,
    "orderShipped" boolean NOT NULL
);


ALTER TABLE public."order" OWNER TO oziomek;

--
-- TOC entry 200 (class 1259 OID 16485)
-- Name: orderDetail; Type: TABLE; Schema: public; Owner: oziomek
--

CREATE TABLE public."orderDetail" (
    "orderDetailID" integer NOT NULL,
    "orderDetailOrderID" integer NOT NULL,
    "orderDetailProductID" integer NOT NULL,
    "orderDetailProductQuantity" integer NOT NULL,
    "orderDetailPrice" real NOT NULL
);


ALTER TABLE public."orderDetail" OWNER TO oziomek;

--
-- TOC entry 198 (class 1259 OID 16472)
-- Name: product; Type: TABLE; Schema: public; Owner: oziomek
--

CREATE TABLE public.product (
    "productID" integer NOT NULL,
    "productName" character varying(50) NOT NULL,
    "productDescription" text,
    "productCategoryID" integer NOT NULL,
    "productPrice" real NOT NULL
);


ALTER TABLE public.product OWNER TO oziomek;

--
-- TOC entry 196 (class 1259 OID 16462)
-- Name: user; Type: TABLE; Schema: public; Owner: oziomek
--

CREATE TABLE public."user" (
    "userID" integer NOT NULL,
    "userEmail" character varying(50) NOT NULL,
    "userFirstName" character varying(50) NOT NULL,
    "userLastName" character varying(50) NOT NULL,
    "userAddress" character varying(100) NOT NULL,
    "userPassword" character varying(50) NOT NULL
);


ALTER TABLE public."user" OWNER TO oziomek;

--
-- TOC entry 3188 (class 0 OID 16467)
-- Dependencies: 197
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: oziomek
--

COPY public.category ("categoryID", "categoryName") FROM stdin;
\.


--
-- TOC entry 3190 (class 0 OID 16480)
-- Dependencies: 199
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: oziomek
--

COPY public."order" ("orderID", "orderUserID", "orderAddress", "orderDate", "orderCity", "orderCountry", "orderTax", "orderShipped") FROM stdin;
\.


--
-- TOC entry 3191 (class 0 OID 16485)
-- Dependencies: 200
-- Data for Name: orderDetail; Type: TABLE DATA; Schema: public; Owner: oziomek
--

COPY public."orderDetail" ("orderDetailID", "orderDetailOrderID", "orderDetailProductID", "orderDetailProductQuantity", "orderDetailPrice") FROM stdin;
\.


--
-- TOC entry 3189 (class 0 OID 16472)
-- Dependencies: 198
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: oziomek
--

COPY public.product ("productID", "productName", "productDescription", "productCategoryID", "productPrice") FROM stdin;
\.


--
-- TOC entry 3187 (class 0 OID 16462)
-- Dependencies: 196
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: oziomek
--

COPY public."user" ("userID", "userEmail", "userFirstName", "userLastName", "userAddress", "userPassword") FROM stdin;
\.


--
-- TOC entry 3055 (class 2606 OID 16471)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY ("categoryID");


--
-- TOC entry 3061 (class 2606 OID 16489)
-- Name: orderDetail orderDetail_pkey; Type: CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."orderDetail"
    ADD CONSTRAINT "orderDetail_pkey" PRIMARY KEY ("orderDetailID");


--
-- TOC entry 3059 (class 2606 OID 16484)
-- Name: order order_pkey; Type: CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY ("orderID");


--
-- TOC entry 3057 (class 2606 OID 16479)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY ("productID");


--
-- TOC entry 3053 (class 2606 OID 16466)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY ("userID");


--
-- TOC entry 3064 (class 2606 OID 16500)
-- Name: orderDetail detail_order_fkey; Type: FK CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."orderDetail"
    ADD CONSTRAINT detail_order_fkey FOREIGN KEY ("orderDetailOrderID") REFERENCES public."order"("orderID");


--
-- TOC entry 3065 (class 2606 OID 16505)
-- Name: orderDetail detail_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."orderDetail"
    ADD CONSTRAINT detail_product_fkey FOREIGN KEY ("orderDetailProductID") REFERENCES public.product("productID");


--
-- TOC entry 3063 (class 2606 OID 16490)
-- Name: order order_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT order_user_fkey FOREIGN KEY ("orderUserID") REFERENCES public."user"("userID");


--
-- TOC entry 3062 (class 2606 OID 16495)
-- Name: product product_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: oziomek
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_category_fkey FOREIGN KEY ("productCategoryID") REFERENCES public.category("categoryID");


-- Completed on 2019-04-14 11:11:16 CEST

--
-- PostgreSQL database dump complete
--

