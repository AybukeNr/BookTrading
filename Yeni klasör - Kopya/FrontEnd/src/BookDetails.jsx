import React, { useState } from 'react'
import './BookDetails.css'
import { useLocation, useNavigate } from 'react-router-dom';
import { useStateValue } from './StateProvider';
import OfferPopUp from './OfferPopUp';
function BookDetails() {
    const [, dispatch] = useStateValue();
    const navigate = useNavigate();
    const location = useLocation();
    const bookDetail = location.state;
    const [showPopUp, setShowPopUp] = useState(false);

    const addToBasket = () => {
        if (bookDetail) {
            dispatch({
                type: 'ADD_TO_BASKET',
                item: {
                    title: bookDetail.title,
                    author: bookDetail.author,
                    isbn: bookDetail.title,
                    publisher: bookDetail.publisher,
                    publishedDate: bookDetail.publishedDate,
                    category: bookDetail.category,
                    image: bookDetail.image,
                    price: bookDetail.price
                }
            });
            navigate('/checkout')
        }
    }

    const offerPopUp = () => {
        setShowPopUp(!showPopUp);
    }

    return (
        <div className='bookDetails'>
            {bookDetail ? (
                <div className="bookDetails_product">
                    <img src={bookDetail.image} />
                    <div>
                        <h2>{bookDetail.title}</h2>
                        <p>ISBN: {bookDetail.isbn}</p>
                        <p>Yazar: {bookDetail.author}</p>
                        <p>Yayınevi: {bookDetail.publisher}</p>
                        <p>Yayın Tarihi: {bookDetail.publishedDate}</p>
                        <p>Kategori: {bookDetail.category}</p>
                        <p>Fiyat: {bookDetail.price ? `${bookDetail.price}` : 'Takasa Açık'}</p>
                    </div>
                </div>
            ) : (
                <p>Kitap bilgisi bulunamadı.</p>
            )}

            {bookDetail.price ? (
                <button onClick={addToBasket}>Satın almak için sepete ekle</button>
            ) : (
                <button onClick={offerPopUp}>Takas için teklif ver</button>
            )}

            {showPopUp && (
                <div className="popUp">
                    <div className="popUp_inner">
                        <h2>Takas için kitap seç</h2>
                        <OfferPopUp />
                        <div>
                            <button onClick={offerPopUp}>İptal</button>
                        </div>
                    </div>
                </div>
            )}

        </div>
    )
}

export default BookDetails