import React, { useState } from 'react'

function Tracking() {
    const [trackingNumber, setTrackingNumber] = useState("");
    const [submitted, setSubmitted] = useState(false);

    // const handleSubmit = (e) => {
    //     e.preventDefault();

    //     if (!trackingNumber.trim()) {
    //         alert("Lütfen bir kargo takip numarası girin.");
    //         return;
    //     }

        // Burada API'ye gönderim 
    //     console.log("Gönderilen takip numarası:", trackingNumber);
    //     setSubmitted(true);
    // };

    //ödeme için kargo takip onayla kısmı 
    // const confirmPayment = async () => {
    //     setLoading(true);
    //     try {
    //         const response = await instanceShipping.patch("/updateShipping", {
    //             shippingSerialNumber: shippingSerialNumber,
    //             trackingNumber: trackingNumber,
    //             userId: userId,
    //         }, {
    //             headers: {
    //                 "Content-Type": "application/json",
    //             },
    //         });
    //         console.log("Ödeme işlemi gerçekleştirildi:", response.data);
    //         setOpenAdDialog(false);
    //     } catch (error) {
    //         console.error("Ödeme işlemi gerçekleştirilirken hata:", error);
    //         setError("Ödeme işlemi gerçekleştirilemedi. Lütfen tekrar deneyin.");
    //     }
    // }
    return (
        <div className='tracking'>
            <form>
                <label htmlFor=""></label>
                <input type="text" />
                <button>Onayla</button>
            </form>
        </div>
    )
}

export default Tracking