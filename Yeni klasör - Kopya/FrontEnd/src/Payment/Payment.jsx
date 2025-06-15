import React, { useEffect, useState } from 'react'
import '../Payment/Payment.css'
import { useStateValue } from '../StateProvider';
import CheckoutProduct from '../CheckoutProduct/CheckoutProduct';
import { Link, useNavigate } from 'react-router-dom';
import CurrencyFormat from 'react-currency-format';
import { getBasketTotal } from '../reducer';
import PaymentCard from '../PaymentCard/PaymentCard';
import { instanceShipping, instanceTransaction, instanceUser } from '../axios';

function Payment() {
    const [{ basket }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState(null);
    const [cardName, setCardName] = useState("");
    const [cardNumber, setCardNumber] = useState("");
    const [expiryDate, setExpiryDate] = useState("");
    const [cvv, setCvv] = useState("");
    const [processing, setProcessing] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        const userId = localStorage.getItem("userId");
        if (userId) {
            instanceUser.get(`/getUserById?id=${userId}`)
                .then((res) => setUserInfo(res.data))
                .catch((err) => console.error("Kullanıcı bilgisi alınamadı:", err));
        }
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!cardName || !cardNumber || !cvv || !expiryDate) {
            setError("Lütfen tüm kart bilgilerini doldurun.");
            return;
        }

        setProcessing(true);
        setError("");
        
         const userId = localStorage.getItem("userId");
        if (!userId) {
            setError("Kullanıcı kimliği bulunamadı. Lütfen tekrar giriş yapın.");
            setProcessing(false);
            return;
        }

        const listIds = basket.map((item) => item.listId);
        const amount = getBasketTotal(basket);

        const requestBody = {
            listId: listIds,
            listType: "SALE",
            userId: userId,
            fullName: cardName,
            cardNumber,
            cvv,
            expiryDate,
            amount,
        };

        try {
            const response = await instanceTransaction.post("/payments/createPayment", requestBody,
                { headers: { "Content-Type": "application/json" } }
            );

            if (response.status === 200 || response.status === 201) {
                dispatch({
                    type: "EMPTY_BASKET",
                    basket: response.data
                });
                navigate("/mySales");
            } else {
                setError("Ödeme sırasında beklenmedik bir hata oluştu.");
            }
        } catch (err) {
            console.error("Ödeme hatası:", err);
            setError("Ödeme başarısız. Lütfen bilgilerinizi kontrol edin.");
        } finally {
            setProcessing(false);
        }
    };

    return (
        <div className='payment'>
            <div className="payment_container">
                <h1> Kitaplarım (<Link to='/checkout'>{basket?.length} adet kitap</Link>) </h1>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Teslimat Adresi: </h3>
                    </div>
                    <div className="payment_address">
                        <p><strong>Ad Soyad: </strong>{userInfo?.firstName + " " + userInfo?.lastName}</p>
                        <p><strong>Email: </strong>{userInfo?.mailAddress}</p>
                        <p><strong>Adres: </strong>{userInfo?.address}</p>
                    </div>
                </div>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Sepetteki Kitaplarım</h3>
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
                                // trustPoint={item.user.trustPoint}
                            />
                        ))}
                    </div>
                </div>

                <div className="payment_section">
                    <div className="payment_title">
                        <h3>Ödeme Bilgileri</h3>
                    </div>
                    <div className="payment_details">
                        <form onSubmit={handleSubmit}>
                            <div className="payment_card">
                                <PaymentCard
                                    cardName={cardName}
                                    setCardName={setCardName}
                                    cardNumber={cardNumber}
                                    setCardNumber={setCardNumber}
                                    cvv={cvv}
                                    setCvv={setCvv}
                                    expiryDate={expiryDate}
                                    setExpiryDate={setExpiryDate}
                                />
                            </div>
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
                                <button type="submit">
                                    <span>{processing ? "Onaylanıyor..." : "Onayla ve Satın al"}</span>
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