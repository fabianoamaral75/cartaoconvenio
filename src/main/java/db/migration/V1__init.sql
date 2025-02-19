
CREATE TABLE public.acesso (
    id_acesso bigint NOT NULL,
    desc_acesso character varying(255) NOT NULL,
    CONSTRAINT acesso_desc_acesso_check CHECK (((desc_acesso)::text = ANY ((ARRAY['ADMIN'::character varying, 'FINANCEIRO'::character varying, 'FINANCEIRO_ADMI'::character varying, 'ENTIDADE_ADMI'::character varying, 'ENTIDADE'::character varying, 'CONVENIADA_ADMI'::character varying, 'CONVENIADA_VENDEDOR'::character varying])::text[])))
);


ALTER TABLE public.acesso OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24212)
-- Name: cartao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cartao (
    id_cartao bigint NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    dt_validade date NOT NULL,
    numeracao character varying(50) NOT NULL,
    status character varying(255) NOT NULL,
    id_funcionario bigint,
    CONSTRAINT cartao_status_check CHECK (((status)::text = ANY ((ARRAY['ATIVO'::character varying, 'BLOQUEADA'::character varying, 'VENCIDO'::character varying, 'CANCELADO'::character varying])::text[])))
);


ALTER TABLE public.cartao OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24218)
-- Name: ciclo_pagamento_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ciclo_pagamento_venda (
    id_ciclo_pagamento_venda bigint NOT NULL,
    ano_mes character varying(6) NOT NULL,
    status character varying(255) NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    dt_pagamento date,
    valor_ciclo numeric(38,2) NOT NULL,
    id_conveniados bigint,
    id_taixa_conveiniados bigint NOT NULL,
    CONSTRAINT ciclo_pagamento_venda_status_check CHECK (((status)::text = ANY ((ARRAY['AGUARDANDO_PAGAMENTO'::character varying, 'PAGAMENTO'::character varying, 'PAGAMENTO_BLOQUEADO'::character varying, 'PAGAMENTO_REJEITADO'::character varying, 'PAGAMENTO_CANCELADO'::character varying, 'PAGAMENTO_APROVADO'::character varying])::text[])))
);


ALTER TABLE public.ciclo_pagamento_venda OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24224)
-- Name: contas_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contas_receber (
    id_contas_receber bigint NOT NULL,
    ano_mes character varying(6) NOT NULL,
    status character varying(255) NOT NULL,
    dt_criacao timestamp without time zone NOT NULL,
    valor_receber numeric(38,2) NOT NULL,
    id_entidade bigint NOT NULL,
    id_taixa_entidade bigint NOT NULL,
    CONSTRAINT contas_receber_status_check CHECK (((status)::text = ANY ((ARRAY['AGUARDANDO_RECEBIMENTO'::character varying, 'RECEBIDO'::character varying, 'RECEBIMENTO_BLOQUEADO'::character varying, 'RECEBIMENTO_REJEITADO'::character varying, 'RECEBIMENTO_CANCELADO'::character varying, 'RECEBIMENTO_APROVADO'::character varying])::text[])))
);


ALTER TABLE public.contas_receber OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24230)
-- Name: conveniados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conveniados (
    id_conveniados bigint NOT NULL,
    status character varying(255) NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    obs character varying(50000),
    site character varying(500),
    id_nicho bigint NOT NULL,
    id_ramo_atividade bigint NOT NULL,
    CONSTRAINT conveniados_status_check CHECK (((status)::text = ANY ((ARRAY['ATIVA'::character varying, 'AGUARDANDO_CONFIRMACAO'::character varying, 'BLOQUEIO'::character varying, 'BLOQUEIO_PAGAMENTO'::character varying, 'DESATIVADA'::character varying, 'DESATIVADA_FIM_CONTRATO'::character varying])::text[])))
);


