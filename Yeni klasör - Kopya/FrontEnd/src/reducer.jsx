export const initialState = {
    bookshelf: [],
    bookList: [
        { title: 'Nutuk(Özel ciltli)', author: 'Mustafa Kemal Atatürk', isbn: '123', publisher: 'İş Bankası Yayıncılık', publishedDate: '1927', category: 'Tarih', price: 1850, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRXJGIvVqD7poyHq7VGM6hVcnGBRw6tKvsGv-FYam-rSXlmlufSY7H_-ehfRUNKYZ_ods&usqp=CAU' },
        { title: 'Empati', author: 'Adam Fawer', isbn: '1234', publisher: 'April Yayıncılık', publishedDate: '2007', category: 'Kurgu', price: 200, image: 'https://bunuokudum.com/uploads/images/202208/img_1920x_63053a8a9be0b8-88474876-10950506.jpeg' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '12', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '14', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', price: 500, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
        { title: 'Hesaplaşma', author: 'Atakan Büyükdağ', isbn: '1268', publisher: 'Destek Yayınları', publishedDate: '2017', category: 'Tarih', image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPmczQk_DGGYwPRkGaGlyd3hZ_1zMEB0Veew&s' },
    ],
    searchedBooks: [],
    searchQuery: '',
    selectedCategory: null,
    bookDetail: [],
    advertisedBook: [],
    advertisements: [
        { id: 1, title: 'Olasılıksız', author: 'Adam Fawer', isbn: 123, publisher: 'April Yayıncılık', publishedDate: '2005', category: 'Kurgu', price: 800, image: 'https://static.nadirkitap.com/fotograf/1255127/28/Kitap_20220724194404125512711.jpg' },
        { id: 2, title: 'Melekler ve Şeytanlar', author: 'Dan Brown', isbn: 12, publisher: 'Altın Kitaplar', publishedDate: '2005', category: 'Kurgu', image: 'https://s3.cloud.ngn.com.tr/kitantik/images/2023-02-17/1br9qfwle6wpd2u1ly2.jpg' },
    ],
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
    user: null,
    users: [
        { id: 1, firstname: 'Senem', lastname: 'Çayıroğlu', email: 'senem@gmail.com', telephone: '05456328152', iban: 'TR12 3456 7890 1234 5678 9012 34', address: 'İstanbul', password: '123456' },
        { id: 2, firstname: 'Aybüke Nur', lastname: 'Çorapçı', email: 'aybuq@gmail.com', telephone: '05423678452', iban: 'TR12 3456 7890 1580 5778 4569 34', address: 'İstanbul', password: '1234567' },
        { id: 3, firstname: 'Beyzanur', lastname: 'Aydın', email: 'beyza@gmail.com', telephone: '05414789652', iban: 'TR12 3456 7890 2244 5434 7890 34', address: 'Eskişehir', password: '1234568' }
    ],
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

        case "SET_SEARCH_QUERY":
            return {
                ...state,
                searchQuery: action.query,
            };
        case "SET_SEARCHED_BOOKS":
            return {
                ...state,
                searchedBooks: action.books,
            };

        case 'SET_SELECTED_CATEGORY':
            return {
                ...state,
                selectedCategory: action.category,
            };

        case 'SET_AD_BOOKS':
            return {
                ...state,
                advertisedBook: action.advertisedBook,
            }

        case 'ADD_TO_AD':
            return {
                ...state,
                advertisedBook: [...state.advertisedBook, action.item],
            }
        case 'REMOVE_FROM_AD':
            return {
                ...state,
                advertisedBook: state.advertisedBook.filter(item => item.listId !== action.id),
            }

        case 'UPDATE_ADVERTISEMENTS':
            return {
                ...state,
                advertisements: state.advertisements.map((advertisedBook) => advertisedBook.id === action.advertisedBook.id ?
                    { ...advertisedBook, ...action.advertisedBook } : advertisedBook),
            }
        case 'SET_BOOKSHELF':
            return {
                ...state,
                bookshelf: action.bookshelf,
            };

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
        case 'UPDATE_IN_BOOKSHELF':
            return {
                ...state,
                bookshelf: state.bookshelf.map((book) => book.id === action.book.id ?
                    { ...book, ...action.book } : book),
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

        case 'REMOVE_USER':
            return {
                ...state,
                users: state.users.filter((user) => user.id !== action.id),
            };

        case 'UPDATE_USER_ACCOUNT':
            return {
                ...state,
                user: state.user.map((account) => account.id === action.account.id ?
                    { ...account, ...action.account } : account),
            };

        default:
            return state;
    }
};

export default reducer;