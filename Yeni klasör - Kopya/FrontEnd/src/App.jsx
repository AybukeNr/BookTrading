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
import Search from '../src/Search/Search'
import Tracking from './Payment/Tracking'
import TradeTracking from './Trade/TradeTracking'
import { Routes, Route, useNavigate } from 'react-router-dom'
import { useStateValue } from './StateProvider'
import { useEffect } from 'react'
import { useState } from 'react'
import { getAuthToken } from './auth'
import { jwtDecode } from 'jwt-decode'
import PrivateRoute from './PrivateRoute'

function App() {
  const [, dispatch] = useStateValue();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getAuthToken();
    if (token) {
      const decodedToken = jwtDecode(token);
      dispatch({
        type: 'SET_USER',
        user: decodedToken
      });
      setIsAuthenticated(true);
      // const currentTime = Date.now() / 1000;
      // if (decodedToken.exp < currentTime) {
      //   removeAuthToken();
      //   setIsAuthenticated(false);
      //   return;
      // }
    } else {
      dispatch({
        type: 'SET_USER',
        user: null
      });
      setIsAuthenticated(false);
    }
    setLoading(false);
  }, [])


  return (
    <div>
      <Header setIsAuthenticated={setIsAuthenticated} />
      <Routes>
        <Route path='/bookshelf' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Bookshelf />
          </PrivateRoute>
        } />
        <Route path='/addBook' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <AddToBookshelf />
          </PrivateRoute>
        } />
        <Route path='/updateBook' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <UpdateInBookshelf />
          </PrivateRoute>
        } />
        <Route path='/myAccount' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Account />
          </PrivateRoute>
        } />
        <Route path='/myAds' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <AdvertisedBooks />
          </PrivateRoute>
        } />
        <Route path='/myOffers' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Offers />
          </PrivateRoute>
        } />
        <Route path='/myTrades' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <TradedBooks />
          </PrivateRoute>
        } />
        <Route path='/mySales' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <SoldBooks />
          </PrivateRoute>
        } />
        <Route path='/trade' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Trade />
          </PrivateRoute>
        } />
        <Route path='/checkout' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Checkout />
          </PrivateRoute>
        } />
        <Route path='/payment' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Payment />
          </PrivateRoute>
        } />
        <Route path='/paymentTracking' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Tracking />
          </PrivateRoute>
        } />
        <Route path='/tradeTracking' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <TradeTracking />
          </PrivateRoute>
        } />
        <Route path='/' element={
          <>
            <Home />
          </>
        } />
        <Route path='/login' element={
          <Login setIsAuthenticated={setIsAuthenticated} />
        } />
        <Route path='/register' element={
          <Register />
        } />

        <Route path='/search' element={
          <>
            <Search />
          </>
        } />

        <Route path='/bookDetails' element={
          <>
            <BookDetails />
          </>
        } />
        <Route path='/userDetails' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <UserDetails />
          </PrivateRoute>
        } />
        <Route path='/admin' element={
          <PrivateRoute isAuthenticated={isAuthenticated} loading={loading}>
            <Admin />
          </PrivateRoute>
        } />
      </Routes>
    </div>
  )
}

export default App