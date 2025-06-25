# Hotel Management System 🏨

A simple Java console-based Hotel Management System using JDBC and MySQL.

## Features

- ✅ Check-in Guest
- ✅ Check-out Guest
- ✅ View Available Rooms

## Requirements

- Java 8+
- MySQL Server
- MySQL JDBC Driver

## Database Setup

```sql
CREATE DATABASE hotel_management;
USE hotel_management;

CREATE TABLE rooms (
    room_number INT PRIMARY KEY,
    guest_name VARCHAR(100),
    occupied BOOLEAN DEFAULT FALSE
);

INSERT INTO rooms (room_number) VALUES (101), (102), (103), (104), (105);
