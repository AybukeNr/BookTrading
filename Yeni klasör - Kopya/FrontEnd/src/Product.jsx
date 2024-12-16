import React from 'react'
import './Product.css'
import { useStateValue } from './StateProvider'

function Product({ id, title, image, price }) {

  const [{ basket }, dispatch] = useStateValue(); 
  console.log('sepet deneme', basket);
  

  const addToBasket = () => {
    dispatch({
      type: 'ADD_TO_BASKET',
      item: {
        id: id,
        title: title,
        image: image,
        price: price
      }
    });
  }

  return (
    <div className='product'>
        <div className="product_info">
            <p>{ title }</p>
            <p className='product_price'>
                <small>₺</small>
                <strong>{ price }</strong>
            </p>
        </div>

        <img src={ image } alt=''/>

        <button onClick={addToBasket}>Satın almak için sepete ekle</button>
        <button>Takas için sepete ekle</button>
    </div>
  )
}

export default Product