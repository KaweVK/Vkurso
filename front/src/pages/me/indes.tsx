import { EnvelopeIcon, IdentificationIcon, PencilIcon, UserIcon } from "@heroicons/react/24/outline";
import { useAuth } from "../../hooks/useAuth";
import UserPhoto from '../../../public/user.webp';
import roleFormarter from "../../utils/roleFormarter";
import { Link } from "react-router-dom";

function Me() {
    const { user } = useAuth();
    const isUserOwnerOrADMIN = (user?.id) || Boolean(user?.role === 'ADMIN');

    return (
        <main>
            <div className="flex">

                <div className="flex flex-col lg:flex-row m-10 gap-10 w-full">
                    {/* Paienl azul */}
                    <div className="flex rounded-lg bg-blue-900 lg:w-1/2 text-white items-center p-2">
                        <div>
                            <img src={user?.photoUrl ? user?.photoUrl : UserPhoto} alt={`Foto de ${user?.fullName.split(" ").at(0)}`} className='rounded-full size-20 lg:size-36 m-4 lg:m-8 border border-white border-4' />
                        </div>

                        <div className="flex flex-col gap-1">
                            <p className="text-lg md:text-3xl font-semibold">{user?.fullName}</p>
                            <p className="text-md">{user?.email}</p>
                            <div className="flex rounded-full bg-sky-600 justify-center py-2 mt-2">
                                <p>{roleFormarter(user?.role)}</p>
                            </div>
                        </div>
                    </div>

                    {/* Painel de infos. pessoais */}
                    <div className="flex flex-col rounded-lg border border-red p-3 lg:w-1/2">
                        <div className="flex justify-between p-3">
                            <div className="flex items-center">
                                <UserIcon className="size-10 p-1 rounded-md bg-blue-500/30 text-blue-600" />
                                <p className="ml-4 text-lg font-semibold">Informações pessoais</p>
                            </div>
                            {isUserOwnerOrADMIN && (
                                <div
                                    className="flex shrink-0 items-center gap-1"
                                    onClick={(event) => event.stopPropagation()}
                                >
                                    <Link
                                        to={`/me/edit`}
                                        state={{ userId: Number (user?.id)}}                                        
                                        className="rounded-md p-2 text-indigo-600 hover:bg-indigo-50"
                                    >
                                        <PencilIcon className="h-5 w-5" />
                                    </Link>
                                </div>)}
                        </div>

                        <div className="flex lg:justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <UserIcon className="size-10 p-2" />
                                <p className="ml-4 hidden lg:flex">Nome completo</p>
                            </div>
                            <div className="flex flex-col lg:flex-row ml-4 items-start">
                                <p className="lg:hidden">Nome completo</p>
                                <p className="font-semibold">{user?.fullName}</p>
                            </div>
                        </div>

                        <div className="flex lg:justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <EnvelopeIcon className="size-10 p-2" />
                                <p className="ml-4 hidden lg:flex">Email</p>
                            </div>
                            <div className="flex flex-col lg:flex-row ml-4 items-start">
                                <p className="lg:hidden">Email</p>
                                <p className="font-semibold">{user?.email}</p>
                            </div>
                        </div>

                        <div className="flex lg:justify-between border-red border-b px-3 py-2">
                            <div className="flex items-center text-gray-500">
                                <IdentificationIcon className="size-10 p-2" />
                                <p className="ml-4 hidden lg:flex">Cargo</p>
                            </div>
                            <div className="flex flex-col lg:flex-row ml-4 items-start">
                                <p className="lg:hidden">Cargo</p>
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