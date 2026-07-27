export function getApiErrorMessage(error) {
  const data = error?.response?.data;

  if (!data) {
    return "Erro inesperado ao comunicar com o servidor.";
  }

  if (typeof data === "string") {
    return data;
  }

  if (data.message) {
    return data.message;
  }

  if (typeof data === "object") {
    return Object.entries(data)
      .map(([field, message]) => `${field}: ${message}`)
      .join("\n");
  }

  return "Erro inesperado.";
}
