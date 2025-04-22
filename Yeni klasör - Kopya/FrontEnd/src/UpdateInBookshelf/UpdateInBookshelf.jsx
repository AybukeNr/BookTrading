import React, { useEffect, useState } from 'react'
import '../UpdateInBookshelf/UpdateInBookshelf.css'
import { useLocation, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import { instanceLibrary } from '../axios';
import axios from 'axios';

function UpdateInBookshelf() {
    const [, dispatch] = useStateValue();
    const navigate = useNavigate();
    const location = useLocation();
    const bookId = location.state?.id;

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [isbn, setIsbn] = useState('');
    const [publisher, setPublisher] = useState('');
    const [publishedDate, setPublishedDate] = useState('');
    const [category, setCategory] = useState('');
    const [image, setImage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const CLOUDINARY_URL =
        "https://api.cloudinary.com/v1_1/dlo9y7tcc/image/upload";
    const CLOUDINARY_UPLOAD_PRESET = "Images";


    useEffect(() => {
        if (bookId) {
            setLoading(true);
            instanceLibrary.get(`/getBookById?id=${bookId}`)
                .then(response => {
                    const book = response.data;
                    setTitle(book.title || '');
                    setAuthor(book.author || '');
                    setIsbn(book.isbn || '');
                    setPublisher(book.publisher || '');
                    setPublishedDate(book.publishedDate || '');
                    setCategory(book.category || '');
                    setImage(book.image || '');
                })
                .catch(err => {
                    setError('Kitap bilgileri alınamadı!');
                })
                .finally(() => {
                    setLoading(false);
                });
        } else {
            setError('Kitap ID bulunamadı!');
        }
    }, [bookId]);

    const handleUpdateBook = async () => {
        if (!title || !author || !isbn || !publisher || !publishedDate || !category || !image) {
            setError('Lütfen tüm alanları düzgünce doldurun!');
            return;
        }

        const userId = localStorage.getItem('userId');

        if (!userId) {
            setError('Kullanıcı kimliği bulunamadı. Lütfen tekrar giriş yapın.');
            return;
        }

        try {
            let imageUrl = "";
            if (image) {
                const formData = new FormData();
                formData.append("file", image);
                formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);

                try {
                    const uploadResponse = await axios.post(CLOUDINARY_URL, formData);
                    imageUrl = uploadResponse.data.secure_url;
                    console.log("Cloudinary'den dönen URL:", imageUrl);
                } catch (error) {
                    console.error("Resim yükleme hatası:", error);
                    setError("Resim yüklenirken hata oluştu. Lütfen tekrar deneyin.");
                    return;
                }
            }

            const updatedBook = {
                title,
                author,
                isbn,
                publisher,
                publishedDate,
                category,
                image: imageUrl || image,
            };

            try {
                const response = await instanceLibrary.put(`/update?id=${bookId}`, updatedBook, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (response.status === 200) {
                    dispatch({
                        type: 'UPDATE_IN_BOOKSHELF',
                        book: response.data,
                    });
                    navigate('/bookshelf', { state: { updated: true } });
                } else {
                    setError('Kitap güncellenirken bir hata oluştu.');
                }
            } catch (err) {
                setError('Sunucuya bağlanırken bir hata oluştu.');
            } finally {
                setLoading(false);
            }
        } catch {
            setError('Kitap eklenirken bir hata oluştu. Lütfen tekrar deneyin.');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className='updateBookInBookshelf'>
            <h2>Kitap Güncelle</h2>

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
                        <option value="Aile">Aile</option>
                        <option value="Bilgisayar">Bilgisayar</option>
                        <option value="Bilim">Bilim</option>
                        <option value="BilimKurgu">Bilim Kurgu</option>
                        <option value="Biyografi">Biyografi</option>
                        <option value="CizgiRoman">Çizgi Roman</option>
                        <option value="Din">Din</option>
                        <option value="Drama">Drama</option>
                        <option value="Eğitim">Eğitim</option>
                        <option value="Felsefe">Felsefe</option>
                        <option value="GençlikKurgu">Gençlik Kurgu</option>
                        <option value="İs">İş</option>
                        <option value="KişiselGelişim">Kişisel Gelişim</option>
                        <option value="Kurgu">Kurgu</option>
                        <option value="Psikoloji">Psikoloji</option>
                        <option value="Şiir">Şiir</option>
                        <option value="SiyasetBilimi">Siyaset Bilimi</option>
                        <option value="SosyalBilimler">Sosyal Bilimler</option>
                        <option value="Spor">Spor</option>
                        <option value="Tarih">Tarih</option>
                        <option value="YemekPisirme">Yemek Pişirme</option>
                    </select>
                </div>

                <h5>Kitap Fotoğrafı:</h5>
                <input type="file" onChange={(e) => setImage(e.target.files[0])} />
                {image && (typeof image === "string" ? (
                    <img src={image} style={{ width: '100px', height: '150px' }} />
                ) : (
                    <img src={URL.createObjectURL(image)} style={{ width: '100px', height: '150px' }} />
                ))}
            </div>

            {error && <p className="errorMessage">{error}</p>}

            <button className='button_updateBook' onClick={handleUpdateBook} disabled={loading}>
                {loading ? 'Güncelleniyor...' : 'Güncelle'}</button>
            <button className='button_cancel' onClick={() => navigate('/bookshelf')}>İptal</button>
        </div>
    )
}

export default UpdateInBookshelf