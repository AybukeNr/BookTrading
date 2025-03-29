import React, { useEffect, useState } from 'react'
import '../Account/Account.css'
import { useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import { instanceUser } from '../axios';

function Account() {
    const navigate = useNavigate();

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [telephone, setTelephone] = useState('');
    const [iban, setIban] = useState('');
    const [address, setAddress] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
  

    const userId = localStorage.getItem('userId'); 

    useEffect(() => {
        const fetchUserData = async () => {
            setLoading(true);
            try {
                const response = await instanceUser.get(`/getUserById?id=${userId}`);
                const data = response.data;
            
                setFirstname(data.firstName || '');
                setLastname(data.lastName || '');
                setEmail(data.mailAddress || '');
                setTelephone(data.phoneNumber || ''); 
                setIban(data.iban || '');
                setAddress(data.address || '');
                setPassword(data.password || '');
            } catch (err) {
                setError("Kullanıcı bilgileri alınamadı!");
            } finally {
                setLoading(false);
            }
        };

        if (userId) {
            fetchUserData();
        } else {
            setError("Kullanıcı ID bulunamadı!");
        }
    }, [userId]);

    const handleUpdateAccount = async () => {
        if (!firstname || !lastname || !email || !telephone || !iban || !address || !password) {
            setError('Lütfen tüm alanları doldurun!');
            return;
        }

        try {
            const response = await instanceUser.put(`/updateUser/${userId}`, {
            
                firstName: firstname,
                lastName: lastname,
                mailAddress: email,
                phoneNumber: telephone,
                iban: iban,
                address: address,
                password: password,
            });

            alert("Hesap başarıyla güncellendi!");
            navigate('/myAccount');
        } catch (err) {
            setError("Hesap güncellenirken hata oluştu!");
        }
    };

    return (
        <div className='profile'>
            <h2>Hesap Bilgilerim</h2>
            {loading ? <p>Yükleniyor...</p> : (
                <form className='profile_details'>

                    <h5>Ad: </h5>
                    <input type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} required />

                    <h5>Soyad: </h5>
                    <input type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} required />

                    <h5>E-Mail: </h5>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

                    <h5>Telefon No: </h5>
                    <input type="tel" value={telephone} onChange={(e) => setTelephone(e.target.value)} required />

                    <h5>IBAN No: </h5>
                    <input type="text" value={iban} onChange={(e) => setIban(e.target.value)} required />

                    <h5>Adres: </h5>
                    <textarea type="text" value={address} onChange={(e) => setAddress(e.target.value)} required />

                    <h5>Şifre: </h5>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />

                </form>
            )}
            {error && <p className="errorMessage">{error}</p>}

            <div>
                <button className='button_updateAccount' onClick={handleUpdateAccount} disabled={loading}>
                    {loading ? "Güncelleniyor..." : "Güncelle"}
                </button>
                <button className='button_cancelUpdate' onClick={() => navigate('/myAccount')}>İptal</button>
            </div>
        </div>
    );
}
   
export default Account