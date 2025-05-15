import React, { useState, useEffect, useRef } from 'react'
import '../Product/Product.css'
import { Rating } from '@mui/material';
import { useStateValue } from '../StateProvider'
import { useNavigate } from 'react-router-dom';
import OfferPopUp from '../OfferPopUp/OfferPopUp';

function Product({ book }) {

  const navigate = useNavigate();
  const [{ basket }, dispatch] = useStateValue();
  const [showOfferPopUp, setShowOfferPopUp] = useState(false);
  const popUpRef = useRef(null);

  console.log('sepet deneme', basket);

  const {
    book: {
      id,
      title,
      author,
      isbn,
      publisher,
      publishedDate,
      category,
      description,
      condition,
      image,
    },
    price,
    user: { firstName, lastName, trustPoint, email, address },
    listId,
  } = book;

  const addToBasket = () => {
    dispatch({
      type: 'ADD_TO_BASKET',
      item: {
        book: {
          id, title, author, isbn, publisher, publishedDate,
          category, description, condition, image, price
        },
        user: {
          firstName, lastName, trustPoint, email
        },
        listId,
      }
    });
  }

  const handleNavigate = () => {
    navigate('/bookDetails', {
      state: {
        listId,
        bookDetail: book,
      }
    });
  }

  const offerPopUp = (e) => {
    e.stopPropagation();
    dispatch({
      type: 'SET_SELECTED_ADVERTISED_BOOK',
      selectedAdvertisedBook: book,
    });
    setShowOfferPopUp(true);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (popUpRef.current && !popUpRef.current.contains(event.target)) {
        setShowOfferPopUp(false);
      }
    };

    if (showOfferPopUp) {
      document.addEventListener('mousedown', handleClickOutside);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [showOfferPopUp]);

  return (
    <>
      <div className={`product ${showOfferPopUp ? 'inactive' : ''}`}>
        <div className="product_info">
          <p onClick={handleNavigate}>{isbn}/{title}-{author}/{publisher}-{publishedDate}/{category}/{condition}</p>
          <p onClick={() => navigate("/userDetails", {
            state: { user: book.user.id }
          })}>{firstName + " " + lastName} - Güvence: {trustPoint}<Rating className='rating' /></p>
          <p onClick={handleNavigate}>{description}</p>

          {price ? (
            <p className='product_price' onClick={handleNavigate}>
              <strong>{price}</strong>
              <small>₺</small>
            </p>
          ) : (
            <p className='product_trade' onClick={handleNavigate}>Takasa açık</p>
          )}

        </div>

        <img src={image} alt='' onClick={handleNavigate} />

        {price ? (
          <button onClick={(e) => { e.stopPropagation(); addToBasket() }}>Satın almak için sepete ekle</button>
        ) : (
          <button onClick={offerPopUp}>Takas için teklif ver</button>
        )}
      </div>
      <>
        {showOfferPopUp && (
          <div className="popUp">
            <div className="popUp_inner" ref={popUpRef}>
              <h2>Takas için kitap seç</h2>
              <OfferPopUp onClose={() => setShowOfferPopUp(false)} />
              <div>
                <button onClick={() => setShowOfferPopUp(false)}>İptal</button>
              </div>
            </div>
          </div>
        )}
      </>
    </>
  )
}

export default Product