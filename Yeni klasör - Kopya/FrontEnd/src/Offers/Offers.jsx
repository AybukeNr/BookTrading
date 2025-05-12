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

      fetchOffers();
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
        fetchOffers();

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
        listId: item.offerListId,
        fromOffers: true,
        bookDetail: {
          book: item.offeredBook,
          user: item.offerer,
          price: item.offeredBook?.price || null,
          fromOffers: true,
        }
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

      fetchOffers();
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
                  <h4>Teklif edilen</h4>
                  <img src={yourBook?.image} alt={yourBook?.title || "Senin kitabın"} onClick={() => handleDetails(yourBook)} />
                  <p>{yourBook?.isbn}</p>
                  <p>{yourBook?.title}/{yourBook?.author}</p>
                </div>

                <div>
                  <h4>Teklif verilen</h4>
                  <img src={theirBook?.image} alt={theirBook?.title || "Karşı tarafın kitabı"} onClick={() => handleDetails(theirBook)} />
                  <p>{theirBook?.isbn}/{theirBook?.title}/{theirBook?.author}</p>
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
              const user = item.offerer;

              return (
                <div className="offer_info" key={index}>
                  <div>
                    <h4>İlandaki Teklifim</h4>
                    <img src={item.offerList?.book?.image} alt={item.offerList?.book?.title || "İlandaki kitap"} />
                    <h4>{item.offerList?.book?.title}</h4>
                    <p><strong>Yazar: </strong>{item.offerList?.book?.author}</p>
                    <p><strong>ISBN: </strong>{item.offerList?.book?.isbn}</p>
                    <p><strong>Açıklama: </strong>{item.offerList?.book?.description}</p>
                    <p><strong>Durum: </strong>{item.offerList?.book?.condition}</p>
                  </div>
                  <div>
                    <h4>Alınan Teklif</h4>
                    <img src={book?.image} alt={book?.title || "Teklif edilen kitap"} onClick={() => handleDetails(book)} />
                    <div onClick={() => handleDetails(book)}>
                      <h4>{book.title}</h4>
                      <p><strong>Yazar: </strong>{book.author}</p>
                      <p><strong>ISBN: </strong>{book.isbn}</p>
                      <p><strong>Açıklama: </strong>{book.description}</p>
                      <p><strong>Durum: </strong>{book.condition}</p>
                      <p>{user.firstName} {user.lastName}</p>
                    </div>
                  </div>
                  {item.offerStatus === "IPTAL_EDILDI" ? (
                    <div className="accept_callback" style={{ fontSize: "16px" }}>
                      <p style={{ color: "blue" }}>Gönderen kişi teklifi iptal etti.</p>
                    </div>
                  ) : item.offerStatus === "KABUL" ? (
                    <div className="accept_callback" style={{ fontSize: "16px" }}>
                      <p style={{ color: "green" }}>Teklifi kabul ettiniz!</p>
                    </div>
                  ) : item.offerStatus === "RET" ? (
                    <div className="accept_callback" style={{ fontSize: "16px" }}>
                      <p style={{ color: "red" }}>Teklifi reddettiniz.</p>
                    </div>
                  ) : (
                    <div className="receive_buttons">
                      <button onClick={() => acceptOffer(item.offerId, item.offererId, item.offerListId, item.offeredBook?.id)}>Teklifi kabul et</button>
                      <button onClick={() => handleRejectOffer(item)}>Teklifi reddet</button>
                    </div>
                  )}
                </div>
              );
            })}
          </>
        ) : (
          <p>Alınan hiç teklif yok.</p>
        )}
        <div>

        </div>
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

