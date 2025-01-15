import React from 'react'
import './Account.css'

function Account() {
    return (
        <div className='profile'>
            <h2>Hesap Bilgilerim</h2>
            <form className='profile_details'>

                <h5>AD: </h5>
                <input type="text" />

                <h5>SOYAD: </h5>
                <input type="text" />

                <h5>EMAIL: </h5>
                <input type="email" />

                <h5>TELEFON NO: </h5> 
                <input type="tel" />

                <h5>ADRES: </h5>
                <textarea type="text" />
                
                <h5>Şifre: </h5> 
                <input type="password" />

            </form>
            <button>Güncelle</button>
        </div>
    )
}

export default Account