<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kitabınız Kargolandı!</title>
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
    </style>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px;">
<table style="max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px;">
    <tr>
        <td style="text-align: center;">
            <h2 style="color: #333333;">Kargonuz Yola Çıktı!</h2>
        </td>
    </tr>
    <tr>
        <td style="text-align: center;">
            <p class="message"><strong> ${listBookName}  </strong>kitabınız yola çıktı.</p>
            <img class="book-image" src="${listBookImage}">
            <p style="color: #000;">Yazar: ${author}</p>
            <p style="color: #000;">Yayınevi: ${publisher}</p>
            <p style="color: #000;">Basım Yılı: ${publishDate}</p>
            <p style="color: #000;">Kategori: ${category}</p>
            <p style="color: #000;">Alıcı: ${receiverName}</p>
            <p style="color: #000;">Teslimat Adresi: ${address}</p>
            <p style="color: #000;">Kargo Takip Numarası : ${trackingNumber} </p>
        </td>
    </tr>

    <tr>
        <td style="text-align: center; padding: 10px 0; border-top: 1px solid #eeeeee;">
            <p style="color: #999999; font-size: 12px;">&copy; 2025 BookSwap. Tüm hakları saklıdır.</p>
        </td>
    </tr>
</table>
</body>
</html>