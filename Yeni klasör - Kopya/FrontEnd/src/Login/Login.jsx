import React, { useState } from 'react'
import '../Login/Login.css'
import { Link, useNavigate } from 'react-router-dom'
import { instanceAuth } from '../axios';
import { setAuthToken } from '../auth';

function Login(props) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // const ProtectedRoute = ({ children }) => {
    //     const token = localStorage.getItem('authToken');

    //     if (!token) {
    //         return <Navigate to='/login' />
    //     }

    //     return children;
    // }

    // const user = async () => {
    //     const token = getAuthToken();

    //     try {
    //         const response = await axios.get('http://localhost:8080/api/v1/users', {
    //             headers: { Authorization: `Bearer ${token}` },
    //         });
    //         console.log(response.data);
    //     } catch (error) {
    //         console.log(error);
    //         alert('Kullanıcı bilgisi alınamadı.');
    //     }
    // }

    const signIn = async (e) => {
        e.preventDefault();
        setError('');

        if (!email || !password) {
            // alert('Lütfen e-mail ve şifre giriniz.');
            setError('Lütfen tüm alanları düzgünce doldurun!');
            return;
        }

        try {
            const response = await instanceAuth.post('/login', {
                email,
                password,
            });

            const token = response.data.token;
            setAuthToken(token);

            alert('Giriş Başarılı');
            props.setIsAuthenticated(true);
            navigate('/');
        } catch (error) {
            if (error.response) {
                setError(error.response.data.message || 'Giriş başarısız. Lütfen tekrar deneyin.');
            } else if (error.request) {
                setError('Sunucuya ulaşılamıyor. Lütfen internet bağlantınızı kontrol edin.');
            } else {
                setError('Bilinmeyen bir hata oluştu.');
            }
        }
    }

    return (
        <div className='login'>
            <Link to='/'>
                <img className='login_logo' src="https://i.hizliresim.com/obkwl66.png" alt="" />
            </Link>

            <div className="login_container">
                <h1>Giriş Yap</h1>
                <form onSubmit={signIn}>
                    
                    <input type="email" placeholder='E-mail giriniz' value={email} onChange={e => setEmail(e.target.value)} />

                    <input type="password" placeholder='Şifre giriniz' value={password} onChange={e => setPassword(e.target.value)} />

                    {error && <p className="error_message">{error}</p>}

                    <button className='login_signInButton' type='submit'>Giriş yap</button>
                </form>

                <button className='login_registerButton' onClick={() => navigate('/register')}>Hesabınız mı yok?</button>
            </div>
        </div>
    );
}
export default Login