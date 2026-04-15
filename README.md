# 🧠 Task Time Tracker API

REST-сервис для учёта рабочего времени сотрудников по задачам.

Проект реализован в рамках тестового задания и покрывает базовые и дополнительные требования, включая безопасность, документацию, метрики и CI/CD.

---

## 📌 Функциональность

### 👤 Аутентификация
- Регистрация пользователя  
- Авторизация (JWT)  

### 📋 Работа с задачами
- Создание задачи  
- Получение задачи по ID (с проверкой владельца)  
- Изменение статуса задачи (`NEW / IN_PROGRESS / DONE`)  

### ⏱ Учёт времени
- Создание записи о затраченном времени  
- Получение записей за период времени  

---

## 🧱 Архитектура

Проект построен по слоям:
 - Controller → Service → Repository → DB

Дополнительно:
- DTO + Mapper (MapStruct)  
- Global Exception Handler  
- JWT Filter  
- Validation (Bean Validation)  

---

## 🔐 Безопасность

- Используется JWT-аутентификация  
- Все эндпоинты (кроме `/auth/**`, `/swagger/**`) требуют токен  
- Проверка доступа к задачам (нельзя получить/изменить чужую)  

---

## 📬 API тестирование

Для удобства тестирования в проекте приложена готовая коллекция Postman:

📁 `TaskTimeTracker.postman_collection.json`

Импортируйте её в Postman и используйте готовые запросы(файл в репозитории).

Также API доступно через Swagger:  
👉 http://localhost:8080/swagger-ui.html  

---

## 🗄 База данных

Используется:
- H2 (для разработки / тестов)  
- PostgreSQL (опционально)  

### 🔄 Миграции
- Flyway  
- Автоматическое применение при старте приложения  

---

## 📊 Метрики и мониторинг

Подключён Spring Boot Actuator.

Доступные endpoint'ы:
- `/actuator/health`  
- `/actuator/metrics`  
- `/actuator/prometheus`  

---

## 🧪 Тестирование

Реализованы:

### ✅ Unit-тесты
- Service слой  
- Проверка бизнес-логики  
- Mockito  

### ✅ Контроллеры
- MockMvc  
- Проверка HTTP-ответов  

---

## 🔄 CI/CD

Настроен GitHub Actions pipeline.

При каждом Pull Request:
- сборка проекта  
- запуск тестов  
- проверка качества кода  

---

## 🧹 Качество кода

Настроены пайпланы при создании PR

Используются инструменты:

- ✅ Checkstyle — стиль кода  
- ✅ SpotBugs — поиск багов  
- ✅ Spotless — автоформатирование  

---

## 🚀 Запуск проекта

### 1. Клонирование
git clone https://github.com/Jonan998/cdek-tz.git

### 2. Запуск (через Docker)

docker-compose -f docker-compose.dev.yml up --build

Приложение будет доступно по адресу:
👉 http://localhost:8080
Метрики
grafana : http://localhost:3000
prometheus: http://localhost:9090

3. Swagger

👉 http://localhost:8080/swagger-ui.html

---

📦 Стек технологий
 - Java 17
 - Spring Boot 3
 - Spring Security
 - JWT
 - Spring Data JPA
 - H2 / PostgreSQL
 - Flyway
 - MapStruct
 - Lombok
 - Swagger (SpringDoc OpenAPI)
 - JUnit 5 + Mockito
 - Grafana + Prometheus
 - Docker

⭐ Дополнительно реализовано
 - JWT аутентификация
 - Валидация DTO
 - Глобальная обработка ошибок (@RestControllerAdvice)
 - Логирование ключевых операций
 - Проверка доступа к ресурсам
 - Метрики
 - CI/CD
 - Code quality инструменты

📌 Примечание

Проект спроектирован с учётом production-практик:

 - централизованная обработка ошибок
 - наблюдаемость (метрики)
 - автоматическая проверка через CI
 - чистая архитектура
