import React, { useEffect, useState } from 'react'
import './Tracking.css'
import { instanceShipping } from '../axios';
import { useLocation, useNavigate } from 'react-router-dom';

function Tracking() {
    const location = useLocation();
    const listId = location.state?.listId;
    const navigate = useNavigate();
    const [trackingNumber, setTrackingNumber] = useState("");
    const [shippingSerialNumber, setShippingSerialNumber] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const userId = localStorage.getItem("userId");
    useEffect(() => {
        if (listId) {
            fetchShippingInfo();
        }
    }, [listId]);

    const fetchShippingInfo = async () => {
        try {
            const response = await instanceShipping.get(`/getExchangeInfos`, {
                params: {
                    userId: userId,
                    listId: listId,
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
                shippingSerialNumber: shippingSerialNumber,
                trackingNumber: trackingNumber,
                userId: userId,
            }, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            console.log("Takip numarası başarıyla gönderildi:", response.data);

            setSubmitted(true);
            setTrackingNumber("");
            navigate("/");
        } catch (error) {
            console.error("Takip numarası gönderilirken hata oluştu:", error);
            setError("Takip numarası gönderilemedi. Lütfen tekrar deneyin.");
        } finally {
            setLoading(false);
        }

        console.log("Gönderilen takip numarası:", trackingNumber);
    };

    return (
        <div className='tracking'>
            <h3>Kargo Takip Numarası Gönder</h3>
            <form onSubmit={handleSubmit}>
                <label htmlFor="trackingNumber">Kargo Takip Numarasını Giriniz:</label>
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