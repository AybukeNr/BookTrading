import React, { useEffect } from 'react'
import '../PaymentCard/PaymentCard.css'
function PaymentCard({ cardName, setCardName, cardNumber, setCardNumber, cvv, setCvv, expiryDate, setExpiryDate }) {

    const [month, setMonth] = React.useState("01");
    const [year, setYear] = React.useState("25");

    useEffect(() => {
        setExpiryDate(`${ month }/${ year }`);
    }, [month, year, setExpiryDate]);

    return (
        <div className='card'>

            <h5>Kart Üzerindeki Ad:</h5>
            <input type="text" className='card_input' value={cardName} onChange={(e) => setCardName(e.target.value)} />

            <h5>Kart No:</h5>
            <input type="text" className='card_input' maxLength={16} size={16} value={cardNumber} onChange={(e) => setCardNumber(e.target.value)} />

            <h5>CVC No:</h5>
            <div className='cvc'>
                <input type="text" maxLength={3} size={3} value={cvv} onChange={(e) => setCvv(e.target.value)} />
                <p className='cvc_info'>Kartınızın arka yüzündeki 3 rakam</p>
            </div>

            <h5>Son Kullanım Tarihi:</h5>
            <select id="month" className='date' value={month} onChange={(e) => setMonth(e.target.value)}>
                {[...Array(12)].map((_, i) => {
                    const m = (i + 1).toString().padStart(2, "0");
                    return (
                        <option key={m} value={m}>
                            {m}
                        </option>
                    );
                })}
            </select>
            <select id="year" className='date' value={year} onChange={(e) => setYear(e.target.value)}>
                {Array.from({ length: 11 }, (_, i) => 25 + i).map((y) => (
                    <option key={y} value={y}>
                        {y}
                    </option>
                ))}
            </select>
        </div>
    )
}

export default PaymentCard