ALTER TABLE public.conveniados OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24238)
-- Name: entidade; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.entidade (
    id_entidade bigint NOT NULL,
    bairro character varying(100) NOT NULL,
    cep character varying(8) NOT NULL,
    cidade character varying(200) NOT NULL,
    cnpj character varying(14) NOT NULL,
    complemento character varying(500),
    status character varying(255) NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    insc_estadual character varying(50),
    insc_municipal character varying(50),
    logradoro character varying(300) NOT NULL,
    nome_entidade character varying(300) NOT NULL,
    numero character varying(20) NOT NULL,
    obs character varying(50000),
    site character varying(500),
    uf character varying(2) NOT NULL,
    CONSTRAINT entidade_status_check CHECK (((status)::text = ANY ((ARRAY['ATIVA'::character varying, 'AGUARDANDO_CONFIRMACAO'::character varying, 'BLOQUEIO'::character varying, 'BLOQUEIO_PAGAMENTO'::character varying, 'DESATIVADA'::character varying, 'DESATIVADA_FIM_CONTRATO'::character varying])::text[])))
);


ALTER TABLE public.entidade OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24246)
-- Name: funcionario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.funcionario (
    id_funcionario bigint NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    id_entidade bigint,
    id_pessoa bigint,
    id_secretaria bigint
);


ALTER TABLE public.funcionario OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24251)
-- Name: itens_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.itens_venda (
    id_itens_venda bigint NOT NULL,
    qty_item integer NOT NULL,
    vlr_unitario numeric(38,2) NOT NULL,
    vlr_total_item numeric(38,2) NOT NULL,
    id_produto bigint,
    id_venda bigint
);


ALTER TABLE public.itens_venda OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24256)
-- Name: limite_credito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.limite_credito (
    id_limite_credito bigint NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    limite numeric(38,2) NOT NULL,
    valor_utilizado numeric(38,2) NOT NULL,
    id_funcionario bigint
);


ALTER TABLE public.limite_credito OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 24261)
-- Name: nicho; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nicho (
    id_nicho bigint NOT NULL,
    desc_nicho character varying(100) NOT NULL,
    id_conveniados bigint
);


ALTER TABLE public.nicho OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 24266)
-- Name: pessoa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa (
    id_pessoa bigint NOT NULL,
    bairro character varying(100) NOT NULL,
    cep character varying(8) NOT NULL,
    cidade character varying(200) NOT NULL,
    complemento character varying(500),
    email character varying(100) NOT NULL,
    logradoro character varying(300) NOT NULL,
    nome_pessoa character varying(300) NOT NULL,
    numero character varying(20) NOT NULL,
    telefone character varying(13) NOT NULL,
    uf character varying(2) NOT NULL,
    id_conveniados bigint,
    id_usuario bigint NOT NULL
);


ALTER TABLE public.pessoa OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24273)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_fisica (
    id_pessoa_fisica bigint NOT NULL,
    cpf character varying(11) NOT NULL,
    dt_nascimento date,
    id_pessoa bigint
);


ALTER TABLE public.pessoa_fisica OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24278)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_juridica (
    id_pessoa_juridica bigint NOT NULL,
    cnpj character varying(18) NOT NULL,
    insc_estadual character varying(40),
    insc_municipal character varying(40),
    nome_fantasia character varying(200) NOT NULL,
    razao_social character varying(200) NOT NULL,
    id_pessoa bigint
);


ALTER TABLE public.pessoa_juridica OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 24285)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto (
    id_produto bigint NOT NULL,
    dt_cadastro timestamp(6) without time zone NOT NULL,
    produto character varying(50) NOT NULL,
    vlr_produto numeric(38,2) NOT NULL,
    id_conveniados bigint
);


ALTER TABLE public.produto OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 24290)
-- Name: ramo_atividade; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ramo_atividade (
    id_ramo_atividade bigint NOT NULL,
    desc_ramo_atividade character varying(100) NOT NULL,
    id_conveniados bigint
);


