import React, { useEffect, useState } from 'react'
import '../Account/Account.css'
import { useLocation, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';

function Account() {
    const [{ user }, dispatch] = useStateValue();
    const navigate = useNavigate();

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [telephone, setTelephone] = useState('');
    const [iban, setIban] = useState('');
    const [address, setAddress] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleUpdateAccount = () => {
        if (!firstname || !lastname || !email || !telephone || !iban || !address || !password) {
            setError('Lütfen düzgünce doldurun!');
            return;
        }

        dispatch({
            type: 'UPDATE_USER_ACCOUNT',
            account: {
                id: user.id,
                firstname,
                lastname,
                email,
                telephone,
                iban,
                address,
                password,
            },
        });
        navigate('/myAccount')
    };

    // useEffect(() => {
    //     if (user) {
    //         setFirstname(user.firstname || '');
    //         setLastname(user.lastname || '');
    //         setEmail(user.email || '');
    //         setTelephone(user.telephone || '');
    //         setAddress(user.address || '');
    //         setIban(user.iban || '');
    //         setPassword(user.password || '');
    //     }
    // }, [user]);

    return (
        <div className='profile'>
            <h2>Hesap Bilgilerim</h2>
            <form className='profile_details'>

                <h5>Ad: </h5>
                <input type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} required/>

                <h5>Soyad: </h5>
                <input type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} required/>

                <h5>E-Mail: </h5>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>

                <h5>Telefon No: </h5>
                <input type="tel" value={telephone} onChange={(e) => setTelephone(e.target.value)} required/>

                <h5>IBAN No: </h5>
                <input type="text" value={iban} onChange={(e) => setIban(e.target.value)} required/>

                <h5>Adres: </h5>
                <textarea type="text" value={address} onChange={(e) => setAddress(e.target.value)} required/>

                <h5>Şifre: </h5>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>

            </form>

            {error && <p className="errorMessage">{error}</p>}

            <div>
                <button className='button_updateAccount' onClick={handleUpdateAccount}>Güncelle</button>
                <button className='button_cancelUpdate' onClick={() => navigate('/myAccount')}>İptal</button>
            </div>
        </div>
    )
}

export default Account