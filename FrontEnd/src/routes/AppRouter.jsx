import React from 'react'
import { BrowserRouter, Routes } from 'react-router'
import Header from '../components/Header/Header'
import { PublicRoutes } from './PublicRoutes'
import { PrivateRoutes } from './PrivateRoutes'
import AdminRoutes from './AdminRoutes'

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Header/>
      <Routes>
        { PublicRoutes }
        { PrivateRoutes }
        {/* {AdminRoutes} */}
      </Routes>
    </BrowserRouter>
  )
}

export default AppRouter