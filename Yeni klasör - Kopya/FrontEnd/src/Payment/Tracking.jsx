import React, { useEffect, useState } from 'react'
import './Tracking.css'
import { instanceShipping } from '../axios';
import { useLocation } from 'react-router-dom';

const userId = localStorage.getItem("userId");
function Tracking() {
    const location = useLocation();
    const listId = location.state?.listId;
    const [trackingNumber, setTrackingNumber] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (listId) {
            fetchShippingInfo();
        }
    }, [listId]);
    
    const fetchShippingInfo = async () => {
        try {
            const response = await instanceShipping.get(`/getExchangeInfos?userId=${userId}&listId=${listId}`, {
                params: {
                    userId: userId,
                },
            });

            const data = response.data;
            console.log("Shipping Info:", data);

            setShippingSerialNumber(data.shippingSerialNumber);
        } catch (err) {
            console.error("Kargo bilgileri alınamadı:", err);
            setError("Kargo bilgileri alınamadı.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!trackingNumber.trim()) {
            alert("Lütfen bir kargo takip numarası girin.");
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await instanceShipping.patch("/updateShipping", {
                shippingSerialNumber,
                trackingNumber,
                userId,
            }, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            console.log("Ödeme işlemi gerçekleştirildi:", response.data);

            setSubmitted(true);
            setTrackingNumber("");
        } catch (error) {
            console.error("Ödeme işlemi gerçekleştirilirken hata:", error);
            setError("Ödeme işlemi gerçekleştirilemedi. Lütfen tekrar deneyin.");
        } finally {
            setLoading(false);
        }

        console.log("Gönderilen takip numarası:", trackingNumber);
        setSubmitted(true);
    };

    return (
        <div className='tracking'>
            <form onSubmit={handleSubmit}>
                <label>Kargo Takip Numarasını Giriniz:</label>
                <input
                    id="trackingNumber"
                    type="text"
                    value={trackingNumber}
                    onChange={(e) => setTrackingNumber(e.target.value)} />
                <button type='submit' disabled={loading}>{loading ? "Onaylanıyor..." : "Onayla"}</button>
            </form>


            {submitted && (
                <p className="mt-4 text-green-600 font-medium" style={{ color: "green" }}>
                    Takip numaranız başarıyla gönderildi!
                </p>
            )}

            {error && (
                <p className="mt-4 text-red-600 font-medium" style={{ color: "red" }}>{error}</p>
            )}
        </div>
    )
}

export default Tracking