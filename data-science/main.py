import os
import sys
import requests
import pandas as pd
import matplotlib.pyplot as plt
from pathlib import Path
from sklearn.linear_model import LinearRegression
import numpy as np

TOKEN = os.getenv("SENTINEL_TOKEN")
API_URL = os.getenv(
    "SENTINEL_API_URL",
    "http://localhost:8080/api"
)

if not TOKEN:
    sys.exit(
        "Defina SENTINEL_TOKEN com um JWT válido antes de executar o script."
    )

headers = {
    "Authorization": f"Bearer {TOKEN}"
}

response = requests.get(
    f"{API_URL}/dashboard/dados-historico",
    headers=headers
)
response.raise_for_status()

dados = response.json()

print(dados)

df = pd.DataFrame(dados)

Path("outputs").mkdir(exist_ok=True)

# CSV
df.to_csv(
    "outputs/historico_financeiro.csv",
    index=False
)

# RECEITAS X DESPESAS
plt.figure(figsize=(10, 5))

plt.plot(
    df["competencia"],
    df["receitas"],
    marker="o",
    label="Receitas"
)

plt.plot(
    df["competencia"],
    df["despesas"],
    marker="o",
    label="Despesas"
)

plt.title("Evolução Financeira")
plt.xlabel("Competência")
plt.ylabel("Valor")
plt.legend()

plt.xticks(rotation=45)

plt.tight_layout()

plt.savefig(
    "outputs/evolucao_financeira.png"
)

plt.close()

# SALDO
plt.figure(figsize=(10, 5))

plt.bar(
    df["competencia"],
    df["saldo"]
)

plt.title("Saldo Mensal")
plt.xlabel("Competência")
plt.ylabel("Saldo")

plt.xticks(rotation=45)

plt.tight_layout()

plt.savefig(
    "outputs/saldo_mensal.png"
)

plt.close()

# Estatísticas
estatisticas = df.describe()

estatisticas.to_csv(
    "outputs/estatisticas.csv"
)

# Economia do mês

df["taxa_economia"] = (
    (df["saldo"] / df["receitas"].replace(0, 1))
    * 100
)

print(df[[
    "competencia",
    "taxa_economia"
]])

plt.figure(figsize=(10,5))

plt.plot(
    df["competencia"],
    df["taxa_economia"],
    marker="o"
)

plt.title("Taxa de Economia")
plt.ylabel("%")
plt.xlabel("Competência")

plt.xticks(rotation=45)

plt.tight_layout()

plt.savefig(
    "outputs/taxa_economia.png"
)

plt.close()

def classificar(taxa):
    if taxa >= 20:
        return "Excelente"
    elif taxa >= 10:
        return "Boa"
    else:
        return "Atenção"

df["classificacao"] = df["taxa_economia"].apply(classificar)

print(
    df[
        [
            "competencia",
            "taxa_economia",
            "classificacao"
        ]
    ]
)

df.to_csv(
    "outputs/classificacao_financeira.csv",
    index=False
)

ultimo_mes = df.iloc[-1]

with open(
    "outputs/relatorio.txt",
    "w",
    encoding="utf-8"
) as f:

    f.write("RELATÓRIO FINANCEIRO\n\n")

    f.write(
        f"Competência: {ultimo_mes['competencia']}\n"
    )

    f.write(
        f"Receitas: R$ {ultimo_mes['receitas']:.2f}\n"
    )

    f.write(
        f"Despesas: R$ {ultimo_mes['despesas']:.2f}\n"
    )

    f.write(
        f"Saldo: R$ {ultimo_mes['saldo']:.2f}\n"
    )

    f.write(
        f"Taxa de economia: {ultimo_mes['taxa_economia']:.2f}%\n"
    )

    f.write(
        f"Classificação: {ultimo_mes['classificacao']}\n"
    )
    
X = np.arange(len(df)).reshape(-1, 1)

y = df["saldo"]

modelo = LinearRegression()

modelo.fit(X, y)

proximo_mes = np.array([[len(df)]])

previsao = modelo.predict(proximo_mes)

print(
    f"Saldo previsto para próximo mês: R$ {previsao[0]:.2f}"
) 


print("Arquivos gerados com sucesso.")
