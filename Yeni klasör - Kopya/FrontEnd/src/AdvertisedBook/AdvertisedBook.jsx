import React, { useEffect, useState } from 'react'
import '../AdvertisedBook/AdvertisedBook.css'
import { useLocation } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import { instanceListing } from '../axios';
import { getAuthToken } from '../auth';

function AdvertisedBook() {
    const [{ advertisedBook }, dispatch] = useStateValue();
    const [advertisedBooks, setAdvertisedBooks] = useState([]);

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
                setAdvertisedBooks(response.data);
    
                console.log("Gelen ilan verileri:", response.data);

                dispatch({
                    type: 'SET_AD_BOOKS',
                    advertisedBook: response.data,
                });

                console.log("Redux state güncellendi:", advertisedBook);
            } catch (error) {
                console.error("İlanlar yüklenirken hata oluştu:", error);
            }
        };
    
        fetchAdvertisedBooks();
    }, [ownerId]);
    
    const removeFromAd = async (id) => {
        try {
            await instanceListing.delete(`/removeAdvertisedBook/${id}`, {
                headers: {
                    Authorization: `Bearer ${getAuthToken()}`,
                },
                withCredentials: true,
            });
    
            dispatch({
                type: 'REMOVE_FROM_AD',
                id: id,
            });
        } catch (error) {
            console.error("İlandan kaldırırken hata oluştu:", error);
        }
    };
    

    // const removeFromAd = (id) => {
    //     dispatch({
    //         type: 'REMOVE_FROM_AD',
    //         id: id,
    //     });
    // }

    return (
        <div className='advertisedBook'>
            <h3>İlanlarım</h3>
            {advertisedBooks && advertisedBooks.length > 0 ? (
                advertisedBooks.map((item) => (
                <div className="BooksInfo" key={item.listId}>
                    <img src={item.book.image} />
                    <div>
                        <h2>{item.book.title}</h2>
                        <p>ISBN: {item.book.isbn}</p>
                        <p>Yazar: {item.book.author}</p>
                        <p>Yayınevi: {item.book.publisher}</p>
                        <p>Yayın Tarihi: {item.book.publishedDate}</p>
                        <p>Kategori: {item.book.category}</p>
                        <p>Değeri: {item.book.price ? `${item.book.price}` : 'Takasa Açık'}</p>
                    </div>
                    <button onClick={() => removeFromAd(item.book.id)}>İlandan Kaldır</button>
                </div>
                ))
            ) : (
                <p>İlanda kitap bulunamadı. İlana kitap koymak için kitaplığa gidin.</p>
            )}
        </div>
    )
}

export default AdvertisedBook