ALTER TABLE public.ramo_atividade OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 24295)
-- Name: salario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salario (
    id_salario bigint NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    valor_bruto numeric(38,2) NOT NULL,
    valor_liquido numeric(38,2) NOT NULL,
    id_funcionario bigint
);


ALTER TABLE public.salario OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 24300)
-- Name: secretaria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.secretaria (
    id_secretaria bigint NOT NULL,
    bairro character varying(100) NOT NULL,
    cep character varying(8) NOT NULL,
    cidade character varying(200) NOT NULL,
    complemento character varying(500),
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    logradoro character varying(300) NOT NULL,
    nome_secretaria character varying(300) NOT NULL,
    numero character varying(20) NOT NULL,
    uf character varying(2) NOT NULL,
    id_entidade bigint NOT NULL
);


ALTER TABLE public.secretaria OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 24355)
-- Name: seq_contas_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_contas_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_contas_receber OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 24356)
-- Name: seq_id_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_acesso OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 24357)
-- Name: seq_id_cartao; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_cartao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_cartao OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 24358)
-- Name: seq_id_ciclo_pagamento_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_ciclo_pagamento_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_ciclo_pagamento_venda OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 24359)
-- Name: seq_id_conveniados; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_conveniados
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_conveniados OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 24360)
-- Name: seq_id_entidade; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_entidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_entidade OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 24361)
-- Name: seq_id_funcionario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_funcionario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_funcionario OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 24362)
-- Name: seq_id_itens_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_itens_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_itens_venda OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 24363)
-- Name: seq_id_limite_credito; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_limite_credito
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_limite_credito OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 24364)
-- Name: seq_id_nicho; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_nicho
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_nicho OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 24365)
-- Name: seq_id_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_pessoa OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 24366)
-- Name: seq_id_pessoa_fisica; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_pessoa_fisica
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_pessoa_fisica OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 24367)
-- Name: seq_id_pessoa_juridica; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_pessoa_juridica
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_pessoa_juridica OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 24368)
-- Name: seq_id_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_produto OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 24369)
-- Name: seq_id_ramo_atividade; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_ramo_atividade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_ramo_atividade OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 24370)
-- Name: seq_id_salario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_salario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_salario OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 24371)
-- Name: seq_id_secretaria; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_secretaria
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_secretaria OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 24372)
-- Name: seq_id_usuario_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_usuario_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_usuario_acesso OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 24373)
-- Name: seq_id_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_id_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_id_venda OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 24374)
-- Name: seq_taxa_calc_limite_credito_func; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_taxa_calc_limite_credito_func
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_taxa_calc_limite_credito_func OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 24375)
-- Name: seq_tx_conveiniados; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_tx_conveiniados
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_tx_conveiniados OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 24376)
-- Name: seq_tx_entidade; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_tx_entidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_tx_entidade OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 24377)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_usuario OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 24307)
-- Name: taixa_conveiniados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.taixa_conveiniados (
    id_taixa_conveiniados bigint NOT NULL,
    status character varying(255) NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    taixa numeric(38,2) NOT NULL,
    id_conveniados bigint,
    CONSTRAINT taixa_conveiniados_status_check CHECK (((status)::text = ANY ((ARRAY['DESATUALIZADA'::character varying, 'ATUAL'::character varying, 'BLOQUEADA'::character varying, 'AGUARDANDO_APROVACAO'::character varying])::text[])))
);


ALTER TABLE public.taixa_conveiniados OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 24313)
-- Name: taixa_entidade; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.taixa_entidade (
    id_taixa_entidade bigint NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    taixa_entidade numeric(38,2) NOT NULL,
    id_entidade bigint,
    CONSTRAINT taixa_entidade_status_check CHECK (((status)::text = ANY ((ARRAY['DESATUALIZADA'::character varying, 'ATUAL'::character varying, 'BLOQUEADA'::character varying, 'AGUARDANDO_APROVACAO'::character varying])::text[])))
);


