# 💳 Digital Wallet System (Core Java + JDBC)

## 📌 Project Overview
Digital Wallet System is a console-based banking application developed using Core Java and JDBC.  
It allows users to register, login, deposit money, withdraw money, transfer funds, and view transaction history.

The system uses MySQL database for data persistence and implements transaction management to ensure data consistency.

---

## 🚀 Features

### 👤 User Module
- User Registration
- Secure Login
- Check Balance
- View Transaction History

### 💰 Wallet Module
- Deposit Money
- Withdraw Money
- Transfer Money Between Users
- Automatic Transaction Recording

### 🔐 Security & Validation
- Password Authentication
- Balance Validation
- JDBC Transaction Management (Commit & Rollback)

---

## 🛠️ Tech Stack

- Java (Core Java)
- JDBC
- MySQL
- Eclipse IDE
- Git & GitHub

---

## 🗄️ Database Schema

### USERS Table
- user_id (Primary Key)
- name
- email
- password
- balance
- status

### TRANSACTIONS Table
- transaction_id (Primary Key)
- sender_id
- receiver_id
- amount
- type
- date_time

---

## ⚙️ How to Run

1. Install MySQL and create database:
   ```sql
   CREATE DATABASE digital_wallet;
Create required tables (refer schema above).

Update database credentials in:

DBConnection.java


Run:

MainApp.java
