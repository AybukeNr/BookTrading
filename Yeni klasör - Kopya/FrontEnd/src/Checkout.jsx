import React from 'react'
import './Checkout.css'
import Subtotal from './Subtotal'
import CheckoutProduct from './CheckoutProduct'
import { useStateValue } from './StateProvider';

function Checkout() {
    const [{ basket, user }, dispatch] = useStateValue();

    return (
        <div className='checkout'>
            <div className="checkout_left">
                <img className='checkout_ad' src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI5W1AjZSgok7PuSE5WdeAZD8f_Das4XX1Ng&s" alt="" />
                <div>
                    <h1 className="checkout_title">Alışveriş Sepetim</h1>
                    {/* <h3>{user?.ad}</h3> */}
                    {/* <h3>{user?.soyad}</h3> */}

                    {basket?.length > 0 ? (
                        basket.map((item, index) => (
                            <CheckoutProduct
                                key={index} 
                                isbn={item.isbn}
                                title={item.title}
                                author={item.author}
                                publisher={item.publisher}
                                publishedDate={item.publishedDate}
                                category={item.category}
                                image={item.image}
                                price={item.price}
                            />
                        ))
                    ) : (
                        <p>Sepetiniz şu anda boş.</p>
                    )}

                </div>
            </div>

            <div className="checkout_right">
                <Subtotal />
            </div>

        </div>
    )
}

export default Checkout