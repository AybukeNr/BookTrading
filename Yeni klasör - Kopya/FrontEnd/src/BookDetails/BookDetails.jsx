import React, { useEffect, useRef, useState } from 'react'
import '../BookDetails/BookDetails.css'
import { useLocation, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import OfferPopUp from '../OfferPopUp/OfferPopUp';
import { Rating } from '@mui/material';
import { instanceListing } from '../axios';

function BookDetails() {
    const [{ bookDetail }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const location = useLocation();
    // const bookDetail = location.state;
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const [showPopUp, setShowPopUp] = useState(false);
    const popUpRef = useRef(null);

    // const fromOffers = bookDetail?.fromOffers;
    const listId = location.state?.listId;
    const fromOffers = location.state?.fromOffers;

    useEffect(() => {
        const fetchBookDetails = async () => {
            setLoading(true);
            setError("");

            try {
                if (location.state?.bookDetail) {
                    dispatch({
                        type: 'SET_BOOK_DETAIL',
                        bookDetail: location.state.bookDetail
                    });
                    setLoading(false);
                    return;
                }

                const response = await instanceListing.get(`/getListsById?listId=${listId}`);
                const data = response.data;

                dispatch({
                    type: 'SET_BOOK_DETAIL',
                    bookDetail: data
                });
            } catch (error) {
                console.error('Kitap detayları alınırken hata oluştu:', error);
                setError("Kitap detayları yüklenirken bir hata oluştu.");
            } finally {
                setLoading(false);
            }
        };
        if (listId) {
            fetchBookDetails();
        }
    }, [listId]);

    // const acceptOffer = () => {
    //     navigate('/trade');
    // }

    const addToBasket = () => {
        if (bookDetail) {
            dispatch({
                type: 'ADD_TO_BASKET',
                item: {
                    listId: bookDetail.listId,
                    book:
                    {
                        title: bookDetail.book.title,
                        author: bookDetail.book.author,
                        isbn: bookDetail.book.isbn,
                        publisher: bookDetail.book.publisher,
                        publishedDate: bookDetail.book.publishedDate,
                        category: bookDetail.book.category,
                        description: bookDetail.book.description,
                        condition: bookDetail.book.condition,
                        image: bookDetail.book.image,
                        price: bookDetail.price,
                    },
                    user: {
                        firstName: bookDetail.user.firstName,
                        lastName: bookDetail.user.lastName,
                        trustPoint: bookDetail.user.trustPoint
                    }
                }
            });
            navigate('/')
        }
    }

    const offerPopUp = () => {
        dispatch({
            type: 'SET_SELECTED_ADVERTISED_BOOK',
            selectedAdvertisedBook: bookDetail,
        });
        setShowPopUp(!showPopUp);
    };


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
                    <img src={bookDetail?.book?.image} alt={bookDetail?.book?.title} />
                    <div>
                        <h2>{bookDetail?.book?.title}</h2>
                        <p onClick={() => navigate("/userDetails")}>
                            {bookDetail?.user?.firstName + " " + bookDetail?.user?.lastName} 
                            {/* - Güvence: {bookDetail?.user?.trustPoint}<Rating className='rating' size='large' value={bookDetail?.user?.trustPoint || 0} readOnly /> */}
                        </p>
                        <p><strong>ISBN: </strong>{bookDetail?.book?.isbn}</p>
                        <p><strong>Yazar: </strong>{bookDetail?.book?.author}</p>
                        <p><strong>Yayınevi: </strong>{bookDetail?.book?.publisher}</p>
                        <p><strong>Yayın Tarihi: </strong>{bookDetail?.book?.publishedDate}</p>
                        <p><strong>Kategori: </strong>{bookDetail?.book?.category}</p>
                        <p><strong>Açıklama: </strong>{bookDetail?.book?.description}</p>
                        <p><strong>Durumu: </strong>{bookDetail?.book?.condition}</p>
                        <p><strong>Değeri: </strong>{bookDetail?.price ? `${bookDetail.price} ₺` : 'Takasa açık'}</p>
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

            {/* {fromOffers && bookDetail && (
                <>
                    <button onClick={acceptOffer}>Teklifi kabul et</button>
                    <button>Teklifi reddet</button>
                </>
            )} */}

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