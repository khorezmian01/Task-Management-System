Используемые технологии: Java 17, Spring Boot, PostgreSQL, Redis, Docker, Swagger UI и JUnit 5.

Для того чтобы запустить код вам нужно клонировать гит репозитоий, скачать докер и собрать docker compose файл. 
Для запуска в контейнере все необходимые конфигурации написаны.
Сборка докер образа
docker-compose up --build

Чтобы остановить и удалить контейнер, выполните команду:

docker stop tsm
docker rm tsm
