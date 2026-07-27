import api from "./api";

export async function buscarContaCorrente(inicio, fim, descricao = "") {
  const response = await api.get("/dashboard/conta-corrente", {
    params: {
      inicio,
      fim,
      descricao,
    },
  });

  return response.data;
}
