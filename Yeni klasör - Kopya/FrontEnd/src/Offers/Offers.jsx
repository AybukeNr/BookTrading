import React, { useEffect, useState } from 'react'
import '../Offers/Offers.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import { Rating } from '@mui/material';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';
import { instanceListing, instanceOffer } from '../axios';

function Offers() {
  const [{ sentOffers, receviedOffers }, dispatch] = useStateValue();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [selectedOffer, setSelectedOffer] = useState(null);

  const ownerId = localStorage.getItem('userId');

  const fetchOffers = async () => {
    try {
      const sentResponse = await instanceListing.get(`/getRecievedOffers`, {
        params: { userId: ownerId }
      });

      const receivedResponse = await instanceOffer.get(`/getOffersByOwnerId`, {
        params: { userId: ownerId }
      });

      console.log("Sent Offers:", sentResponse.data);
      console.log("Received Offers:", receivedResponse.data);

      dispatch({
        type: 'SET_OFFER_SENT',
        payload: sentResponse.data
      });
      dispatch({
        type: 'SET_OFFER_RECEIVE',
        payload: receivedResponse.data
      });

      // setSentOffers(sentResponse.data);
      // setReceivedOffers(receivedResponse.data);

    } catch (error) {
      console.error("Teklifler alınırken hata:", error);
    }
  };

  useEffect(() => {
    if (ownerId) {
      fetchOffers();
    }
  }, [ownerId]);

  const acceptOffer = async (offerId, senderId, bookId) => {
    try {
      await instanceOffer.put(`/updateOffer`, {
        offererId: senderId,
        offerId: offerId,
        listingId: bookId,
        offerStatus: "KABUL"
      });

      dispatch({
        type: 'UPDATE_OFFER_STATUS',
        id: offerId,
        status: "KABUL"
      });
      navigate('/trade');
    } catch (error) {
      console.error("Teklif kabul edilirken hata:", error);
    }
  };

  const confirmRejectOffer = async () => {
    if (selectedOffer) {
      try {
        await instanceOffer.put(`/updateOffer`, {
          offererId: selectedOffer.offererId,
          offerId: selectedOffer.offerId,
          listingId: selectedOffer.offerListId,
          offerStatus: "RET"
        });

        dispatch({
          type: 'UPDATE_OFFER_STATUS',
          id: selectedOffer.offerId,
          status: "RET"
        });
      } catch (error) {
        console.error("Teklif reddedilirken hata:", error);
      }
    }
    setOpen(false);
  };

  const handleRejectOffer = (offer) => {
    setSelectedOffer(offer);
    setOpen(true);
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
        description: item.description,
        image: item.image,
        price: item.price,
        fromOffers: true
      }
    });
  };

  // const handleReceiveOffer = (newBook) => {
  //   {
  //     dispatch({
  //       type: 'ADD_TO_OFFER_RECEIVE',
  //       item: newBook,
  //     });
  //   }
  // };

  // const acceptOffer = () => {
  //   navigate('/trade');
  // }

  // const handleRejectOffer = () => {
  //   setOpen(true);
  // };

  // const cancelRejectOffer = () => {
  //   setOpen(false);
  // };


  // const handleDetails = (item) => {
  //   navigate("/bookDetails", {
  //     state: {
  //       id: item.id,
  //       title: item.title,
  //       author: item.author,
  //       isbn: item.isbn,
  //       publisher: item.publisher,
  //       publishedDate: item.publishedDate,
  //       category: item.category,
  //       image: item.image,
  //       price: item.price,
  //       fromOffers: true
  //     }
  //   });
  // }

  return (
    <div className='offers'>
      <div className="send">
        <h2>Gönderdiğim Teklifler</h2>
        {sentOffers?.length > 0 ? (
          sentOffers.map((item, index) => (
            <div className="offer_info" key={index}>
              <img src={item.book.image} alt={item.book.title} />
              <div>
                <h4>{item.book.title}</h4>
                <p>Ad Soyad <Rating className='rating' /></p>
                <p><strong>Yazar: </strong>{item.book.author}</p>
                <p><strong>Yayınevi: </strong>{item.book.publisher}</p>
                <p><strong>Yayın Tarihi: </strong>{item.book.publishedDate}</p>
                <p><strong>ISBN: </strong> {item.book.isbn}</p>
                <p><strong>Kategori: </strong>{item.book.category}</p>
                <p><strong>Açıklama: </strong>{item.book.description}</p>
              </div>
              <div className='accept_callback' style={{ fontSize: "14px" }}>
                {item.offerStatus === "KABUL" ? (
                  <p style={{ color: "green" }}>Teklif kabul edildi!</p>
                ) : item.offerStatus === "RET" ? (
                  <p style={{ color: "red" }}>Teklif reddedildi!</p>
                ) : (
                  <p style={{ color: "orange" }}>Beklemede</p>
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
        {receviedOffers?.length > 0 ? (
          receviedOffers.map((item, index) => (
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
                <p><strong>Açıklama: </strong>{item.description}</p>
              </div>
              <div className='receive_buttons'>
                <button onClick={() => acceptOffer(item.offerId, item.offererId, item.offerListId)}>Teklifi kabul et</button>
                <button onClick={() => handleRejectOffer(item)}>Teklifi reddet</button>
              </div>
            </div>
          ))
        ) : (
          <p>Alınan hiç kitap yok.</p>
        )}
      </div>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>Teklifi Reddet</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Kitap teklifini reddetmek istediğinize emin misiniz?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)} color="primary">İptal</Button>
          <Button onClick={confirmRejectOffer} color="error">Evet</Button>
        </DialogActions>
      </Dialog>

    </div>
  )
}

export default Offers

