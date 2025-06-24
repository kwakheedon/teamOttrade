import React from 'react'
import { Route } from 'react-router'
import MainPage from '../pages/public/MainPage'
import CommunityPage from '../pages/public/CommunityPage'
import BoardPage from '../pages/public/BoardPage'
import NoticePage from '../pages/public/NoticePage'
import QnAPage from '../pages/public/QnAPage'
import BoardDetailPage from '../pages/public/BoardDetailPage'
import NoticeDetailPage from '../pages/public/NoticeDetailPage'
import QnADetailPage from '../pages/public/QnADetailPage'

//권한 없이 접근이 가능한 페이지 모음
export const PublicRoutes = (
    <>
        <Route path='/' element={<MainPage/>}/>
        <Route path='/community' element={<CommunityPage/>}/>
        <Route path='/board' element={<BoardPage/>}/>
        <Route path='/notice' element={<NoticePage/>}/>
        <Route path='/qna' element={<QnAPage/>}/>
        <Route path="/board/:id" element={<BoardDetailPage/>} />
        <Route path="/notice/:id" element={<NoticeDetailPage/>} />
        <Route path="/qna/:id" element={<QnADetailPage/>} />
    </>
)