ALTER TABLE public.taixa_entidade OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 24319)
-- Name: taxa_calc_limite_credito_func; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.taxa_calc_limite_credito_func (
    id_taxa_calc_limite_credito_func bigint NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    taxa_base numeric(38,2) NOT NULL,
    id_entidade bigint,
    CONSTRAINT taxa_calc_limite_credito_func_status_check CHECK (((status)::text = ANY ((ARRAY['CANCELA'::character varying, 'ATUAL'::character varying, 'DESATUALIZADA'::character varying, 'AGUARDANDO_APROVACAO'::character varying])::text[])))
);


ALTER TABLE public.taxa_calc_limite_credito_func OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 24325)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id_usuario bigint NOT NULL,
    dt_atual_senha timestamp(6) without time zone NOT NULL,
    dt_criacao timestamp(6) without time zone NOT NULL,
    login character varying(50) NOT NULL,
    senha character varying(20) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 24330)
-- Name: usuario_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario_acesso (
    id_usuario_acesso bigint NOT NULL,
    id_acesso bigint,
    id_usuario bigint
);


ALTER TABLE public.usuario_acesso OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 24335)
-- Name: venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda (
    id_venda bigint NOT NULL,
    ano_mes character varying(6) NOT NULL,
    status_venda_paga character varying(255) NOT NULL,
    status_venda_recebida character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    dt_alteracao timestamp(6) without time zone NOT NULL,
    dt_venda timestamp(6) without time zone NOT NULL,
    login_user character varying(6) NOT NULL,
    valor_calc_taixa_conveniado numeric(38,2) NOT NULL,
    valor_calc_taxa_entidade numeric(38,2) NOT NULL,
    valor_venda numeric(38,2) NOT NULL,
    id_cartao bigint NOT NULL,
    id_conveniados bigint,
    id_taixa_conveiniados bigint NOT NULL,
    id_taixa_entidade bigint NOT NULL,
    CONSTRAINT venda_status_check CHECK (((status)::text = ANY ((ARRAY['ABERTA'::character varying, 'APROVADA'::character varying, 'CANCELADA'::character varying, 'REJEITADA'::character varying, 'AGUARDANDO_PAGAMENTO'::character varying, 'PAGAMENTO_NAO_APROVADO'::character varying, 'PAGAMENTO_AUTORIZADO'::character varying, 'PAGAMENTO_APROVADO'::character varying, 'BLOQUEADA'::character varying])::text[]))),
    CONSTRAINT venda_status_venda_paga_check CHECK (((status_venda_paga)::text = ANY ((ARRAY['VENDA_PAGA'::character varying, 'AGURARDANDO_PAGAMENTO'::character varying, 'VENDA_CANCELADA'::character varying, 'PAGAMENTO_REJEITADO'::character varying])::text[]))),
    CONSTRAINT venda_status_venda_recebida_check CHECK (((status_venda_recebida)::text = ANY ((ARRAY['VENDA_RECEBIDA'::character varying, 'AGURARDANDO_RECEBIMENTO'::character varying, 'AGURARDANDO_CANCELADA'::character varying, 'RECEBIMENTO_REJEITADO'::character varying])::text[])))
);


ALTER TABLE public.venda OWNER TO postgres;

--
-- TOC entry 3377 (class 2606 OID 24211)
-- Name: acesso acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acesso
    ADD CONSTRAINT acesso_pkey PRIMARY KEY (id_acesso);


--
-- TOC entry 3379 (class 2606 OID 24217)
-- Name: cartao cartao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cartao
    ADD CONSTRAINT cartao_pkey PRIMARY KEY (id_cartao);


--
-- TOC entry 3381 (class 2606 OID 24223)
-- Name: ciclo_pagamento_venda ciclo_pagamento_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciclo_pagamento_venda
    ADD CONSTRAINT ciclo_pagamento_venda_pkey PRIMARY KEY (id_ciclo_pagamento_venda);


