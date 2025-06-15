<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kitabınız Satıldı!</title>
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
            text-align: center;
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
            color: #ffffff !important;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
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
<div class="container">
    <p class="message">Merhaba,<strong>${listBookName}</strong> kitabınız satıldı.</p>
    <p>İlan No: ${listId}</p>
    <p>Fiyat: ${price}</p>
    <img class="book-image" src="${listBookImage}" alt="${listBookName}">
    <div class="buyer-info">
        <p><strong>Lütfen kitabınızı en kısa süre içerisinde kargolayın!</strong></p>
        <p><strong>Alıcı Bilgileri:</strong></p>
        <p><strong>Ad Soyad:</strong> ${buyerName}</p>
        <p><strong>Adres:</strong> ${buyerAddress}</p>
        <p><strong style="color: red">Son Kargolama Tarihi: ${lastShippingDate}</strong></p>
               <p class="message">Satış ve kargo bilgileri için aşağıdaki butona tıklayabilirsiniz:</p>
        <div style="text-align: center; margin-top: 20px;">
            <a href="${salesLink}" class="offer-link">Satışlara Gitmek İçin Tıklayın</a>
        </div>

    </div>

</div>
</body>
</html>
