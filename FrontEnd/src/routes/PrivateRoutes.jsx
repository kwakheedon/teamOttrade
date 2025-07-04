import React from 'react'
import { Route } from 'react-router-dom'
import PrivateRoute from './PrivateRoute'
import MyPage from '../pages/private/MyPage';
import AccountDeletePage from '../pages/private/AccountDeletePage';
import BoardEditPage from '../pages/private/BoardEditPage';
import BoardWritePage from '../pages/private/BoardWritePage';


export const PrivateRoutes = (
    <>
        <Route path='mypage' element={<MyPage/>}/>
        <Route path='board/edit' element={<BoardEditPage/>}/>
        <Route path='profile/delete' element={<AccountDeletePage/>}/>
        <Route path='board/edit/:id' element={<BoardEditPage/>}/>
        <Route path='board/write' element={<BoardWritePage/>}/>
    </>
)
