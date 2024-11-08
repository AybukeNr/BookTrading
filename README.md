# BookTrading

1. Kullanıcı Servisi (User Service)
Kullanıcı Servisi, sistemin kullanıcı yönetimi ile ilgili tüm işlemlerini kapsar. Bu servis, kullanıcıların kayıt olması, giriş yapması ve profil bilgilerini yönetmesi gibi temel fonksiyonları sağlar. Kullanıcılar, kayıt olduktan sonra ilgi alanlarına göre kitap kategorileri seçerler ve bu seçimler, kişisel öneriler oluşturulurken kullanılır. Ayrıca, her kullanıcının kişisel kitaplığını yönetmesine izin verir; kullanıcılar bu kitaplığa kitap ekleyebilir, düzenleyebilir veya silebilir. Diğer servisler, kullanıcının bilgilerini ve kitaplık verilerini almak için bu servisi kullanır.

2. Kitap Servisi (Book Service)
Kitap Servisi, sistemdeki tüm kitap verilerinin merkezi yönetimini sağlar. Kullanıcıların eklediği kitaplar hakkında detaylı bilgiler (örneğin, kitap adı, yazar, tür, basım yılı, ISBN, durum) bu serviste saklanır. Kullanıcılar, kişisel kitaplıklarına yeni kitaplar ekleyebilir veya var olanları güncelleyebilir. Kitap Servisi, aynı zamanda Trade Servisi ile entegre çalışarak, kullanıcıların satış ve takas ilanları oluştururken kullanabilecekleri kitap bilgilerini sağlar. Öneri sistemi için de kitap verilerini sağlar, böylece kullanıcıya kişiselleştirilmiş ilan önerileri sunulabilir.

3. İlan Servisi (Listing Service)
İlan Servisi, kullanıcıların kitaplarını satmak veya takas etmek için ilanlar oluşturmasını ve yönetmesini sağlar. Kullanıcılar, kişisel kitaplıklarından seçtikleri kitaplarla satış veya takas ilanları oluşturabilirler. İlan oluştururken, kullanıcılar takas seçeneği veya satış fiyatı belirleyebilir. İlan Servisi, kullanıcıların sistemdeki diğer kullanıcıların ilanlarına göz atmasına ve bu ilanlara teklif göndermesine imkan tanır. Ayrıca, ilan detaylarını yönetir ve ilanlar üzerinde arama, filtreleme gibi işlemleri destekler.

4. Takas ve Satış Servisi (Trade Service)
Trade Servisi, kitapların hem takas hem de satış süreçlerini yönetir. Kullanıcılar, diğer kullanıcıların ilanlarına takas veya satın alma teklifinde bulunabilirler. Teklifler kabul edilirse, takas veya satış işlemleri başlatılır. Takas işlemlerinde her iki taraf da güvence bedeli yatırır. Güvence bedeli yatırıldıktan sonra kitapların kargoya verilmesi ve kargo takip numaralarının girilmesi gereklidir. Satış işlemlerinde ise alıcı, ödemeyi gerçekleştirdikten sonra kargo süreci başlar. Kitaplar alıcıya ulaştığında, taraflar işlemi onaylar ve güvence bedeli ya da satış tutarı ilgili kişilere aktarılır. Trade Servisi, kitap satın alma durumunu da yönetir ve kullanıcıların başarılı bir şekilde kitap satın almasını sağlar.

5. Öneri Servisi (Recommendation Service)
Öneri Servisi, kullanıcılara kişisel kitaplıkları, geçmiş gezintileri ve ilgi alanlarına göre dinamik öneriler sunar. Kullanıcıların kitap okuma alışkanlıkları, gezdiği ilan türleri ve kayıt sırasında seçtikleri ilgi kategorileri analiz edilerek, kişiselleştirilmiş takas veya satış ilanları önerilir. Öneri algoritması, kullanıcıların kişisel kitaplıklarında bulunan kitaplara veya ilgilendiği türlere yakın kitapları içeren ilanları ön plana çıkarır. Öneriler düzenli aralıklarla güncellenir ve kullanıcının ana sayfasına sunulur. Bu servis, kullanıcıların ilgisini çekebilecek yeni ilanlar keşfetmelerini teşvik eder.

6. Bildirim Servisi (Notification Service)
Bildirim Servisi, kullanıcıları sistemdeki önemli olaylar hakkında bilgilendirir. Kullanıcılar, takas veya satış teklifleri, kabul/red durumları, güvence bedeli yatırma hatırlatmaları, kargo sürecindeki gelişmeler gibi bildirimler alırlar. Ayrıca, satılan veya takas edilen kitaplar hakkında anlık bildirimler gönderilir. Bu servis, kullanıcıların sistemdeki etkileşimlerini aktif bir şekilde takip etmelerine ve süreçlerde zamanında aksiyon almalarına yardımcı olur.
