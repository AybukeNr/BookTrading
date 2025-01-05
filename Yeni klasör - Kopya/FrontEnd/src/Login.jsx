import React, { useState } from 'react'
import './Login.css'
import { Link, useNavigate } from 'react-router-dom'
import instance from './axios';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    // const ProtectedRoute = ({ children }) => {
    //     const token = localStorage.getItem('authToken');

    //     if (!token) {
    //         return <Navigate to='/login' />
    //     }

    //     return children;
    // }

    const user = async () => {
        const token = localStorage.getItem('authToken');

        try {
            const response = await instance.get('http://localhost:5173/user', {
                headers: { Authorization: `Bearer ${token}` },
            });
            console.log(response.data);
        } catch (error) {
            console.log(error);
            alert('Kullanıcı bilgisi alınamadı.');
        }
    }

    const signIn = async (e) => {
        e.preventDefault();
        
        if (!email || !password) {
            alert('Lütfen e-mail ve şifre giriniz.');
            return;
        }

        try {
            const response = await instance.post('/auth/login', {
                email: email,
                password: password,
            });

            const token = response.data.token;
            localStorage.setItem('authToken', token);

            alert('Giriş Başarılı');
            navigate('/');
        } catch (error) {
            console.log(error);
            alert('Giriş Başarısız')
        }
    }

    return (
        <div className='login'>
            <Link to='/'>
                <img className='login_logo' src="https://i.hizliresim.com/obkwl66.png" alt="" />
            </Link>

            <div className="login_container">
                <h1>Giriş Yap</h1>
                <form>
                    <h5>E-mail giriniz:</h5>
                    <input type="email" value={email} onChange={e => setEmail(e.target.value)} />

                    <h5>Şifre giriniz:</h5>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} />

                    <button className='login_signInButton' type='submit' onClick={signIn}>Giriş yap</button>
                </form>

                <button className='login_registerButton' onClick={() => navigate('/register')}>Hesabınız mı yok?</button>
            </div>
        </div>
    );
}
export default Login