--
-- TOC entry 3383 (class 2606 OID 24229)
-- Name: contas_receber contas_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contas_receber
    ADD CONSTRAINT contas_receber_pkey PRIMARY KEY (id_contas_receber);


--
-- TOC entry 3385 (class 2606 OID 24237)
-- Name: conveniados conveniados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conveniados
    ADD CONSTRAINT conveniados_pkey PRIMARY KEY (id_conveniados);


--
-- TOC entry 3387 (class 2606 OID 24245)
-- Name: entidade entidade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entidade
    ADD CONSTRAINT entidade_pkey PRIMARY KEY (id_entidade);


--
-- TOC entry 3389 (class 2606 OID 24250)
-- Name: funcionario funcionario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcionario
    ADD CONSTRAINT funcionario_pkey PRIMARY KEY (id_funcionario);


--
-- TOC entry 3391 (class 2606 OID 24255)
-- Name: itens_venda itens_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itens_venda
    ADD CONSTRAINT itens_venda_pkey PRIMARY KEY (id_itens_venda);


--
-- TOC entry 3393 (class 2606 OID 24260)
-- Name: limite_credito limite_credito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.limite_credito
    ADD CONSTRAINT limite_credito_pkey PRIMARY KEY (id_limite_credito);


--
-- TOC entry 3395 (class 2606 OID 24265)
-- Name: nicho nicho_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nicho
    ADD CONSTRAINT nicho_pkey PRIMARY KEY (id_nicho);


--
-- TOC entry 3399 (class 2606 OID 24277)
-- Name: pessoa_fisica pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id_pessoa_fisica);


--
-- TOC entry 3401 (class 2606 OID 24284)
-- Name: pessoa_juridica pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id_pessoa_juridica);


--
-- TOC entry 3397 (class 2606 OID 24272)
-- Name: pessoa pessoa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT pessoa_pkey PRIMARY KEY (id_pessoa);


--
-- TOC entry 3403 (class 2606 OID 24289)
-- Name: produto produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (id_produto);


--
-- TOC entry 3405 (class 2606 OID 24294)
-- Name: ramo_atividade ramo_atividade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ramo_atividade
    ADD CONSTRAINT ramo_atividade_pkey PRIMARY KEY (id_ramo_atividade);


--
-- TOC entry 3407 (class 2606 OID 24299)
-- Name: salario salario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salario
    ADD CONSTRAINT salario_pkey PRIMARY KEY (id_salario);


--
-- TOC entry 3409 (class 2606 OID 24306)
-- Name: secretaria secretaria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.secretaria
    ADD CONSTRAINT secretaria_pkey PRIMARY KEY (id_secretaria);


--
-- TOC entry 3411 (class 2606 OID 24312)
-- Name: taixa_conveiniados taixa_conveiniados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taixa_conveiniados
    ADD CONSTRAINT taixa_conveiniados_pkey PRIMARY KEY (id_taixa_conveiniados);


--
-- TOC entry 3413 (class 2606 OID 24318)
-- Name: taixa_entidade taixa_entidade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taixa_entidade
    ADD CONSTRAINT taixa_entidade_pkey PRIMARY KEY (id_taixa_entidade);


--
-- TOC entry 3415 (class 2606 OID 24324)
-- Name: taxa_calc_limite_credito_func taxa_calc_limite_credito_func_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taxa_calc_limite_credito_func
    ADD CONSTRAINT taxa_calc_limite_credito_func_pkey PRIMARY KEY (id_taxa_calc_limite_credito_func);


--
-- TOC entry 3419 (class 2606 OID 24334)
-- Name: usuario_acesso usuario_acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT usuario_acesso_pkey PRIMARY KEY (id_usuario_acesso);


--
-- TOC entry 3417 (class 2606 OID 24329)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 3421 (class 2606 OID 24344)
-- Name: venda venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT venda_pkey PRIMARY KEY (id_venda);


