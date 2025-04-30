import React, { useEffect, useState } from 'react'
import '../AdvertisedBook/AdvertisedBook.css'
import { useStateValue } from '../StateProvider';
import { instanceListing } from '../axios';
import { getAuthToken } from '../auth';

function AdvertisedBook() {
    const [{ advertisedBook }, dispatch] = useStateValue();
    // const [advertisedBooks, setAdvertisedBooks] = useState([]);

    const ownerId = localStorage.getItem('userId');

    useEffect(() => {
        if (!ownerId) {
            console.error("Kullanıcı ID bulunamadı.");
            return;
        }

        const fetchAdvertisedBooks = async () => {
            try {
                const response = await instanceListing.get(`/getListsByOwnerId?ownerId=${ownerId}`, {
                    headers: {
                        Authorization: `Bearer ${getAuthToken()}`,
                    },
                });
                // setAdvertisedBooks(response.data);

                console.log("Gelen ilan verileri:", response.data);

                dispatch({
                    type: 'SET_AD_BOOKS',
                    advertisedBook: response.data,
                });
            } catch (error) {
                console.error("İlanlar yüklenirken hata oluştu:", error);
            }
        };

        if (location.state?.updated || advertisedBook.length === 0) {
            fetchAdvertisedBooks();
        }
    }, [ownerId, location.state]);

    useEffect(() => {
        console.log("Redux state güncellendi (takip):", advertisedBook);
    }, [advertisedBook]);
    

    const removeFromAd = async (listId) => {
        try {
            await instanceListing.delete(`/delete/${listId}`, {
                headers: {
                    Authorization: `Bearer ${getAuthToken()}`,
                },
            });

            dispatch({
                type: 'REMOVE_FROM_AD',
                id: listId,
            });

        } catch (error) {
            console.error("İlandan kaldırırken hata oluştu:", error);
        }
    };

    return (
        <div className='advertisedBook'>
            <h3>İlanlarım</h3>
            {advertisedBook && advertisedBook.length > 0 ? (
                advertisedBook.map((item) => (
                    item.book ? (
                        <div className="BooksInfo" key={item.listId}>
                            <img src={item.book.image} alt={item.book.title || "Kitap görseli"}
                            />
                            <div>
                                <h2>{item.book.title}</h2>
                                <p>ISBN: {item.book.isbn}</p>
                                <p>Yazar: {item.book.author}</p>
                                <p>Yayınevi: {item.book.publisher}</p>
                                <p>Yayın Tarihi: {item.book.publishedDate}</p>
                                <p>Kategori: {item.book.category}</p>
                                <p>Değeri: {item.type === 'SALE' && item.price ? `${item.price} ₺` : item.type === 'EXCHANGE' ? 'Takasa Açık' : 'Hatalı değer'}</p>
                            </div>
                            <button onClick={() => { console.log("Butona tıklandı"); removeFromAd(item.listId)}} >İlandan Kaldır</button>
                        </div>
                    ) : null
                ))
            ) : (
                <p>İlanda kitap bulunamadı. İlana kitap koymak için kitaplığa gidin.</p>
            )}
        </div>
    )
}

export default AdvertisedBook