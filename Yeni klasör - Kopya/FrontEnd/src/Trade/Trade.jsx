import React, { useEffect, useState } from 'react'
import '../Trade/Trade.css'
import PaymentCard from '../PaymentCard/PaymentCard'
import { useLocation, useNavigate } from 'react-router-dom';
import { instanceLibrary, instanceListing, instanceTransaction, instanceUser } from '../axios';
function Trade() {
  const navigate = useNavigate();
  const location = useLocation();
  const tradeItem = location.state?.tradeData;
  const [currentUser, setCurrentUser] = useState(null);
  const [otherUser, setOtherUser] = useState(null);
  const [transactionInfo, setTransactionInfo] = useState(null);
  const [cardName, setCardName] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");
  const [processing, setProcessing] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [sendBook, setSendBook] = useState(null);
  const [receiveBook, setReceiveBook] = useState(null);

  useEffect(() => {
    if (tradeItem) {
      console.log("Trade item:", tradeItem);

      if (tradeItem.currentUserId) {
        instanceUser.get(`/getUserById?id=${tradeItem.currentUserId}`)
          .then(res => {
            setCurrentUser(res.data);
            console.log("Current user:", res.data);
          })
          .catch(err => console.error("Giriş yapan kullanıcı bilgisi alınamadı:", err));
      }

      instanceUser.get(`/getUserById?id=${tradeItem.otherUserId}`)
        .then(res => {
          setOtherUser(res.data);
          console.log("Other user (from API):", res.data);
        })
        .catch(err => console.error("Karşı taraf kullanıcı bilgisi alınamadı:", err));

      instanceTransaction.get(`/transactions/getTransactionInfos?userId=${tradeItem.currentUserId}&listId=${tradeItem.listingId}`)
        .then(res => {
          setTransactionInfo(res.data);
          console.log("Transaction infos (from API):", res.data);
        })
        .catch(err => console.error("Takas bilgileri alınamadı:", err));

      if (!tradeItem.listBook && tradeItem.listingId) {
        instanceListing.get(`/getListsByListById?id=${tradeItem.listingId}`)
          .then(res => {
            console.log("Listelenen kitap (listBook):", res.data);
            setReceiveBook(res.data);
          })
          .catch(err => console.error("Listelenen kitap bilgisi alınamadı:", err));
      }

      if (!tradeItem.offeredBook && tradeItem.bookId) {
        instanceLibrary.get(`/getBookById?id=${tradeItem.bookId}`)
          .then(res => {
            console.log("Teklif edilen kitap (offeredBook):", res.data);
            setSendBook(res.data);
          })
          .catch(err => console.error("Teklif edilen kitap bilgisi alınamadı:", err));
      }

      if (tradeItem.tradeType === 'sent') {
        setSendBook(tradeItem.offeredBook);
        setReceiveBook(tradeItem.listBook);
      } else if (tradeItem.tradeType === 'received' || tradeItem.tradeType === 'accepted') {
        setSendBook(tradeItem.listBook);
        setReceiveBook(tradeItem.offeredBook);
      }
    }
  }, [tradeItem]);

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
      listId: [tradeItem?.listingId],
      listType: transactionInfo?.listType,
      userId: userId,
      fullName: cardName,
      cardNumber,
      cvv,
      expiryDate,
      amount: transactionInfo?.trustFee,
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

  const confirmTrade = async () => {
    setLoading(true);
    setError("");

    if (!cardName || !cardNumber || !cvv || !expiryDate) {
      setError("Lütfen tüm kart bilgilerini doldurun.");
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
      const paymentRequestBody = {
        listId: [tradeItem?.listingId],
        listType: transactionInfo?.listType,
        userId: userId,
        fullName: cardName,
        cardNumber,
        cvv,
        expiryDate,
        amount: transactionInfo?.trustFee,
      };

      console.log("Ödeme isteği gönderiliyor:", paymentRequestBody);

      const paymentResponse = await instanceTransaction.post("/payments/createPayment", paymentRequestBody,
        { headers: { "Content-Type": "application/json" } }
      );

      if (paymentResponse.status !== 200 && paymentResponse.status !== 201) {
        setError("Ödeme sırasında beklenmedik bir hata oluştu.");
        return;
      }

      console.log("Ödeme başarılı:", paymentResponse.data);
      navigate("/myTrades");

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
  }

  return (
    <div className='trade'>
      <div className="trade_container">
        <h1> Takas Detayları </h1>
        <div className="trade_section">
          <div className="trade_title">
            <h3>Teslimat Adresleri: </h3>
          </div>
          <div className="trade_address">
            <div>
              <h4>Sizin Bilgileriniz</h4>
              {currentUser ? (
                <>
                  <p>{currentUser.firstName} {currentUser.lastName}</p>
                  <p>{currentUser.mailAddress}</p>
                  <p>{currentUser.address}</p>
                </>
              ) : (
                <p>Kullanıcı bilgileri yükleniyor...</p>
              )}
            </div>
            <div>
              <h4>Diğer Kullanıcı Bilgileri</h4>
              {otherUser ? (
                <>
                  <p>{otherUser.firstName} {otherUser.lastName}</p>
                  <p>{otherUser.mailAddress}</p>
                  <p>{otherUser.address}</p>
                </>
              ) : (
                <p>Diğer kullanıcı bilgileri yükleniyor...</p>
              )}
            </div>
          </div>
        </div>

        <div className="trade_section">
          <div className="trade_title">
            <h3>Takaslanacak Kitaplar</h3>
          </div>
          <div className="trade_items">
            <div className="sendBook">
              <h4>Gönderilecek Kitap</h4>
              {sendBook ? (
                <div className="offer_info">
                  <img src={sendBook.image} alt={sendBook.title} />
                  <div>
                    <h4>{sendBook.title}</h4>
                    <p><strong>Yazar:</strong> {sendBook.author}</p>
                    <p><strong>Yayınevi:</strong> {sendBook.publisher}</p>
                    <p><strong>Yayın Tarihi:</strong> {sendBook.publishedDate}</p>
                    <p><strong>ISBN:</strong> {sendBook.isbn}</p>
                    <p><strong>Kategori:</strong> {sendBook.category}</p>
                    <p><strong>Açıklama:</strong> {sendBook.description}</p>
                    <p><strong>Durum:</strong> {sendBook.condition}</p>
                  </div>
                </div>
              ) : (
                <p>Kitap bilgisi yükleniyor...</p>
              )}
            </div>
            <div className="receiveBook">
              <h4>Alınacak Kitap</h4>
              {receiveBook ? (
                <div className="offer_info">
                  <img src={receiveBook.image} alt={receiveBook.title} />
                  <div>
                    <h4>{receiveBook.title}</h4>
                    <p><strong>Yazar:</strong> {receiveBook.author}</p>
                    <p><strong>Yayınevi:</strong> {receiveBook.publisher}</p>
                    <p><strong>Yayın Tarihi:</strong> {receiveBook.publishedDate}</p>
                    <p><strong>ISBN:</strong> {receiveBook.isbn}</p>
                    <p><strong>Kategori:</strong> {receiveBook.category}</p>
                    <p><strong>Açıklama:</strong> {receiveBook.description}</p>
                    <p><strong>Durum:</strong> {receiveBook.condition}</p>
                  </div>
                </div>
              ) : (
                <p>Kitap bilgisi yükleniyor...</p>
              )}
            </div>
          </div>
        </div>

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
              {error && <p style={{ color: "crimson", fontWeight: "500", marginTop: "10px" }}>{error}</p>}
              <div>
                { }
                <button className="trade_button" type='button'
                  onClick={confirmTrade}
                  disabled={loading || processing}
                >
                  <span>Onayla ve Takası gerçekleştir</span>
                </button>
              </div>
            </form>

            <p>"Takası gerçekleştir diyerek, <a href="#" target="_blank">Takas Sözleşmesi</a> ve  <a href="#" target="_blank">Ön Bilgilendirme Formu</a>'nu okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>
          </div>
        </div>

      </div>
    </div >
  )
}

export default Trade