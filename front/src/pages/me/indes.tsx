import { EnvelopeIcon, IdentificationIcon, UserIcon } from "@heroicons/react/24/outline";
import { useAuth } from "../../hooks/useAuth";
import Thumb from '../../assets/image.png';
import roleFormarter from "../../utils/roleFormarter";

function Me() {
    const { user } = useAuth();

    return (
        <main>
            <div className="flex items-stretch min-h-screen">

                <div className="flex m-10 gap-10 w-full">
                    {/* Paienl azul */}
                    <div className="flex rounded-lg bg-blue-900 w-1/2 text-white items-center p-2">
                        <div>
                            <img src={Thumb} alt={`Foto de ${user?.fullName.split(" ").at(0)}`} className='rounded-full size-36 m-8 border border-white border-4' />
                        </div>

                        <div className="flex flex-col gap-1">
                            <p className="text-3xl font-semibold">{user?.fullName}</p>
                            <p>{user?.email}</p>
                            <p>Aprendendo hoje, construindo o amanhã!</p>
                            <div className="flex rounded-full bg-sky-600 max-w-20 justify-center py-2 mt-2">
                                <p>{roleFormarter(user?.role)}</p>
                            </div>
                        </div>
                    </div>

                    {/* Painel de infos. pessoais */}
                    <div className="flex flex-col rounded-lg border border-red p-3 w-1/2">
                        <div className="flex justify-between p-3">
                            <div className="flex items-center">
                                <UserIcon className="size-10 p-1 rounded-md bg-blue-500/30 text-blue-600" />
                                <p className="ml-4 text-lg font-semibold">Informações pessoais</p>
                            </div>
                        </div>

                        <div className="flex justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <UserIcon className="size-10 p-2" />
                                <p className="ml-4">Nome completo</p>
                            </div>
                            <div className="flex justify-start items-center">
                                <p className="font-semibold">{user?.fullName}</p>
                            </div>
                        </div>

                        <div className="flex justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <EnvelopeIcon className="size-10 p-2" />
                                <p className="ml-4">Email</p>
                            </div>
                            <div className="flex justify-start items-center">
                                <p className="font-semibold">{user?.email}</p>
                            </div>
                        </div>

                        <div className="flex justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <IdentificationIcon className="size-10 p-2" />
                                <p className="ml-4">Cargo</p>
                            </div>
                            <div className="flex justify-start items-center">
                                <p className="font-semibold">{roleFormarter(user?.role)}</p>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </main>
    )
}

export default Me;