export const initialState = {
    bookshelf: [
        { id: 1, title: 'Olasılıksız', author: 'Adam Fawer', isbn: 123, publisher: 'April Yayıncılık', publishedDate: '2005', category: 'Kurgu', price: 800, image: 'https://static.nadirkitap.com/fotograf/1255127/28/Kitap_20220724194404125512711.jpg' },
        { id: 2, title: 'Melekler ve Şeytanlar', author: 'Dan Brown', isbn: 12, publisher: 'Altın Kitaplar', publishedDate: '2005', category: 'Gizem', image: 'https://s3.cloud.ngn.com.tr/kitantik/images/2023-02-17/1br9qfwle6wpd2u1ly2.jpg' },
        { id: 4, title: 'Olasılıksız', author: 'Adam Fawer', isbn: 1, publisher: 'April Yayıncılık', publishedDate: '2000', category: 'Kurgu', price: 500, image: 'https://static.nadirkitap.com/fotograf/1255127/28/Kitap_20220724194404125512711.jpg' },
        { id: 5, title: 'Bütün Şiirleri', author: 'Sabahattin Ali', isbn: 1234, publisher: 'Yapı Kredi Yayınları', publishedDate: '2012', category: 'Şiir', image: 'https://avatars.mds.yandex.net/i?id=0ba43a652eca109c85bfea51086c6fcb4638c6d2-4551157-images-thumbs&n=13' },
        { id: 6, title: 'Sofienin Dünyası', author: 'Jostein Gaarder', isbn: 1237, publisher: 'Pan Yayıncılık', publishedDate: '1991', category: 'Felsefe', image: 'https://s3.cloud.ngn.com.tr/kitantik/images/2023-11-29/1br9qfwlpi2ow751yf3.jpg' },

    ],
    bookList: [
        { title: 'Nutuk(Özel ciltli)', author: 'Mustafa Kemal Atatürk', isbn: '123', publisher: 'İş Bankası Yayıncılık', publishedDate: '1927', category: 'Tarih', price: 1850, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRXJGIvVqD7poyHq7VGM6hVcnGBRw6tKvsGv-FYam-rSXlmlufSY7H_-ehfRUNKYZ_ods&usqp=CAU' },
        { title: 'Empati', author: 'Adam Fawer', isbn: '1234', publisher: 'April Yayıncılık', publishedDate: '2007', category: 'Kurgu', price: 200, image: 'https://bunuokudum.com/uploads/images/202208/img_1920x_63053a8a9be0b8-88474876-10950506.jpeg' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '12', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '14', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', price: 500, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '1268', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
    ],
    selectedCategory: null,
    bookDetail: [],
    advertisedBook: [],
    offerSent: [],
    offerReceive: [
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '12', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
    ],
    basket: [],
    notifications: [
        'Yeni bir mesajınız var.',
        'Kitap takası teklifi alındı.',
        'Profiliniz güncellendi.',
    ],
    user: null
};

export const getBasketTotal = (basket) => {
    return basket?.reduce((amount, item) => item.price + amount, 0);
}

const reducer = (state, action) => {
    console.log(action);

    switch (action.type) {
        case 'ADD_TO_BASKET':
            return {
                ...state,
                basket: [...state.basket, action.item],
            };

        case 'REMOVE_FROM_BASKET':
            const index = state.basket.findIndex((BasketItem) => BasketItem.id === action.id);
            let newBasket = [...state.basket]

            if (index >= 0) {
                newBasket.splice(index, 1);
            } else {
                console.warn(`Ürün (id: ${action.id}) sepette olmadığı için kaldırılamıyor!`);
            }

            return {
                ...state,
                basket: newBasket
            }

        case 'SET_SELECTED_CATEGORY':
            return {
                ...state,
                selectedCategory: action.category,
            };

        case 'ADD_TO_AD':
            return {
                ...state,
                advertisedBook: [...state.advertisedBook, action.item],
            }
        case 'REMOVE_FROM_AD':
            return {
                ...state,
                advertisedBook: state.advertisedBook.filter((book) => book.id !== action.id),
            }

        // case 'REMOVE_FROM_BOOKSHELF':
        //     const bookIndex = state.bookshelf.findIndex((book) => book.id === action.id);
        //     let newBookshelf = [...state.bookshelf]

        //     if (bookIndex >= 0) {
        //         newBookshelf.splice(bookIndex, 1);
        //     } else {
        //         console.warn(`Kitap (id: ${action.id}) kitaplıkta bulunamadı!`);
        //     }
        //     return {
        //         ...state,
        //         bookshelf: newBookshelf
        //     }

        case 'REMOVE_FROM_BOOKSHELF':
            return {
                ...state,
                bookshelf: state.bookshelf.filter((book) => book.id !== action.id),
            };

        case 'ADD_TO_BOOKSHELF':
            return {
                ...state,
                bookshelf: [...state.bookshelf, action.book],
            };

        case 'ADD_TO_OFFER_SENT':
            return {
                ...state,
                offerSent: [...state.offerSent, action.item],
            };

        case 'ADD_TO_OFFER_RECEIVE':
            return {
                ...state,
                offerReceive: [...state.offerReceive, action.item],
            }

        case 'SET_USER':
            return {
                ...state,
                user: action.user,
            }
        default:
            return state;
    }
};

export default reducer;