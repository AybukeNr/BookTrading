<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Takas Tamamladı</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 5px;
            text-align: center;
        }
        .message {
            color: #666666;
        }
        .book-image {
            max-width: 100px;
            height: auto;
            margin: 10px 0;
        }
        .offer-link {
            display: inline-block;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 20px;
        }
        .buyer-info {
            text-align: left;
            margin-top: 15px;
            background-color: #f9f9f9;
            padding: 10px;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<tr>
    <td style="text-align: center;">
        <h2 style="color: #333333;">Takas Durumu : <strong>${exchangeStatus}</strong></h2>
    </td>
</tr>
<div class="container">
    <p class="message">Merhaba, <strong>${listBookName}</strong> kitabınız için takas tamamlandı.</p>
    <img class="book-image" src="data:image/jpeg;base64,${Base64.getEncoder().encodeToString(listBookImage)}" alt="${listBookName}">
    <p class="message">Teklif edilen kitap: <strong>${offeredBookName}</strong></p>
    <img class="book-image" src="data:image/jpeg;base64,${Base64.getEncoder().encodeToString(offeredBookImage)}" alt="${offeredBookName}">
    <div class="buyer-info">
        <h1><strong>Alıcı Bilgileri</strong></h1>
        <p><strong>Ad Soyad:</strong> ${buyerName}</p>
        <p><strong>IBAN:</strong> ${iban}</p>
        <p><strong>Toplam Miktar:</strong> ${refundAmount}</p>
    </div>

    <p class="message">Takas ve kargo bilgileri için aşağıdaki bağlantıya tıklayabilirsiniz:</p>
    <a class="offer-link" href="${exchangesLink}">Takas Bilgilerini Gör</a>
</div>
</body>
</html>
