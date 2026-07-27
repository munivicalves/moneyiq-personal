import api from "./api";

export async function listarReceitas(mes, ano) {
  const inicio = `${ano}-${String(mes).padStart(2, "0")}-01`;

  const ultimoDia = new Date(ano, mes, 0).getDate();

  const fim = `${ano}-${String(mes).padStart(2, "0")}-${String(
    ultimoDia,
  ).padStart(2, "0")}`;

  const response = await api.get("/transacoes", {
    params: {
      tipo: "RECEITA",
      inicio,
      fim,
    },
  });

  return response.data;
}

export async function criarReceita(receita) {
  const response = await api.post("/transacoes", receita);

  return response.data;
}

export async function excluirReceita(id) {
  await api.delete(`/transacoes/${id}`);
}
