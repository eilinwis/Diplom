# Порядок запуска автотестов

***

1. Запуск Docker контейнера:

     * Скачать Docker с официального сайта https://www.docker.com, установить

     * Запустить контейнер c помощью файла docker-compose.yml (Находится в репозитории)


2. Загрузить приложение aqa-shop.jar и поместить в каталог artifacts корневой папки проекта. Файл application.properties должен находиться в той же папке. (Данные файлы в репозитории)


3. Запуск приложения с терминала командой: java -jar aqa-shop.jar


4. Загрузить и установить приложение Node.js c сайта https://nodejs.org/en/


5. Поместить папку gate-simulator в корневой каталог проекта (Данные файлы в репозитории).


5. В командной строке прописать команду для запуска npm start



[![Build status](https://ci.appveyor.com/api/projects/status/h7sevxbb0aevnml3?svg=true)](https://ci.appveyor.com/project/eilinwis/diplom)