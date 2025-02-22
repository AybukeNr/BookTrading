import React from 'react'
import '../PaymentCard/PaymentCard.css'
function PaymentCard() {
    return (
        <div className='card'>

            <h5>Kart Üzerindeki Ad:</h5>
            <input type="text" className='card_input' />

            <h5>Kart No:</h5>
            <input type="text" className='card_input' maxLength={16} size={16}/>

            <h5>CVC No:</h5>
            <div className='cvc'>
                <input type="text" maxLength={3} size={3} />
                <p className='cvc_info'>Kartınızın arka yüzündeki 3 rakam</p>
            </div>

            <h5>Son Kullanım Tarihi:</h5>
            <select id="month" className='date'>
                <option value="1">01</option>
                <option value="2">02</option>
                <option value="3">03</option>
                <option value="4">04</option>
                <option value="5">05</option>
                <option value="6">06</option>
                <option value="7">07</option>
                <option value="8">08</option>
                <option value="9">09</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
            </select>
            <select id="year" className='date'>
                <option value="2025">2025</option>
                <option value="2026">2026</option>
                <option value="2027">2027</option>
                <option value="2028">2028</option>
                <option value="2029">2029</option>
                <option value="2030">2030</option>
                <option value="2031">2031</option>
                <option value="2032">2032</option>
                <option value="2033">2033</option>
                <option value="2034">2034</option>
                <option value="2035">2035</option>
            </select>
            
        </div>
        
        
    )
}

export default PaymentCard