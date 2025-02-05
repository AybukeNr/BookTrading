import React from 'react'
import './UserDetails.css'
import { Rating } from '@mui/material'
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import { useNavigate } from 'react-router-dom';

function UserDetails() {
    // const navigate = useNavigate();

    return (
        <div className='userDetails'>
            <div className="userInfo">
                <AccountCircleOutlinedIcon />
                <p>Ad Soyad</p>
                <p>Kullanıcı Değerlendirmesi: <Rating className='rating' /></p>
                <p>Email</p>
                <p>Telefon</p>
            </div>
            <div className="userAds">
                <h3>Kullanıcının İlanları:</h3>
                <div className='AdsBooks'>
                    <div className="bookInfo">
                        <p>ISBN/Başlık-Yazar/Yayınevi-Yayın tarihi/Kategori</p>

                            <p className='bookPrice'>
                                <strong>Fiyat</strong>
                                <small>₺</small>
                            </p>
                            {/* <p className='bookTrade'>Takasa açık</p> */}

                    </div>

                    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRXJGIvVqD7poyHq7VGM6hVcnGBRw6tKvsGv-FYam-rSXlmlufSY7H_-ehfRUNKYZ_ods&usqp=CAU" alt='' />
                </div>
            </div>
        </div>
    )
}

export default UserDetails