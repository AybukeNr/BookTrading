import React, { useEffect, useState } from 'react'
import './TradeTracking.css'
import { instanceShipping } from '../axios';
import { useLocation, useNavigate } from 'react-router-dom';

function TradeTracking() {
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
        if (!listId) {
            setError("List ID alınamadı.");
        } else {
            fetchShippingInfo();
        }
    }, [listId]);

    const fetchShippingInfo = async () => {
        try {
            const response = await instanceShipping.get(`/getExchangeInfos`, {
                params: {
                    userId,
                    listId
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
        setLoading(true);
        setError("");

        if (!trackingNumber) {
            setError("Lütfen kargo takip numarasını girin.");
            setLoading(false);
            return;
        }

        const userId = localStorage.getItem("userId");
        if (!userId) {
            setError("Kullanıcı kimliği bulunamadı. Lütfen tekrar giriş yapın.");
            setLoading(false);
            return;
        }

        try {
            await fetchShippingInfo();
            const shippingRequestData = {
                shippingSerialNumber: shippingSerialNumber,
                trackingNumber: trackingNumber,
                userId: userId,
            };

            console.log("Kargo takip güncelleme isteği:", shippingRequestData);

            const shippingResponse = await instanceShipping.patch("/updateShipping", shippingRequestData);

            console.log("Takas işlemi gerçekleştirildi:", shippingResponse.data);

            setSubmitted(true);
            setTrackingNumber("");
            navigate("/");

        } catch (error) {
            console.error("İşlem hatası:", error);
            if (error.response?.data?.message) {
                setError(error.response.data.message);
            } else {
                setError("İşlem gerçekleştirilemedi. Lütfen tekrar deneyin.");
            }
        } finally {
            setLoading(false);
        }
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

export default TradeTracking
