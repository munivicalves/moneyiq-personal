import { Check, AlertCircle } from "lucide-react";

export default function AlertModal({
  type = "success",
  title,
  message,
  onClose,
}) {
  const isSuccess = type === "success";

  const bgColor = isSuccess
    ? "bg-emerald-50 dark:bg-emerald-950"
    : "bg-red-50 dark:bg-red-950";
  const borderColor = isSuccess
    ? "border-emerald-200 dark:border-emerald-800"
    : "border-red-200 dark:border-red-800";
  const iconBgColor = isSuccess
    ? "bg-emerald-100 dark:bg-emerald-900"
    : "bg-red-100 dark:bg-red-900";
  const iconColor = isSuccess
    ? "text-emerald-600 dark:text-emerald-400"
    : "text-red-600 dark:text-red-400";
  const titleColor = isSuccess
    ? "text-emerald-900 dark:text-emerald-100"
    : "text-red-900 dark:text-red-100";
  const messageColor = isSuccess
    ? "text-emerald-700 dark:text-emerald-200"
    : "text-red-700 dark:text-red-200";
  const buttonColor = isSuccess
    ? "bg-emerald-600 hover:bg-emerald-700 dark:bg-emerald-700 dark:hover:bg-emerald-600"
    : "bg-red-600 hover:bg-red-700 dark:bg-red-700 dark:hover:bg-red-600";

  return (
    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4">
      <div
        className={`
          w-full max-w-[420px]
          border rounded-2xl shadow-lg p-6
          ${bgColor} ${borderColor}
        `}
      >
        {/* Ícone */}
        <div className="flex justify-center mb-4">
          <div
            className={`w-16 h-16 rounded-full flex items-center justify-center ${iconBgColor}`}
          >
            {isSuccess ? (
              <Check className={`w-8 h-8 ${iconColor}`} />
            ) : (
              <AlertCircle className={`w-8 h-8 ${iconColor}`} />
            )}
          </div>
        </div>

        {/* Título */}
        <h2 className={`text-center text-xl font-bold mb-2 ${titleColor}`}>
          {title}
        </h2>

        {/* Mensagem */}
        <p className={`text-center text-sm mb-6 ${messageColor}`}>{message}</p>

        {/* Botão */}
        <button
          onClick={onClose}
          className={`
            w-full py-2.5 px-4 rounded-lg
            text-white font-semibold
            transition-colors duration-200
            ${buttonColor}
          `}
        >
          Entendido
        </button>
      </div>
    </div>
  );
}
