import api from "./api";

function salvarSessao(data) {
  localStorage.setItem("token", data.token);
  localStorage.setItem("nome", data.nome);
  localStorage.setItem("email", data.email);
}

export async function login(credenciais) {
  const response = await api.post("/auth/login", credenciais);

  salvarSessao(response.data);

  return response.data;
}

export async function registrar(usuario) {
  const response = await api.post("/auth/register", usuario);

  salvarSessao(response.data);

  return response.data;
}
