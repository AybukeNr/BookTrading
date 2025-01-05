import React, { useState } from 'react'
import './Bookshelf.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';

function Bookshelf({ showButtons = true }) {
    const [{ bookshelf }, dispatch] = useStateValue();
    const navigate = useNavigate();

    const removeFromBookshelf = (id) => {
        dispatch({
            type: 'REMOVE_FROM_BOOKSHELF',
            id: id,
        })
    }

    return (
        <div className='bookshelf'>
            <div className="bookshelf_container">
                <h1 className="bookshelf_title">Kitaplığım</h1>
                <div className="bookshelf_row">
                    {bookshelf.map((book, index) => (
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

                            {showButtons && (
                                <button onClick={() => removeFromBookshelf(book.id)}>Kitaplıktan Çıkar</button>
                            )}
                        </div>
                    ))}
                </div>

                {showButtons && (
                    <button onClick={e => navigate("/addBook", { replace: true })}>Kitap Ekle</button>
                )}
            </div>
        </div>
    )
}

export default Bookshelf