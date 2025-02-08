import './App.css'
import Header from './Header'
import Home from './Home'
import Checkout from './Checkout'
import Login from './Login'
import Register from './Register'
import Payment from './Payment'
import Bookshelf from './Bookshelf'
import AddToBookshelf from './AddToBookshelf'
import UpdateInBookshelf from './UpdateInBookshelf'
import Account from './Account'
import Offers from './Offers'
import AdvertisedBooks from './AdvertisedBook'
import TradedBooks from './TradedBooks'
import SoldBooks from './SoldBooks'
import BookDetails from './BookDetails'
import UserDetails from './UserDetails'
import Trade from './Trade'
import { Routes, Route } from 'react-router-dom'
// import { useEffect } from 'react'
import { useStateValue } from './StateProvider'

function App() {
  const [{ }, dispatch] = useStateValue();

  // useEffect(() => {
  //   const token = localStorage.getItem('authToken');

  //   if (token) {
  //     dispatch({
  //       type: 'SET_USER',
  //       user: { token }
  //     })

  //   } else {
  //     dispatch({
  //       type: 'SET_USER',
  //       user: null
  //     })
  //   }

  // }, [])

  
  return (
    <div>
      <Routes>
        <Route path='/' element={
          <>
            <Header />
            <Home />
          </>
        } />
        <Route path='/login' element={
          <Login />
        } />
        <Route path='/register' element={
          <Register />
        } />
        <Route path='/bookshelf' element={
          <>
            <Header />
            <Bookshelf />
          </>
        } />
        <Route path='/addBook' element={
          <>
            <Header />
            <AddToBookshelf />
          </>
        } />
        <Route path='/updateBook' element={
          <>
            <Header />
            <UpdateInBookshelf />
          </>
        } />
        <Route path='/myAccount' element={
          <>
            <Header />
            <Account />
          </>
        } />
        <Route path='/myAds' element={
          <>
            <Header />
            <AdvertisedBooks />
          </>
        } />
        <Route path='/myOffers' element={
          <>
            <Header />
            <Offers />
          </>
        } />
        <Route path='/myTrades' element={
          <>
            <Header />
            <TradedBooks />
          </>
        } />
        <Route path='/mySales' element={
          <>
            <Header />
            <SoldBooks />
          </>
        } />
        <Route path='/trade' element={
          <>
            <Header />
            <Trade />
          </>
        } />
        <Route path='/bookDetails' element={
          <>
            <Header />
            <BookDetails />
          </>
        } />
        <Route path='/userDetails' element={
          <>
            <Header />
            <UserDetails />
          </>
        } />
        <Route path='/checkout' element={
          <>
            <Header />
            <Checkout />
          </>
        } />
        <Route path='/payment' element={
          <>
            <Header />
            <Payment />
          </>
        } />
      </Routes>
    </div>
  )
}

export default App