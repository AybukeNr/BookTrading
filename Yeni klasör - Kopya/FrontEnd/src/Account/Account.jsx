import React, { useEffect, useState } from 'react';
import '../Account/Account.css';
import { useNavigate } from 'react-router-dom';
import { instanceUser } from '../axios';
import { useFormik } from 'formik';
import { accountSchemas } from './AccountSchemas';

function Account() {
    const navigate = useNavigate();
    const [initialValues, setInitialValues] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchUserData = async () => {
            setLoading(true);
            try {
                const response = await instanceUser.get(`/getUserById?id=${userId}`);
                const data = response.data;

                setInitialValues({
                    firstname: data.firstName || '',
                    lastname: data.lastName || '',
                    email: data.mailAddress || '',
                    telephone: data.phoneNumber || '',
                    iban: data.iban || '',
                    address: data.address || '',
                    password: data.password || '',
                });
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

    const formik = useFormik({
        initialValues: initialValues || {
            firstname: '',
            lastname: '',
            email: '',
            telephone: '',
            iban: '',
            address: '',
            password: '',
        },
        enableReinitialize: true,
        validationSchema: accountSchemas,
        onSubmit: async (values) => {
            try {
                await instanceUser.put(`/updateUser/${userId}`, {
                    userId,
                    firstName: values.firstname,
                    lastName: values.lastname,
                    mailAddress: values.email,
                    phoneNumber: values.telephone,
                    iban: values.iban,
                    address: values.address,
                    password: values.password,
                });

                alert("Hesap başarıyla güncellendi!");
                setTimeout(() => navigate('/'), 1000);
            } catch (err) {
                setError("Hesap güncellenirken hata oluştu!");
            }
        },
    });

    if (loading || !initialValues) {
        return <p>Yükleniyor...</p>;
    }

    return (
        <div className='profile'>
            <h2>Hesap Bilgilerim</h2>

            {error && <p className="errorMessage">{error}</p>}

            <form className='profile_details' onSubmit={formik.handleSubmit}>
                <h5>Ad:</h5>
                <input
                    type="text"
                    name="firstname"
                    value={formik.values.firstname}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                />
                {formik.touched.firstname && formik.errors.firstname && (
                    <p className="errors">{formik.errors.firstname}</p>
                )}

                <h5>Soyad:</h5>
                <input
                    type="text"
                    name="lastname"
                    value={formik.values.lastname}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                />
                {formik.touched.lastname && formik.errors.lastname && (
                    <p className="errors">{formik.errors.lastname}</p>
                )}

                <h5>E-Mail:</h5>
                <input
                    type="email"
                    name="email"
                    value={formik.values.email}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                />
                {formik.touched.email && formik.errors.email && (
                    <p className="errors">{formik.errors.email}</p>
                )}

                <h5>Telefon No:</h5>
                <input
                    type="tel"
                    name="telephone"
                    value={formik.values.telephone}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    maxLength={11} size={11}
                />
                {formik.touched.telephone && formik.errors.telephone && (
                    <p className="errors">{formik.errors.telephone}</p>
                )}

                <h5>IBAN No:</h5>
                <input
                    type="text"
                    name="iban"
                    value={formik.values.iban}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    maxLength={26} size={26}
                />
                {formik.touched.iban && formik.errors.iban && (
                    <p className="errors">{formik.errors.iban}</p>
                )}

                <h5>Adres:</h5>
                <textarea
                    name="address"
                    value={formik.values.address}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    maxLength={175} size={175}
                />
                {formik.touched.address && formik.errors.address && (
                    <p className="errors">{formik.errors.address}</p>
                )}

                <h5>Şifre:</h5>
                <input
                    type="password"
                    name="password"
                    value={formik.values.password}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    minLength={6}
                />
                {formik.touched.password && formik.errors.password && (
                    <p className="errors">{formik.errors.password}</p>
                )}

                <div>
                    <button type="submit" className='button_updateAccount'>
                        Güncelle
                    </button>
                    <button type="button" className='button_cancelUpdate' onClick={() => navigate('/myAccount')}>
                        İptal
                    </button>
                </div>
            </form>
        </div>
    );
}

export default Account;
