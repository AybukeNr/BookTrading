import React from 'react'
import './Checkout.css'
import Subtotal from './Subtotal'

function Checkout() {
    return (
        <div className='checkout'>
            <div className="checkout_left">
                <img className='checkout_ad' src="https://i.hizliresim.com/dvmrg8d." alt="" />
                <div>
                    <h1 className="checkout_title">Alışveriş Sepetim</h1>
                    {/* basketItem */}
                    {/* basketItem */}
                    {/* basketItem */}
                    {/* basketItem */}
                </div>
            </div>

            <div className="checkout_right">
                <Subtotal />
            </div>

        </div>
    )
}

export default Checkout