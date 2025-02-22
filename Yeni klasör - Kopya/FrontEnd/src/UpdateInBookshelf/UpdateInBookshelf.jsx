import React, { useEffect, useState } from 'react'
import '../UpdateInBookshelf/UpdateInBookshelf.css'
import { useLocation, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';

function UpdateInBookshelf() {
    const [, dispatch] = useStateValue();
    const navigate = useNavigate();
    const location = useLocation();
    const bookToUpdate = location.state;

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [isbn, setIsbn] = useState('');
    const [publisher, setPublisher] = useState('');
    const [publishedDate, setPublishedDate] = useState('');
    const [category, setCategory] = useState('');
    const [price, setPrice] = useState('');
    const [image, setImage] = useState('');
    const [tradeOption, setTradeOption] = useState(price ? 'sale' : 'trade');
    const [error, setError] = useState('');

    useEffect(() => {
        if (bookToUpdate) {
            setTitle(bookToUpdate.title || '');
            setAuthor(bookToUpdate.author || '');
            setIsbn(bookToUpdate.isbn || '');
            setPublisher(bookToUpdate.publisher || '');
            setPublishedDate(bookToUpdate.publishedDate || '');
            setCategory(bookToUpdate.category || '');
            setImage(bookToUpdate.image || '');

            if (bookToUpdate.price) {
                setTradeOption('sale');
                setPrice(bookToUpdate.price || '');
            } else {
                setTradeOption('trade');
            }
        }
    }, [bookToUpdate]);

    const handleUpdateBook = () => {
        if (!title || !author || !isbn || !publisher || !publishedDate || !category || !tradeOption || (tradeOption === 'sale' && !price)) {
            setError('Lütfen tüm alanları düzgünce doldurun!');
            return;
        }

        dispatch({
            type: 'UPDATE_IN_BOOKSHELF',
            book: {
                title,
                author,
                isbn,
                publisher,
                publishedDate,
                category,
                price: tradeOption === 'sale' ? price : null,
                image,
            },
        });
        navigate('/bookshelf')
    };



    return (
        <div className='updateBookInBookshelf'>
            <h2>Kitap Güncelle</h2>

            {error && <p className="errorMessage">{error}</p>}

            <div className="book">
                <h5>Kitap Başlığı:</h5>
                <input type="text" placeholder="Kitabın başlığını giriniz" value={title} onChange={(e) => setTitle(e.target.value)} />

                <h5>Kitap Yazarı:</h5>
                <input type="text" placeholder="Kitabın yazarını giriniz" value={author} onChange={(e) => setAuthor(e.target.value)} />

                <h5>Kitap ISBN:</h5>
                <input type="text" placeholder="Kitap ISBN giriniz" value={isbn} onChange={(e) => setIsbn(e.target.value)} />

                <h5>Kitap Yayınevi:</h5>
                <input type="text" placeholder="Kitabın yayınevini giriniz" value={publisher} onChange={(e) => setPublisher(e.target.value)} />

                <h5>Kitap Yayın Tarihi:</h5>
                <input type="text" placeholder="Kitabın yayın yılını giriniz" value={publishedDate} onChange={(e) => setPublishedDate(e.target.value)} />

                <h5>Kitap Kategorisi Seçiniz:</h5>
                <div className='book_category'>
                    <select id="category" value={category} onChange={(e) => setCategory(e.target.value)}>
                        <option value="">Bir kategori seçin</option>
                        <option value="aile">Aile</option>
                        <option value="bilgisayar">Bilgisayar</option>
                        <option value="bilim">Bilim</option>
                        <option value="bilimKurgu">Bilim Kurgu</option>
                        <option value="biyografi">Biyografi</option>
                        <option value="cizgiRoman">Çizgi Roman</option>
                        <option value="din">Din</option>
                        <option value="drama">Drama</option>
                        <option value="eğitim">Eğitim</option>
                        <option value="felsefe">Felsefe</option>
                        <option value="gençlikKurgu">Gençlik Kurgu</option>
                        <option value="is">İş</option>
                        <option value="kişiselGelişim">Kişisel Gelişim</option>
                        <option value="kurgu">Kurgu</option>
                        <option value="psikoloji">Psikoloji</option>
                        <option value="şiir">Şiir</option>
                        <option value="siyasetBilimi">Siyaset Bilimi</option>
                        <option value="sosyalBilimler">Sosyal Bilimler</option>
                        <option value="spor">Spor</option>
                        <option value="tarih">Tarih</option>
                        <option value="yemekPisirme">Yemek Pişirme</option>
                    </select>
                </div>

                <h5>Kitap Değerlendirme Tipi:</h5>
                <div className='trade_sale'>
                    <label className="container">
                        Takasa aç
                        <input
                            type="radio"
                            name="tradeOption"
                            value="trade"
                            checked={tradeOption === 'trade'}
                            onChange={(e) => setTradeOption(e.target.value)}
                        />
                        <span className="checkmark"></span>
                    </label>
                    <label className="container">
                        Satışa aç
                        <input
                            type="radio"
                            name="tradeOption"
                            value="sale"
                            checked={tradeOption === 'sale'}
                            onChange={(e) => setTradeOption(e.target.value)}
                        />
                        <span className="checkmark"></span>
                    </label>
                </div>

                {tradeOption === 'sale' && (
                    <>
                        <h5>Fiyat giriniz:</h5>
                        <input type="text" placeholder="Fiyat (₺) giriniz." value={price} onChange={(e) => setPrice(e.target.value)} />
                    </>
                )}

                <h5>Kitap Fotoğrafı:</h5>
                <input type="file" onChange={(e) => setImage(URL.createObjectURL(e.target.files[0]))} />
                {image && <img src={image} style={{ width: '100px', height: '150px' }} />}
            </div>

            {error && <p className="errorMessage">{error}</p>}

            <button className='button_updateBook' onClick={handleUpdateBook}>Güncelle</button>
            <button className='button_cancel' onClick={() => navigate('/bookshelf')}>İptal</button>
        </div>
    )
}

export default UpdateInBookshelf