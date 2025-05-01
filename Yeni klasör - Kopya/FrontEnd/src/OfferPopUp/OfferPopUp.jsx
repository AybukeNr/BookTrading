import React, { useEffect, useRef, useState } from 'react'
import '../OfferPopUp/OfferPopUp.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import { instanceListing } from '../axios';
import { getAuthToken } from '../auth';
function OfferPopUp({ onClose }) {
  const [{ advertisedBook }, dispatch] = useStateValue();
  const [selectedBook, setSelectedBook] = useState(null);
  const navigate = useNavigate();
  const popupRef = useRef(null);
  const [exchangeBooks, setExchangeBooks] = useState([]);

  useEffect(() => {
      if (!advertisedBook || advertisedBook.length === 0) {
          const storedUserId = localStorage.getItem("userId");
          const fetchAdvertisedBooks = async () => {
              try {
                  const response = await instanceListing.get(`/getListsByOwnerId?ownerId=${storedUserId}`, {
                      headers: {
                          Authorization: `Bearer ${getAuthToken()}`,
                      },
                  });

                  dispatch({
                      type: 'SET_AD_BOOKS',
                      advertisedBook: response.data,
                  });

              } catch (error) {
                  console.error("OfferPopUp içinde veri yüklenemedi:", error);
              }
          };

          fetchAdvertisedBooks();
      }
  }, [advertisedBook, dispatch]);

  useEffect(() => {
      if (advertisedBook && advertisedBook.length > 0) {
          const filtered = advertisedBook.filter(item => item.type === 'EXCHANGE');
          setExchangeBooks(filtered);
      }
  }, [advertisedBook]);

  const handleSelect = (listId) => {
    setSelectedBook((prevId) => (prevId === listId ? null : listId));
  }  

  const offerSubmit = () => {
    if (selectedBook) {
      const bookToSend = exchangeBooks.find((book) => book.listId === selectedBook);
      dispatch({
        type: 'ADD_TO_OFFER_SENT',
        item: {
          ...bookToSend,
          status: 'GONDERILDI',
        },
      });
      navigate('/myOffers');
    } else {
      alert("Lütfen teklif göndermek için bir kitap seçin!");
    }
  }

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (popupRef.current && !popupRef.current.contains(event.target)) {
        if (onClose) {
          onClose();
        }
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [onClose]);

  return (
    <div className='offerPopUp' ref={popupRef}>
      {exchangeBooks?.length > 0 ? (
        exchangeBooks.map((item) => (
          <div className={`offerPopUp_book ${selectedBook === item.listId ? 'selected' : ''}`} key={item.listId} onClick={() => handleSelect(item.listId)}>
            <img src={item.book.image} alt={item.book.title || "Kitap görseli"}/>
            <div className="offerPopUp_bookInfo">
              <p><strong>{item.book.title}</strong></p>
              <p>ISBN: {item.book.isbn}</p>
              <p>Yazar: {item.book.author}</p>
              <p>Yayınevi: {item.book.publisher}</p>
              <p>Yayın Tarihi: {item.book.publishedDate}</p>
              <p>Kategori: {item.book.category}</p>              
              {/* <p>Açıklama: {item.book.description}</p> */}
            </div>
          </div>
        ))
      ) : (
        <p>Takasa açık kitabınız şu anda yok.</p>
      )}
      <div>
        <button onClick={offerSubmit} className='offerPopUp_button'>Teklif Gönder</button>
      </div>
    </div>
  )
}

export default OfferPopUp