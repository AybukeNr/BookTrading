import React, { useEffect, useState } from 'react'
import '../TradedBooks/TradedBooks.css'

function TradedBooks() {
  const [tradedBooks, setTradedBooks] = useState([]);

  useEffect(() => {
    const fetchTradedBooks = async () => {
      try {
        const userId = localStorage.getItem("userId");
        const response = await instanceTransaction.get(`/transactions/getTransactionInfos?userId=${userId}`);
        setTradedBooks(response.data);
      } catch (error) {
        console.error("Takas verileri alınırken hata oluştu:", error);
      }
    };

    fetchTradedBooks();
  }, []);

  return (
    <div className='trades'>
      <h3>Takaslarım</h3>
      {tradedBooks.map((tradedBook, index) => (
        <>
          <div className="bookInfos" key={index}>
            <div className='bookSent'>
              <h4>Gönderilen Kitap:</h4>
              <img src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' alt='' />
              <p><strong>Başlık: </strong></p>
              <p><strong>Yazar: </strong> </p>
              <p><strong>Yayınevi: </strong></p>
              <p><strong>Yayın Tarihi: </strong>  </p>
              <p><strong>ISBN: </strong> </p>
              <p><strong>Kategori: </strong> </p>
              <p><strong>Açıklama: </strong></p>
              <p><strong>Durum: </strong></p>
            </div>
            <div className='bookReceive'>
              <h4>Alınan Kitap:</h4>
              <img src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' alt='' />
              <p><strong>Başlık: </strong></p>
              <p><strong>Yazar: </strong> </p>
              <p><strong>Yayınevi: </strong></p>
              <p><strong>Yayın Tarihi: </strong>  </p>
              <p><strong>ISBN: </strong> </p>
              <p><strong>Kategori: </strong></p>
              <p><strong>Açıklama: </strong></p>
              <p><strong>Durum: </strong></p>
            </div>
          </div>
          <div className="tradersInfos">
            <div className='sender'>
              <h4>Gönderen:</h4>
              <p>Ad Soyad: </p>
              <p>Email: </p>
              <p>Adres: </p>
            </div>
            <div className='receiver'>
              <h4>Alıcı:</h4>
              <p>Ad Soyad: </p>
              <p>Email: </p>
              <p>Adres: </p>
            </div>
          </div>
        </>
      ))}
    </div>
  )
}

export default TradedBooks