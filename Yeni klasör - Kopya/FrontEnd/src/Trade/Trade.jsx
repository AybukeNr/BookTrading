import React from 'react'
import '../Trade/Trade.css'
import PaymentCard from '../PaymentCard/PaymentCard'
import { useStateValue } from '../StateProvider';
function Trade() {
  const [{ receiveOffer, sendOffer, otherUser, user }, dispatch] = useStateValue();

  // const tradeOffer = async () => {
  //   try {
  //     const response = await instanceOffer.post(`/createTrade`, {
  //       offerId: receiveOffer.offerId,
  //       offererId: receiveOffer.offererId,
  //       offeredListId: sendOffer.offeredListId,
  //       offeredBookId: sendOffer.offeredBookId,
  //       offerStatus: "KABUL",
  //     });
  //     console.log("Trade offer created successfully:", response.data);
  //   } catch (error) {
  //     console.error("Error creating trade offer:", error);
  //   }
  // }

  return (
    <div className='trade'>
      <div className="trade_container">
        <h1> Ürünler </h1>
        <div className="trade_section">
          <div className="trade_title">
            <h3>Teslimat Adresleri: </h3>
          </div>
          <div className="trade_address">
            <h4>Diğer Kullanıcı Adresi</h4>
            <p>{otherUser?.firstName} {otherUser?.lastName}</p>
            <p>{otherUser?.email}</p>
            <p>{otherUser?.address}</p>
            <h4>Senin Adresin</h4>
            <p>{user?.firstName} {user?.lastName}</p>
            <p>{user?.email}</p>
            <p>{user?.address}</p>
          </div>
        </div>

        <div className="trade_section">
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
        </div>

        <div className="trade_section">
          <div className="trade_title">
            <h3>Güvence Bedeli Bilgileri</h3>
          </div>
          <div className="trade_details">
            <form>
              <div className="trade_card">
                <PaymentCard />
              </div>
              <div>
                <button className="trade_button">
                  <span>Onayla ve Takası gerçekleştir</span>
                </button>
              </div>
              {/* {error && <div>{error}</div>} */}
            </form>

            <p>"Takası gerçekleştir diyerek, <a href="#" target="_blank">Takas Sözleşmesi</a> ve  <a href="#" target="_blank">Ön Bilgilendirme Formu</a>'nu okuduğunuzu, anladığınızı ve kabul ettiğinizi beyan etmiş olursunuz."</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Trade