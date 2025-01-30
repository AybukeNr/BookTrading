import React, { useState } from 'react'
import './Register.css'
import { Link, useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';
import { categories, registerSchemas } from './RegisterSchemas';

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

    const handleCategoryChange = (category) => {
        const currentCategories = values.categories;
        if (currentCategories.includes(category)) {
            const updatedCategories = currentCategories.filter((cat) => cat !== category);
            handleChange({ target: { name: 'categories', value: updatedCategories } });
        } else if (currentCategories.length < 3) {
            handleChange({ target: { name: 'categories', value: [...currentCategories, category] } });
        }
    };

    const submit = (values, action) => {
        setTimeout(() => {
            action.resetForm();
            navigate('/login');
        }, 400);
    }

    const { values, errors, handleSubmit, handleChange, handleBlur, touched } = useFormik({
        initialValues: {
            firstname: '',
            lastname: '',
            email: '',
            telephone: '',
            address: '',
            password: '',
            confirmPassword: '',
            categories: [],
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

                    <input type="text" id='firstname' placeholder='Adınızı giriniz' value={values.firstname} onChange={handleChange} onBlur={handleBlur} />
                    {errors.firstname && touched.firstname && <p className='errors'>{errors.firstname}</p>}

                    <input type="text" id='lastname' placeholder='Soyadınızı giriniz' value={values.lastname} onChange={handleChange} onBlur={handleBlur} />
                    {errors.lastname && touched.lastname && <p className='errors'>{errors.lastname}</p>}

                    <input type="email" id='email' placeholder='Email giriniz' value={values.email} onChange={handleChange} onBlur={handleBlur} />
                    {errors.email && touched.email && <p className='errors'>{errors.email}</p>}

                    <input type="tel" id='telephone' placeholder='Telefon numarası giriniz (Örn: 05XX XXX XXXX)' value={values.telephone} onChange={handleChange} onBlur={handleBlur} maxLength={11} size={11} />
                    {errors.telephone && touched.telephone && <p className='errors'>{errors.telephone}</p>}

                    <textarea type="text" id='address' placeholder='Adres giriniz' value={values.address} onChange={handleChange} onBlur={handleBlur} />
                    {errors.address && touched.address && <p className='errors'>{errors.address}</p>}

                    <input type="password" id='password' placeholder='Şifre giriniz' value={values.password} onChange={handleChange} onBlur={handleBlur} minLength={6} />
                    {errors.password && touched.password && <p className='errors'>{errors.password}</p>}

                    <input type="password" id='confirmPassword' placeholder='Şifreyi tekrar giriniz' value={values.confirmPassword} onChange={handleChange} onBlur={handleBlur} minLength={6} />
                    {errors.confirmPassword && touched.confirmPassword && <p className='errors'>{errors.confirmPassword}</p>}

                    <div className="category-selection">
                        <h4>3 Kategori Seçiniz:</h4>
                        <div className="categories">
                            {categories.map((category) => (
                                <div key={category} className="category">
                                    <input type="checkbox" id={category} value={category} checked={values.categories.includes(category)} onChange={() => handleCategoryChange(category)} onBlur={handleBlur} disabled={!values.categories.includes(category) && values.categories.length >= 3} />
                                    <label htmlFor={category}>{category}</label>
                                </div>
                            ))}
                        </div>
                        {errors.categories && touched.categories && <p className='errors'>{errors.categories}</p>}
                    </div>

                    <p style={{ marginTop: 10 }}>"Devam ederek, <a href="#" target="_blank">Kullanım Koşulları</a> ve  <a href="#" target="_blank">Gizlilik Politikası</a>'nı okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>

                    <button className='register_button' type='submit' >Kaydol</button>
                </form>


            </div>
        </div>
    )
}

export default Register