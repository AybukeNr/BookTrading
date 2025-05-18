import React, { useEffect, useState } from 'react'
import '../TradedBooks/TradedBooks.css'
import { instanceTransaction } from '../axios';

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
              <img src={tradedBook.listBook.image} alt={tradedBook.listBook.title}/>
              <p><strong>Başlık: </strong>{tradedBook.listBook.title}</p>
              <p><strong>Yazar: </strong>{tradedBook.listBook.author} </p>
              <p><strong>Yayınevi: </strong>{tradedBook.listBook.publisher}</p>
              <p><strong>Yayın Tarihi: </strong>{tradedBook.listBook.publishedDate}</p>
              <p><strong>ISBN: </strong>{tradedBook.listBook.isbn}</p>
              <p><strong>Kategori: </strong>{tradedBook.listBook.category}</p>
              <p><strong>Açıklama: </strong>{tradedBook.listBook.description}</p>
              <p><strong>Durum: </strong>{tradedBook.listBook.condition}</p>
            </div>
            <div className='bookReceive'>
              <h4>Alınan Kitap:</h4>
              <img src={tradedBook.offererBook.image} alt={tradedBook.offerBook.title}/>
              <p><strong>Başlık: </strong>{tradedBook.offerBook.title}</p>
              <p><strong>Yazar: </strong>{tradedBook.offerBook.author}</p>
              <p><strong>Yayınevi: </strong>{tradedBook.offerBook.publisher}</p>
              <p><strong>Yayın Tarihi: </strong>{tradedBook.offerBook.publisedDate}</p>
              <p><strong>ISBN: </strong>{tradedBook.offerBook.isbn}</p>
              <p><strong>Kategori: </strong>{tradedBook.offerBook.category}</p>
              <p><strong>Açıklama: </strong>{tradedBook.offerBook.description}</p>
              <p><strong>Durum: </strong>{tradedBook.offerBook.condition}</p>
            </div>
          </div>
          <div className="tradersInfos">
            <div className='sender'>
              <h4>Gönderen:</h4>
              <p><strong>Ad Soyad: </strong>{tradedBook.owner.fullName}</p>
              <p><strong>Email: </strong>{tradedBook.owner.email}</p>
              <p><strong>Adres: </strong>{tradedBook.owner.address}</p>
            </div>
            <div className='receiver'>
              <h4>Alıcı:</h4>
              <p><strong>Ad Soyad: </strong>{tradedBook.offerer.fullName}</p>
              <p><strong>Email: </strong>{tradedBook.offerer.email}</p>
              <p><strong>Adres: </strong>{tradedBook.offerer.address}</p>
            </div>
          </div>
        </>
      ))}
    </div>
  )
}

export default TradedBooks