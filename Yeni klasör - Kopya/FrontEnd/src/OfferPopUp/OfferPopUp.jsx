import React, { useEffect, useRef, useState } from 'react'
import '../OfferPopUp/OfferPopUp.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
function OfferPopUp({ onClose }) {
  const [{ bookshelf }, dispatch] = useStateValue();
  const [selectedBook, setSelectedBook] = useState(null);
  const navigate = useNavigate();
  const popupRef = useRef(null);

  const booksForTrade = bookshelf.filter((item) => !item.price);

  const handleSelect = (id) => {
    setSelectedBook((prevId) => (prevId === id ? null : id));
  }

  const offerSubmit = () => {
    if (selectedBook) {
      const bookToSend = booksForTrade.find((book) => book.id === selectedBook);
      dispatch({
        type: 'ADD_TO_OFFER_SENT',
        item: {
          ...bookToSend,
          status: 'sent',
        },
      });
      navigate('/myOffers');
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
          <div className={`offerPopUp_book ${selectedBook === item.id ? 'selected' : ''}`} key={item.id} onClick={() => handleSelect(item.id)}>
            <img src={item.image} />
            <div className="offerPopUp_bookInfo">
              <p><strong>{item.title}</strong></p>
              <p>ISBN: {item.isbn}</p>
              <p>Yazar: {item.author}</p>
              <p>Yayınevi: {item.publisher}</p>
              <p>Yayın Tarihi: {item.publishedDate}</p>
              <p>Kategori: {item.category}</p>
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