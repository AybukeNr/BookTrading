import React, { useEffect, useState } from 'react'
import '../TradedBooks/TradedBooks.css'
import { instanceListing, instanceOffer, instanceTransaction, instanceUser } from '../axios';

function TradedBooks() {
  const [tradedBooks, setTradedBooks] = useState([]);

  // useEffect(() => {
  //   const fetchTradedBooks = async () => {
  //     try {
  //       const userId = localStorage.getItem("userId");
  //       const response = await instanceTransaction.get(`/transactions/getUsersExchanges?ownerId=${userId}`);

  //       const transactions = response.data;

  //       const detailedExchanges = await Promise.all(transactions.map(async (trx) => {
  //         const listRes = await instanceListing.get(`/getListsById?listId=${trx.listId}`);
  //         const offererRes = await instanceUser.get(`/getUserById?id=${trx.offererId}`);

  //         return {
  //           book: listRes.data.book,
  //           sender: listRes.data.user,
  //           receiver: offererRes.data,
  //         };

  //       }));

  //       setTradedBooks(detailedExchanges);
  //     } catch (error) {
  //       console.error("Takas verileri alınırken hata oluştu:", error);
  //     }
  //   };

  //   fetchTradedBooks();
  // }, []);


  useEffect(() => {
    const fetchTradedBooks = async () => {
      try {
        const userId = localStorage.getItem("userId");
        const response = await instanceOffer.get(`/getAllAcceptedOffers?userId=${userId}`);
        const offers = response.data;

        const detailedData = await Promise.all(offers.map(async (offer) => {
          const senderRes = await instanceUser.get(`/getUserById?id=${offer.offererId}`);
          const receiverRes = await instanceUser.get(`/getUserById?id=${offer.offerList.ownerId}`);

          return {
            sentBook: offer.offeredBook,
            receivedBook: offer.offerList.book,
            sender: senderRes.data,
            receiver: receiverRes.data
          };
        }));

        setTradedBooks(detailedData);
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
        <div key={index}>
          <div className="bookInfos">
            <div className='bookSent'>
              <h4>Gönderilen Kitap:</h4>
              <img src={tradedBook.sentBook.image} alt={tradedBook.sentBook.title} />
              <p><strong>Başlık: </strong>{tradedBook.sentBook.title}</p>
              <p><strong>Yazar: </strong>{tradedBook.sentBook.author} </p>
              <p><strong>Yayınevi: </strong>{tradedBook.sentBook.publisher}</p>
              <p><strong>Yayın Tarihi: </strong>{tradedBook.sentBook.publishedDate}</p>
              <p><strong>ISBN: </strong>{tradedBook.sentBook.isbn}</p>
              <p><strong>Kategori: </strong>{tradedBook.sentBook.category}</p>
              <p><strong>Açıklama: </strong>{tradedBook.sentBook.description}</p>
              <p><strong>Durum: </strong>{tradedBook.sentBook.condition}</p>
            </div>
            <div className='bookReceive'>
              <h4>Alınan Kitap:</h4>
              <img src={tradedBook.receivedBook.image} alt={tradedBook.receivedBook.title} />
              <p><strong>Başlık: </strong>{tradedBook.receivedBook.title}</p>
              <p><strong>Yazar: </strong>{tradedBook.receivedBook.author}</p>
              <p><strong>Yayınevi: </strong>{tradedBook.receivedBook.publisher}</p>
              <p><strong>Yayın Tarihi: </strong>{tradedBook.receivedBook.publisedDate}</p>
              <p><strong>ISBN: </strong>{tradedBook.receivedBook.isbn}</p>
              <p><strong>Kategori: </strong>{tradedBook.receivedBook.category}</p>
              <p><strong>Açıklama: </strong>{tradedBook.receivedBook.description}</p>
              <p><strong>Durum: </strong>{tradedBook.receivedBook.condition}</p>
            </div>
          </div>
          <div className="tradersInfos">
            <div className='sender'>
              <h4>Gönderen:</h4>
              <p><strong>Ad Soyad: </strong>{tradedBook.sender.firstName + " " + tradedBook.sender.lastName}</p>
              <p><strong>Email: </strong>{tradedBook.sender.mailAddress}</p>
              <p><strong>Adres: </strong>{tradedBook.sender.address}</p>
            </div>
            <div className='receiver'>
              <h4>Alıcı:</h4>
              <p><strong>Ad Soyad: </strong>{tradedBook.receiver.firstName + " " + tradedBook.receiver.lastName}</p>
              <p><strong>Email: </strong>{tradedBook.receiver.mailAddress}</p>
              <p><strong>Adres: </strong>{tradedBook.receiver.address}</p>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}

export default TradedBooks