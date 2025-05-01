import React, { useEffect, useState } from 'react'
import '../Home/Home.css'
import Product from '../Product/Product'
import Slider from "react-slick";
import { useStateValue } from '../StateProvider';
import { instanceListing } from '../axios';
// import axios from '../axios';

const ownerId = localStorage.getItem('userId');
function Home() {
    const [{ bookList, selectedCategory }, dispatch] = useStateValue();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBooks = async () => {
            setLoading(true);
            setError('');
            try {
                const response = await instanceListing.get(`/getListsExcludingOwner?ownerId=${ownerId}`);

                dispatch({
                    type: "SET_BOOK_LIST",
                    books: response.data
                });
                dispatch({
                    type: "SET_SEARCHED_BOOKS",
                    books: response.data
                });
            } catch (err) {
                setError('Kitaplar yüklenirken bir hata oluştu.');
            } finally {
                setLoading(false);
            }
        };
        fetchBooks();
    }, [dispatch]);


    useEffect(() => {
        dispatch({
            type: "SET_SEARCHED_BOOKS",
            books: bookList
        });
    }, [bookList, dispatch]);

    const settings = {
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 2000,
        responsive: [
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                },
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                },
            },
        ],
    };

    const filteredBooks = selectedCategory ?
        bookList.filter((book) => book.book.category === selectedCategory) : bookList;

    return (
        <div className='home'>
            <div className="home_container">
                {!selectedCategory && (
                    <div className="home_slider">
                        <Slider {...settings}>
                            <div>
                                <img className="slider_image" src="https://i0.wp.com/www.yesilist.com/wp-content/uploads/2019/05/1-1.jpg?resize=800%2C445&ssl=1" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://www.arttablo.com/upload/U-eski-kitap-fotograf-kanvas-tablo1461329134-800.jpg" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://st2.depositphotos.com/3800275/8013/i/450/depositphotos_80130112-stock-photo-opened-hardback-book-diary-with.jpg" />
                            </div>
                        </Slider>
                    </div>
                )}

                <h2 className='home_title'>
                    {selectedCategory ? (selectedCategory + ' Kategorisi İlanları') : 'İlanlar'}
                </h2>

                {loading ? <p>Yükleniyor...</p> : error ? <p>{error}</p> : (
                    <div className="home_row">
                        {filteredBooks.length > 0 ? filteredBooks.map((book, index) => (
                            <Product
                                key={index}
                                title={book.book.title}
                                author={book.book.author}
                                isbn={book.book.isbn}
                                publisher={book.book.publisher}
                                publishedDate={book.book.publishedDate}
                                category={book.book.category}
                                // description={book.book.description}
                                price={book.price}
                                image={book.book.image}
                            />
                        )) : (
                            <p>Sonuç bulunamadı.</p>
                        )}
                    </div>
                )}

                {!selectedCategory && (
                    <>
                        <h2 className='home_title'>
                            Önerilen İlanlar
                        </h2>

                        <div className="home_row">
                            {/* öneri sistemi ile gelecek kitaplar */}
                        </div>
                    </>
                )}
            </div>
        </div>
    )
}

export default Home