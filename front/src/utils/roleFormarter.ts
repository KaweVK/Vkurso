import type { Role } from "../types/user";

function roleFormarter(role: Role | undefined) {
    if (role === "USER") {
        return "Aluno"
    } else if (role === "INSTRUCTOR") {
        return "Instrutor"
    } else {
        return "Administrador"
    }

}

export default roleFormarter;