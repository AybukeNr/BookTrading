import React, { useEffect, useRef, useState } from "react";
import "../OfferPopUp/OfferPopUp.css";
import { useStateValue } from "../StateProvider";
import { useNavigate } from "react-router-dom";
import { instanceListing } from "../axios";
import { getAuthToken } from "../auth";
import { instanceOffer } from "../axios";
function OfferPopUp({ onClose }) {
  const [{ advertisedBook, selectedAdvertisedBook }, dispatch] = useStateValue();
  const [selectedBook, setSelectedBook] = useState(null);
  const navigate = useNavigate();
  const popupRef = useRef(null);
  const [exchangeBooks, setExchangeBooks] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    console.log("Seçilen ilan (karşı tarafın kitabı):", selectedAdvertisedBook);
  }, [selectedAdvertisedBook]);

  useEffect(() => {
    if (!advertisedBook || advertisedBook.length === 0) {
      const storedUserId = localStorage.getItem("userId");
      const fetchAdvertisedBooks = async () => {
        try {
          const response = await instanceListing.get(
            `/getListsByOwnerId?ownerId=${storedUserId}`,
            {
              headers: {
                Authorization: `Bearer ${getAuthToken()}`,
              },
            }
          );

          dispatch({
            type: "SET_AD_BOOKS",
            advertisedBook: response.data,
          });
        } catch (error) {
          console.error("OfferPopUp içinde veri yüklenemedi:", error);
        }
      };

      fetchAdvertisedBooks();
    }
  }, [advertisedBook, dispatch]);

  useEffect(() => {
    if (advertisedBook && advertisedBook.length > 0) {
      const filtered = advertisedBook.filter(
        (item) => item.type === "EXCHANGE" &&
          item.status !== "AWAITING_SHIPMENT" &&
          item.status !== "SUSPENDED"
      );
      setExchangeBooks(filtered);
    }
  }, [advertisedBook]);

  const handleSelect = (listId) => {
    setSelectedBook((prevId) => (prevId === listId ? null : listId));
  };

  const offerSubmit = async () => {
    const storedUserId = localStorage.getItem("userId");
    const bookToSend = exchangeBooks.find(
      (book) => book.listId === selectedBook
    );

    if (!storedUserId || !selectedAdvertisedBook || !bookToSend) {
      alert("Gerekli bilgiler eksik.");
      return;
    }

    setLoading(true);

    const offerRequest = {
      offererId: storedUserId,
      listingId: selectedAdvertisedBook.listId,
      offeredBookId: bookToSend.book.id,
    };

    try {
      const response = await instanceOffer.post("/createOffers", offerRequest, {
        headers: {
          Authorization: `Bearer ${getAuthToken()}`,
        },
      });

      console.log("Teklif başarıyla oluşturuldu:", response.data);

      dispatch({
        type: "ADD_TO_OFFER_SENT",
        item: {
          ...response.data,
          status: "GONDERILDI",
        },
      });

      navigate("/myOffers");
    } catch (error) {
      console.error("Teklif gönderme hatası:", error);
      alert("Teklif oluşturulurken bir hata oluştu.");
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (popupRef.current && !popupRef.current.contains(event.target)) {
        if (onClose) {
          onClose();
        }
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [onClose]);

  return (
    <div className="offerPopUp" ref={popupRef}>
      {exchangeBooks?.length > 0 ? (
        exchangeBooks.map((item) => (
          <div
            key={item.listId}
            className={`offerPopUp_book ${selectedBook === item.listId ? "selected" : ""}`}
            onClick={(e) => { e.stopPropagation(); handleSelect(item.listId) }}
          >
            <img
              src={item.book.image}
              alt={item.book.title || "Kitap görseli"}
            />
            <div className="offerPopUp_bookInfo">
              <p>
                <strong>{item.book.title}</strong>
              </p>
              <p><strong>ISBN: </strong>{item.book.isbn}</p>
              <p><strong>Yazar: </strong>{item.book.author}</p>
              <p><strong>Yayınevi: </strong>{item.book.publisher}</p>
              <p><strong>Yayın Tarihi: </strong>{item.book.publishedDate}</p>
              <p><strong>Kategori: </strong>{item.book.category}</p>
              <p><strong>Açıklama: </strong>{item.book.description}</p>
              <p><strong>Durumu: </strong>{item.book.condition}</p>
            </div>
          </div>
        ))
      ) : (
        <p>Takasa açık kitabınız şu anda yok.</p>
      )}
      <div>
        <button onClick={offerSubmit} className="offerPopUp_button">
          {loading ? 'Teklif Gönderiliyor...' : 'Teklif Gönder'}
        </button>
      </div>
    </div>
  );
}

export default OfferPopUp;