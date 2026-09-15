import type { Role } from "../types/user";

function courseLevelFormarter(role: Role | undefined) {
    if (role === "USER") {
        return "Aluno"
    } else if (role === "INSTRUCTOR") {
        return "Instrutor"
    } else {
        return "ADMIN"
    }

}

export default courseLevelFormarter;