import './App.css'
import Header from '../src/Header/Header'
import Home from '../src/Home/Home'
import Checkout from '../src/Checkout/Checkout'
import Login from '../src/Login/Login'
import Register from '../src/Register/Register'
import Payment from '../src/Payment/Payment'
import Bookshelf from '../src/Bookshelf/Bookshelf'
import AddToBookshelf from '../src/AddToBookshelf/AddToBookshelf'
import UpdateInBookshelf from '../src/UpdateInBookshelf/UpdateInBookshelf'
import Account from '../src/Account/Account'
import Offers from '../src/Offers/Offers'
import AdvertisedBooks from '../src/AdvertisedBook/AdvertisedBook'
import TradedBooks from '../src/TradedBooks/TradedBooks'
import SoldBooks from '../src/SoldBooks/SoldBooks'
import BookDetails from '../src/BookDetails/BookDetails'
import UserDetails from '../src/UserDetails/UserDetails'
import Trade from '../src/Trade/Trade'
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