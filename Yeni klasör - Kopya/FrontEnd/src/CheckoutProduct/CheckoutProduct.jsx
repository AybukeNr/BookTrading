import React from 'react'
import '../CheckoutProduct/CheckoutProduct.css'
import { useStateValue } from '../StateProvider';

function CheckoutProduct({id, image, title, author, isbn, publisher, publishedDate, category, price}) {
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
            <p className='checkoutProduct_name'>{isbn}/{title}-{author}/{publisher}-{publishedDate}/{category}</p>

            <p className='checkoutProduct_price'>
                <small>₺</small>
                <strong>{price}</strong>
            </p>

            <p className='checkoutProduct_description'></p>
            <button onClick={removeFromBasket}>Sepetten çıkar</button>
        </div>

    </div>
  )
}

export default CheckoutProduct