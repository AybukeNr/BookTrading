import React from 'react'
import './SoldBooks.css'

function SoldBooks() {
  return (
    <div className='soldBooks'>
      <h3>Satışlarım</h3>
      <div className='book_Info'>
        <h4>Satılan Kitap:</h4>
        <img src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' alt='' />
        <div className='book_Detail'>
          <p>Başlık: </p>
          <p>Yazar: </p>
          <p>Yayınevi: </p>
          <p>Yayın Tarihi: </p>
          <p>ISBN: </p>
          <p>Kategori: </p>
          <p>Fiyatı: </p>
        </div>
      </div>
      <div className="personInfo">
        <div className="sellerInfo">
          <h4>Satıcı:</h4>
          <p>Ad Soyad: </p>
          <p>Telefon: </p>
          <p>Adres: </p>
        </div>
        <div className="buyerInfo">
          <h4>Alıcı:</h4>
          <p>Ad Soyad: </p>
          <p>Telefon: </p>
          <p>Adres: </p>
        </div>
      </div>
    </div>
  )
}

export default SoldBooks