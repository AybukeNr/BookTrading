import React, { useState } from 'react'
import './AddToBookshelf.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';

function AddToBookshelf() {
    const [, dispatch] = useStateValue();
    const navigate = useNavigate();

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [isbn, setIsbn] = useState('');
    const [publisher, setPublisher] = useState('');
    const [publishedDate, setPublishedDate] = useState('');
    const [category, setCategory] = useState('');
    const [price, setPrice] = useState('');
    const [image, setImage] = useState('');
    const [tradeOption, setTradeOption] = useState('');
    const [error, setError] = useState('');


    const handleAddBook = () => {
        if (!title || !author || !isbn || !publisher || !publishedDate || !category || !tradeOption || (tradeOption === 'sale' && !price)) {
            setError('Lütfen tüm alanları doldurun!');
            return;
        }
        
        dispatch({
            type: 'ADD_TO_BOOKSHELF',
            book: {
                id: Date.now(),
                title: title,
                author: author,
                isbn: isbn,
                publisher: publisher,
                publishedDate: publishedDate,
                category: category,
                price: tradeOption === 'sale' ? price : null,
                image: image,
            },
        });
        setTitle('');
        setAuthor('');
        setIsbn('');
        setPublisher('');
        setPublishedDate('');
        setCategory('');
        setPrice('');
        setImage('');
        setTradeOption('');
        setError('');
        
        setTimeout(() => {
            navigate('/bookshelf')
        }, 200);
    };


    return (
        <div className='AddBookToBookshelf'>
            <h2>Kitap Ekle</h2>

            {error && <p className="error-message">{error}</p>}            

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
            </div>

            {error && <p className="error-message">{error}</p>}

            <button className='button_addBook' onClick={handleAddBook}>Ekle</button>
            <button className='button_cancel' onClick={() => navigate('/bookshelf')}>İptal</button>
        </div>

    )
}

export default AddToBookshelf