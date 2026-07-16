import '../../index.css'
import type { Course, CourseLevel } from "../../types"
import Image from '../../assets/image.png'

type Props = {
  course: Course;
};

const BEGGINER_COLOR = "absolute bottom-2 right-2 rounded-xl border-[1px] border-green-900 p-1 mb-[5px] text-sm text-green-900";
const INTERMEDIATE_COLOR = "absolute bottom-2 right-2 rounded-xl border-[1px] border-yellow-500 p-1 mb-[5px] text-sm text-yellow-500";
const ADVANCED_COLOR = "absolute bottom-2 right-2 rounded-xl border-[1px] border-red-700 p-1 mb-[5px] text-sm text-red-700";

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
        <div className="flex bg-white h-[230px] w-[180px] justify-center font-mono grid grid-rows-2 rounded-xl dark:bg-sky-100">
            <div className="relative row-span-1">
                <img src={Image} alt="Thumbnail do curso" className='rounded-t-xl'/>
            </div>
            <div className="relative row-span-1 grid grid-rows-3">
                <p className='m-[5px]'>{course.title}</p>
                <p className='m-[5px]'>{course.description}</p>
                <p className={colorLevel(course.level)}>{course.level}</p>
                <p className='text-xl font-semibold text-blue-900 m-[5px]'>R${course.price}</p>
            </div>
        </div>
    )
}