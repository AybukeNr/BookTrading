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
<div class="container">
    <p class="message">Merhaba,<strong>${listBookName}</strong> kitabınız satıldı.</p>
    <p>İlan No: ${listId}</p>
<#--    <img class="book-image" src="data:image/jpeg;base64,${Base64.getEncoder().encodeToString(listBookImage)}" alt="${listBookName}">-->

    <div class="buyer-info">
        <p><strong>Lütfen kitabınızı en kısa süre içerisinde kargolayın!</strong></p>
        <p><strong>Alıcı Bilgileri:</strong></p>
        <p><strong>Ad Soyad:</strong> ${buyerName}</p>
        <p><strong>Adres:</strong> ${buyerAddress}</p>
        <p><strong style="color: red">Son Kargolama Tarihi: ${lastShippingDate}</strong></p>
    </div>

    <p class="message">Satış ve kargo bilgileri için aşağıdaki butona tıklayabilirsiniz:</p>
    <a class="offer-link" href="">Satış Bilgilerini Gör</a>
</div>
</body>
</html>
