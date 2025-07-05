import React from 'react'
import { Route } from 'react-router-dom'
import PrivateRoute from './PrivateRoute'
import MyPage from '../pages/private/MyPage';
import AccountDeletePage from '../pages/private/AccountDeletePage';
import BoardEditPage from '../pages/private/BoardEditPage';
import BoardWritePage from '../pages/private/BoardWritePage';
import MyHistoryPage from '../pages/private/MyHistoryPage';
import ProfileEditPage from '../pages/private/ProfileEditPage';


export const PrivateRoutes = (
    <>
        <Route path='mypage' element={<MyPage/>}>
            <Route index element={<MyHistoryPage/>}/>
            <Route path='history' element={<MyHistoryPage/>}/>
            <Route path='edit' element={<ProfileEditPage/>}/>
            {/* <Route path='delete' element={<AccountDeletePage/>}/> */}
        </Route>
        <Route path='board/edit/:id' element={<BoardEditPage/>}/>
        <Route path='board/write' element={<BoardWritePage/>}/>
    </>
)
