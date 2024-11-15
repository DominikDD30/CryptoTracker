import React, { useEffect, useRef, useState } from 'react';
import './App.css';
import { getCryptoName } from './cryptoNames';
import SubscribeModal from './SubscribeModal';
import ApiClient from './apiClient';
import CryptoEntity from './cryptoEntity';


const apiClient=new ApiClient();
function App() {
  const [showSubscribeModal,setShowSubscribeModal]=useState(false);
  const [cryptocurrencies,setCryptocurrencies]=useState<CryptoEntity[]>([]);
  const [cryptocurrenciesHourlyAverage,setCryptocurrenciesHourlyAverage]=useState<CryptoEntity[]>([]);
  const [cryptocurrenciesDailyAverage,setCryptocurrenciesDailyAverage]=useState<CryptoEntity[]>([]);
  const [selectedCrypto,setSelectedCrypto]=useState<CryptoEntity>();
  const [redHighlighted,setRedHighlighted]=useState<string[]>([]);
  const [greenHighlighted,setGreenHighlighted]=useState<string[]>([]);
  const [isLoading,setIsLoading]=useState(false);
  const prevCryptocurrencies = useRef<CryptoEntity[]>([]); 


  useEffect(() => {
    setIsLoading(true);
    apiClient.getCryptoPrices().then((response) => {
      setCryptocurrencies(response.data);
      prevCryptocurrencies.current=response.data;
    });

    apiClient.getHourlyCryptoPrices().then((response) => {
      setCryptocurrenciesHourlyAverage(response.data);
    });
    apiClient.getDailyCryptoPrices().then((response) => {
      setCryptocurrenciesDailyAverage(response.data);
    });
    setIsLoading(false);
  }, []);
  


  // useEffect(() => {
  //   const fetchCryptoPrices = async () => {
  //     const startTime = performance.now();
  //     console.log("Fetch started at", new Date().toISOString());
  
  //     try {
  //       const response = await apiClient.getCryptoPrices();
  //       const apiFetchTime = performance.now();
  //       console.log("API response received at", new Date().toISOString());
  //       console.log(`Time from start to API response: ${(apiFetchTime - startTime).toFixed(2)} ms`);
  
  //       const newCryptocurrencies = response.data;
  //       let isUpdated = false;
  
  //       const updatedGreenHighlighted = [];
  //       const updatedRedHighlighted = [];
  
  //       newCryptocurrencies.forEach((crypto, index) => {
  //         const old = prevCryptocurrencies.current[index];
  
  //         if (!old) {
  //           isUpdated = true;
  //           return;
  //         }
  
  //         if (crypto.price.toFixed(4).toString() !== old.price.toFixed(4).toString()) {
  //           isUpdated = true;
  
  //           if (crypto.price > old.price) {
  //             updatedGreenHighlighted.push(crypto.symbol);
  //           } else {
  //             updatedRedHighlighted.push(crypto.symbol);
  //           }
  //         }
  //       });
  
  //       if (isUpdated) {
  //         prevCryptocurrencies.current = newCryptocurrencies;
  //         setCryptocurrencies(newCryptocurrencies);
  //         console.log("New Prices Updated");
  
  //         if (updatedGreenHighlighted.length > 0) {
  //           setGreenHighlighted((prev) => [...prev, ...updatedGreenHighlighted]);
  //           setTimeout(() => {
  //             setGreenHighlighted((prev) => prev.filter((symbol) => !updatedGreenHighlighted.includes(symbol)));
  //           }, 2500);
  //         }
  
  //         if (updatedRedHighlighted.length > 0) {
  //           setRedHighlighted((prev) => [...prev, ...updatedRedHighlighted]);
  //           setTimeout(() => {
  //             setRedHighlighted((prev) => prev.filter((symbol) => !updatedRedHighlighted.includes(symbol)));
  //           }, 2500);
  //         }
  //       }
  
  //       const endTime = performance.now();
  //       console.log(`Total time from start to end of processing: ${(endTime - startTime).toFixed(2)} ms`);
  
  //     } catch (error) {
  //       console.error("Error fetching cryptocurrency prices", error);
  //     }
  //   };
  
  //   fetchCryptoPrices();
  //   const intervalId = setInterval(fetchCryptoPrices, 8000);
  
  //   return () => clearInterval(intervalId);
  // }, []);
  

  useEffect(() => {
    const fetchCryptoPrices = async () => {
      try {
        const startTime = performance.now();
        console.log("Fetch started at", new Date().toISOString());

        const response = await apiClient.getCryptoPrices();
        const newCryptocurrencies = response.data;
        let isUpdated = false;
  
        newCryptocurrencies.forEach((crypto,index) => {
          const old = prevCryptocurrencies.current[index];
  
          // if (old === undefined) {
          //   isUpdated = true;
          //   return;
          // }
  
          if (crypto.price.toFixed(4).toString() !== old.price.toFixed(4).toString()) {
            isUpdated = true;
            console.log("Is Update Staged");
  
            if (crypto.price > old.price) {
              setGreenHighlighted((prev) => [...prev, crypto.symbol]);
              setTimeout(() => {
                setGreenHighlighted((prev) => prev.filter((symbol) => symbol !== crypto.symbol));
              }, 2500);
            } else {
              setRedHighlighted((prev) => [...prev, crypto.symbol]);
              setTimeout(() => {
                setRedHighlighted((prev) => prev.filter((symbol) => symbol !== crypto.symbol));
              }, 2500);
            }
          }
        });
  
        if (isUpdated) {
          prevCryptocurrencies.current = newCryptocurrencies;
          setCryptocurrencies(newCryptocurrencies);
          console.log("New Prices Updated");
        }

      const endTime = performance.now();
      console.log(`Total time from start to end of processing: ${(endTime - startTime).toFixed(2)} ms`);
      console.log("\n");
      } catch (error) {
        console.error("Error fetching cryptocurrency prices", error);
      }
    };
  
    fetchCryptoPrices();
    const intervalId = setInterval(fetchCryptoPrices, 6300);
  
    return () => clearInterval(intervalId);
  }, []);

  

  const handleSubscribeSubmit = async (data: {percentage: string; email: string; }) => {
    console.log('Subscribed with:', data);

    try {
        await apiClient.saveSubscription(selectedCrypto!.symbol, selectedCrypto!.price, parseFloat(data.percentage), data.email);
        showCustomAlert('Thank you for subscribing. We will send you updates when the price changes over the threshold.');
    } catch (error) {
        console.error('Subscription failed:', error);
        showCustomAlert('Subscription failed. Please try again later.');
    }
  };

  if (isLoading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="crypto-tracker">
      <h1>Crypto Tracker</h1>
      <table className="crypto-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Price</th>
            <th>1h</th>
            <th>1h %</th>
            <th>24h</th>
            <th>24h %</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {cryptocurrencies.map((crypto,index) => (
            <tr key={index}>
              <td>{index+1}</td>
              <td>
                  <div className="crypto-name">
                    <img src={`crypto_images/${crypto.symbol}.png`} alt={`${crypto.symbol} logo`} />
                    <strong>{getCryptoName(crypto.symbol)} </strong><span>{crypto.symbol}</span>
                  </div>
              </td>
              <td style={{color: redHighlighted.includes(crypto.symbol)? 'red': greenHighlighted.includes(crypto.symbol)? 'green'
                  : 'black'}}>
                {crypto.price.toFixed(4)}$
              </td>

              <td>
                {cryptocurrenciesHourlyAverage.find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price}$
              </td>

              <td className={calculatePercentageChange(crypto.price,cryptocurrenciesHourlyAverage.
                find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price || 0)
                .toFixed(2).includes('-') ? 'negative' : 'positive'}>

                {calculatePercentageChange(crypto.price,cryptocurrenciesHourlyAverage.
                find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price || 0).toFixed(2)}%
              </td>

              <td>
                {cryptocurrenciesDailyAverage.find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price}$
              </td>

              <td className={calculatePercentageChange(crypto.price,cryptocurrenciesDailyAverage.
                find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price || 0)
                .toFixed(2).includes('-') ? 'negative' : 'positive'}>

                {calculatePercentageChange(crypto.price,cryptocurrenciesDailyAverage.
                find((c) => c.symbol.substring(0,c.symbol.length-4) === crypto.symbol)?.price || 0).toFixed(2)}%
              </td>

              <td><p className='subscribe-link' onClick={()=>{setShowSubscribeModal(true);setSelectedCrypto(crypto)}}>subscribe</p></td>
            </tr>
          ))}
        </tbody>
      </table>
      {showSubscribeModal && (
        <SubscribeModal
          onClose={() => {setShowSubscribeModal(false);setSelectedCrypto(undefined)}}
          onSubmit={handleSubscribeSubmit}
        />
      )}
    </div>
  );
}

const calculatePercentageChange = (currentPrice: number, previousPrice: number) => {
  return ((currentPrice - previousPrice) / previousPrice) * 100;
}

const showCustomAlert = (message) => {
  const alert = document.createElement('div');
  alert.className = 'custom-alert';
  alert.textContent = message;

  document.body.appendChild(alert);

  alert.style.display = 'block';

  setTimeout(() => {
    alert.style.display = 'none';
    document.body.removeChild(alert); 
  }, 4000);
};


export default App;
