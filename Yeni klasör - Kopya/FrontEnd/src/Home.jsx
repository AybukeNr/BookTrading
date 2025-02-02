import React, { useState } from 'react'
import './Home.css'
import Product from './Product'
import Slider from "react-slick";
import { useStateValue } from './StateProvider';

function Home() {
    const [{ bookList, selectedCategory }] = useStateValue();

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

    const filteredBooks = selectedCategory ? bookList.filter((book) => book.category === selectedCategory) : bookList;

    return (
        <div className='home'>
            <div className="home_container">
                {/* <img className='home_image' src='https://img.freepik.com/premium-photo/library-background-bookshelf-background-ornate-bookshelf-book-background-vintage-library_605423-33197.jpg' alt='' /> */}

                <div className="home_beginning">
                    <div className="home_slider">
                        <Slider {...settings}>
                            <div>
                                <img className="slider_image" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnRWRRs46gNNev76jvdlWqWKASDAckhz0g2Q&s" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://www.arttablo.com/upload/U-eski-kitap-fotograf-kanvas-tablo1461329134-800.jpg" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://st2.depositphotos.com/3800275/8013/i/450/depositphotos_80130112-stock-photo-opened-hardback-book-diary-with.jpg" />
                            </div>
                        </Slider>
                    </div>
                </div>

                    <h2 className='home_title'>Önerilenler</h2>

                <div className="home_row">
                    {filteredBooks.map((book, index) => (
                        <Product
                            key={index}
                            title={book.title}
                            author={book.author}
                            isbn={book.isbn}
                            publisher={book.publisher}
                            publishedDate={book.publishedDate}
                            category={book.category}
                            price={book.price}
                            image={book.image}
                        />
                    ))}
                </div>

            </div>
        </div>
    )
}

export default Home