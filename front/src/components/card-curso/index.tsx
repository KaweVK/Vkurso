import '../../index.css'
import type { Course, CourseLevel } from "../../types"
import Image from '../../assets/image.png'

type Props = {
  course: Course;
};

const BEGGINER_COLOR =
    "rounded-xl border border-green-900 px-2 py-1 text-xs font-semibold text-green-900 bg-green-200/50";

const INTERMEDIATE_COLOR =
    "rounded-xl border border-yellow-500 px-2 py-1 text-xs font-semibold text-yellow-500 bg-yellow-200/50";

const ADVANCED_COLOR =
    "rounded-xl border border-red-700 px-2 py-1 text-xs font-semibold text-red-700 bg-red-200/50";

export default function CardCurso({ course }: Props) {

    const colorLevel = (level:CourseLevel) => {
        if (level == "BEGINNER") {
            return BEGGINER_COLOR
        } else if (level == "INTERMEDIATE") {
            return INTERMEDIATE_COLOR
        } else {
            return ADVANCED_COLOR
        }
    };

    return (
    <div className="flex h-[320px] w-[240px] flex-col overflow-hidden rounded-xl bg-white font-mono shadow-md transition hover:-translate-y-1 hover:shadow-lg">

        <div className="relative h-[140px] w-full shrink-0 overflow-hidden">
            <img
                src={Image}
                alt="Thumbnail do curso"
                className="h-full w-full object-cover"
            />

            {course.status === 'DRAFT' && (
                <span className="absolute left-2 top-2 rounded bg-amber-100 px-2 py-1 text-xs font-semibold text-amber-800">
                    Rascunho
                </span>
            )}
        </div>

        <div className="flex flex-1 flex-col p-3">

            <h2 className="line-clamp-2 text-lg font-bold text-blue-950">
                {course.title}
            </h2>

            <p className="mt-1 line-clamp-3 text-sm leading-5 text-gray-600">
                {course.description}
            </p>

            <div className="mt-auto flex items-end justify-between">

                <p className={colorLevel(course.level)}>
                    {course.level}
                </p>

                <p className="text-lg font-semibold text-blue-900">
                    R$ {course.price}
                </p>

            </div>
        </div>
    </div>
)
}
