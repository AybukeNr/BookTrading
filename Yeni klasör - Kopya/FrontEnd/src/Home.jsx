import React, { useState } from 'react'
import './Home.css'
import Product from './Product'
import Slider from "react-slick";
import { useStateValue } from './StateProvider';

function Home() {
    const [{ bookList }] = useStateValue();
    const [selectedCategory, setSelectedCategory] = useState(null);

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

    const handleCategory = (category) => {
        setSelectedCategory(category);
    }

    const filteredBooks = selectedCategory ? bookList.filter((book) => book.category === selectedCategory) : bookList;

    return (
        <div className='home'>
            <div className="home_container">
                <img className='home_image' src='https://img.freepik.com/premium-photo/library-background-bookshelf-background-ornate-bookshelf-book-background-vintage-library_605423-33197.jpg' alt='' />

                <div className="home_beginning">
                    <div className="home_category">
                        <h4 className='home_categoryTitle'>Kategoriler</h4>
                        <ul className='home_categoryList'>
                            <li onClick={() => handleCategory('Kurgu')}>Kurgu</li>
                            <li onClick={() => handleCategory('Bilim')}>Bilim</li>
                            <li onClick={() => handleCategory('Tarih')}>Tarih</li>
                            <li onClick={() => handleCategory('Biyografi')}>Biyografi</li>
                            <li onClick={() => handleCategory('Fantazi')}>Fantazi</li>
                            <li onClick={() => handleCategory('Gizem')}>Gizem</li>
                            <li onClick={() => handleCategory('Romantik')}>Romantik</li>
                            <li onClick={() => handleCategory('Macera')}>Macera</li>
                            <li onClick={() => handleCategory('Gerilim')}>Gerilim</li>
                            <li onClick={() => handleCategory('Şiir')}>Şiir</li>
                            <li onClick={() => handleCategory('Dram')}>Dram</li>
                            <li onClick={() => handleCategory('Bilim Kurgu')}>Bilim Kurgu</li>
                            <li onClick={() => handleCategory('Felsefe')}>Felsefe</li>
                            <li onClick={() => handleCategory('Kişisel Gelişim')}>Kişisel Gelişim</li>
                        </ul>
                    </div>

                    <div className="home_slider">
                        <Slider {...settings}>
                            <div>
                                <img className="slider_image" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnRWRRs46gNNev76jvdlWqWKASDAckhz0g2Q&s" alt="Slider 1" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://www.arttablo.com/upload/U-eski-kitap-fotograf-kanvas-tablo1461329134-800.jpg" alt="Slider 2" />
                            </div>
                            <div>
                                <img className="slider_image" src="https://st2.depositphotos.com/3800275/8013/i/450/depositphotos_80130112-stock-photo-opened-hardback-book-diary-with.jpg" alt="Slider 3" />
                            </div>
                        </Slider>
                    </div>
                </div>

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