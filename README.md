# BookTrading

1. Kullanıcı Servisi (User Service)
Bu servis, kullanıcı kaydı, giriş, kullanıcı profili yönetimi ve kişisel kitaplıkların oluşturulmasını yönetir.

Özellikler:

Kullanıcı kaydı ve giriş işlemleri
Kullanıcının ilgi kategorilerini seçmesi
Kişisel kitaplık yönetimi (kitap ekleme, düzenleme, silme)
API Örnekleri:

POST /users/register: Kullanıcı kaydı
GET /users/{userId}/library: Kullanıcının kitaplığını getir
POST /users/{userId}/library: Kitap ekle
Veritabanı:

Kullanıcı bilgileri (PostgreSQL)
Kitaplık verileri (MongoDB)
2. Kitap Servisi (Book Service)
Kitap bilgilerini ve kullanıcıların kitaplıklarını yönetir.

Özellikler:

Kitap ekleme, güncelleme, silme
Kitapların durum bilgisi ve görselleri
API Örnekleri:

POST /books: Yeni bir kitap ekle
GET /books/{bookId}: Kitap detaylarını getir
Veritabanı:

Kitap bilgileri (MongoDB)
3. İlan Servisi (Listing Service)
Kullanıcıların satış ve takas ilanları oluşturmasını ve bu ilanları yönetmesini sağlar.

Özellikler:

İlan oluşturma (satış veya takas)
İlan düzenleme ve silme
İlan detaylarını görüntüleme
API Örnekleri:

POST /listings: Yeni bir ilan oluştur
GET /listings/{listingId}: İlan detaylarını getir
PUT /listings/{listingId}: İlanı güncelle
Veritabanı:

İlan bilgileri (PostgreSQL)
4. Takas Servisi (Exchange Service)
Kitap takas tekliflerini yönetir ve takas işlemlerini gerçekleştirir.

Özellikler:

Teklif gönderme ve alma
Güvence bedeli işlemleri
Kargo bilgilerini yönetme ve takas sürecini takip etme
API Örnekleri:

POST /exchanges/{listingId}/offer: Takas teklifi gönder
POST /exchanges/{exchangeId}/deposit: Güvence bedeli yatır
POST /exchanges/{exchangeId}/shipping: Kargo bilgilerini gir
Veritabanı:

Teklif ve takas bilgileri (PostgreSQL)
Kargo takip bilgileri (MongoDB)
5. Mesajlaşma Servisi (Messaging Service)
Kullanıcılar arasındaki mesajlaşma işlemlerini yönetir.

Özellikler:

Mesaj gönderme ve alma
Mesajların geçmişini görüntüleme
API Örnekleri:

POST /messages: Yeni bir mesaj gönder
GET /messages/conversation/{userId1}/{userId2}: İki kullanıcı arasındaki mesajları getir
Veritabanı:

Mesajlar (MongoDB)
6. Öneri Servisi (Recommendation Service)
Kullanıcıya kişisel kitaplık ve ilgi kategorilerine göre satış ve takas ilan önerileri sunar.

Özellikler:

Öneri algoritmasının çalıştırılması
Kullanıcının geçmiş gezintilerine ve kitaplık bilgilerine göre öneri oluşturma
API Örnekleri:

GET /recommendations/{userId}: Kullanıcıya özel ilan önerilerini getir
Veritabanı:

Kullanıcı davranış verileri ve öneriler (Redis ve MongoDB)
7. Bildirim Servisi (Notification Service)
Kullanıcıya teklif, mesaj veya sistemle ilgili bildirimleri iletir.

Özellikler:

Teklif bildirimleri
Mesajlaşma bildirimleri
Kargo ve güvence bedeli işlemleri için bildirimler
API Örnekleri:

POST /notifications: Yeni bir bildirim gönder
GET /notifications/{userId}: Kullanıcının bildirimlerini getir
Veritabanı:

Bildirimler (Redis veya PostgreSQL)