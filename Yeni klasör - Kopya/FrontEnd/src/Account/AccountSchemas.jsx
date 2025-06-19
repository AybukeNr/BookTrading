import * as yup from 'yup';

export const accountSchemas = yup.object({
    firstname: yup.string().required('Ad boş olamaz').max(15, 'Maksimum 15 karakterde olmalı'),
    lastname: yup.string().required('Soyad boş olamaz').max(20, 'Maksimum 20 karakterde olmalı'),
    email: yup.string().email('Geçerli bir e-posta girin').required('E-posta boş olamaz'),
    telephone: yup.string()
        .matches(/^[0-9]{11,}$/, 'Telefon numarası11 rakamdan oluşmalı')
        .required('Telefon numarası boş olamaz'),
    iban: yup.string()
        .matches(/^TR[0-9]{24}$/, 'IBAN TR ile başlamalı ve 26 karakter olmalı')
        .required('IBAN boş olamaz'),
    address: yup.string().required('Adres boş olamaz').max(175, 'Maksimum 175 karakterde olmalı'),
    password: yup.string().min(6, 'Şifre en az 6 karakter olmalı').required('Şifre boş olamaz'),
});