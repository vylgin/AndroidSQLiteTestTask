TestTask
========

#Задача#

В приложении SQLite  Database c двумя таблицами - 
user(uid integer, nick varchar(20)), message(uid integer, msg 
varchar(255), tm DateTime, a integer, b integer,c  integer,d  integer, 
e varchar(25), dd varchar(25), f varchar(25), g integer).

БД заранее заполнена несколькими пользователями, для каждого из них – по 
нескольку сообщений. Значения полей a,b...g  произвольные. 
В приложении две Activity.

Первая показывает список пользователей (отображаются ники, алфавитная 
сортировка). При нажатии на пользователя открывается вторая Activity, 
которая показывает список сообщений выбранного пользователя. Рядом с 
каждым сообщением показывается время сообщения и значения полей a,b...g. 
Сортировка сообщений – по их времени.

Работает  БД  только с первым активити. Вторая получает 
данные от первой, а не из БД напрямую.


#Решение#
Я заменил атрибут  d varchar(25) из таблицы message на  dd
varchar(25), так как в данной таблице уже есть атрибут d  integer.

Базу данных я создавал с помощью программы SQLite Database Browser и
ее файл поместил в каталог assets. Программа создает копию бд из
каталога assets в системный каталог, копия не создается, если бд уже
создавалась.

С бд работает только первая активити, формируется список ников
ListView из бд в алфавитном порядке. При нажатии на один из пунктов
listView, в структуру Bundle помещаются массивы атрибутов, которые
передаются второму активити, которое отображает сообщения
отсортированные по времени.

Программу протестировал на виртуальных аппаратах с android 2.2 и
android 3.1 с разрешением экрана 240x432 и 500x400 соответственно.
