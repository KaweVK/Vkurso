import { useEffect, useState } from "react";
import metricsService from "../../../services/metricsService";
import type { Metrics } from "../../../types/metrics";

export default function Dashboard() {
  const [metrics, setMetrics] = useState<Metrics>()

  useEffect(() => {
    const loadMetrics = async () => {
      try {
        const data = await metricsService.getMetrics();
        setMetrics(data)
      } catch (error) {
        console.error('Erro ao carregar progresso:', error);
      }
    }

    loadMetrics();
  }, [])

  return (
    <div className="lg:px-10 py-20">
        <h1 className="text-2xl font-bold">
          Dashboard
        </h1>

        <p className="mt-2 text-gray-600">
          Visão geral do sistema.
        </p>

        <div className="mt-6 grid grid-cols-2 gap-4 md:grid-cols-3">
          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Usuários
            </p>

            <p className="mt-2 text-3xl font-bold">
              {metrics?.totalUsers}
            </p>
          </div>

          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Cursos publicados
            </p>

            <p className="mt-2 text-3xl font-bold">
              {metrics?.totalPublishedCourses}
            </p>
          </div>

          <div className="rounded-lg border bg-white p-5">
            <p className="text-sm text-gray-500">
              Matrículas ativas
            </p>

            <p className="mt-2 text-3xl font-bold">
              {metrics?.totalEnrollments}
            </p>
          </div>
      </div>
    </div>
  );
}