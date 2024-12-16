import React from 'react';
import './Header.css'
import SearchIcon from '@mui/icons-material/Search';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import { Link } from 'react-router-dom';
import { useStateValue } from './StateProvider';

function Header() {
    const [{ basket }, dispatch] = useStateValue();


    return (
        <div className='header'>

            <Link to='/'>
                <img className='header_logo' src='https://i.hizliresim.com/obkwl66.png' />
            </Link>

            <div className='header_search'>
                <input className='header_searchInput' type='text' placeholder='Aradığınız metni girin.' />
                <SearchIcon className='header_searchIcon' />
            </div>

            <div className='header_nav'>
                <div className='header_option'>
                    <span className='header_optionLineOne'>Merhaba!</span>
                    <span className='header_optionLineTwo'>Giriş Yap</span>
                </div>
                <div className='header_option'>
                    <span className='header_optionLineOne'>Benim</span>
                    <span className='header_optionLineTwo'>Kitaplığım</span>
                </div>
                <div className='header_option'>
                    <span className='header_optionLineOne'>Bir Şey</span>
                    <span className='header_optionLineTwo'>Buluruz</span>
                </div>

                <Link to='/checkout'>
                    <div className='header_optionBasket'>
                        <ShoppingBasketIcon />
                        <span className='header_optionLineTwo header_basketCount'>{ basket?.length }</span>
                    </div>
                </Link>
            </div>
        </div>
    )
}

export default Header