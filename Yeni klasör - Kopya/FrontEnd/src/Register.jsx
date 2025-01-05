import React from 'react'
import './Register.css'
import { Link, useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';
import { registerSchemas } from './RegisterSchemas';

function Register() {
    const navigate = useNavigate();
        // const register = async (e) => {
        //     e.preventDefault();
    
        //     if (!email || !password) {
        //         alert('Lütfen e-mail ve şifre giriniz.');
        //         return;
        //     }
    
        //     try {
        //         await instance.post('/auth/register', {
        //             email: email,
        //             password: password,
        //         });
        //         alert('Kayıt Başarılı');
        //     } catch (error) {
        //         console.log(error);
        //         alert('Kayıt Başarısız')
        //     }
        // }
    
    const submit = (values, action) => {
        setTimeout(() => {
            action.resetForm();
            navigate('/login');
        }, 400);
    }

    const { values, errors, handleSubmit, handleChange, handleBlur, touched} = useFormik({
        initialValues: {
            firstname: '',
            lastname: '',
            email: '',
            telephone: '',
            address: '',
            password: '',
            confirmPassword: ''
        },
        validationSchema: registerSchemas,
        onSubmit: submit

    });
    return (
        <div className='register'>
            <Link to='/'>
                <img className='register_logo' src="https://i.hizliresim.com/obkwl66.png" alt="" />
            </Link>
            <div className="register_container">
                <h1>Kaydol</h1>
                <form onSubmit={handleSubmit}>
                    
                    <input type="text" id='firstname' placeholder='Adınızı giriniz' value={values.firstname} onChange={handleChange} onBlur={handleBlur}/>
                    {errors.firstname && touched.firstname && <p className='errors'>{errors.firstname}</p>}

                    <input type="text" id='lastname' placeholder='Soyadınızı giriniz' value={values.lastname} onChange={handleChange} onBlur={handleBlur}/>
                    {errors.lastname && touched.lastname && <p className='errors'>{errors.lastname}</p>}

                    <input type="email" id='email' placeholder='Email giriniz' value={values.email} onChange={handleChange} onBlur={handleBlur}/>
                    {errors.email && touched.email && <p className='errors'>{errors.email}</p>}

                    <input type="tel" id='telephone' placeholder='Telefon numarası giriniz (Örn: 05XX XXX XXXX)' value={values.telephone} onChange={handleChange} onBlur={handleBlur} maxLength={11} size={11}/>
                    {errors.telephone && touched.telephone && <p className='errors'>{errors.telephone}</p>}

                    <textarea type="text" id='address' placeholder='Adres giriniz' value={values.address} onChange={handleChange} onBlur={handleBlur}/>
                    {errors.address && touched.address && <p className='errors'>{errors.address}</p>}

                    <input type="password" id='password' placeholder='Şifre giriniz' value={values.password} onChange={handleChange} onBlur={handleBlur} minLength={6}/>
                    {errors.password && touched.password && <p className='errors'>{errors.password}</p>}

                    <input type="password" id='confirmPassword' placeholder='Şifreyi tekrar giriniz' value={values.confirmPassword} onChange={handleChange} onBlur={handleBlur} minLength={6}/>
                    {errors.confirmPassword && touched.confirmPassword && <p className='errors'>{errors.confirmPassword}</p>}

                    <p>"Devam ederek, <a href="#" target="_blank">Kullanım Koşulları</a> ve  <a href="#" target="_blank">Gizlilik Politikası</a>'nı okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>

                    <button className='register_button' type='submit' >Kaydol</button>
                </form>


            </div>
        </div>
    )
}

export default Register