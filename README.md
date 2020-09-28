# Notification Service
Использовалась IDE **IntelliJ IDEA Ultimate**. Проект реализован на **Java 14** с использованием **Spring Boot**. База данных **PostgreSQL**. Сборка с помощью **Maven**.

Перед запуском проекта необходимо поменять настройки в файле `application.properties`

Настройки для БД:
1. `spring.datasource.url` - путь до нашей базы данных
2. `spring.datasource.username` - имя пользователя
3. `spring.datasource.password` - пароль

Всё должно быть готово к запуску. 
1. Указываем в конфигурации запуска в качестве основного класса `NotificationserviceApplication`
2. Запускаем
