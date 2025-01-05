import React from 'react'
import './Subtotal.css'
import CurrencyFormat from 'react-currency-format'
import { useStateValue } from './StateProvider';
import { getBasketTotal } from './reducer';
import { useNavigate } from 'react-router-dom';

function Subtotal() {
    const navigate = useNavigate();
    const [{ basket }, dispatch] = useStateValue();

    return (
        <div className='subtotal'>
            <CurrencyFormat
                renderText={(value) => (
                    <>
                        <p>
                            Toplam ({ basket.length } adet kitap): <strong>{value}</strong>
                        </p>

                        <small className='subtotal_gift'>
                            <input type="checkbox" /> Burası hediye kısmı
                        </small>
                    </>
                )}
                
                decimalScale={2}
                value={getBasketTotal(basket)}
                displayType={"text"}
                thousandSeparator={true}
                prefix={"₺"}
            />

            <button onClick={e => navigate("/payment", { replace: true })}>Ödeme için devam et</button>
        </div>
    )
}

export default Subtotal