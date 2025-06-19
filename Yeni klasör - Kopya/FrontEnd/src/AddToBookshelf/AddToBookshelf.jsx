import React, { useState } from 'react'
import '../AddToBookshelf/AddToBookshelf.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import { instanceLibrary } from '../axios';
import axios from "axios";

function AddToBookshelf() {
    const [, dispatch] = useStateValue();
    const navigate = useNavigate();

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [isbn, setIsbn] = useState('');
    const [publisher, setPublisher] = useState('');
    const [publishedDate, setPublishedDate] = useState('');
    const [category, setCategory] = useState('');
    const [description, setDescription] = useState('');
    const [condition, setCondition] = useState('');
    const [image, setImage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const CLOUDINARY_URL =
        "https://api.cloudinary.com/v1_1/dlo9y7tcc/image/upload";
    const CLOUDINARY_UPLOAD_PRESET = "Images";

    const handleAddBook = async () => {
        if (!title || !author || !isbn || !publisher || !publishedDate || !description || !condition || !category || !image) {
            setError('Lütfen tüm alanları doldurun!');
            return;
        }

        setLoading(true);
        setError('');

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

            const bookData = {
                ownerId: userId,
                title,
                author,
                isbn,
                publisher,
                publishedDate,
                description,
                condition,
                category,
                image: imageUrl,
            };

            try {
                const response = await instanceLibrary.post('/createBook', bookData, {
                    headers: { 'Content-Type': 'application/json' }
                });

                if (response.status === 201 || response.status === 200) {
                    dispatch({
                        type: 'ADD_TO_BOOKSHELF',
                        book: response.data
                    });

                    setTitle('');
                    setAuthor('');
                    setIsbn('');
                    setPublisher('');
                    setPublishedDate('');
                    setDescription('');
                    setCondition('');
                    setCategory('');
                    setImage('');
                    setError('');

                    setTimeout(() => {
                        navigate('/bookshelf');
                    }, 200);
                }
            } catch {
                setError('Kitap eklenirken bir hata oluştu. Lütfen tekrar deneyin.');
            } finally {
                setLoading(false)
            }
        } catch (error) {
            console.error('Kitap eklenirken hata oluştu:', error);
            setError('Kitap eklenirken bir hata oluştu. Lütfen tekrar deneyin.');
        } finally {
            setLoading(false);
        }
    }
    return (
        <div className='AddBookToBookshelf'>
            <h2>Kitap Ekle</h2>

            {loading && <p>Kitap ekleniyor...</p>}

            <div className="book">
                <h5>Kitap Başlığı:</h5>
                <input type="text" placeholder="Kitabın başlığını giriniz" value={title} onChange={(e) => setTitle(e.target.value)} />

                <h5>Kitap Yazarı:</h5>
                <input type="text" placeholder="Kitabın yazarını giriniz" value={author} onChange={(e) => setAuthor(e.target.value)} />

                <h5>Kitap ISBN:</h5>
                <input type="text" placeholder="Kitap ISBN giriniz" value={isbn} onChange={(e) => setIsbn(e.target.value)} maxLength={13}/>

                <h5>Kitap Yayınevi:</h5>
                <input type="text" placeholder="Kitabın yayınevini giriniz" value={publisher} onChange={(e) => setPublisher(e.target.value)} />

                <h5>Kitap Yayın Tarihi:</h5>
                <input type="text" placeholder="Kitabın yayın yılını giriniz" value={publishedDate} onChange={(e) => setPublishedDate(e.target.value)} />

                <h5>Kitap Açıklaması:</h5>
                <input type="text" placeholder="Kitabın açıklamasını giriniz" value={description} onChange={(e) => setDescription(e.target.value)} maxLength={255} />

                <h5>Kitap Durumunu Seçiniz:</h5>
                <div className='book_condition'>
                    <select id="condition" value={condition} onChange={(e) => setCondition(e.target.value)}>
                        <option value="">Bir durum seçin</option>
                        <option value="Yeni">Yeni</option>
                        <option value="İyi">İyi</option>    
                        <option value="Kabul Edilebilir">Kabul Edilebilir</option>
                        <option value="Kötü">Kötü</option>
                    </select>
                </div>

                <h5>Kitap Kategorisi Seçiniz:</h5>
                <div className='book_category'>
                    <select id="category" value={category} onChange={(e) => setCategory(e.target.value)}>
                        <option value="">Bir kategori seçin</option>
                        <option value="Bilgisayar Bilimi">Bilgisayar Bilimi</option>
                        <option value="Bilim">Bilim</option>
                        <option value="Biyografi">Biyografi</option>
                        <option value="Çizgi Roman">Çizgi Roman</option>
                        <option value="Din">Din</option>
                        <option value="Dram">Dram</option>
                        <option value="Eğitim">Eğitim</option>
                        <option value="Felsefe">Felsefe</option>
                        <option value="Gençlik Romanları">Gençlik Romanları</option>
                        <option value="İş">İş</option>
                        <option value="Kişisel Gelişim">Kişisel Gelişim</option>
                        <option value="Kurgu">Kurgu</option>
                        <option value="Psikoloji">Psikoloji</option>
                        <option value="Şiir">Şiir</option>
                        <option value="Toplum Bilimi">Toplum Bilimi</option>
                        <option value="Spor">Spor</option>
                        <option value="Tarih">Tarih</option>
                        <option value="Yemek">Yemek</option>
                    </select>
                </div>

                <h5>Kitap Fotoğrafı:</h5>
                <input type="file" onChange={(e) => setImage(e.target.files[0])} />
            </div>

            {error && <p className="error-message">{error}</p>}

            <button className='button_addBook' onClick={handleAddBook} disabled={loading}>
                {loading ? 'Ekleniyor...' : 'Ekle'} </button>
            <button className='button_cancel' onClick={() => navigate('/bookshelf')} disabled={loading}>
                İptal</button>
        </div>
    )
}
export default AddToBookshelf