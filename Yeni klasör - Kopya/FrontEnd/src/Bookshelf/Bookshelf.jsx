import React, { useEffect, useState } from 'react'
import '../Bookshelf/Bookshelf.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import HighlightOffOutlinedIcon from '@mui/icons-material/HighlightOffOutlined';
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';
import { instanceLibrary } from '../axios';
import { getAuthToken } from '../auth';

const userId = localStorage.getItem('userId');
function Bookshelf() {
    const [{ bookshelf, advertisedBook }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [bookId, setBookId] = useState(null);
    const [tradeOption, setTradeOption] = useState('');
    const [openAdDialog, setOpenAdDialog] = useState(false);
    const [bookToAdvertise, setBookToAdvertise] = useState(null);
    const [price, setPrice] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchBooks = async () => {
            setLoading(true);
            setError('');
            try {
                const response = await instanceLibrary.get(`/getBookByOwnerId?ownerId=${userId}`)
                const data = response.data;

                dispatch({
                    type: 'SET_BOOKSHELF',
                    bookshelf: data,
                })
            } catch (error) {
                console.error("Kitapları alırken hata oluştu:", error);
                setError("Kitaplar yüklenirken bir hata oluştu.");
            } finally {
                setLoading(false);
            }
        };
        fetchBooks();
    }, [userId]);

    const handleOpen = (id) => {
        setBookId(id);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setBookId(null);
    };

    const handleOpenAdDialog = (book) => {
        setBookToAdvertise(book);
        setOpenAdDialog(true);
    };

    const handleCloseAdDialog = () => {
        setOpenAdDialog(false);
        setBookToAdvertise(null);
    };

    const removeFromBookshelf = async () => {
        if (!bookId) {
            console.error("Kitap ID alınamadı!");
            return;
        }
        try {
            await instanceLibrary.delete(`/deleteBook/${bookId}`, {
                headers: {
                    Authorization: `Bearer ${getAuthToken()}`,
                },
            });

            dispatch({
                type: 'REMOVE_FROM_BOOKSHELF',
                id: bookId,
            });

        } catch (error) {
            console.error("Kitap silinirken hata oluştu:", error);
            setError("Kitap silinirken bir hata oluştu.");
        }

        handleClose();
    };

    const addToAd = async () => {
        if (!bookToAdvertise) return;

        if (!tradeOption) {
            setError("Lütfen bir seçenek seçiniz.");
            return;
        }

        if (tradeOption === 'SALE' && !price) {
            setError("Lütfen bir fiyat giriniz.");
            return;
        }

        const updatedBook = {
            ...bookToAdvertise,
            ownerId: userId, 
            bookId: bookToAdvertise.id, 
            type: tradeOption,
            price: tradeOption === 'SALE' ? price : null,
        }

        console.log("Gönderilen Veri:", updatedBook);

        try {
            await instanceLibrary.post('/createLists', updatedBook, {
                headers: {
                    Authorization: `Bearer ${getAuthToken()}`,
                },
            });

            dispatch({
                type: 'ADD_TO_AD',
                item: updatedBook,
            });

            navigate("/myAds", { state: updatedBook });
            handleCloseAdDialog();
        } catch (error) {
            console.error("Kitap ilan verilirken hata oluştu:", error);
            setError("Kitap ilana eklenirken bir hata oluştu.");
        }
    };

    return (
        <div className='bookshelf'>
            <div className="bookshelf_container">
                <h1 className="bookshelf_title">Kitaplığım</h1>

                {error && <p className="error-message">{error}</p>}
                {loading && <p>Kitap ekleniyor...</p>}

                <div className="bookshelf_row">
                    {bookshelf.map((book, index) => {
                        const isAdvertised = advertisedBook?.some(adBook => adBook.id === book.id);
                        return (
                            <div className="bookshelf_card" key={index}>
                                <div className="book_info">
                                    <HighlightOffOutlinedIcon onClick={() => handleOpen(book.id)} className='removeButton' />
                                    <p>{book.isbn}/{book.title}-{book.author}/{book.publisher}-{book.publishedDate}/{book.category}</p>

                                    {/* {book.price ? (
                                        <p className='book_price'>
                                            <strong>{book.price}</strong>
                                            <small>₺</small>
                                        </p>
                                    ) : (
                                        <p className='book_trade'>Takasa açık</p>
                                    )} */}
                                </div>

                                <img src={book.image} alt='' />

                                {!isAdvertised && (
                                    <button className='adButton' onClick={() => handleOpenAdDialog((book))}>İlana Koy</button>
                                )}

                                <button className='updateButton' onClick={() => navigate("/updateBook", { state: book })}>Güncelle</button>

                            </div>
                        )
                    })}
                </div>

                <AddCircleOutlineOutlinedIcon onClick={e => navigate("/addBook", { replace: true })} alt='Kitap ekle' className='addBook_button' />

                {/* Add to AdvertisedBook dialog */}
                <Dialog open={openAdDialog} onClose={handleCloseAdDialog}>
                    <DialogTitle>Kitap Değerlendirme Tipi:</DialogTitle>
                    <DialogContent>
                        {error && <p style={{ color: "red", fontSize: "14px" }}>{error}</p>}
                        <DialogContent>
                            <div className='trade_sale'>
                                <label className="container">
                                    Takasa aç
                                    <input
                                        type="radio"
                                        name="tradeOption"
                                        value="EXCHANGE"
                                        onChange={(e) => setTradeOption(e.target.value)}
                                    />
                                    <span className="checkmark"></span>
                                </label>
                                <label className="container">
                                    Satışa aç
                                    <input
                                        type="radio"
                                        name="tradeOption"
                                        value="SALE"
                                        onChange={(e) => setTradeOption(e.target.value)}
                                    />
                                    <span className="checkmark"></span>
                                </label>
                            </div>
                        </DialogContent>
                        {tradeOption === 'SALE' && (
                            <>
                                <h5 style={{
                                    margin: 0,
                                    fontSize: "16px",
                                    color: " #213547"
                                }}>Fiyat giriniz:</h5>
                                <input type="text" placeholder="Fiyat (₺) giriniz." style={{
                                    width: "auto",
                                    padding: "10px",
                                    fontSize: "14px",
                                    border: "1px solid darkgray",
                                    borderRadius: "8px",
                                    outline: "none"
                                }} value={price} onChange={(e) => setPrice(e.target.value)} />
                            </>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseAdDialog} color="gray">İptal</Button>
                        <Button onClick={addToAd} color="primary">İlana Koy</Button>
                    </DialogActions>
                </Dialog>

                {/* Remove from bookshelf dialog */}
                <Dialog open={open} onClose={handleClose}>
                    <DialogTitle>Kitaplıktan Çıkar</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Kitabı kitaplıktan çıkarmak istediğinize emin misiniz?
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} color="primary">İptal</Button>
                        <Button onClick={removeFromBookshelf} color="error">Kitaplıktan Çıkar</Button>
                    </DialogActions>
                </Dialog>

            </div>
        </div>
    )
}

export default Bookshelf