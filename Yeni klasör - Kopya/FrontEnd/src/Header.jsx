import React, { useEffect, useRef, useState } from 'react';
import './Header.css'
import SearchIcon from '@mui/icons-material/Search';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { Link, useNavigate } from 'react-router-dom';
import { useStateValue } from './StateProvider';
import Badge from '@mui/material/Badge';

function Header() {
    const [{ basket, user }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState("");
    const [dropdownVisible, setdropdownVisible] = useState(false);
    const [notificationsVisible, setNotificationsVisible] = useState(false);
    const [hasNotifications, setHasNotifications] = useState(true);
    const notificationRef = useRef(null);
    const dropdownRef = useRef(null);

    const handleAuthentication = () => {
        if (user) {
            localStorage.removeItem('authToken');
            dispatch({
                type: 'SET_USER',
                user: null
            });
            navigate('/login');
        } else {

        }
    }

    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
    };

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        console.log("Arama Yapılıyor: ", searchQuery);
        // API'ye istek yapılacak
    };

    const dropdown = () => {
        setdropdownVisible(!dropdownVisible);
    }

    const toggleNotifications = () => {
        setNotificationsVisible(!notificationsVisible);
        if (hasNotifications) {
            setHasNotifications(false);
        }
    };

    const notifications = [
        'Yeni bir mesajınız var.',
        'Kitap takası teklifi alındı.',
        'Profiliniz güncellendi.',
    ];

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setdropdownVisible(false);
            }
            if (notificationRef.current && !notificationRef.current.contains(event.target)) {
                setNotificationsVisible(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);


    return (
        <div className='header'>

            <Link to='/'>
                <img className='header_logo' src='https://i.hizliresim.com/obkwl66.png' />
            </Link>

            <div className='header_search' onSubmit={handleSearchSubmit}>
                <input className='header_searchInput' type='text' placeholder='Aradığınız metni girin.' value={searchQuery} onChange={handleSearchChange} />
                <SearchIcon className='header_searchIcon' onClick={handleSearchSubmit}/>
            </div>

            <div className='header_nav'>
                <Link to={!user && '/login'}>
                    <div onClick={handleAuthentication} className='header_option'>
                        <span className='header_optionLineOne'>Merhaba!</span>
                        <span className='header_optionLineTwo'>{user ? 'Çıkış Yap' : 'Giriş Yap'}</span>
                    </div>
                </Link>
                <Link to='/bookshelf'>
                    <div className='header_option'>
                        <span className='header_optionLineOne'>Benim</span>
                        <span className='header_optionLineTwo'>Kitaplığım</span>
                    </div>
                </Link>

                <div className='header_optionAccount' onClick={dropdown} ref={dropdownRef}>
                    <AccountBoxOutlinedIcon />
                    {dropdownVisible && (
                        <div className="dropdown_menu">
                            <ul>
                                <Link to='/myAccount'><li>Hesap Bilgilerim</li></Link>
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