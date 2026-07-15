import '../../index.css'
import type { Course } from "../../types"
import Image from '../../assets/image.png'

type Props = {
  course: Course;
};

export default function CardCurso({ course }: Props) {
    return (
        <div className="flex bg-sky-100 h-[230px] w-[180px] justify-center font-mono grid grid-rows-2 rounded-xl">
            <div className="relative row-span-1">
                <img src={Image} alt="Thumbnail do curso" className='rounded-t-xl'/>
            </div>
            <div className="relative row-span-1 grid grid-rows-3">
                <p className='m-[5px]'>{course.title}</p>
                <p className='m-[5px]'>{course.description}</p>
                <p className='absolute bottom-2 right-2 rounded-xl border-[1px] p-1 mb-[5px] text-white mix-blend-difference text-sm'>{course.level}</p>
                <p className='m-[5px]'>R${course.price}</p>
            </div>
        </div>
    )
}