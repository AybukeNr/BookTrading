import React from 'react';
import '../Login/Login.css';
import { Link, useNavigate } from 'react-router-dom';
import { instanceAuth } from '../axios';
import { setAuthToken } from '../auth';
import { jwtDecode } from "jwt-decode";
import { useStateValue } from '../StateProvider';
import { useFormik } from 'formik';
import { loginSchema } from './LoginSchemas';

function Login(props) {
    const navigate = useNavigate();
    const [, dispatch] = useStateValue();

    const formik = useFormik({
        initialValues: {
            email: '',
            password: '',
        },
        validationSchema: loginSchema,
        onSubmit: async (values, { setSubmitting, setErrors, resetForm }) => {
            try {
                const response = await instanceAuth.post('/login', {
                    email: values.email,
                    password: values.password,
                });

                const token = response.data.token;
                setAuthToken(token);
                const decodedToken = jwtDecode(token);
                console.log("Token İçeriği:", decodedToken);

                const userId = decodedToken.id;
                localStorage.setItem('userId', userId);

                dispatch({
                    type: 'SET_USER',
                    user: decodedToken,
                });

                alert('Giriş Başarılı');
                props.setIsAuthenticated(true);
                resetForm();
                navigate('/');
            } catch (error) {
                if (error.response) {
                    setErrors({ email: error.response.data.message || 'Giriş başarısız' });
                } else if (error.request) {
                    setErrors({ email: 'Sunucuya ulaşılamıyor. İnternet bağlantınızı kontrol edin.' });
                } else {
                    setErrors({ email: 'Bilinmeyen bir hata oluştu.' });
                }
            } finally {
                setSubmitting(false);
            }
        },
    });

    return (
        <div className='login'>
            <Link to='/'>
                <img className='login_logo' src="https://i.hizliresim.com/obkwl66.png" alt="logo" />
            </Link>

            <div className="login_container">
                <h1>Giriş Yap</h1>
                <form onSubmit={formik.handleSubmit}>
                    <input
                        type="email"
                        name="email"
                        placeholder="E-mail giriniz"
                        value={formik.values.email}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                    {formik.touched.email && formik.errors.email && (
                        <p className="error_message">{formik.errors.email}</p>
                    )}

                    <input
                        type="password"
                        name="password"
                        placeholder="Şifre giriniz"
                        value={formik.values.password}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                    {formik.touched.password && formik.errors.password && (
                        <p className="error_message">{formik.errors.password}</p>
                    )}

                    <button className='login_signInButton' type='submit' disabled={formik.isSubmitting}>
                        {formik.isSubmitting ? 'Giriş Yapılıyor...' : 'Giriş yap'}
                    </button>
                </form>

                <button className='login_registerButton' onClick={() => navigate('/register')}>
                    Hesabınız mı yok?
                </button>
            </div>
        </div>
    );
}

export default Login;
