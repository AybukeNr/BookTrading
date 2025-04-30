import React, { useEffect, useRef, useState } from 'react';
import '../Header/Header.css'
import SearchIcon from '@mui/icons-material/Search';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { Link, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import Badge from '@mui/material/Badge';
// import { instance } from '../axios';

// const instance = axios.create({
//     baseURL: 'http://localhost:8080/api',
// });

// instance.interceptors.request.use(
//     (config) => {
//         const token = localStorage.getItem('authToken');
//         if (token) {
//             config.headers.Authorization = `Bearer ${token}`;
//         }
//         return config;
//     },
//     (error) => Promise.reject(error)
// );

function Header() {
    const [{ basket, user, notifications, searchQuery, bookList }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [dropdownVisible, setdropdownVisible] = useState(false);
    const [dropdownCatVisible, setdropdownCatVisible] = useState(false);
    const [notificationsVisible, setNotificationsVisible] = useState(false);
    const [hasNotifications, setHasNotifications] = useState(true);
    const notificationRef = useRef(null);
    const dropdownRef = useRef(null);
    const dropdownCatRef = useRef(null);

    const handleAuthentication = async() => {
        if (user) {
            localStorage.removeItem('authToken');
            dispatch({
                type: 'SET_USER',
                user: null
            });
            navigate('/login');
        } else {
            navigate('/login');
        }
    }

    //search

    // const handleSearchChange = async (e) => {
    //     const query = e.target.value;
    //     dispatch({ type: 'SET_SEARCH_QUERY', query });

    //     if (!query) {
    //         dispatch({ type: "SET_SEARCHED_BOOKS", books: bookList });
    //         return;
    //     }

    //     try {
    //         const response = await instance.get(`/books/search?query=${query}`);
    //         dispatch({ type: "SET_SEARCHED_BOOKS", books: response.data });
    //     } catch (error) {
    //         console.error("Arama sırasında hata oluştu", error);
    //     }
    // };

    const handleSearchChange = (e) => {
        dispatch({
            type: 'SET_SEARCH_QUERY',
            query: e.target.value,
        }),
        searchBooks(e.target.value);
    };

    const searchBooks = (query) => {
        if (!query) {
            dispatch({ type: "SET_SEARCHED_BOOKS", books: bookList });
            return;
        }
    
        const results = bookList.filter(book =>
            book.title.toLowerCase().includes(query.toLowerCase()) ||
            book.author.toLowerCase().includes(query.toLowerCase())
        );
    
        dispatch({ type: "SET_SEARCHED_BOOKS", books: results });
    };
    
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (!searchQuery.trim()) return;
        searchBooks(searchQuery);
    };

    //Account dropdown
    const dropdown = () => {
        setdropdownVisible(!dropdownVisible);
    }

    //Category Dropdown
    const dropdownCategory = () => {
        setdropdownCatVisible(!dropdownCatVisible);
    }

    const handleCategory = (category) => {
        dispatch({
            type: 'SET_SELECTED_CATEGORY',
            category: category,
        });
    }

    //notification
    const toggleNotifications = () => {
        setNotificationsVisible(!notificationsVisible);
        if (hasNotifications) {
            setHasNotifications(false);
        }
    };


    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setdropdownVisible(false);
            }
            if (dropdownCatRef.current && !dropdownCatRef.current.contains(event.target)) {
                setdropdownCatVisible(false);
            }
            if (notificationRef.current && !notificationRef.current.contains(event.target)) {
                setNotificationsVisible(false);
            }
        };
        document.addEventListener('click', handleClickOutside);
        return () => {
            document.removeEventListener('click', handleClickOutside);
        };
    }, []);


    return (
        <div className='header'>

            <Link to='/'>
                <img className='header_logo' src='https://i.hizliresim.com/obkwl66.png' />
            </Link>

            <form className='header_search' onSubmit={handleSearchSubmit}>
                <input className='header_searchInput' type='text' placeholder='Aradığınız metni girin.' value={searchQuery} onChange={handleSearchChange} />
                <SearchIcon className='header_searchIcon' onClick={handleSearchSubmit} />
            </form>
        
            <div className='header_nav'>
                <Link to={!user && '/login'}>
                    <div onClick={handleAuthentication} className='header_option'>
                        <span className='header_optionLineTwo'>{user ? 'Çıkış Yap' : 'Giriş Yap'}</span>
                    </div>
                </Link>

                <div className='header_optionCategory' onClick={dropdownCategory} ref={dropdownCatRef}>
                    <span className='header_optionLineTwo'>Kategoriler</span>
                    {dropdownCatVisible && (
                        <div className="dropdown_category">
                            <ul>
                                <li onClick={() => handleCategory('Computers')}>Bilgisayar Bilimi</li>
                                <li onClick={() => handleCategory('Science')}>Bilim</li>
                                <li onClick={() => handleCategory('Biography')}>Biyografi</li>
                                <li onClick={() => handleCategory('Comics')}>Çizgi Roman</li>
                                <li onClick={() => handleCategory('Religion')}>Din</li>
                                <li onClick={() => handleCategory('Drama')}>Dram</li>
                                <li onClick={() => handleCategory('Education')}>Eğitim</li>
                                <li onClick={() => handleCategory('Philosophy')}>Felsefe</li>
                                <li onClick={() => handleCategory('Juvenile_Ficton')}>Gençlik Kurgu</li>
                                <li onClick={() => handleCategory('Bussiness')}>İş</li>
                                <li onClick={() => handleCategory('Self_Help')}>Kişisel Gelişim</li>
                                <li onClick={() => handleCategory('Ficton')}>Kurgu</li>
                                <li onClick={() => handleCategory('Pyschology')}>Psikoloji</li>
                                <li onClick={() => handleCategory('Poetry')}>Şiir</li>
                                <li onClick={() => handleCategory('Social_Sciences')}>Toplum Bilimi</li>
                                <li onClick={() => handleCategory('Sports')}>Spor</li>
                                <li onClick={() => handleCategory('History')}>Tarih</li>
                                <li onClick={() => handleCategory('Cooking')}>Yemek Pişirme</li>
                            </ul>
                        </div>
                    )}
                </div>

                <div className='header_optionAccount' onClick={dropdown} ref={dropdownRef}>
                    <AccountBoxOutlinedIcon />
                    {dropdownVisible && (
                        <div className="dropdown_menu">
                            <ul>
                                <Link to='/myAccount'><li>Hesap Bilgilerim</li></Link>
                                <Link to='/bookshelf'><li>Kitaplığım</li></Link>
                                <Link to='/myAds'><li>İlanlarım</li></Link>
                                <Link to='/myOffers'><li>Tekliflerim</li></Link>
                                <Link to='/myTrades'><li>Takaslarım</li></Link>
                                <Link to='/mySales'><li>Satışlarım</li></Link>
                            </ul>
                        </div>
                    )}
                </div>

                <div className='header_optionNotification' onClick={toggleNotifications} ref={notificationRef}>
                    <Badge color='warning' variant={hasNotifications ? "dot" : undefined}>
                        <NotificationsNoneIcon />
                    </Badge>
                    {notificationsVisible && (
                        <div className="notifications">
                            <ul>
                                {notifications.map((notification, index) => (
                                    <ul key={index}>{notification}</ul>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>

                <Link to='/checkout'>
                    <div className='header_optionBasket'>
                        <ShoppingBasketIcon />
                        <span className='header_optionLineTwo header_basketCount'>{basket?.length}</span>
                    </div>
                </Link>
            </div>
        </div>
    )
}

export default Header 