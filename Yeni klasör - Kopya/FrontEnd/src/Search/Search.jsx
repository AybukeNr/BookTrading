import React from 'react'
import { useStateValue } from '../StateProvider';
import Product from '../Product/Product';

function Search() {
    const [{ searchedBooks, searchQuery }] = useStateValue();

    return (
        <div className="search-results">
            <h2 className="home_title">Arama Sonuçları: "{searchQuery}"</h2>
            {searchedBooks.length > 0 ? (
                <div className="home_row">
                    {searchedBooks.map((book, index) => (
                        <Product key={index} book={book} />
                    ))}
                </div>
            ) : (
                <p>Sonuç bulunamadı.</p>
            )}
        </div>
    );
}

export default Search