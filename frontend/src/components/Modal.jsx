export default function Modal({ title, children, onClose }) {
  return (
    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4">
      <div
        className="
          bg-white dark:bg-slate-900
          border border-slate-200 dark:border-slate-800
          text-slate-900 dark:text-slate-100
          w-full max-w-[500px] max-h-[90vh] overflow-y-auto
          rounded-2xl shadow-lg p-5 sm:p-6
        "
      >
        <div className="flex justify-between mb-4">
          <h2 className="text-lg font-semibold text-slate-900 dark:text-white">
            {title}
          </h2>

          <button
            onClick={onClose}
            className="
              text-slate-500 dark:text-slate-400
              hover:text-slate-900 dark:hover:text-white
              transition
            "
          >
            ✕
          </button>
        </div>

        {children}
      </div>
    </div>
  );
}
