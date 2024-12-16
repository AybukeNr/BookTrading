import React from 'react'
import './Subtotal.css'
import CurrencyFormat from 'react-currency-format'

function Subtotal() {
    return (
        <div className='subtotal'>
            <CurrencyFormat
                renderText={(value) => (
                    <>
                        <p>
                            Toplam (0 adet kitap): <strong>0</strong>
                        </p>

                        <small className='subtotal_gift'>
                            <input type="checkbox" /> Burası hediye kısmı
                        </small>
                    </>
                )}
                
                decimalScale={2}
                value={0}
                displayType={"text"}
                thousandSeparator={true}
                prefix={"₺"}
            />

            <button>Ödeme için devam et</button>
        </div>
    )
}

export default Subtotal