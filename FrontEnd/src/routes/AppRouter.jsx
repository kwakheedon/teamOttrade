import React from 'react'
import { BrowserRouter, Routes } from 'react-router'
import Header from '../components/Header/Header'
import MainPage from '../page/public/MainPage'
import NoticePage from '../page/public/NoticePage'
import QnAPage from '../page/public/QnAPage'
import CommunityPage from '../page/public/CommunityPage'

const AppRouter = () => {
  return (
    <BrowserRouter>
        <Header/>
        <Routes>
            <Route path='/' element={<MainPage/>}/>
            <Route path='/NoticePage' element={<NoticePage/>}/>
            <Route path='/QnAPage' element={<QnAPage/>}/>
            <Route path='/CommunityPage' element={<CommunityPage/>}/>
        </Routes>
    </BrowserRouter>
  )
}

export default AppRouter