export default function Dashboard() {
  return (
    <div className="py-20">
      <div className="px-10">
        <h1 className="text-2xl font-bold">
          Dashboard
        </h1>

        <p className="mt-2 text-gray-600">
          Visão geral do sistema.
        </p>

        <div className="mt-6 grid grid-cols-1 gap-4 md:grid-cols-3">
          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Usuários
            </p>

            <p className="mt-2 text-3xl font-bold">
              125
            </p>
          </div>

          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Cursos
            </p>

            <p className="mt-2 text-3xl font-bold">
              32
            </p>
          </div>

          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Matrículas
            </p>

            <p className="mt-2 text-3xl font-bold">
              487
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}