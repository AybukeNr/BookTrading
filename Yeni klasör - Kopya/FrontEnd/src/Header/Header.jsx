import React, { useEffect, useRef, useState } from 'react';
import '../Header/Header.css'
import SearchIcon from '@mui/icons-material/Search';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
// import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { Link, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import Badge from '@mui/material/Badge';
import { removeAuthToken, removeDecodedUserId } from '../auth';

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

function Header(props) {
    const [{ basket, user, notifications, searchQuery, bookList }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [input, setInput] = useState('');
    const [dropdownAccVisible, setdropdownAccVisible] = useState(false);
    const [dropdownCatVisible, setdropdownCatVisible] = useState(false);
    // const [notificationsVisible, setNotificationsVisible] = useState(false);
    // const [hasNotifications, setHasNotifications] = useState(true);
    const notificationRef = useRef(null);
    const dropdownAccRef = useRef(null);
    const dropdownCatRef = useRef(null);

    const handleAuthentication = async () => {
        if (!user) {
            navigate('/login');
        } else {
            removeAuthToken();
            removeDecodedUserId();
            dispatch({
                type: 'SET_USER',
                user: null
            });
            props.setIsAuthenticated(false);
            navigate('/login');
        }
    }

    const handleSearch = (e) => {
        e.preventDefault();
        dispatch({
            type: "SET_SEARCH_QUERY",
            query: input
        });
        const results = bookList.filter(book =>
            book.book.title.toLowerCase().includes(input.toLowerCase())
        );
        dispatch({
            type: "SET_SEARCHED_BOOKS",
            books: results
        });
        navigate("/search");
    };

    const dropdownAccount = () => {
        setdropdownAccVisible(!dropdownAccVisible);
    }

    const dropdownCategory = () => {
        setdropdownCatVisible(!dropdownCatVisible);
    }

    const handleCategory = (category) => {
        dispatch({
            type: 'SET_SELECTED_CATEGORY',
            category: category,
        });
        navigate("/");
    }

    // const toggleNotifications = () => {
    //     setNotificationsVisible(!notificationsVisible);
    //     if (hasNotifications) {
    //         setHasNotifications(false);
    //     }
    // };


    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownAccRef.current && !dropdownAccRef.current.contains(event.target)) {
                setdropdownAccVisible(false);
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

            <Link to='/' onClick={() => { dispatch({ type: 'SET_SELECTED_CATEGORY', category: '' });}}>
                <img className='header_logo' src='https://i.hizliresim.com/obkwl66.png' />
            </Link>

            <form className='header_search' onSubmit={handleSearch}>
                <input className='header_searchInput' type='text' placeholder='Aradığınız metni girin.' value={input} onChange={(e) => setInput(e.target.value)} />
                <SearchIcon className='header_searchIcon' type='submit' onClick={handleSearch} />
            </form>

            <div className='header_nav'>
                {user ? (
                    <div onClick={handleAuthentication} className='header_option'>
                        <span className='header_optionLineTwo'>Çıkış Yap</span>
                    </div>
                ) : (
                    <Link to="/login" className='header_option'>
                        <div className='header_option'>
                            <span className='header_optionLineTwo'>Giriş Yap</span>
                        </div>
                    </Link>
                )}
                <div className='header_optionCategory' onClick={dropdownCategory} ref={dropdownCatRef}>
                    <span className='header_optionLineTwo'>Kategoriler</span>
                    {dropdownCatVisible && (
                        <div className="dropdown_category">
                            <ul>
                                <li onClick={() => handleCategory('Bilgisayar Bilimi')}>Bilgisayar Bilimi</li>
                                <li onClick={() => handleCategory('Bilim')}>Bilim</li>
                                <li onClick={() => handleCategory('Biyografi')}>Biyografi</li>
                                <li onClick={() => handleCategory('Çizgi Roman')}>Çizgi Roman</li>
                                <li onClick={() => handleCategory('Din')}>Din</li>
                                <li onClick={() => handleCategory('Dram')}>Dram</li>
                                <li onClick={() => handleCategory('Eğitim')}>Eğitim</li>
                                <li onClick={() => handleCategory('Felsefe')}>Felsefe</li>
                                <li onClick={() => handleCategory('Gençlik Romanları')}>Gençlik Romanları</li>
                                <li onClick={() => handleCategory('İş')}>İş</li>
                                <li onClick={() => handleCategory('Kişisel Gelişim')}>Kişisel Gelişim</li>
                                <li onClick={() => handleCategory('Kurgu')}>Kurgu</li>
                                <li onClick={() => handleCategory('Psikoloji')}>Psikoloji</li>
                                <li onClick={() => handleCategory('Şiir')}>Şiir</li>
                                <li onClick={() => handleCategory('Toplum Bilimi')}>Toplum Bilimi</li>
                                <li onClick={() => handleCategory('Spor')}>Spor</li>
                                <li onClick={() => handleCategory('Tarih')}>Tarih</li>
                                <li onClick={() => handleCategory('Yemek')}>Yemek Pişirme</li>
                            </ul>
                        </div>
                    )}
                </div>

                <div className='header_optionAccount' onClick={dropdownAccount} ref={dropdownAccRef}>
                    <AccountBoxOutlinedIcon />
                    {dropdownAccVisible && (
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

                {/* <div className='header_optionNotification' onClick={toggleNotifications} ref={notificationRef}>
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
                </div> */}

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