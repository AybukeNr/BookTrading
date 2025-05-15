import React, { useEffect, useState } from 'react'
import '../Payment/Payment.css'
import { useStateValue } from '../StateProvider';
import CheckoutProduct from '../CheckoutProduct/CheckoutProduct';
import { Link, useNavigate } from 'react-router-dom';
import CurrencyFormat from 'react-currency-format';
import { getBasketTotal } from '../reducer';
import PaymentCard from '../PaymentCard/PaymentCard';
// import axios from '../axios';

function Payment() {
    const [{ basket, user }, dispatch] = useStateValue();
    const navigate = useNavigate();


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

    // const handleSubmit = async (event) => {
    //     event.preventDefault();
    //     setProcessing(true);

    //     const payload = await stripe.confirmCardPayment(clientSecret, {
    //         payment_method: {
    //             card: elements.getElement(CardElement)
    //         }
    //     }).then(({paymentIntent}) => {
    //         setSucceeded(true);
    //         setError(null);
    //         setProcessing(false);

    //         navigate.replace('/orders');
    //     })
    // }

    // const handleChange = event => {
    //     setDisabled(event.empty);
    //     setError(event.error ? event.error.message : "");
    // }

    return (
        <div className='payment'>
            <div className="payment_container">
                <h1> Ürünlerim (<Link to='/checkout'>{basket?.length} adet ürün</Link>) </h1>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Teslimat Adresi: </h3>
                    </div>
                    <div className="payment_address">
                        <p><strong>Ad Soyad:</strong>{user?.firstName + " " + user?.lastName}</p>
                        <p><strong>Email:</strong>{user?.email}</p>
                        <p><strong>Adres:</strong>{user?.address}</p>
                    </div>
                </div>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Sepetteki Ürünlerim</h3>
                    </div>
                    <div className="payment_items">
                        {basket.map((item, index) => (
                            <CheckoutProduct key={index}
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
                        ))}
                    </div>
                </div>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Ödeme Bilgileri</h3>
                    </div>
                    <div className="payment_details">
                        <form>
                            {/* form içine onSubmit={handleSubmit}  */}
                            <div className="payment_card">
                                <PaymentCard />
                            </div>
                            {/* <CardElement onChange={handleChange} /> */}
                            <div className="payment_priceContainer">
                                <CurrencyFormat
                                    renderText={(value) => (
                                        <h3>
                                            Sipariş Toplamı: {value} ₺
                                        </h3>
                                    )}

                                    decimalScale={2}
                                    value={getBasketTotal(basket)}
                                    displayType={"text"}
                                    thousandSeparator={true}
                                />
                                <button disabled={processing || disabled || succeeded}>
                                    <span>{processing ? <p>Yükleniyor</p> : "Onayla ve Satın al"}</span>
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