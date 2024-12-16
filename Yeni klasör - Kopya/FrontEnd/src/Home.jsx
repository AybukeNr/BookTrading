import React from 'react'
import './Home.css'
import Product from './Product'
function Home() {
  return (
    <div className='home'>
        <div className="home_container">
            <img className='home_image' src='https://i.hizliresim.com/dvmrg8d.' alt=''/> 
            {/* üst kısma sloganlı resim gelecek */}
        <div className="home_row">
            <Product title='Nutuk-Mustafa Kemal Atatürk-Özel ciltli antika' price={ 1850 } image='https://i.hizliresim.com/fhjnd8v.jpg'/>
            <Product />
            <Product />
        </div>
        <div className="home_row">
            <Product />
            <Product />
        </div>
        <div className="home_row">
            <Product />
            <Product />
            <Product />
        </div>
        </div>
    </div>
  )
}

export default Home