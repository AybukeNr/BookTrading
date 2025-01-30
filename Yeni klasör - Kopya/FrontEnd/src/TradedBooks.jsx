import React from 'react'
import './TradedBooks.css'

function TradedBooks() {
  return (
    <div className='trades'>
      <h3>Takaslarım</h3>
      <div className="bookInfos">
        <div className='bookSent'>
          <h4>Gönderilen Kitap:</h4>
          <img src='#' alt='' />
          <p>Başlık: </p>
          <p>Yazar: </p>
          <p>Yayınevi: </p>
          <p>Yayın Tarihi: </p>
          <p>ISBN: </p>
          <p>Kategori: </p>
        </div>
        <div className='bookReceive'>
          <h4>Alınan Kitap:</h4>
          <img src='#' alt='' />
          <p>Başlık: </p>
          <p>Yazar: </p>
          <p>Yayınevi: </p>
          <p>Yayın Tarihi: </p>
          <p>ISBN: </p>
          <p>Kategori: </p>
        </div>
      </div>
      <div className="userInfos">
        <div className='sender'>
        <h4>Gönderen:</h4>
        <p>Ad: </p>
        <p>Soyad: </p>
        <p>Telefon: </p>
        <p>Adres: </p>
        </div>
        <div className='receiver'>
        <h4>Alıcı:</h4>
        <p>Ad: </p>
        <p>Soyad: </p>
        <p>Telefon: </p>
        <p>Adres: </p>
        </div>
      </div>
    </div>
  )
}

export default TradedBooks