<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>İlanınıza Teklif Geldi!</title>
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
    </style>
</head>
<body>
<div class="container">
    <p class="message">Merhaba, <strong>${listBookName}</strong> kitabınıza bir teklif geldi.</p>
    <p>İlan no: ${listId}</p>
    <img class="book-image" src="${listBookImage}" >
    <p class="message">Teklif edilen kitap: <strong>${offeredBookName}</strong></p>
    <img class="book-image" src="${offeredBookImage}" >
    <p class="message">Teklifi görmek için aşağıdaki bağlantıya tıklayabilirsiniz:</p>
         <a class="offer-link" href="${offersLink}">Teklifi Gör</a>
</div>
</body>
</html>
