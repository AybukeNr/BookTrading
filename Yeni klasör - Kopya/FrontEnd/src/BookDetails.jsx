import React, { useEffect, useRef, useState } from 'react'
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
    const popUpRef = useRef(null);

    const fromOffers = bookDetail?.fromOffers;

    const acceptOffer = () => {
        navigate('/trade');
       }

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

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                setShowPopUp(false);
            }
        };

        if (showPopUp) {
            document.addEventListener('mousedown', handleClickOutside);
        }
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [showPopUp]);


    return (
        <div className='bookDetails'>
            {bookDetail ? (
                <div className="bookDetails_product">
                    <img src={bookDetail.image} alt={bookDetail.title}/>
                    <div>
                        <h2>{bookDetail.title}</h2>
                        <p><strong>ISBN: </strong>{bookDetail.isbn}</p>
                        <p><strong>Yazar: </strong>{bookDetail.author}</p>
                        <p><strong>Yayınevi: </strong>{bookDetail.publisher}</p>
                        <p><strong>Yayın Tarihi: </strong>{bookDetail.publishedDate}</p>
                        <p><strong>Kategori: </strong>{bookDetail.category}</p>
                        <p><strong>Fiyat: </strong>{bookDetail.price ? `${bookDetail.price}` : 'Takasa Açık'}</p>
                    </div>
                </div>
            ) : (
                <p>Kitap bilgisi bulunamadı.</p>
            )}

            {/* Tekliflerim sayfasından geldiyse aşağıdaki butonlar gözükmeyecek*/}
            {!fromOffers && (
                <>
                    {bookDetail.price ? (
                        <button onClick={addToBasket}>Satın almak için sepete ekle</button>
                    ) : (
                        <button onClick={offerPopUp}>Takas için teklif ver</button>
                    )}
                </>
            )}

            {fromOffers && (
                <>
                <button onClick={acceptOffer}>Teklifi kabul et</button>
                <button>Teklifi reddet</button>
                </>
            )}

            {showPopUp && (
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

        </div>
    )
}

export default BookDetails