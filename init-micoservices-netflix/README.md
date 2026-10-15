# Netflix Microservices Shop

Pet-проект интернет-магазина на Spring Boot и Netflix Cloud: Eureka, API Gateway,
PostgreSQL, Redis, сервис каталога и авторизации.

## Запуск

Из каталога `init-micoservices-netflix` выполните:

```powershell
docker compose up --build
```

При первом запуске дождитесь старта контейнеров PostgreSQL, Redis и сервисов.
Остановка без удаления данных:

```powershell
docker compose down
```

Данные PostgreSQL и загруженные изображения хранятся в Docker volumes. Для
полного сброса данных используйте `docker compose down --volumes`.

## Адреса

- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Shop: http://localhost:8081
- Admin: http://localhost:8084
- Auth service: http://localhost:8083

Для локального запуска Compose admin-пользователь по умолчанию: `admin` /
`admin-password`. Перед использованием задайте собственные значения переменных
`ADMIN_USERNAME`, `ADMIN_PASSWORD`, `ADMIN_JWT_SECRET` и `JWT_SECRET`. `JWT_SECRET`
должен быть не короче 32 байт и одинаковым для auth-service и shop.

Регистрация доступна на `POST /auth-public/register`; ответ содержит ID,
необходимый для входа через `POST /auth-public/login`. Ответ входа включает
`accessToken`, который передаётся сервису shop заголовком
`Authorization: Bearer <accessToken>`.

## Проверка

Каждый сервис можно проверить отдельно из своей папки командой:

```powershell
.\mvnw.cmd test
```