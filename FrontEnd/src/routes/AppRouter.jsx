import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router'
import Header from '../components/Header/Header'
import MainPage from '../page/public/MainPage'

const AppRouter = () => {
  return (
    <BrowserRouter>
        <Header/>
        <Routes>
            <Route path='/' element={<MainPage/>}/>
        </Routes>
    </BrowserRouter>
  )
}

export default AppRouter