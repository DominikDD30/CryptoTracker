import axios from 'axios';
import CryptoEntity from './cryptoEntity';

export const axiosInstance= axios.create({
    baseURL: "http://cryptoapp.127.0.0.1.nip.io",
    });
  
    class ApiClient{
  
      saveSubscription=(symbol:string,currentPrice:number,percentOffSet:number,email:string)=>{
          axiosInstance.post('/subscriptions',{
            symbol:symbol,currentPrice:currentPrice,percentOffSet:percentOffSet,email:email});
      }

      getCryptoPrices=()=>{
          return axiosInstance.get<CryptoEntity[]>('/crypto/prices');
        }
    
       getHourlyCryptoPrices=()=>{
            return axiosInstance.get<CryptoEntity[]>('/crypto/prices/hourly');
          }

          getDailyCryptoPrices=()=>{
            return axiosInstance.get<CryptoEntity[]>('/crypto/prices/daily');
          }
}

export default ApiClient;