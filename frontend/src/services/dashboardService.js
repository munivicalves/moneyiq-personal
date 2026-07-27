import api from "./api";

export async function buscarDashboard(competencia) {
  const { data } = await api.get("/dashboard", {
    params: {
      competencia,
    },
  });

  return data;
}

export async function buscarCategorias(competencia) {
  const { data } = await api.get("/dashboard/categorias", {
    params: { competencia },
  });

  return data;
}
