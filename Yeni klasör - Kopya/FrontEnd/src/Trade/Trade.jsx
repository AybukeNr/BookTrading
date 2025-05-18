import React, { useEffect, useState } from 'react'
import '../Trade/Trade.css'
import PaymentCard from '../PaymentCard/PaymentCard'
import { useStateValue } from '../StateProvider';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { instanceShipping, instanceTransaction, instanceUser } from '../axios';
function Trade() {
  const [{ tradeData }, dispatch] = useStateValue();
  const navigate = useNavigate();
  const location = useLocation();
  const offererId = location.state?.tradeData?.offererId;
  const ownerId = location.state?.tradeData?.offerList?.ownerId;
  const [openAdDialog, setOpenAdDialog] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [offerer, setOfferer] = useState(null);
  const [owner, setOwner] = useState(null);
  const [trackingNumber, setTrackingNumber] = useState("");
  const [cardName, setCardName] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");
  const [processing, setProcessing] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (offererId) {
      instanceUser.get(`/getUserById?id=${offererId}`)
        .then(res => setOfferer(res.data))
        .catch(err => console.error("Teklif veren bilgisi alınamadı:", err));
    } else if (ownerId) {
      instanceUser.get(`/getUserById?id=${ownerId}`)
        .then(res => setOwner(res.data))
        .catch(err => console.error("Teklif veren bilgisi alınamadı:", err));
    }
    console.log("Teklif veren ID:", offererId);
    console.log("Teklif alan ID:", ownerId);
  }, [offererId, ownerId]);

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      instanceUser.get(`/getUserById?id=${userId}`)
        .then((res) => setUserInfo(res.data))
        .catch((err) => console.error("Kullanıcı bilgisi alınamadı:", err));
    }
  }, []);

  //güvence bedeli ödeme
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!cardName || !cardNumber || !cvv || !expiryDate) {
      setError("Lütfen tüm kart bilgilerini doldurun.");
      return;
    }

    setProcessing(true);
    setError("");

    const userId = localStorage.getItem("userId");

    if (!userId) {
      setError("Kullanıcı kimliği bulunamadı. Lütfen tekrar giriş yapın.");
      setProcessing(false);
      return;
    }

    const requestBody = {
      listId: tradeData?.offeredListId,
      listType: "EXCHANGE",
      userId: userId,
      fullName: cardName,
      cardNumber,
      cvv,
      expiryDate,
      amount: 10, // Güvence bedeli tutarı
      trackingNumber,
    };

    try {
      const response = await instanceTransaction.post("/payments/createPayment", requestBody,
        { headers: { "Content-Type": "application/json" } }
      );

      if (response.status === 200 || response.status === 201) {
        console.log("Ödeme başarılı:", response.data);
        navigate("/");
      } else {
        setError("Ödeme sırasında beklenmedik bir hata oluştu.");
      }
    } catch (err) {
      console.error("Ödeme hatası:", err);
      setError("Ödeme başarısız. Lütfen bilgilerinizi kontrol edin.");
    } finally {
      setProcessing(false);
    }
  };

  //takas için kargo takip ile onaylama popup
  const confirmTrade = async () => {
    setLoading(true);
    try {
      const response = await instanceShipping.patch("/updateShipping", {
        shippingSerialNumber: shippingSerialNumber,
        trackingNumber: trackingNumber,
        userId: userId
      });
      console.log("Takas işlemi gerçekleştirildi:", response.data);
      setOpenAdDialog(false);
    } catch (error) {
      console.error("Takas işlemi gerçekleştirilirken hata:", error);
      setError("Takas işlemi gerçekleştirilemedi. Lütfen tekrar deneyin.");
    }
  }

  const handleOpenAdDialog = () => {
    setOpenAdDialog(true);
  };

  const handleCloseAdDialog = () => {
    setOpenAdDialog(false);
  };

  return (
    <div className='trade'>
      <div className="trade_container">
        <h1> Ürünler </h1>
        <div className="trade_section">
          <div className="trade_title">
            <h3>Teslimat Adresleri: </h3>
          </div>
          <div className="trade_address">
            <div>
              <h4>Kullanıcı Bilgileri</h4>
              <p>{userInfo?.firstName} {userInfo?.lastName}</p>
              <p>{userInfo?.mailAddress}</p>
              <p>{userInfo?.address}</p>
            </div>
            {offerer ? (
              <div>
                <h4>Diğer Kullanıcı Bilgileri:</h4>
                <p>{offerer?.firstName} {offerer?.lastName}</p>
                <p>{offerer?.mailAddress}</p>
                <p>{offerer?.address}</p>
              </div>
            ) : owner ? (
               <div>
                <h4>Diğer Kullanıcı Bilgileri:</h4>
                <p>{owner?.firstName} {owner?.lastName}</p>
                <p>{owner?.mailAddress}</p>
                <p>{owner?.address}</p>
              </div>
            ) : (
              <p>Diğer kullanıcı bilgileri yükleniyor...</p>
            )}
          </div>
        </div>

        {/* <div className="trade_section">
          <div className="trade_title">
            <h3>Takaslanacak Kitaplar</h3>
          </div>
          <div className="trade_items">
            <div className="sendBook">
              <h4>Gönderilecek Kitap</h4>
              {sendBook.map(item => (
                <div className="offer_info" key={item.index}>
                  <img src={item.image} alt={item.title} />
                  <div>
                    <h4>{item.title}</h4>
                    <p>Yazar: {item.author}</p>
                    <p>Yayınevi: {item.publisher}</p>
                    <p>Yayın Tarihi: {item.publishedDate}</p>
                    <p>ISBN: {item.isbn}</p>
                    <p>Kategori: {item.category}</p>
                    <p>Açıklama: {item.description}</p>
                    <p>Durum: {item.condition}</p>
                  </div>
                </div>
              ))}
            </div>
            <div className="receiveBook">
              <h4>Alınacak Kitap</h4>
              {receiveBook.map((item, index) => (
                <div className="offer_info" key={index}>
                  <img src={item.image} alt={item.title} />
                  <div>
                    <h4>{item.title}</h4>
                    <p>Yazar: {item.author}</p>
                    <p>Yayınevi: {item.publisher}</p>
                    <p>Yayın Tarihi: {item.publishedDate}</p>
                    <p>ISBN: {item.isbn}</p>
                    <p>Kategori: {item.category}</p>
                    <p>Açıklama: {item.description}</p>
                    <p>Durum: {item.condition}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div> */}

        <div className="trade_section">
          <div className="trade_title">
            <h3>Güvence Bedeli Bilgileri</h3>
          </div>
          <div className="trade_details">
            <form onSubmit={handleSubmit}>
              <div className="trade_card">
                <PaymentCard
                  cardName={cardName}
                  setCardName={setCardName}
                  cardNumber={cardNumber}
                  setCardNumber={setCardNumber}
                  cvv={cvv}
                  setCvv={setCvv}
                  expiryDate={expiryDate}
                  setExpiryDate={setExpiryDate} />
              </div>
              <div>
                <button className="trade_button" type='button' onClick={handleOpenAdDialog}>
                  <span>Onayla ve Takası gerçekleştir</span>
                </button>
              </div>
            </form>

            <p>"Takası gerçekleştir diyerek, <a href="#" target="_blank">Takas Sözleşmesi</a> ve  <a href="#" target="_blank">Ön Bilgilendirme Formu</a>'nu okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>
          </div>
        </div>

        <Dialog open={openAdDialog} onClose={handleCloseAdDialog}>
          <DialogTitle>Kargo takip numarası ile takası onayla</DialogTitle>
          <DialogContent>
            {error && <p style={{ color: "crimson", fontWeight: "500", marginTop: "10px" }}>{error}</p>}
            Kargo takip numarası giriniz:
            <DialogContentText>
              <input type="text" style={{ borderRadius: "8px", border: "1px solid orange", outlineColor: "rgb(57, 139, 217)" }} value={trackingNumber} onChange={(e) => setTrackingNumber(e.target.value)} />
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseAdDialog} color="primary">İptal</Button>
            <Button onClick={confirmTrade} color="error">{loading ? "Onaylanıyor..." : "Onayla"}</Button>
          </DialogActions>
        </Dialog>

      </div>
    </div>
  )
}

export default Trade