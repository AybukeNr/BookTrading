import React, { useState, useEffect, useRef } from 'react'
import './Product.css'
import { Rating } from '@mui/material';
import { useStateValue } from './StateProvider'
import { useNavigate } from 'react-router-dom';
import OfferPopUp from './OfferPopUp';

function Product({ id, title, author, isbn, publisher, publishedDate, category, image, price }) {

  const navigate = useNavigate();
  const [{ basket }, dispatch] = useStateValue();
  const [showOfferPopUp, setShowOfferPopUp] = useState(false);
  const popUpRef = useRef(null);

  console.log('sepet deneme', basket);

  const addToBasket = () => {
    dispatch({
      type: 'ADD_TO_BASKET',
      item: {
        id: id,
        title: title,
        author: author,
        isbn: isbn,
        publisher: publisher,
        publishedDate: publishedDate,
        category: category,
        image: image,
        price: price
      }
    });
  }

  const handleNavigate = () => {
    navigate('/bookDetails', {
      state: {
        id,
        title,
        author,
        isbn,
        publisher,
        publishedDate,
        category,
        image,
        price
      }
    });
  }


  const offerPopUp = () => {
    setShowOfferPopUp(!showOfferPopUp);
  }

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
        <div className="product_info" onClick={handleNavigate}>
          <p>{isbn}/{title}-{author}/{publisher}-{publishedDate}/{category}</p>
          <Rating className='rating'/>

          {price ? (
            <p className='product_price'>
              <strong>{price}</strong>
              <small>₺</small>
            </p>
          ) : (
            <p className='product_trade'>Takasa açık</p>
          )}

        </div>

        <img src={image} alt='' onClick={handleNavigate} />

        {price ? (
          <button onClick={addToBasket}>Satın almak için sepete ekle</button>
        ) : (
          <button onClick={offerPopUp}>Takas için teklif ver</button>
        )}
      </div>
      <>
        {showOfferPopUp && (
          <div className="popUp">
            <div className="popUp_inner" ref={popUpRef}>
              <h2>Takas için kitap seç</h2>
              <OfferPopUp onClose={offerPopUp} />
              <div>
                <button onClick={offerPopUp}>İptal</button>
              </div>
            </div>
          </div>
        )}
      </>
    </>
  )
}

export default Product