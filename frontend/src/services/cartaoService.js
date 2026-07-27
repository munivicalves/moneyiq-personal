import api from "./api";

export async function listarCartoes() {
  const response = await api.get("/cartoes");
  return response.data;
}

export async function criarCartao(payload) {
  const response = await api.post("/cartoes", payload);
  return response.data;
}

export async function atualizarCartao(id, payload) {
  const response = await api.put(`/cartoes/${id}`, payload);
  return response.data;
}

export async function excluirCartao(id) {
  await api.delete(`/cartoes/${id}`);
}

export async function buscarFatura(cartaoId, competencia) {
  const response = await api.get(
    `/cartoes/${cartaoId}/fatura?competencia=${competencia}`,
  );

  return response.data;
}

export async function criarDespesaCartao(cartaoId, payload) {
  const response = await api.post(`/cartoes/${cartaoId}/despesas`, payload);

  return response.data;
}

export async function excluirDespesaCartao(cartaoId, despesaId) {
  await api.delete(`/cartoes/${cartaoId}/despesas/${despesaId}`);
}
