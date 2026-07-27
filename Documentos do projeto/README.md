# Documentos do Projeto — Sentinel Prime

Índice da documentação técnica e de usuário do **Sentinel Prime — Sistema de
Controle Financeiro Pessoal** (Projetos de Software II).

Cada artefato é mantido **separadamente**, em formato versionável (Markdown e
HTML/Mermaid), sempre alinhado ao código. Para entrega, é possível gerar um
**PDF consolidado** a partir destes arquivos (ver [seção final](#gerar-pdf-para-entrega)).

---

## Artefatos

| Documento | Conteúdo |
|-----------|----------|
| [REQUISITOS.md](REQUISITOS.md) | Escopo do projeto, atores, requisitos funcionais (RF), não funcionais (RNF), regras de negócio e rastreabilidade. |
| [Diagrama de Casos de Uso](sentinel_prime_use_case_diagram.html) | Ator e casos de uso (UC01–UC08) — UML em Mermaid. |
| [Diagrama de Dados (DER)](sentinel_prime_DER.html) | Modelo entidade-relacionamento com as 8 entidades — Mermaid. |
| [Diagrama de Classes](sentinel_prime_class_diagram.html) | Classes do modelo de domínio (entidades JPA) e relacionamentos — Mermaid. |
| [Diagrama de Componentes](sentinel_prime_component_diagram.html) | Interações entre os componentes (clientes → segurança → controllers → services → repositories → banco) — Mermaid. |
| [QUADRO-DE-GESTAO.md](QUADRO-DE-GESTAO.md) | Quadro de gestão das atividades: Kanban, atividades por etapa/checkpoint e cronograma. |
| [MANUAL-DO-USUARIO.md](MANUAL-DO-USUARIO.md) | Documentação do usuário: como usar cada tela do sistema. |

> Os diagramas `.html` usam [Mermaid](https://mermaid.js.org/) e renderizam ao abrir
> o arquivo no navegador (com suporte a tema claro/escuro).

---

## Mapa de cobertura (etapas da disciplina)

| Etapa | Artefato |
|-------|----------|
| 4.1 Escopo do projeto | [REQUISITOS.md](REQUISITOS.md) — seção 2 |
| 4.2 Requisitos funcionais e não funcionais | [REQUISITOS.md](REQUISITOS.md) — seções 4 e 5 |
| 4.3 Diagrama de casos de uso | [Casos de Uso](sentinel_prime_use_case_diagram.html) |
| 4.4 Quadro de gestão das atividades | [QUADRO-DE-GESTAO.md](QUADRO-DE-GESTAO.md) |
| 4.6 Documentos de casos de uso | [REQUISITOS.md](REQUISITOS.md) — rastreabilidade (seção 7) |
| 4.7 Diagrama de dados (DER) | [DER](sentinel_prime_DER.html) |
| 4.8 Diagrama de classes do modelo de dados | [Diagrama de Classes](sentinel_prime_class_diagram.html) |
| 4.8 Diagrama de interações entre componentes (CP3) | [Diagrama de Componentes](sentinel_prime_component_diagram.html) |
| 4.11 Manual do usuário | [MANUAL-DO-USUARIO.md](MANUAL-DO-USUARIO.md) |

> A prototipação de telas (4.5) é mantida no **Figma**.

---

## Modelo de dados (8 entidades)

`Usuario`, `Transacao`, `Categoria`, `CartaoCredito`, `DespesaCartao`,
`DespesaFixa`, `ContaBancaria` e `FaturaCartao`.

Definições no pacote `backend/springboot-api/src/main/java/com/sentinelprime/backend/model`.

---

## Gerar PDF para entrega

Os arquivos Markdown podem ser convertidos em um PDF único. Exemplos:

- **VS Code** — extensão *Markdown PDF* (botão direito → *Markdown PDF: Export (pdf)*).
- **Pandoc** — `pandoc REQUISITOS.md MANUAL-DO-USUARIO.md -o documento-do-projeto.pdf`.

Os diagramas `.html` podem ser exportados como imagem/PDF diretamente pelo navegador
(*Imprimir → Salvar como PDF*) e anexados ao documento consolidado.

---

> O antigo `doc do projeto.pdf` (baseado em imagens, com o modelo desatualizado de
> 3 entidades) foi **substituído** por estes artefatos versionáveis.
