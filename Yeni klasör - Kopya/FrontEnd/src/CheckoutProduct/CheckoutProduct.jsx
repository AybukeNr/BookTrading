import React from 'react'
import '../CheckoutProduct/CheckoutProduct.css'
import { useStateValue } from '../StateProvider';
import { Rating } from '@mui/material';

function CheckoutProduct({ id, image, title, author, isbn, publisher, publishedDate, description, condition, category, price, firstName, lastName, trustPoint, email }) {
  const [{ basket }, dispatch] = useStateValue();

  const removeFromBasket = () => {
    dispatch({
      type: 'REMOVE_FROM_BASKET',
      id: id,
    })
  }


  return (
    <div className='checkoutProduct'>
      <img className='checkoutProduct_image' src={image} alt="" />

      <div className='checkoutProduct_info'>
        <p className='checkoutProduct_name'>{isbn}/{title}-{author}/{publisher}-{publishedDate}/{category}/{condition}</p>
        <p>{firstName + " " + lastName} - Güvence: {trustPoint}<Rating className='rating' /></p>
        <p className='checkoutProduct_description'>{description}</p>

        <p className='checkoutProduct_price'>
          <strong>{price}</strong>
          <small>₺</small>
        </p>

        <p className='checkoutProduct_email'>{email}</p>
        
        <button onClick={removeFromBasket}>Sepetten çıkar</button>
      </div>

    </div>
  )
}

export default CheckoutProduct