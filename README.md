# Sentinel Prime

[![CI](https://github.com/AlexandreR84/Sentinel-Prime/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/AlexandreR84/Sentinel-Prime/actions/workflows/ci.yml)

Sentinel Prime e um sistema de controle financeiro pessoal com backend Spring Boot, frontend React/Vite, aplicativo Android e scripts de apoio para data science.

## Estrutura

- `backend/springboot-api` - API REST em Spring Boot.
- `frontend` - aplicacao web em React, Vite e Tailwind CSS.
- `android-app` - aplicativo Android nativo.
- `data-science` - scripts e dependencias Python para analises.
- `Documentos do projeto` - diagramas e documentacao de apoio.

## Requisitos

- Java 21
- Node.js 22 ou superior
- PostgreSQL
- Python 3.12 ou superior
- Android Studio ou Gradle para o app Android

## Backend

O backend usa estas variaveis de ambiente, com defaults locais:

- `DB_URL` - default `jdbc:postgresql://localhost:5432/sentinelprime`
- `DB_USERNAME` - default `postgres`
- `DB_PASSWORD` - default `postgres`

Para sobrescrever no PowerShell antes de subir a aplicacao:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/sentinelprime"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
```

Rodar:

```powershell
cd "backend/springboot-api"
.\mvnw.cmd spring-boot:run
```

Testar:

```powershell
cd "backend/springboot-api"
.\mvnw.cmd test
```

Os testes usam H2 em memoria e nao dependem do PostgreSQL local.

## Frontend

```powershell
cd frontend
npm ci
npm run dev
```

Validar:

```powershell
npm run lint
npm run build
```

## Android

Aplicativo nativo integrado ao backend (login/JWT e dashboard via API REST).

**Download do APK:** [sentinel-prime-v0.1.0.apk](https://github.com/AlexandreR84/Sentinel-Prime/releases/download/v0.1.0/sentinel-prime-v0.1.0.apk) (Android 8.0+; habilite "Instalar de fontes desconhecidas").

```powershell
cd android-app
.\gradlew.bat test
```

## Data Science

```powershell
cd data-science
python -m venv venv
.\venv\Scripts\Activate.ps1
pip install -r requirements.txt
python -m py_compile main.py
```

## GitHub

O repositorio usa:

- CI em `.github/workflows/ci.yml` para backend, frontend, Android e data science.
- Dependabot em `.github/dependabot.yml`.
- Templates de issue e pull request em `.github/`.
- `CONTRIBUTING.md` para fluxo de contribuicao.
- `SECURITY.md` para orientacoes de seguranca.
