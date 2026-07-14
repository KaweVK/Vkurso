import '../../index.css'
import type { Course } from "../../types"
import Image from '../../assets/image.png'

type Props = {
  course: Course;
};

export default function CardCurso({ course }: Props) {
    return (
        <div className="flex bg-sky-100 h-[230px] w-[180px] justify-center font-mono grid grid-rows-2">
            <div className="flex row-span-1 self-center">
                <img src={Image} alt="Thumbnail do curso"/>
            </div>
            <div className="flex row-span-1 grid grid-rows-3">
                <p className=''>{course.title}</p>
                <p className='rounded-xl border-[1px] place-self-center gap-4'>{course.level}</p>
                <p className=''>{course.description}</p>
                <p className=''>R${course.price}</p>
            </div>
        </div>
    )
}