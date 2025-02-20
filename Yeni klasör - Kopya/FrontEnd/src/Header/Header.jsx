import React, { useEffect, useRef, useState } from 'react';
import '../Header/Header.css'
import SearchIcon from '@mui/icons-material/Search';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { Link, useNavigate } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import Badge from '@mui/material/Badge';

function Header() {
    const [{ basket, user, notifications }, dispatch] = useStateValue();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState("");
    // const [searchResults, setSearchResults] = useState([]); // API sonuçları için state
    const [dropdownVisible, setdropdownVisible] = useState(false);
    const [dropdownCatVisible, setdropdownCatVisible] = useState(false);
    const [notificationsVisible, setNotificationsVisible] = useState(false);
    const [hasNotifications, setHasNotifications] = useState(true);
    const notificationRef = useRef(null);
    const dropdownRef = useRef(null);
    const dropdownCatRef = useRef(null);

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

    //search
    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
    };

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        // setLoading(true);
        // setError(null);

        // try {
        //     const response = await axios.get(`https://api.example.com/search`, {
        //         params: { query: searchQuery }
        //     });

        //     setSearchResults(response.data.results); 
        // } catch (error) {
        //     console.error("Arama sırasında hata oluştu:", error);
        //     setError("Arama sırasında bir hata meydana geldi.");
        // } finally {
        //     setLoading(false);
        // }

        console.log("Filtrelenmiş Sonuçlar:", filteredResults); //bu önceki kısım silinecek.
        // API'ye istek yapılacak 
        // Örnek: axios.get(`/search?query=${searchQuery}`)
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
                <SearchIcon className='header_searchIcon' onClick={handleSearchSubmit} />
            </div>

            {/* arama kısmı */}
            {/* {loading && <p>Aranıyor...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

            {searchResults.length > 0 && (
                <div className="search-results">
                    {searchResults.map((item) => (
                        <div key={item.id} className="search-item">
                            {item.name}
                        </div>
                    ))}
                </div>
            )} */}

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
                                <li onClick={() => handleCategory('Aile')}>Aile</li>
                                <li onClick={() => handleCategory('Bilgisayar')}>Bilgisayar</li>
                                <li onClick={() => handleCategory('Bilim')}>Bilim</li>
                                <li onClick={() => handleCategory('Biyografi')}>Biyografi</li>
                                <li onClick={() => handleCategory('Çizgi Roman')}>Çizgi Roman</li>
                                <li onClick={() => handleCategory('Din')}>Din</li>
                                <li onClick={() => handleCategory('Drama')}>Drama</li>
                                <li onClick={() => handleCategory('Eğitim')}>Eğitim</li>
                                <li onClick={() => handleCategory('Felsefe')}>Felsefe</li>
                                <li onClick={() => handleCategory('Gençlik Kurgu')}>Gençlik Kurgu</li>
                                <li onClick={() => handleCategory('İş')}>İş</li>
                                <li onClick={() => handleCategory('Kişisel Gelişim')}>Kişisel Gelişim</li>
                                <li onClick={() => handleCategory('Kurgu')}>Kurgu</li>
                                <li onClick={() => handleCategory('Psikoloji')}>Psikoloji</li>
                                <li onClick={() => handleCategory('Şiir')}>Şiir</li>
                                <li onClick={() => handleCategory('Siyaset Bilimi')}>Siyaset Bilimi</li>
                                <li onClick={() => handleCategory('Sosyal Bilimler')}>Sosyal Bilimler</li>
                                <li onClick={() => handleCategory('Spor')}>Spor</li>
                                <li onClick={() => handleCategory('Tarih')}>Tarih</li>
                                <li onClick={() => handleCategory('Yemek Pişirme')}>Yemek Pişirme</li>

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