import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router'
import Header from '../components/Header/Header'
import { PublicRoutes } from './PublicRoutes'
import { PrivateRoutes } from './PrivateRoutes'
import AdminRoutes from './AdminRoutes'
import PrivateRoute from './PrivateRoute'

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Header/>
      <Routes>
        { PublicRoutes }
        <Route element={<PrivateRoute/>}>
          { PrivateRoutes }
        </Route>
        {/* {AdminRoutes} */}
      </Routes>
    </BrowserRouter>
  )
}

export default AppRouter