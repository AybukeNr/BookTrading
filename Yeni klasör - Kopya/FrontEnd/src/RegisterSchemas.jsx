import * as yup from 'yup'

export const registerSchemas = yup.object().shape({
    firstname: yup.string().required('Ad girmek zorunlu').max(15, 'Maksimum 15 karakterde olmalı'),
    lastname: yup.string().required('Soyad girmek zorunlu').max(20, 'Maksimum 20 karakterde olmalı'),
    email: yup.string().email('Geçerli email adresi giriniz').required('Email adresi zorunlu'),
    telephone: yup.string().required('Telefon girilmesi zorunlu').max(11,'Telefon numarasını 11 haneli girin').min(11,'Telefon numarasını 11 haneli girin'),
    address: yup.string().required('Adres girmek zorunlu').max(150,'Maksimum 150 karakterde olmalı'),
    password: yup.string().required('Şifre alanı zorunlu').min(6,'Şifre minimum 6 karakter içermeli'),
    confirmPassword: yup.string().required('Şifre tekrarı zorunlu').oneOf([yup.ref('password')], 'Şifreler eşleşmiyor')
})
