import React, { useState } from 'react'
import './Bookshelf.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';
import HighlightOffOutlinedIcon from '@mui/icons-material/HighlightOffOutlined';
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';

function Bookshelf() {
    const [{ bookshelf, advertisedBook }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [bookId, setBookId] = useState(null);

    const handleOpen = (id) => {
        setBookId(id);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setBookId(null);
    };


    const removeFromBookshelf = () => {
        dispatch({
            type: 'REMOVE_FROM_BOOKSHELF',
            id: bookId,
        })
        handleClose();
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
                                    <HighlightOffOutlinedIcon onClick={() => handleOpen(book.id)} className='removeButton' />
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

                            </div>
                        )
                    })}
                </div>

                <AddCircleOutlineOutlinedIcon onClick={e => navigate("/addBook", { replace: true })} alt='Kitap ekle' className='addBook_button' />

                <Dialog open={open} onClose={handleClose}>
                    <DialogTitle>Kitaplıktan Çıkar</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Kitabı kitaplıktan çıkarmak istediğinize emin misiniz?
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} color="primary">İptal</Button>
                        <Button onClick={removeFromBookshelf} color="error">Evet</Button>
                    </DialogActions>
                </Dialog>

            </div>
        </div>
    )
}

export default Bookshelf