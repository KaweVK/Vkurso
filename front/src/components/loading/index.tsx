export default function Loading() {
    return (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-6 h-6 animate-spin"
            viewBox="0 0 24 24"
            fill="none"
        >
            <circle
                cx="12"
                cy="12"
                r="10"
                color='white'
                stroke="currentColor"
                strokeWidth="3"
                strokeLinecap="round"
                strokeDasharray="60"
                strokeDashoffset="20"
            />
        </svg>
    )
}