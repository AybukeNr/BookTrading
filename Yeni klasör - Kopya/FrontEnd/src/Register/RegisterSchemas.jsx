import * as yup from 'yup'

export const categories = [
    "Bilgisayar Bilimi",
    "Bilim",
    "Biyografi",
    "Çizgi Roman",
    "Din",
    "Dram",
    "Eğitim",
    "Felsefe", ,
    "Gençlik Romanları",
    "İş",
    "Kişisel Gelişim",
    "Kurgu",
    "Psikoloji",
    "Şiir",
    "Siyaset Bilimi",
    "Toplum Bilimi",
    "Spor",
    "Tarih",
    "Yemek"
];

export const registerSchemas = yup.object().shape({
    firstname: yup.string().required('Ad girmek zorunlu').max(15, 'Maksimum 15 karakterde olmalı'),
    lastname: yup.string().required('Soyad girmek zorunlu').max(20, 'Maksimum 20 karakterde olmalı'),
    email: yup.string().email('Geçerli email adresi giriniz').required('Email adresi zorunlu'),
    telephone: yup.string().required('Telefon girilmesi zorunlu').max(11, 'Telefon numarasını 11 haneli girin').min(11, 'Telefon numarasını 11 haneli girin'),
    address: yup.string().required('Adres girmek zorunlu').max(175, 'Maksimum 175 karakterde olmalı'),
    iban: yup.string().required('Lütfen IBAN giriniz.').max(26, 'IBAN numarası TR ile başlamalı ve 26 karakter girilmeli.').min(26, 'IBAN numarası TR ile başlamalı ve 26 karakter girilmeli.'),
    password: yup.string().required('Şifre alanı zorunlu').min(6, 'Şifre minimum 6 karakter içermeli'),
    confirmPassword: yup.string().required('Şifre tekrarı zorunlu').oneOf([yup.ref('password')], 'Şifreler eşleşmiyor'),
    categories: yup.array().min(3, "Tam olarak 3 kategori seçmelisiniz.").max(3, "Tam olarak 3 kategori seçmelisiniz.").required("Kategori seçimi gereklidir.")
})
