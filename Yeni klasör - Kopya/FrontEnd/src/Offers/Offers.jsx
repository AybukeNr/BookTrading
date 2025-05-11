import React, { useEffect, useState } from 'react'
import '../Offers/Offers.css'
import { useStateValue } from '../StateProvider';
import { useNavigate } from 'react-router-dom';
import { Rating } from '@mui/material';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Button } from '@mui/material';
import { instanceListing, instanceOffer } from '../axios';
import HighlightOffOutlinedIcon from '@mui/icons-material/HighlightOffOutlined';

function Offers() {
  const [{ sentOffers, receivedOffers }, dispatch] = useStateValue();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [selectedOffer, setSelectedOffer] = useState(null);

  const ownerId = localStorage.getItem('userId');

  const fetchOffers = async () => {
    try {
      const receivedResponse = await instanceListing.get(`/getRecievedOffers`, {
        params: { userId: ownerId },
      });
      const sentResponse = await instanceOffer.get(`/getOffersByOwnerId`, {
        params: { userId: ownerId },
      });

      dispatch({
        type: 'SET_OFFER_SENT',
        payload: sentResponse.data
      });
      dispatch({
        type: 'SET_OFFER_RECEIVE',
        payload: receivedResponse.data
      });

    } catch (error) {
      console.error("Teklifler alınırken hata:", error);
    }
  };

  useEffect(() => {
    if (ownerId) {
      fetchOffers();
    }
  }, [ownerId]);

  const acceptOffer = async (offerId, offererId, offeredListId, offeredBookId) => {
    try {
      await instanceOffer.put(`/updateOffer`, {
        offererId: offererId,
        offerId: offerId,
        listingId: offeredListId,
        offerStatus: "KABUL",
        bookId: offeredBookId,
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
          offerStatus: "RET",
          bookId: selectedOffer.offeredBook?.id || 0,
        });

        dispatch({
          type: 'UPDATE_OFFER_STATUS',
          id: selectedOffer.offerId,
          status: "RET",
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
        condition: item.condition,
        image: item.image,
        price: item.price,
        fromOffers: true
      }
    });
  };

  const handleCancelOffer = async (item) => {
    try {
      await instanceOffer.put(`/updateOffer`, {
        offererId: item.offererId,
        offerId: item.id,
        listingId: item.offeredListId,
        offerStatus: "IPTAL_EDILDI",
        bookId: item.offeredBook?.id || 0,
      });

      dispatch({
        type: 'UPDATE_OFFER_STATUS',
        id: item.offerId,
        status: "IPTAL_EDILDI"
      });

      // fetchOffers();
      console.log("Teklif Durumu:", item.offerStatus);

    } catch (error) {
      console.error("Teklif iptal edilirken hata:", error);
    }
  }

  return (
    <div className='offers'>
      <div className="send">
        <h2>Gönderdiğim Teklifler</h2>
        {sentOffers?.length > 0 ? (
          sentOffers.filter(item => item.offerStatus !== "IPTAL_EDILDI").map((item, index) => {
            const yourBook = item.offeredBook;
            const theirBook = item.offerList?.book;
            const theirUser = item.offerList?.owner;

            return (
              <div className="offer_info" key={index}>
                <HighlightOffOutlinedIcon onClick={() => handleCancelOffer(item)} className='offer_cancel_button' />
                <div>
                  <h4>Senin Kitabın</h4>
                  <img
                    src={yourBook?.image}
                    alt={yourBook?.title || "Senin kitabın"}
                    onClick={() => handleDetails(yourBook)}
                  />
                  <p>{yourBook?.title}</p>
                  <p>{yourBook?.author}</p>
                  <p>{yourBook?.isbn}</p>
                </div>

                <div>
                  <h4>Karşı Tarafın Kitabı</h4>
                  <img
                    src={theirBook?.image}
                    alt={theirBook?.title || "Karşı tarafın kitabı"}
                    onClick={() => handleDetails(theirBook)}
                  />
                  <p>{theirBook?.title}</p>
                  <p>{theirBook?.author}</p>
                  <p>{theirBook?.isbn}</p>
                  <p>{theirUser?.firstName} {theirUser?.lastName}</p>
                </div>

                <div className="accept_callback" style={{ fontSize: "14px" }}>
                  {item.offerStatus === "KABUL" ? (
                    <p style={{ color: "green" }}>Teklif kabul edildi!</p>
                  ) : item.offerStatus === "RET" ? (
                    <p style={{ color: "red" }}>Teklif reddedildi!</p>
                  ) : (
                    <p style={{ color: "orange" }}>Beklemede</p>
                  )}
                </div>
              </div>
            );
          })
        ) : (
          <p>Gönderilen hiç kitap yok.</p>
        )}
      </div>
      <div className="receive">
        <h2>Aldığım Teklifler</h2>
        {receivedOffers?.length > 0 ? (
          <>
            {console.log("ALINAN TEKLİFLER RENDER EDİLİYOR", receivedOffers)}
            {receivedOffers.map((item, index) => {
              const book = item.offeredBook;
              return (
                <div className="offer_info" key={index}>
                  <img
                    src={book?.image}
                    alt={book?.title || "Teklif edilen kitap"}
                    onClick={() => handleDetails(book)}
                  />
                  <div onClick={() => handleDetails(book)}>
                    <h4>{book.title}</h4>
                    <p>
                      <strong>Yazar: </strong>
                      {book.author}
                    </p>
                    <p>
                      <strong>Yayınevi: </strong>
                      {book.publisher}
                    </p>
                    <p>
                      <strong>Yayın Tarihi: </strong>
                      {book.publishedDate}
                    </p>
                    <p>
                      <strong>ISBN: </strong>
                      {book.isbn}
                    </p>
                    <p>
                      <strong>Kategori: </strong>
                      {book.category}
                    </p>
                  </div>
                  {item.offerStatus === "IPTAL_EDILDI" ? (
                    <div className="accept_callback" style={{ fontSize: "16px" }}>
                      <p style={{ color: "red" }}>Gönderen kişi teklifi iptal etti.</p>
                    </div>
                  ) : (
                    <div className="receive_buttons">
                      <button
                        onClick={() =>
                          acceptOffer(
                            item.offerId,
                            item.offererId,
                            item.offerListId,
                            item.offeredBook?.id
                          )
                        }
                      >
                        Teklifi kabul et
                      </button>
                      <button onClick={() => handleRejectOffer(item)}>
                        Teklifi reddet
                      </button>
                    </div>
                  )}
                </div>
              );
            })}
          </>
        ) : (
          <p>Alınan hiç teklif yok.</p>
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

