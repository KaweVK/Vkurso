import type { ReactNode } from "react";
import { NavLink } from "react-router-dom";

interface SidebarItem {
    label: string;
    to: string;
    icon: ReactNode;
    active?: boolean;
}

interface SidebarProps {
    title: string;
    description: string;
    items: SidebarItem[];
}

export default function Sidebar({
    title,
    description,
    items,
}: Readonly<SidebarProps>) {
    return (
        <div className="flex flex-col w-[300px] mt-20 h-[620px] rounded-xl shadow-md bg-blue-400/10 p-8">
            <div className="flex flex-col">
                <p className="text-2xl font-semibold">
                    {title}
                </p>

                <p className="text-md mt-4 max-w-[220px]">
                    {description}
                </p>
            </div>

            <div className="flex flex-col mt-20">
                {items.map((item) => (
                    <NavLink
                        key={item.to}
                        end
                        to={item.to}
                        className={({ isActive }) =>
                            `flex mb-5 p-2 rounded-md items-center ${isActive
                                ? "text-blue-900 bg-blue-700/20"
                                : "text-gray-500 hover:text-blue-900 hover:bg-blue-700/20"
                            }`
                        }
                    >

                        {item.icon}

                        <p className="m-2">
                            {item.label}
                        </p>
                    </NavLink>
                ))}
            </div>
        </div>
    );
}