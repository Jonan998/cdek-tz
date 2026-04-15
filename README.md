# 🧠 Task Time Tracker API

REST-сервис для учёта рабочего времени сотрудников по задачам.

Проект реализован в рамках тестового задания и покрывает базовые и дополнительные требования, включая безопасность, документацию, метрики и CI/CD.

📌 Функциональность
👤 Аутентификация
Регистрация пользователя
Авторизация (JWT)
Stateless security
📋 Работа с задачами
Создание задачи
Получение задачи по ID (с проверкой владельца)
Изменение статуса задачи (NEW / IN_PROGRESS / DONE)
⏱ Учёт времени
Создание записи о затраченном времени
Получение записей за период времени
🧱 Архитектура

Проект построен по слоям:

Controller → Service → Repository → DB

Дополнительно:

DTO + Mapper (MapStruct)
Global Exception Handler
JWT Filter
Validation (Bean Validation)
🔐 Безопасность
Используется JWT-аутентификация
Все эндпоинты (кроме /auth/**, /swagger/**) требуют токен
Проверка доступа к задачам (нельзя получить/изменить чужую)
📄 API Документация

Swagger доступен по адресу:

http://localhost:8080/swagger-ui.html

OpenAPI JSON:

http://localhost:8080/v3/api-docs
📬 Примеры запросов (Postman)
🔐 Регистрация
POST /auth/register
Content-Type: application/json

{
  "username": "user",
  "password": "password123"
}
🔑 Логин
POST /auth/login

{
  "username": "user",
  "password": "password123"
}
📋 Создание задачи
POST /tasks
Authorization: Bearer <token>

{
  "title": "Task 1",
  "description": "Описание",
  "status": "NEW"
}
📄 Получение задачи
GET /tasks/{taskId}
Authorization: Bearer <token>
🔄 Обновление статуса
PATCH /tasks/{taskId}/status?status=DONE
Authorization: Bearer <token>
⏱ Создание записи времени
POST /time-records/create

{
  "taskId": "uuid",
  "startTime": "2026-04-15T10:00:00",
  "endTime": "2026-04-15T12:00:00",
  "description": "Работа над задачей"
}
📊 Получение записей за период
GET /time-records/get?start=2026-04-01T00:00:00&end=2026-04-30T23:59:59
🗄 База данных

Используется:

H2 (для разработки / тестов)
PostgreSQL (опционально)
🔄 Миграции
Flyway
Автоматическое применение при старте приложения
📊 Метрики и мониторинг

Подключён Spring Boot Actuator.

Доступные endpoint'ы:
/actuator/health
/actuator/metrics
/actuator/prometheus
📈 Prometheus

Пример конфигурации:

scrape_configs:
  - job_name: 'task-time-tracker'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
🧪 Тестирование

Реализованы:

✅ Unit-тесты
Service слой
Проверка бизнес-логики
Mockito
✅ Контроллеры
MockMvc
Проверка HTTP-ответов
🔄 CI/CD

Настроен GitHub Actions pipeline.

При каждом Pull Request:

сборка проекта
запуск тестов
проверка качества кода
🧹 Качество кода

Используются инструменты:

✅ Checkstyle — стиль кода
✅ SpotBugs — поиск багов
✅ Spotless — автоформатирование
🚀 Запуск проекта
1. Клонирование
git clone https://github.com/your-repo/task-time-tracker.git
cd task-time-tracker
2. Запуск
mvn clean install
mvn spring-boot:run
3. Swagger
http://localhost:8080/swagger-ui.html
📦 Стек технологий
Java 17+
Spring Boot 3
Spring Security
JWT
Spring Data JPA
H2 / PostgreSQL
Flyway
MapStruct
Lombok
Swagger (SpringDoc OpenAPI)
JUnit 5 + Mockito
Actuator + Prometheus
Docker (опционально)
⭐ Дополнительно реализовано
JWT аутентификация
Валидация DTO
Глобальная обработка ошибок (@RestControllerAdvice)
Логирование ключевых операций
Проверка доступа к ресурсам
Метрики
CI/CD
Code quality инструменты
📌 Примечание

Проект спроектирован с учётом production-практик:

stateless authentication
централизованная обработка ошибок
наблюдаемость (метрики)
автоматическая проверка через CI
чистая архитектура
