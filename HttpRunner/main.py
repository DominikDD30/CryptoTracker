import aiohttp
import asyncio
import time

requestsNumber = 1000  # Liczba równoczesnych zapytań
num_attempts = 5  # Liczba prób
pause_duration = 1  # Pauza między próbami w sekundach

# Funkcja do pobierania danych z API
async def fetch_data(session, url):
    async with session.get(url) as response:
        return await response.json()

# Funkcja do wykonywania wszystkich zapytań
async def fetch_all():
    url = "http://cryptoapp.127.0.0.1.nip.io/crypto/prices"
    async with aiohttp.ClientSession() as session:
        tasks = [fetch_data(session, url) for _ in range(requestsNumber)]  # 1000 równoczesnych zapytań
        results = await asyncio.gather(*tasks)  # Czekaj na wszystkie odpowiedzi

    return results

# Funkcja główna wykonująca 5 prób i obliczająca średni czas
async def main():
    total_time = 0  # Zmienna do sumowania czasów wszystkich prób

    # Wykonaj num_attempts prób
    for attempt in range(num_attempts):
        print(f"Attempt {attempt + 1}/{num_attempts}...")
        start_time = time.time()
        prices = await fetch_all()  # Wywołanie wszystkich zapytań
        end_time = time.time()

        elapsed_time = end_time - start_time
        total_time += elapsed_time  # Dodaj czas tej próby do całkowitego czasu

        print(f"Finished {requestsNumber} requests in {elapsed_time:.2f} seconds")
        print(f"Some Results: {prices[:5]}")  # Wyświetlenie pierwszych 5 wyników
        if attempt < num_attempts - 1:  # Pauza tylko jeśli to nie jest ostatnia próba

            print(f"Waiting for {pause_duration} seconds before next attempt...")
            await asyncio.sleep(pause_duration)

    # Oblicz średni czas
    avg_time = total_time / num_attempts
    print(f"\nAverage time for {num_attempts} attempts: {avg_time:.2f} seconds")

# Uruchomienie głównej funkcji
asyncio.run(main())
