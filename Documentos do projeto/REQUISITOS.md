# Documento de Requisitos — Sentinel Prime

**Sistema de Controle Financeiro Pessoal**
Projetos de Software II — Especificação de Requisitos de Software (ERS)

---

## Sumário

1. [Introdução](#1-introdução)
2. [Escopo do projeto](#2-escopo-do-projeto)
3. [Atores do sistema](#3-atores-do-sistema)
4. [Requisitos Funcionais (RF)](#4-requisitos-funcionais-rf)
5. [Requisitos Não Funcionais (RNF)](#5-requisitos-não-funcionais-rnf)
6. [Regras de Negócio (RN)](#6-regras-de-negócio-rn)
7. [Rastreabilidade](#7-rastreabilidade)

---

## 1. Introdução

### 1.1 Objetivo

Especificar os requisitos funcionais e não funcionais do **Sentinel Prime**, um
sistema de controle financeiro pessoal que permite ao usuário registrar e
acompanhar receitas, despesas, gastos no cartão de crédito e visualizar um extrato
consolidado com o saldo de cada período.

### 1.2 Definições e abreviações

| Termo | Significado |
|-------|-------------|
| **RF** | Requisito Funcional |
| **RNF** | Requisito Não Funcional |
| **RN** | Regra de Negócio |
| **UC** | Caso de Uso |
| **Competência** | Mês/ano de referência de um lançamento (ex.: 2026-06) |
| **Receita** | Entrada de valor (salário, extras) |
| **Despesa** | Saída de valor |
| **Fatura** | Total dos gastos de um cartão em uma competência |
| **JWT** | JSON Web Token, usado na autenticação |

### 1.3 Visão geral do produto

O sistema é disponibilizado em **duas interfaces** que compartilham a mesma conta e
a mesma API REST:

- **Web** — React, Vite e Tailwind CSS.
- **Mobile** — aplicativo Android nativo.

O **backend** é uma API REST em Spring Boot (Java 21), com persistência em
PostgreSQL, e está implantado em nuvem.

---

## 2. Escopo do projeto

### 2.1 Incluído no escopo

- Cadastro e autenticação de usuários.
- Gestão de **receitas** (com tipo: salário/extras).
- Gestão de **despesas** (avulsas e fixas/recorrentes), classificadas por categoria.
- Gestão de **cartões de crédito**, suas **despesas** (com parcelamento) e **faturas**.
- **Contas bancárias** cadastradas pelo usuário.
- **Extrato consolidado** (conta corrente) com saldo acumulado e exportação CSV.
- **Dashboard** com indicadores e gráficos do período.
- Filtragem por **competência** (mês/ano) e por período de datas.
- Interface **web responsiva** com tema claro/escuro e **app Android**.
- Implantação em **nuvem** (acesso remoto).

### 2.2 Fora do escopo

- Integração automática com bancos/Open Finance (lançamentos são manuais).
- Múltiplos perfis/papéis de acesso (o sistema tem um único tipo de usuário).
- Funcionalidades de planejamento de investimentos e metas (não previstas nesta versão).
- Compartilhamento de contas entre diferentes usuários.

### 2.3 Restrições

- Idioma: **Português (Brasil)**; moeda: **Real (BRL)**.
- Cada usuário acessa **apenas os próprios dados**.

---

## 3. Atores do sistema

| Ator | Descrição |
|------|-----------|
| **Usuário** | Pessoa que utiliza o sistema para controlar suas finanças pessoais. É o único ator e responsável por todas as interações. |
| **Sistema (API)** | Processa as requisições, aplica as regras de negócio e persiste os dados. |

---

## 4. Requisitos Funcionais (RF)

### 4.1 Autenticação e conta

| ID | Requisito |
|----|-----------|
| **RF01** | O sistema deve permitir o **cadastro** de um usuário com nome, e-mail e senha. |
| **RF02** | O sistema deve permitir o **login** com e-mail e senha, retornando um token de autenticação (JWT). |
| **RF03** | O sistema deve permitir ao usuário **encerrar a sessão** (logout). |
| **RF04** | O sistema deve **restringir o acesso** às funcionalidades internas a usuários autenticados. |

### 4.2 Receitas

| ID | Requisito |
|----|-----------|
| **RF05** | O sistema deve permitir **registrar uma receita** (descrição, valor, tipo de receita e data). |
| **RF06** | O sistema deve permitir **listar as receitas** do usuário filtradas por competência. |
| **RF07** | O sistema deve permitir **excluir** uma receita. |
| **RF08** | O sistema deve exibir **totais de receitas** do período (total geral, salário e extras). |

### 4.3 Despesas

| ID | Requisito |
|----|-----------|
| **RF09** | O sistema deve permitir **registrar uma despesa** (descrição, valor, categoria e data), podendo marcá-la como **fixa/recorrente**. |
| **RF10** | O sistema deve permitir **listar as despesas** do usuário filtradas por competência. |
| **RF11** | O sistema deve permitir **excluir** uma despesa. |
| **RF12** | O sistema deve manter **despesas fixas** com dia de vencimento, vigência (início/fim ou indeterminada) e situação (ativa/inativa). |
| **RF13** | O sistema deve exibir **totais de despesas** do período (total, quantidade e média). |

### 4.4 Cartão de crédito

| ID | Requisito |
|----|-----------|
| **RF14** | O sistema deve permitir **cadastrar, editar e excluir cartões de crédito** (nome, bandeira e dia de vencimento). |
| **RF15** | O sistema deve permitir **lançar despesas no cartão**, com categoria e **parcelamento** (parcela atual / total de parcelas). |
| **RF16** | O sistema deve permitir **excluir** uma despesa de cartão. |
| **RF17** | O sistema deve apresentar a **fatura** de um cartão por competência, com o total e a lista de lançamentos. |

### 4.5 Contas bancárias e extrato

| ID | Requisito |
|----|-----------|
| **RF18** | O sistema deve permitir **cadastrar contas bancárias** do usuário (nome, banco, agência, número, tipo e saldo inicial). |
| **RF19** | O sistema deve apresentar um **extrato consolidado (conta corrente)** reunindo receitas, despesas e faturas em ordem de data, com **saldo acumulado**. |
| **RF20** | O sistema deve permitir **filtrar o extrato** por intervalo de datas e por texto de busca na descrição. |
| **RF21** | O sistema deve permitir **exportar o extrato em CSV**. |

### 4.6 Dashboard e relatórios

| ID | Requisito |
|----|-----------|
| **RF22** | O sistema deve exibir um **dashboard** com saldo do período, total de receitas, total de despesas, gráficos de despesas por categoria e as últimas transações. |
| **RF23** | O sistema deve permitir **filtrar as informações por competência** (mês/ano). |

### 4.7 Interface e preferências

| ID | Requisito |
|----|-----------|
| **RF24** | O sistema deve oferecer alternância entre **tema claro e escuro**, preservando a preferência do usuário. |
| **RF25** | O sistema deve estar disponível em **interface web** e **aplicativo Android**, ambos usando a mesma conta. |

---

## 5. Requisitos Não Funcionais (RNF)

### 5.1 Usabilidade

| ID | Requisito |
|----|-----------|
| **RNF01** | A interface web deve ser **responsiva**, adaptando-se a desktop e dispositivos móveis. |
| **RNF02** | Valores monetários devem ser exibidos no formato **brasileiro (R$)** e datas em **dd/mm/aaaa**. |
| **RNF03** | Listagens extensas devem ser **paginadas** para preservar a legibilidade e o desempenho. |
| **RNF04** | Ações destrutivas (exclusões) devem exigir **confirmação** do usuário. |

### 5.2 Segurança

| ID | Requisito |
|----|-----------|
| **RNF05** | As senhas devem ser armazenadas de forma **cifrada** (hash BCrypt), nunca em texto puro. |
| **RNF06** | A autenticação deve ser **stateless** via **JWT**, exigido nas rotas protegidas. |
| **RNF07** | Cada usuário deve acessar **somente os próprios dados**. |
| **RNF08** | O backend deve aplicar política de **CORS** restrita ao domínio do frontend. |

### 5.3 Desempenho e disponibilidade

| ID | Requisito |
|----|-----------|
| **RNF09** | O sistema deve estar **acessível remotamente** (implantado em nuvem). |
| **RNF10** | O backend deve expor um **health check** para monitoramento de disponibilidade. |

### 5.4 Portabilidade e tecnologia

| ID | Requisito |
|----|-----------|
| **RNF11** | O backend deve ser desenvolvido em **Java 21 / Spring Boot**, expondo uma **API REST**. |
| **RNF12** | A persistência deve usar **PostgreSQL** em produção. |
| **RNF13** | O frontend web deve ser desenvolvido em **React + Vite + Tailwind CSS**. |
| **RNF14** | O aplicativo mobile deve ser **Android nativo**. |

### 5.5 Qualidade e manutenção

| ID | Requisito |
|----|-----------|
| **RNF15** | O backend deve possuir **testes automatizados** (abordagem TDD), executáveis com banco em memória (H2) sem dependência de infraestrutura externa. |
| **RNF16** | O código deve ser versionado em **Git**, com integração contínua (CI) validando backend, frontend, Android e data science. |

---

## 6. Regras de Negócio (RN)

| ID | Regra |
|----|-------|
| **RN01** | O **e-mail** de cadastro deve ser **único** no sistema. |
| **RN02** | Uma transação é sempre do tipo **RECEITA** ou **DESPESA**. |
| **RN03** | O **valor** de uma transação deve ser **maior que zero**. |
| **RN04** | Uma **despesa fixa não indeterminada** deve possuir **data de fim**; se indeterminada, a data de fim é dispensada. |
| **RN05** | No extrato (conta corrente), **receitas** entram como **entrada** e **despesas/faturas** como **saída**; o **saldo** é acumulado em ordem cronológica. |
| **RN06** | A **fatura** de um cartão corresponde à soma das despesas daquele cartão em uma mesma **competência**. |
| **RN07** | A **data de cadastro** do usuário e a **data de criação** dos lançamentos são definidas automaticamente pelo sistema. |
| **RN08** | Toda operação financeira pertence ao **usuário autenticado** que a criou. |

---

## 7. Rastreabilidade

### 7.1 Requisitos × Casos de Uso

| Caso de Uso | Requisitos relacionados |
|-------------|-------------------------|
| UC01 — Cadastrar Usuário | RF01 |
| UC02 — Realizar Login | RF02, RF04 |
| UC03 — Registrar Receita | RF05, RF06 |
| UC04 — Registrar Despesa | RF09, RF10, RF12 |
| UC05 — Editar Transação | RF14 |
| UC06 — Excluir Transação | RF07, RF11, RF16 |
| UC07 — Visualizar Dashboard Financeiro | RF22, RF23 |
| UC08 — Gerar Relatório de Despesas | RF19, RF20, RF21 |

### 7.2 Requisitos × Entidades do modelo de dados

| Entidade | Requisitos relacionados |
|----------|-------------------------|
| **Usuario** | RF01, RF02, RF04 |
| **Transacao** | RF05–RF11, RF13, RF19, RF22 |
| **Categoria** | RF09, RF22 |
| **DespesaFixa** | RF12 |
| **CartaoCredito** | RF14, RF17 |
| **DespesaCartao** | RF15, RF16, RF17 |
| **FaturaCartao** | RF17, RF19 |
| **ContaBancaria** | RF18 |

---

*Sentinel Prime — Controle Financeiro Inteligente.*
