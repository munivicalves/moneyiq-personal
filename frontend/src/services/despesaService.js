import api from "./api";

export async function listarDespesas(mes, ano) {
  const inicio = `${ano}-${String(mes).padStart(2, "0")}-01`;

  const ultimoDia = new Date(ano, mes, 0).getDate();

  const fim = `${ano}-${String(mes).padStart(2, "0")}-${ultimoDia}`;

  const response = await api.get("/transacoes", {
    params: {
      tipo: "DESPESA",
      inicio,
      fim,
    },
  });

  return response.data;
}

export async function criarDespesa(despesa) {
  const response = await api.post("/transacoes", despesa);

  return response.data;
}

export async function excluirDespesa(id) {
  await api.delete(`/transacoes/${id}`);
}
