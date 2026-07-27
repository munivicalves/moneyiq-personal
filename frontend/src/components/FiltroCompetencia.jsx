import { ChevronLeft, ChevronRight } from "lucide-react";
import { useState } from "react";

const meses = [
  "Janeiro",
  "Fevereiro",
  "Março",
  "Abril",
  "Maio",
  "Junho",
  "Julho",
  "Agosto",
  "Setembro",
  "Outubro",
  "Novembro",
  "Dezembro",
];

export default function FiltroCompetencia({ onChange }) {
  const hoje = new Date();

  const [mes, setMes] = useState(hoje.getMonth());
  const [ano, setAno] = useState(hoje.getFullYear());

  function anterior() {
    let novoMes = mes - 1;
    let novoAno = ano;

    if (novoMes < 0) {
      novoMes = 11;
      novoAno--;
    }

    setMes(novoMes);
    setAno(novoAno);

    onChange?.({ mes: novoMes + 1, ano: novoAno });
  }

  function proximo() {
    let novoMes = mes + 1;
    let novoAno = ano;

    if (novoMes > 11) {
      novoMes = 0;
      novoAno++;
    }

    setMes(novoMes);
    setAno(novoAno);

    onChange?.({ mes: novoMes + 1, ano: novoAno });
  }

  return (
    <div className="flex items-center justify-between gap-3 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl px-3 py-2 shadow-sm">
      <button
        onClick={anterior}
        className="p-1 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg"
      >
        <ChevronLeft size={18} />
      </button>

      <div className="text-center min-w-[120px]">
        <div className="text-sm font-semibold text-slate-800 dark:text-slate-100">
          {meses[mes]}
        </div>

        <div className="text-xs text-slate-500 dark:text-slate-400">{ano}</div>
      </div>

      <button
        onClick={proximo}
        className="p-1 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg"
      >
        <ChevronRight size={18} />
      </button>
    </div>
  );
}
