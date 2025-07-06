import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Undo2 } from 'lucide-react'

const UndoButton = () => {
    const navigate = useNavigate()

    return (
        <div className="back-btn" onClick={() => navigate(-1)}>
            <Undo2 color='#1C1E1F' size={25}/>
        </div>
    )
}

export default UndoButton