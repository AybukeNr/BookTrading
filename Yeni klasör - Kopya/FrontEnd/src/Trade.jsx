import React from 'react'
import './Trade.css'
import PaymentCard from './PaymentCard'
import { useStateValue } from './StateProvider';
function Trade() {
  const [{ offerReceive }, dispatch] = useStateValue();

  return (
    <div className='trade'>
      <div className="trade_container">
        <h1> Ürünler </h1>

        {/* delivery-address */}
        <div className="trade_section">
          <div className="trade_title">
            <h3>Teslimat Adresleri: </h3>
          </div>
          <div className="trade_address">
            <p>Email</p>
            {/* <p>{user?.email}</p> */}
            <p>Adres</p>
            {/* <p>{user?.address}</p> */}
          </div>
        </div>


        {/* review-items */}
        <div className="trade_section">
          <div className="trade_title">
            <h3>Takaslanacak Kitaplar</h3>
          </div>
          <div className="trade_items">
            <div className="sendBook">
              <h4>Gönderilecek Kitap</h4>
              {/* {offerSent.map(item => (
                <div className="offer_info" key={item.index}>
                  <img src={item.image} alt={item.title} />
                  <div>
                    <h4>{item.title}</h4>
                    <p>Yazar: {item.author}</p>
                    <p>Yayınevi: {item.publisher}</p>
                    <p>Yayın Tarihi: {item.publishedDate}</p>
                    <p>ISBN: {item.isbn}</p>
                    <p>Kategori: {item.category}</p>
                  </div>
                </div>
              ))} */}
            </div>
            <div className="receiveBook">
              <h4>Alınacak Kitap</h4>
              {offerReceive.map((item, index) => (
                <div className="offer_info" key={index}>
                  <img src={item.image} alt={item.title} />
                  <div>
                    <h4>{item.title}</h4>
                    <p>Yazar: {item.author}</p>
                    <p>Yayınevi: {item.publisher}</p>
                    <p>Yayın Tarihi: {item.publishedDate}</p>
                    <p>ISBN: {item.isbn}</p>
                    <p>Kategori: {item.category}</p>
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
                  <span>"Onayla ve Takası gerçekleştir"</span>
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