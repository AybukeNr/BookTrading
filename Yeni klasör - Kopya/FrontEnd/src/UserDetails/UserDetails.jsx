import React, { useEffect } from 'react'
import '../UserDetails/UserDetails.css'
import { Rating } from '@mui/material'
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import { useLocation } from 'react-router-dom';
import { useStateValue } from '../StateProvider';
import { instanceListing, instanceUser } from '../axios';

function UserDetails() {
    const [{ userDetail , userAds }, dispatch] = useStateValue();
    const { state } = useLocation();
    const userId = state?.user;

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const response = await instanceUser.get(`/getUserById?id=${userId}`);
                const data = response.data;

                dispatch({
                    type: 'SET_USER_DETAILS',
                    payload: data
                });
            } catch (error) {
                console.error("Kullanıcı detayları alınırken hata:", error);
            }
        };

        if (userId) {
            fetchUserDetails();
        }
    }, [userId]);

    useEffect(() => {
        const fetchUserAds = async () => {
            try {
                const response = await instanceListing.get(`/getListsByOwnerId?ownerId=${userId}`);
                const data = response.data;

                dispatch({
                    type: 'SET_USER_ADS',
                    payload: data
                });
            } catch (error) {
                console.error("Kullanıcı detayları alınırken hata:", error);
            }
        };

        if (userId) {
            fetchUserAds();
        }
    }, [userId]);

    return (
        <div className='userDetails'>
            <div className="userInfo">
                <AccountCircleOutlinedIcon />
                <p>{userDetail.firstName} {userDetail.lastName}</p>
                {/* <p>Kullanıcı Değerlendirmesi: {userDetail.trustPoint} <Rating className='rating' value={userDetail.trustPoint || 0} readOnly /></p> */}
                <p>{userDetail.mailAddress}</p>
                <p>{userDetail.phoneNumber}</p>
            </div>

            <div className="userAds">
                <h3>Kullanıcının İlanları:</h3>
                {userAds.length > 0 ? (
                    userAds.map((adv) => (
                        <div className='AdsBooks' key={adv.listId}>
                            <div className="bookInfo">
                                <p>{adv.book.isbn}/{adv.book.title}-{adv.book.author}/{adv.book.publisher}-{adv.book.publishedDate}/{adv.book.category}/{adv.book.condition}</p>
                                <p>{adv.book.description}</p>
                                <p><strong>{adv.type === 'SALE' && adv.price ? `${adv.price} ₺` : adv.type === 'EXCHANGE' ? 'Takasa açık' : 'Hatalı değer'}</strong></p>
                            </div>
                            <img src={adv.book.image} alt={adv.title} />
                        </div>
                    ))) : (
                    <p>İlan bulunamadı.</p>
                )}
            </div>
        </div>
    )
}

export default UserDetails