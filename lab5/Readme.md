## Щоб запустити данний проект для лабораторної 5 слідуйте наступним крокам:
  1. Для початку вам потрібний аккаунт на ажурі. Створіть там Redis cache та Event hub, в якому створіть event hub instance (секція entities). В секції Settings оберіть SAS Policy, там створіть нове правило, та скопіюйте "Primary key" та "Connection String". Вставте їх в файл src\main\java\com\lab5\resteventhub\service\SendDataEventHubImpl.java, а також замініть значення Namespace EventHubName (там знаходяться коментарі, тому знайти потрібне значення має бути неважко).
  2. Також пеобхідно створити Redis Cache, та зробити ті ж самі дії як і в першому кроці, але вже в файлі src\main\java\com\lab5\resteventhub\service\SendDataConsoleImpl.java
  3. Коли всі значення будуть заміненні на коректні, запустіть програму. 
  4. Щоб завантажити значення в Event Hub або Redis, зробіть POST запит на localhost:9000/api, за допомогою curl-a, Postman або будь якої іншої програми. Необхідно встановити значення хедера Content-Type - application/json. А тілом запиту буде наступне:
  
  ```
  
  {
   "url": "https://www.dallasopendata.com/resource/7sd2-ngbc.json",
    "strategy": "eventHub"
  }
  
```

5. Якщо все було налаштовано правильно, значення з ендпоїнта, відповідно до вашого варіанту, будуть наділсані в Event Hub або Redis в залежності від налаштування.
6. Щоб перевірити чи значення були отримані:
Event Hub > Entities > Event hubs > ваш Event Hub > Features > Proccess Data > Explore ( можливо буде необхідно почекати 1-2 хв).
Redis Cache > Console > введіть команду hgetAll ConsoleLog