--
-- TOC entry 3448 (class 2606 OID 24508)
-- Name: taxa_calc_limite_credito_func fk_ativ_mud; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taxa_calc_limite_credito_func
    ADD CONSTRAINT fk_ativ_mud FOREIGN KEY (id_entidade) REFERENCES public.entidade(id_entidade);


--
-- TOC entry 3422 (class 2606 OID 24378)
-- Name: cartao fk_cartao_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cartao
    ADD CONSTRAINT fk_cartao_funcionario FOREIGN KEY (id_funcionario) REFERENCES public.funcionario(id_funcionario);


--
-- TOC entry 3423 (class 2606 OID 24383)
-- Name: ciclo_pagamento_venda fk_ciclo_pg_venda_conv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciclo_pagamento_venda
    ADD CONSTRAINT fk_ciclo_pg_venda_conv FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3424 (class 2606 OID 24388)
-- Name: ciclo_pagamento_venda fk_ciclo_pg_venda_tx_conveiniados; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciclo_pagamento_venda
    ADD CONSTRAINT fk_ciclo_pg_venda_tx_conveiniados FOREIGN KEY (id_taixa_conveiniados) REFERENCES public.taixa_conveiniados(id_taixa_conveiniados);


--
-- TOC entry 3425 (class 2606 OID 24393)
-- Name: contas_receber fk_contas_receber_entidade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contas_receber
    ADD CONSTRAINT fk_contas_receber_entidade FOREIGN KEY (id_entidade) REFERENCES public.entidade(id_entidade);


--
-- TOC entry 3426 (class 2606 OID 24398)
-- Name: contas_receber fk_contas_receber_taixa_enti; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contas_receber
    ADD CONSTRAINT fk_contas_receber_taixa_enti FOREIGN KEY (id_taixa_entidade) REFERENCES public.taixa_entidade(id_taixa_entidade);


--
-- TOC entry 3427 (class 2606 OID 24403)
-- Name: conveniados fk_conveniados_nicho; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conveniados
    ADD CONSTRAINT fk_conveniados_nicho FOREIGN KEY (id_nicho) REFERENCES public.nicho(id_nicho);


--
-- TOC entry 3428 (class 2606 OID 24408)
-- Name: conveniados fk_conveniados_ramo_atividade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conveniados
    ADD CONSTRAINT fk_conveniados_ramo_atividade FOREIGN KEY (id_ramo_atividade) REFERENCES public.ramo_atividade(id_ramo_atividade);


--
-- TOC entry 3429 (class 2606 OID 24413)
-- Name: funcionario fk_funcionario_entidade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcionario
    ADD CONSTRAINT fk_funcionario_entidade FOREIGN KEY (id_entidade) REFERENCES public.entidade(id_entidade);

--
-- TOC entry 3431 (class 2606 OID 24423)
-- Name: funcionario fk_funcionario_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcionario
    ADD CONSTRAINT fk_funcionario_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);

--
-- TOC entry 3433 (class 2606 OID 24433)
-- Name: funcionario fk_funcionario_secretaria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcionario
    ADD CONSTRAINT fk_funcionario_secretaria FOREIGN KEY (id_secretaria) REFERENCES public.secretaria(id_secretaria);


--
-- TOC entry 3434 (class 2606 OID 24438)
-- Name: itens_venda fk_itens_venda_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itens_venda
    ADD CONSTRAINT fk_itens_venda_produto FOREIGN KEY (id_produto) REFERENCES public.produto(id_produto);


--
-- TOC entry 3435 (class 2606 OID 24443)
-- Name: itens_venda fk_itens_venda_venda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itens_venda
    ADD CONSTRAINT fk_itens_venda_venda FOREIGN KEY (id_venda) REFERENCES public.venda(id_venda);


