import React, { useState } from 'react'
import './Bookshelf.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';

function Bookshelf({ showButtons = true }) {
    const [{ bookshelf, advertisedBook }, dispatch] = useStateValue();
    const navigate = useNavigate();

    const removeFromBookshelf = (id) => {
        dispatch({
            type: 'REMOVE_FROM_BOOKSHELF',
            id: id,
        })
    }

    const addToAd = (book) => {
        dispatch({
            type: 'ADD_TO_AD',
            item: book,
        });

        navigate("/myAds", { state: book });
    };

    return (
        <div className='bookshelf'>
            <div className="bookshelf_container">
                <h1 className="bookshelf_title">Kitaplığım</h1>
                <div className="bookshelf_row">
                    {bookshelf.map((book, index) => {
                        const isAdvertised = advertisedBook.some(adBook => adBook.id === book.id);
                        return (
                            <div className="bookshelf_card" key={index}>
                                <div className="book_info">
                                    <p>{book.isbn}/{book.title}-{book.author}/{book.publisher}-{book.publishedDate}/{book.category}</p>

                                    {book.price ? (
                                        <p className='book_price'>
                                            <strong>{book.price}</strong>
                                            <small>₺</small>
                                        </p>
                                    ) : (
                                        <p className='book_trade'>Takasa açık</p>
                                    )}
                                </div>

                                <img src={book.image} alt='' />

                                {!isAdvertised && (
                                    <button className='adButton' onClick={() => addToAd(book)}>İlana Koy</button>
                                )}

                                <button className='updateButton' onClick={() => navigate("/updateBook", { state: book })}>Güncelle</button>

                                <button onClick={() => removeFromBookshelf(book.id)} className='removeButton'>Kitaplıktan Çıkar</button>

                            </div>
                        )
                    })}
                </div>

                <button onClick={e => navigate("/addBook", { replace: true })}>Kitap Ekle</button>

            </div>
        </div>
    )
}

export default Bookshelf