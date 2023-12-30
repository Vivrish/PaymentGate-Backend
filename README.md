# TJV-Semestral-work - Platebný systém

# Author: Nikolai Chaunin (FIT ČVUT)


Tento projekt je malý platební systém, ve kterém uživateli mohou registrovat účty pomocí jména a hesla, přidávat do učtu několik bankovních karet a posílat peníze jiným uživatelům.
Vnitřní struktura aplikace obsahuje tři vrstvy a hodně využívá OOP. Databáze (víz obrázek) komunikuje s aplikaci pomocí frameworku Hibernate.
Dotaz nad CRUD: například interface Transaction (víz model na druhém obrázku) má v sobě metodu getTransactionsOfLast, která spojuje tabulky User a Transaction a navíc filtruje podle atributu date.   
