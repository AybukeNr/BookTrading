import React, { useEffect, useState } from "react";
import "../SoldBooks/SoldBooks.css";
import { instanceListing, instanceTransaction, instanceUser } from "../axios";

function SoldBooks() {
  const [sales, setSales] = useState([]);

  useEffect(() => {
    const fetchSales = async () => {
      try {
        const userId = localStorage.getItem("userId");
        const transactionRes = await instanceTransaction.get(`/transactions/getUsersSales?ownerId=${userId}`
        );
        const transactions = transactionRes.data;

        const detailedSales = await Promise.all(
          transactions.map(async (tx) => {
            const listRes = await instanceListing.get(`/getListsById?listId=${tx.listId}`);
            const offererRes = await instanceUser.get(`/getUserById?id=${tx.offererId}`);

            return {
              book: listRes.data.book,
              seller: listRes.data.user,
              buyer: offererRes.data,
              price: listRes.data.price,
            };
          })
        );

        setSales(detailedSales);
      } catch (error) {
        console.error("Satış verileri alınırken hata oluştu:", error);
      }
    };

    fetchSales();
  }, []);

  return (
    <div className="soldBooks">
      <h3>Satışlarım</h3>

      {sales.map((sale, index) => (
        <div key={index} className="book_Info">
          <h4>Satılan Kitap:</h4>
          <img src={sale.book.image} alt={sale.book.title} />
          <div className="book_Detail">
            <p>
              <strong>Başlık:</strong> {sale.book.title}
            </p>
            <p>
              <strong>Yazar:</strong> {sale.book.author}
            </p>
            <p>
              <strong>Yayınevi:</strong> {sale.book.publisher}
            </p>
            <p>
              <strong>Yayın Tarihi:</strong> {sale.book.publishedDate}
            </p>
            <p>
              <strong>ISBN:</strong> {sale.book.isbn}
            </p>
            <p>
              <strong>Kategori:</strong> {sale.book.category}
            </p>
            <p>
              <strong>Açıklama:</strong> {sale.book.description}
            </p>
            <p>
              <strong>Durum:</strong> {sale.book.condition}
            </p>
            <p>
              <strong>Fiyatı:</strong> {sale.price ?? "Belirtilmemiş"}
            </p>
          </div>

          <div className="personInfo">
            <div className="sellerInfo">
              <h4>Satıcı:</h4>
              <p>
                <strong>Ad Soyad:</strong> {sale.seller.firstName}{" "}
                {sale.seller.lastName}
              </p>
              <p>
                <strong>Email:</strong> {sale.seller.mailAddress}
              </p>
              <p>
                <strong>Adres:</strong> {sale.seller.address}
              </p>
            </div>

            <div className="buyerInfo">
              <h4>Alıcı:</h4>
              <p>
                <strong>Ad Soyad:</strong> {sale.buyer.firstName}{" "}
                {sale.buyer.lastName}
              </p>
              <p>
                <strong>Email:</strong> {sale.buyer.mailAddress}
              </p>
              <p>
                <strong>Adres:</strong> {sale.buyer.address}
              </p>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

export default SoldBooks;