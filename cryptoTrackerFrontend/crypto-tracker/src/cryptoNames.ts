const cryptoNames = [
    { id: 1, symbol: 'BTC', name: 'Bitcoin' },
    { id: 2, symbol: 'ETH', name: 'Ethereum' },
    { id: 3, symbol: 'UNI', name: 'Uniswap' },
    { id: 4, symbol: 'LTC', name: 'Litecoin' },
    { id: 5, symbol: 'ADA', name: 'Cardano' },
    { id: 6, symbol: 'DOGE', name: 'Dogecoin' },
    { id: 7, symbol: 'OP', name: 'Optimism' },
    { id: 8, symbol: 'DOT', name: 'Polkadot' },
    { id: 9, symbol: 'FIL', name: 'Filecoin' },
    { id: 10, symbol: 'SOL', name: 'Solana' },
    { id: 11, symbol: 'TRX', name: 'TRON' },
    { id: 12, symbol: 'USDC', name: 'USD Coin' },
    { id: 13, symbol: 'USDT', name: 'Tether' },
    { id: 14, symbol: 'ETC', name: 'Ethereum Classic' },
    { id: 15, symbol: 'ATOM', name: 'Cosmos' },
    { id: 16, symbol: 'STX', name: 'Stacks' },
    { id: 17, symbol: 'BCH', name: 'Bitcoin Cash' },
    { id: 18, symbol: 'KCS', name: 'KuCoin Token' },
    { id: 19, symbol: 'GRT', name: 'The Graph' },
    { id: 20, symbol: 'LEO', name: 'UNUS SED LEO' },
    { id: 21, symbol: 'BNB', name: 'Binance Coin' },
    { id: 22, symbol: 'TON', name: 'Toncoin' },
    { id: 23, symbol: 'ICP', name: 'Internet Computer' },
    { id: 24, symbol: 'POL', name: 'Polygon' },
    { id: 25, symbol: 'AAVE', name: 'Aave' },
    { id: 26, symbol: 'OM', name: 'MANTRA' },
    { id: 27, symbol: 'LINK', name: 'Chainlink' },
    { id: 28, symbol: 'VET', name: 'VeChain' },
    { id: 29, symbol: 'XLM', name: 'Stellar' },
    { id: 30, symbol: 'XMR', name: 'Monero' },
    { id: 31, symbol: 'XRP', name: 'Ripple' },
    { id: 32, symbol: 'AVAX', name: 'Avalanche'},
    { id: 33, symbol: 'DAI', name: 'Dai'}
];

export const getCryptoName = (symbol:string) => {
    const crypto = cryptoNames.find((crypto) => crypto.symbol === symbol);
    return crypto ? crypto.name : null;
  };

export default cryptoNames;
