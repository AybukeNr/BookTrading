import React, { useState } from 'react'
import '../Offers/Offers.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import { Rating } from '@mui/material';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';

function Offers({ id, title, author, isbn, publisher, publishedDate, category, image, price }) {
  const [{ offerSent, offerReceive, acceptBook }, dispatch] = useStateValue();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

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

  const handleRejectOffer = () => {
    setOpen(true);
  };

  const cancelRejectOffer = () => {
    setOpen(false);
  };


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
                <p>Ad Soyad <Rating className='rating' /></p>
                <p><strong>Yazar: </strong>{item.author}</p>
                <p><strong>Yayınevi: </strong>{item.publisher}</p>
                <p><strong>Yayın Tarihi: </strong>{item.publishedDate}</p>
                <p><strong>ISBN: </strong> {item.isbn}</p>
                <p><strong>Kategori: </strong>{item.category}</p>
              </div>
              <div className='accept_callback' style={{ fontSize: "14px" }}> 
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
                <p>Ad Soyad <Rating className='rating' /></p>
                <p><strong>Yazar: </strong>{item.author}</p>
                <p><strong>Yayınevi: </strong>{item.publisher}</p>
                <p><strong>Yayın Tarihi: </strong>{item.publishedDate}</p>
                <p><strong>ISBN: </strong>{item.isbn}</p>
                <p><strong>Kategori: </strong>{item.category}</p>
              </div>
              <div className='receive_buttons'>
                <button onClick={acceptOffer}>Teklifi kabul et</button>
                <button onClick={() => handleRejectOffer(item)}>Teklifi reddet</button>
              </div>
            </div>
          ))
        ) : (
          <p>Alınan hiç kitap yok.</p>
        )}
      </div>

      <Dialog open={open} onClose={cancelRejectOffer}>
        <DialogTitle>Teklifi Reddet</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Kitap teklifini reddetmek istediğinize emin misiniz?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={cancelRejectOffer} color="primary">İptal</Button>
          <Button color="error">Evet</Button>
        </DialogActions>
      </Dialog>

    </div>
  )
}

export default Offers

