import React from 'react'
import './AdvertisedBook.css'
import { useLocation } from 'react-router-dom';
import { useStateValue } from './StateProvider';

function AdvertisedBook() {
    const [{ advertisedBook }, dispatch] = useStateValue();
    const location = useLocation();
    const bookDetail = location.state;

    const removeFromAd = (id) => {
        dispatch({
            type: 'REMOVE_FROM_AD',
            id: id,
        });
    }

    return (
        <div className='advertisedBook'>
            <h3>İlanlarım</h3>
            {advertisedBook.length > 0 ? (
                advertisedBook.map((book, index) => (
                <div className="BooksInfo" key={index}>
                    <img src={book.image} />
                    <div>
                        <h2>{book.title}</h2>
                        <p>ISBN: {book.isbn}</p>
                        <p>Yazar: {book.author}</p>
                        <p>Yayınevi: {book.publisher}</p>
                        <p>Yayın Tarihi: {book.publishedDate}</p>
                        <p>Kategori: {book.category}</p>
                        <p>Fiyat: {book.price ? `${book.price}` : 'Takasa Açık'}</p>
                    </div>
                    <button onClick={() => removeFromAd(book.id)}>İlandan Kaldır</button>
                </div>
                ))
            ) : (
                <p>İlanda kitap bulunamadı. İlana kitap koymak için kitaplığa gidin.</p>
            )}
        </div>
    )
}

export default AdvertisedBook