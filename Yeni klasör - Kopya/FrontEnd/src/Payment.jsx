import React, { useEffect, useState } from 'react'
import './Payment.css'
import { useStateValue } from './StateProvider';
import CheckoutProduct from './CheckoutProduct';
import { Link, useNavigate } from 'react-router-dom';
import CurrencyFormat from 'react-currency-format';
import { getBasketTotal } from './reducer';
import PaymentCard from './PaymentCard';
import { CardElement, useElements, useStripe } from '@stripe/react-stripe-js';
import axios from './axios';

function Payment() {
    const [{ basket, user }, dispatch] = useStateValue();
    const navigate = useNavigate();

    // const stripe = useStripe();
    // const elements = useElements();

    const [succeeded, setSucceeded] = useState(false);
    const [processing, setProcessing] = useState("");
    const [error, setError] = useState(null);
    const [disabled, setDisabled] = useState(true);
    const [clientSecret, setclientSecret] = useState(true);
    

    // useEffect(() => {
    //     //müşteriden ödeme almaya yarayan özel stripe secret oluşturacak..
    //     const getClientSecret = async () => {
    //         const response = await axios({
    //             method: 'post',
    //             //Stripe, para birimlerinin alt birimlerindeki toplamı bekliyor
    //             // url: `/payments/create?total=${getBasketTotal(basket)*100}`
    //         })
    //         setclientSecret(response.data.clientSecret);
    //     }
    //     getClientSecret();
    // }, [basket])

    const handleSubmit = async (event) => {
        event.preventDefault();
        setProcessing(true);

        const payload = await stripe.confirmCardPayment(clientSecret, {
            payment_method: {
                card: elements.getElement(CardElement)
            }
        }).then(({paymentIntent}) => {
            setSucceeded(true);
            setError(null);
            setProcessing(false);

            navigate.replace('/orders');
        })
    }

    // const handleChange = event => {
    //     setDisabled(event.empty);
    //     setError(event.error ? event.error.message : "");
    // }

    return (
        <div className='payment'>
            <div className="payment_container">
                <h1> Ürünlerim (<Link to='/checkout'>{basket?.length} adet ürün</Link>) </h1>

                {/* delivery-address */}
                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Teslimat Adresi: </h3>
                    </div>
                    <div className="payment_address">
                        <p>Email</p>
                        {/* <p>{user?.email}</p> */}
                        <p>Adres</p>
                        {/* <p>{user?.address}</p> */}
                    </div>
                </div>


                {/* review-items */}
                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Sepetteki Ürünlerim</h3>
                    </div>
                    <div className="payment_items">
                        {basket.map(item  => (
                            <CheckoutProduct 
                                id={item.id}
                                isbn={item.isbn}
                                title={item.title}
                                author={item.author}
                                publisher={item.publisher}
                                publishedDate={item.publishedDate}
                                category={item.category}
                                image={item.image}
                                price={item.price}
                            />
                        ))}
                    </div>
                </div>


                {/* payment-method */}
                <div className="payment_section">

                    <div className="payment_title">
                        <h3>Ödeme Bilgileri</h3>
                    </div>

                    <div className="payment_details">
                        <form onSubmit={handleSubmit}>
                            <div className="payment_card">
                                <PaymentCard />
                            </div>
                            {/* <CardElement onChange={handleChange} /> */}
                            <div className="payment_priceContainer">
                                <CurrencyFormat
                                    renderText={(value) => (
                                        <h3>
                                            Sipariş Toplamı: {value}
                                        </h3>
                                    )}

                                    decimalScale={2}
                                    value={getBasketTotal(basket)}
                                    displayType={"text"}
                                    thousandSeparator={true}
                                    prefix={"₺"}
                                />
                                <button disabled={processing || disabled || succeeded}>
                                    <span>{processing ? <p>Yükleniyor</p>: "Onayla ve Satın al"}</span>
                                </button>
                            </div>
                            {error && <div>{error}</div>}
                        </form>
                        
                        <p>"Satın al diyerek, <a href="#" target="_blank">Mesafeli Satış Sözleşmesi</a> ve  <a href="#" target="_blank">Ön Bilgilendirme Formu</a>'nu okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Payment