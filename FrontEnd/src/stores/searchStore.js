import React from 'react'
import { create } from 'zustand'

const useSearchStore = create((set) => ({
    hsSgn: '',
    korePrnm: '',
    setSearchItem: (hsSgn, korePrnm) => 
        set({ hsSgn, korePrnm })
}))

export default useSearchStore