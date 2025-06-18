import { Route, Routes } from 'react-router'
import './App.css'
import Main from './page/Main'
import Header from './components/Header'

function App() {

  return (
    <>
      <Header/>
      <Routes>
        <Route path='/' element={<Main/>}/>
      </Routes>
    </>
  )
}

export default App
