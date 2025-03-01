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
import Admin from '../src/Admin/Admin'
import { Routes, Route } from 'react-router-dom'
import { useStateValue } from './StateProvider'
import { useEffect } from 'react'
import { useState } from 'react'
import { AuthTokenControl } from './auth'
import { getAuthToken } from './auth'

function App() {
  const [{ }, dispatch] = useStateValue();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = getAuthToken();

    if (token) {
      dispatch({
        type: 'SET_USER',
        user: { token }
      });
      setIsAuthenticated(true);
    } else {
      dispatch({
        type: 'SET_USER',
        user: null
      });
      setIsAuthenticated(false);
    }

  }, [])


  return (
    <div>
      <Routes>
        {(isAuthenticated || AuthTokenControl()) && (
          <>
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
          </>
        )}
        <Route path='/' element={
          <>
            <Header />
            <Home />
          </>
        } />
        <Route path='/login' element={
          <Login setIsAuthenticated={setIsAuthenticated} />
        } />
        <Route path='/register' element={
          <Register />
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
        <Route path='/admin' element={
          <>
            <Admin />
          </>
        } />
      </Routes>
    </div>
  )
}

export default App