--
-- TOC entry 3436 (class 2606 OID 24448)
-- Name: limite_credito fk_limite_credito_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.limite_credito
    ADD CONSTRAINT fk_limite_credito_funcionario FOREIGN KEY (id_funcionario) REFERENCES public.funcionario(id_funcionario);


--
-- TOC entry 3437 (class 2606 OID 24453)
-- Name: nicho fk_nicho_conve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nicho
    ADD CONSTRAINT fk_nicho_conve FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3438 (class 2606 OID 24458)
-- Name: pessoa fk_pessoa_conveniados; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT fk_pessoa_conveniados FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3440 (class 2606 OID 24468)
-- Name: pessoa_fisica fk_pessoa_fisica_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT fk_pessoa_fisica_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);


--
-- TOC entry 3441 (class 2606 OID 24473)
-- Name: pessoa_juridica fk_pessoa_juridica_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT fk_pessoa_juridica_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);


--
-- TOC entry 3439 (class 2606 OID 24463)
-- Name: pessoa fk_pessoa_uusario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT fk_pessoa_uusario FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 3442 (class 2606 OID 24478)
-- Name: produto fk_produto_conveniado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT fk_produto_conveniado FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3443 (class 2606 OID 24483)
-- Name: ramo_atividade fk_ramo_atividade_conve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ramo_atividade
    ADD CONSTRAINT fk_ramo_atividade_conve FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3444 (class 2606 OID 24488)
-- Name: salario fk_salario_funcionario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salario
    ADD CONSTRAINT fk_salario_funcionario FOREIGN KEY (id_funcionario) REFERENCES public.funcionario(id_funcionario);


--
-- TOC entry 3445 (class 2606 OID 24493)
-- Name: secretaria fk_secretaria_entidade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.secretaria
    ADD CONSTRAINT fk_secretaria_entidade FOREIGN KEY (id_entidade) REFERENCES public.entidade(id_entidade);


--
-- TOC entry 3447 (class 2606 OID 24503)
-- Name: taixa_entidade fk_taixa_enti_entidade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taixa_entidade
    ADD CONSTRAINT fk_taixa_enti_entidade FOREIGN KEY (id_entidade) REFERENCES public.entidade(id_entidade);


--
-- TOC entry 3446 (class 2606 OID 24498)
-- Name: taixa_conveiniados fk_tx_conveiniado_conveniado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.taixa_conveiniados
    ADD CONSTRAINT fk_tx_conveiniado_conveniado FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3449 (class 2606 OID 24513)
-- Name: usuario_acesso fk_usuario_ac_acesso; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT fk_usuario_ac_acesso FOREIGN KEY (id_acesso) REFERENCES public.acesso(id_acesso);


--
-- TOC entry 3450 (class 2606 OID 24518)
-- Name: usuario_acesso fk_usuario_ac_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT fk_usuario_ac_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 3451 (class 2606 OID 24523)
-- Name: venda fk_venda_cartao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT fk_venda_cartao FOREIGN KEY (id_cartao) REFERENCES public.cartao(id_cartao);


--
-- TOC entry 3452 (class 2606 OID 24528)
-- Name: venda fk_venda_conveniado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT fk_venda_conveniado FOREIGN KEY (id_conveniados) REFERENCES public.conveniados(id_conveniados);


--
-- TOC entry 3453 (class 2606 OID 24533)
-- Name: venda fk_venda_tx_conveiniado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT fk_venda_tx_conveiniado FOREIGN KEY (id_taixa_conveiniados) REFERENCES public.taixa_conveiniados(id_taixa_conveiniados);


--
-- TOC entry 3454 (class 2606 OID 24538)
-- Name: venda fk_venda_tx_entidade; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT fk_venda_tx_entidade FOREIGN KEY (id_taixa_entidade) REFERENCES public.taixa_entidade(id_taixa_entidade);


-- Completed on 2025-02-16 14:32:46 -03

--
-- PostgreSQL database dump complete
--

