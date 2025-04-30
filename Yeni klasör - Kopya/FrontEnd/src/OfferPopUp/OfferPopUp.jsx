import React, { useEffect, useRef, useState } from 'react'
import '../OfferPopUp/OfferPopUp.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
function OfferPopUp({ onClose }) {
  const [{ advertisedBook }, dispatch] = useStateValue();
  const [selectedBook, setSelectedBook] = useState(null);
  const navigate = useNavigate();
  const popupRef = useRef(null);

  const booksForTrade = advertisedBook.filter((item) => item.type === 'EXCHANGE');

  const handleSelect = (listId) => {
    setSelectedBook((prevId) => (prevId === listId ? null : listId));
  }

  const offerSubmit = () => {
    if (selectedBook) {
      const bookToSend = booksForTrade.find((book) => book.listId === selectedBook);
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
      {booksForTrade?.length > 0 ? (
        booksForTrade.map((item) => (
          <div className={`offerPopUp_book ${selectedBook === item.listId ? 'selected' : ''}`} key={item.listId} onClick={() => handleSelect(item.listId)}>
            <img src={item.book.image} alt={item.book.title || "Kitap görseli"}/>
            <div className="offerPopUp_bookInfo">
              <p><strong>{item.book.title}</strong></p>
              <p>ISBN: {item.book.isbn}</p>
              <p>Yazar: {item.book.author}</p>
              <p>Yayınevi: {item.book.publisher}</p>
              <p>Yayın Tarihi: {item.book.publishedDate}</p>
              <p>Kategori: {item.book.category}</p>              
              <p>Açıklama: {item.book.description}</p>              
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