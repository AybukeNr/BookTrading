import React from 'react'
import './Offers.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';

function Offers() {
  const [{ offerSent, offerReceive }, dispatch] = useStateValue();
  const navigate = useNavigate();

  const handleReceiveOffer = (newBook) => {
    {
      dispatch({
        type: 'ADD_TO_OFFER_RECEIVE',
        item: newBook,
      });
    }
  };

  const acceptOffer = () => {
   navigate('/trade');
  }

  return (
    <div className='offers'>
      <div className="send">
        <h2>Gönderdiğim Teklifler</h2>
        {offerSent?.length > 0 ? (
          offerSent.map((item, index) => (
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
          ))
        ) : (
          <p>Gönderilen hiç kitap yok.</p>
        )}
      </div>
      <div className="receive">
        <h2>Aldığım Teklifler</h2>
        {offerReceive?.length > 0 ? (
          offerReceive.map((item, index) => (
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
              <div className='receive_buttons'>
                <button onClick={acceptOffer}>Teklifi kabul et</button>
                <button>Teklifi reddet</button>
              </div>
            </div>
          ))
        ) : (
          <p>Alınan hiç kitap yok.</p>
        )}
      </div>
    </div>
  )
}

export default Offers

