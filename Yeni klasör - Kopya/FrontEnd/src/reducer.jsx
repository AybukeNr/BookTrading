export const initialState = {
    bookshelf: [],
    bookList: [],
    searchedBooks: [],
    searchQuery: '',
    selectedCategory: null,
    selectedAdvertisedBook: null,
    selectedAdvertisement: [],
    bookDetail: [],
    userDetail: [],
    userAds: [],
    advertisedBook: [],
    advertisements: [
        { id: 1, title: 'Olasılıksız', author: 'Adam Fawer', isbn: 123, publisher: 'April Yayıncılık', publishedDate: '2005', category: 'Kurgu', price: 800, image: 'https://static.nadirkitap.com/fotograf/1255127/28/Kitap_20220724194404125512711.jpg' },
        { id: 2, title: 'Melekler ve Şeytanlar', author: 'Dan Brown', isbn: 12, publisher: 'Altın Kitaplar', publishedDate: '2005', category: 'Kurgu', image: 'https://s3.cloud.ngn.com.tr/kitantik/images/2023-02-17/1br9qfwle6wpd2u1ly2.jpg' },
    ],
    sentOffers: [],
    receivedOffers: [],
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
    return basket?.reduce((amount, item) => item.book.price + amount, 0);
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

        case 'EMPTY_BASKET':
            return {
                ...state,
                basket: [],
            };

        case 'SET_BASKET':
            return {
                ...state,
                basket: action.basket,
            };

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

        case 'SET_SELECTED_ADVERTISED_BOOK':
            return {
                ...state,
                selectedAdvertisedBook: action.selectedAdvertisedBook,
            };

        case 'SET_BOOK_LIST':
            return {
                ...state,
                bookList: action.books,
            };

        case 'SET_AD_BOOKS':
            return {
                ...state,
                advertisedBook: action.advertisedBook,
            }

        case 'SET_AD_UPDATED':
            return {
                ...state,
                adUpdated: action.payload,
            };

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
                sentOffers: [...state.sentOffers, action.item],
            };

        case 'ADD_TO_OFFER_RECEIVE':
            return {
                ...state,
                receivedOffers: [...state.receivedOffers, action.item],
            }

        case 'SET_OFFER_SENT':
            return {
                ...state,
                sentOffers: action.payload,
            };

        case 'SET_OFFER_RECEIVE':
            return {
                ...state,
                receivedOffers: action.payload,
            };

        case 'UPDATE_OFFER_STATUS':
            return {
                ...state,
                sentOffers: state.sentOffers.map((offer) =>
                    offer.id === action.id ? { ...offer, offerStatus: action.status } : offer),
            };

        case 'SET_TRADE_DATA':
            return {
                ...state,
                tradeData: {
                    ...state.tradeData,
                    offerId: action.payload.offerId,
                    offererId: action.payload.offererId,
                    offeredListId: action.payload.offeredListId,
                    bookId: action.payload.bookId,
                    offerer: action.payload.offerer,
                }
            };

        case 'SET_BOOK_DETAIL':
            return {
                ...state,
                bookDetail: action.bookDetail,
            };

        case 'SET_USER_DETAILS':
            return {
                ...state,
                userDetail: action.payload,
            };

        case 'SET_USER_ADS':
            return {
                ...state,
                userAds: action.payload,
            };

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