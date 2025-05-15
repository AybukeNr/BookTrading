import React from 'react'
import '../Checkout/Checkout.css'
import Subtotal from '../Subtotal/Subtotal'
import CheckoutProduct from '../CheckoutProduct/CheckoutProduct'
import { useStateValue } from '../StateProvider';

function Checkout() {
    const [{ basket, user }, dispatch] = useStateValue();

    return (
        <div className='checkout'>
            <div className="checkout_left">
                <img className='checkout_ad' src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI5W1AjZSgok7PuSE5WdeAZD8f_Das4XX1Ng&s" alt="" />
                <div>
                    <h1 className="checkout_title">Alışveriş Sepetim</h1>
                    {/* <h3>{user?.firstName}</h3>
                    <h3>{user?.lastName}</h3> */}

                    {basket?.length > 0 ? (
                        basket.map((item, index) => (
                            <CheckoutProduct
                                key={index} 
                                isbn={item.book.isbn}
                                title={item.book.title}
                                author={item.book.author}
                                publisher={item.book.publisher}
                                publishedDate={item.book.publishedDate}
                                category={item.book.category}
                                description={item.book.description}
                                condition={item.book.condition}
                                image={item.book.image}
                                price={item.book.price}
                                firstName={item.user.firstName}
                                lastName={item.user.lastName}
                                trustPoint={item.user.trustPoint}
                                email={item.user.email}
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