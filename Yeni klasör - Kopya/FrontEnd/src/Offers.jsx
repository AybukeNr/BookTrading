import React from 'react'
import './Offers.css'
import { useStateValue } from './StateProvider';
import { useNavigate } from 'react-router-dom';
import { Rating } from '@mui/material';
import { red } from '@mui/material/colors';

function Offers({ id, title, author, isbn, publisher, publishedDate, category, image, price }) {
  const [{ offerSent, offerReceive, acceptBook }, dispatch] = useStateValue();
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

  const handleDetails = (item) => {
    navigate("/bookDetails", {
      state: {
        id: item.id,
        title: item.title,
        author: item.author,
        isbn: item.isbn,
        publisher: item.publisher,
        publishedDate: item.publishedDate,
        category: item.category,
        image: item.image,
        price: item.price,
        fromOffers: true
      }
    });
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
                <Rating className='rating' />
                <p><strong>Yazar: </strong>{item.author}</p>
                <p><strong>Yayınevi: </strong>{item.publisher}</p>
                <p><strong>Yayın Tarihi: </strong>{item.publishedDate}</p>
                <p><strong>ISBN: </strong> {item.isbn}</p>
                <p><strong>Kategori: </strong>{item.category}</p>
              </div>
              <div className='accept_callback'>
                {acceptBook ? (
                  <p style={{ color: "green" }}>Teklif kabul edildi!</p>
                ) : (
                  <p style={{ color: "red" }}>Teklif reddedildi!</p>
                )}
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
            <div className="offer_info" key={index} >
              <img src={item.image} alt={item.title} onClick={() => handleDetails(item)} />
              <div onClick={() => handleDetails(item)}>
                <h4>{item.title}</h4>
                <Rating className='rating' />
                <p><strong>Yazar: </strong>{item.author}</p>
                <p><strong>Yayınevi: </strong>{item.publisher}</p>
                <p><strong>Yayın Tarihi: </strong>{item.publishedDate}</p>
                <p><strong>ISBN: </strong>{item.isbn}</p>
                <p><strong>Kategori: </strong>{item.category}</p>
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

