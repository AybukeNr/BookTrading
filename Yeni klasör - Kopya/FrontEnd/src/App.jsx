import './App.css'
import Header from './Header'
import Home from './Home'
import Checkout from './Checkout'
import { Routes, Route } from 'react-router-dom'

function App() {
  return (
      <div>
        <Routes>
          <Route path='/' element={
            <>
              <Header />
              <Home />
            </>
          } />
          <Route path='/checkout' element={
            <>
              <Header />
              <Checkout />
            </>
          } />
        </Routes>
      </div>
  )
}

export default App