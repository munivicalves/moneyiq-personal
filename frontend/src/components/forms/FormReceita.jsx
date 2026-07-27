import { useState } from "react";
import Modal from "../Modal";
import AlertModal from "../AlertModal";
import { criarReceita } from "../../services/receitaService";
import { getApiErrorMessage } from "../../services/apiError";

export default function FormReceita({ onClose }) {
  const [form, setForm] = useState({
    descricao: "",
    valor: "",
    dataTransacao: "",
    tipoReceita: "SALARIO",
  });

  const [alert, setAlert] = useState(null);

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      const valorNumerico = Number(
        form.valor
          .replace("R$", "")
          .replace(/\./g, "")
          .replace(",", ".")
          .trim(),
      );

      const payload = {
        tipo: "RECEITA",
        tipoReceita: form.tipoReceita,
        descricao: form.descricao,
        valor: valorNumerico,
        dataTransacao: form.dataTransacao,
      };

      await criarReceita(payload);

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Receita cadastrada com sucesso!",
        onClose: () => {
          setAlert(null);
          onClose?.();
        },
      });
    } catch (error) {
      console.error("Erro ao cadastrar receita", error);
      console.error("Resposta da API:", JSON.stringify(error?.response?.data));

      setAlert({
        type: "error",
        title: "Erro",
        message: getApiErrorMessage(error),
        onClose: () => setAlert(null),
      });
    }
  }

  function formatCurrency(value) {
    value = value.replace(/\D/g, "");

    value = (Number(value) / 100).toLocaleString("pt-BR", {
      style: "currency",
      currency: "BRL",
    });

    return value;
  }

  return (
    <>
      <Modal title="Nova Receita" onClose={onClose}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <input
            name="descricao"
            placeholder="Descrição"
            className="input"
            value={form.descricao}
            onChange={handleChange}
            required
          />

          <input
            name="valor"
            placeholder="Valor"
            className="input"
            value={form.valor}
            onChange={(e) =>
              setForm({
                ...form,
                valor: formatCurrency(e.target.value),
              })
            }
            required
          />

          <input
            type="date"
            name="dataTransacao"
            className="input"
            value={form.dataTransacao}
            onChange={handleChange}
            required
          />

          <select
            name="tipoReceita"
            className="input"
            value={form.tipoReceita}
            onChange={handleChange}
          >
            <option value="SALARIO">Salário</option>
            <option value="EXTRA">Extra</option>
            <option value="OUTROS">Outros</option>
          </select>

          <button type="submit" className="btn">
            Salvar Receita
          </button>
        </form>
      </Modal>

      {alert && <AlertModal {...alert} />}
    </>
  );
}
