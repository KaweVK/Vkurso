import type { CourseLevel } from "../types";

function courseLevelFormarter(level: CourseLevel | undefined) {
    if (level === "BEGINNER") {
        return "Iniciante"
    } else if (level === "INTERMEDIATE") {
        return "Intermediário"
    } else {
        return "Avançado"
    }

}

export default courseLevelFormarter;