import React from 'react'
import { Route } from 'react-router'
import PrivateRoute from './PrivateRoute'
import MyPage from '../pages/private/MyPage';
import AccountDeletePage from '../pages/private/AccountDeletePage';
import BoardEditPage from '../pages/private/BoardEditPage';
import BoardWritePage from '../pages/private/BoardWritePage';


export const PrivateRoutes = (
    <>
        <Route path='/mypage' element={<PrivateRoute><MyPage/></PrivateRoute>}/>
        <Route path='/profile/edit' element={<PrivateRoute><BoardEditPage/></PrivateRoute>}/>
        <Route path='/profile/delete' element={<PrivateRoute><AccountDeletePage/></PrivateRoute>}/>
        <Route path='/board/edit/:id' element={<PrivateRoute><BoardEditPage/></PrivateRoute>}/>
        <Route path='/board/write' element={<PrivateRoute><BoardWritePage/></PrivateRoute>}/>
    </>
)
