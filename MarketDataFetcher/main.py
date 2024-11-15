import asyncio
import json
import aiohttp
from fastapi import FastAPI
from kafka import KafkaProducer
from pydantic import BaseModel
from typing import List


class CryptoPriceDto(BaseModel):
    symbol: str
    price: str


class CryptoPricesWrapperDto(BaseModel):
    priceDtos: List[CryptoPriceDto]


cryptocurrenciesList = ['BTC', 'ETH', 'BNB', 'SOL', 'USDC', 'XRP', 'DOGE', 'TRX', 'TON', 'ADA', 'AVAX', 'LINK', 'BCH',
                        'DOT', 'DAI', 'LTC', 'UNI', 'ICP', 'XMR', 'XLM', 'ETC', 'POL', 'STX', 'FIL', 'AAVE', 'OP',
                        'VET', 'ATOM', 'GRT', 'OM']

cryptocurrenciesList = [symbol + 'USDT' for symbol in cryptocurrenciesList]

KAFKA_TOPIC = "crypto-prices"
KAFKA_SERVERS = ["kafka-service:9092"]
producer = KafkaProducer(bootstrap_servers=KAFKA_SERVERS, value_serializer=lambda v: json.dumps(v).encode('utf-8'))


async def fetch_price(session, symbol):
    url = f"https://api.binance.com/api/v3/ticker/price?symbol={symbol}"
    try:
        async with session.get(url) as response:
            if response.status == 200:
                return await response.json()
            else:
                print(f"Error fetching data for {symbol}: {response.status}")
    except Exception as e:
        print(f"Exception for {symbol}: {e}")
    return None


def publish_to_kafka(data):
    try:
        producer.send(KAFKA_TOPIC, value=data.dict())
        print(f"Published to Kafka: {data}")
    except Exception as e:
        print(f"Failed to publish message: {e}")


async def fetch_and_publish_prices():

    async with aiohttp.ClientSession() as session:
        while True:
            tasks = [fetch_price(session, symbol) for symbol in cryptocurrenciesList]
            all_price_data = []

            for task in asyncio.as_completed(tasks):
                price_data = await task
                if price_data:
                    crypto_price_dto = CryptoPriceDto(symbol=price_data["symbol"], price=price_data["price"])
                    all_price_data.append(crypto_price_dto)

            if all_price_data:
                wrapper_dto = CryptoPricesWrapperDto(priceDtos=all_price_data)
                publish_to_kafka(wrapper_dto)


            await asyncio.sleep(6)


app = FastAPI()


@app.on_event("startup")
async def startup_event():
    await asyncio.create_task(fetch_and_publish_prices())


@app.get("/")
async def root():
    return {"message": "DataFetcher is running"}
