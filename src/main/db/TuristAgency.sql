-- Drop the schema if it exists
DROP SCHEMA IF EXISTS TURISTAGENCY;

-- Create a new schema with UTF-8 character set
CREATE SCHEMA TURISTAGENCY DEFAULT CHARACTER SET utf8;

-- Use the created schema
USE TURISTAGENCY;

-- Create a table for users
CREATE TABLE User (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    jmbg VARCHAR(13),
    address VARCHAR(255),
    phone_number VARCHAR(20),
    registration_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20),
    PRIMARY KEY (id)
);

-- Create a table for trip categories
CREATE TABLE TripCategory (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
);

-- Create a table for trips
CREATE TABLE Trip (
    id BIGINT AUTO_INCREMENT,
    transportation_mode VARCHAR(50),
    accommodation_unit VARCHAR(50),
    destination_name VARCHAR(255),
    location_image VARCHAR(255),
    trip_category_id BIGINT,
    departure_date_time DATETIME,
    return_date_time DATETIME,
    number_of_nights INT,
    arrangement_price DECIMAL(10, 2),
    total_seats INT,
    available_seats INT,
    PRIMARY KEY (id),
    FOREIGN KEY (trip_category_id) REFERENCES TripCategory(id) ON DELETE CASCADE
);

-- Inserting at least five rows with role set to 'BUYER'
INSERT INTO User (email, password, first_name, last_name, date_of_birth, address, phone_number, role)
VALUES
('user1@example.com', 'password1', 'John', 'Doe', '1990-01-01', '123 Main St', '555-1234', 'BUYER'),
('user2@example.com', 'password2', 'Jane', 'Smith', '1985-05-15', '456 Oak St', '555-5678', 'BUYER'),
('user3@example.com', 'password3', 'Bob', 'Johnson', '1988-09-30', '789 Pine St', '555-9876', 'BUYER'),
('user4@example.com', 'password4', 'Alice', 'Williams', '1992-03-22', '101 Cedar St', '555-4321', 'BUYER'),
('user5@example.com', 'password5', 'Eva', 'Davis', '1980-11-10', '202 Birch St', '555-8765', 'BUYER');
