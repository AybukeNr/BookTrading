import axios from 'axios'
import { getAuthToken, removeAuthToken } from './auth';
import { useNavigate } from "react-router-dom";

const createInstance = (baseURL) => {
    const instance = axios.create({
        baseURL,
    });


instance.interceptors.request.use(
    (config) => {
        const token = getAuthToken();
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        console.log("Oturum süresi doldu. Çıkış yapılıyor...");
        removeAuthToken('authToken'); 
        const navigate = useNavigate();   
        navigate('/login');
      }
      return Promise.reject(error);
    }
  );
  return instance;
};

const instanceAuth = createInstance('http://localhost:8080/api/v1/auth');
const instanceUser = createInstance('http://localhost:8080/api/v1/users');
const instanceTransaction = createInstance('http://localhost:9093/api/v1/transactions');
const instanceShipping = createInstance('http://localhost:8082/api/v1/shippings');
const instanceOffer = createInstance('http://localhost:9091/api/v1/offers');
const instanceLibrary = createInstance('http://localhost:9090/api/v1/books');
const instanceListing = createInstance('http://localhost:8081/listing/api/v1/lists');

export { instanceAuth, instanceUser, instanceTransaction, instanceShipping, instanceOffer, instanceLibrary, instanceListing };

