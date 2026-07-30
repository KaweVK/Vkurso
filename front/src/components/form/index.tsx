import type { ReactNode } from 'react';
import Navbar from '../navbar';
import Loading from '../loading';

export const inputClassName = 'mt-2 w-full rounded-lg border border-indigo-200 bg-white p-3 text-gray-800 outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200';

type FormShellProps = {
    title: string;
    description: string;
    children: ReactNode;
    onSubmit: (event: React.FormEvent<HTMLFormElement>) => void;
    loading: boolean;
    error: string;
    submitLabel: string;
    onCancel: () => void;
};

export function FormShell({ title, description, children, onSubmit, loading, error, submitLabel, onCancel }: FormShellProps) {
    return (
        <>
            <Navbar />
            <main className="min-h-screen bg-indigo-100 px-6 py-10 dark:bg-indigo-950">
                <form onSubmit={onSubmit} className="mx-auto max-w-2xl rounded-2xl bg-white p-8 shadow-lg">
                    <h1 className="text-3xl font-bold text-indigo-950">{title}</h1>
                    <p className="mt-2 text-gray-500">{description}</p>
                    <div className="mt-8 space-y-5">{children}</div>
                    {error && <p className="mt-5 rounded-lg bg-red-50 p-3 text-sm text-red-700">{error}</p>}
                    <div className="mt-8 flex gap-3">
                        <button type="button" onClick={onCancel} disabled={loading} className="rounded-lg border border-indigo-200 px-5 py-3 font-semibold text-indigo-700 transition hover:bg-indigo-50 disabled:opacity-50">Cancelar</button>
                        <button type="submit" disabled={loading} className="rounded-lg bg-indigo-600 px-5 py-3 font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-50">{loading ? <Loading /> : submitLabel}</button>
                    </div>
                </form>
            </main>
        </>
    );
}

export function FieldLabel({ children, htmlFor }: { children: ReactNode; htmlFor: string }) {
    return <label htmlFor={htmlFor} className="block text-sm font-semibold text-indigo-950">{children}</label>;
}
