import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Header from '../components/Header/Header'
import { PublicRoutes } from './PublicRoutes'
import { PrivateRoutes } from './PrivateRoutes'
import PrivateRoute from './PrivateRoute'
import NotFoundPage from '../pages/public/NotFoundPage'

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Header/>
      <Routes>
        { PublicRoutes }
        <Route element={<PrivateRoute/>}>
          { PrivateRoutes }
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default AppRouter