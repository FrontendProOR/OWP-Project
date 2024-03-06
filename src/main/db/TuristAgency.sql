-- Drop the schema if it exists
DROP SCHEMA IF EXISTS TURISTAGENCY;

-- Create a new schema with UTF-8 character set
CREATE SCHEMA TURISTAGENCY DEFAULT CHARACTER SET utf8;

-- Use the created schema
USE TURISTAGENCY;

-- Create a table for users
CREATE TABLE User (
    id BIGINT AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    registration_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20),
    jmbg VARCHAR(13),
    PRIMARY KEY (id)
);

-- Create a table for trip categories
CREATE TABLE TravelCategory (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
);

-- Create a table for trips
CREATE TABLE Travel (
    id BIGINT AUTO_INCREMENT,
    transportation_type VARCHAR(50),
    accommodation_type VARCHAR(50),
    destination_name VARCHAR(255),
    location_image VARCHAR(255),
    travel_category_id BIGINT,
    departure_date_time DATETIME,
    return_date_time DATETIME,
    number_of_nights INT,
    arrangement_price DECIMAL(10, 2),
    total_seats INT,
    available_seats INT,
    PRIMARY KEY (id),
    FOREIGN KEY (travel_category_id) REFERENCES TravelCategory(id) ON DELETE CASCADE
);

-- Insert data for users with role BUYER
INSERT INTO User (first_name, last_name, password, email, date_of_birth, address, phone_number, role, jmbg)
VALUES
    ('John', 'Doe', 'password123', 'john@example.com', '1990-05-15', '123 Street St, City', '1234567890', 'BUYER', '1234567890123'),
    ('Alice', 'Smith', 'securepass', 'alice@example.com', '1985-09-22', '456 Avenue Ave, Town', '0987654321', 'BUYER', '9876543210987'),
    ('Bob', 'Johnson', 'mypassword', 'bob@example.com', '1982-11-10', '789 Road Rd, Village', '5551234567', 'BUYER', '4567890123456'),
    ('Emily', 'Brown', 'p@ssw0rd', 'emily@example.com', '1995-03-30', '321 Lane Ln, County', '7777777777', 'BUYER', '7890123456789'),
    ('Michael', 'Lee', 'pass123', 'michael@example.com', '1988-07-18', '555 Hill Hill, State', '3333333333', 'BUYER', '2345678901234');

-- Insert data for users with role MANAGER
INSERT INTO User (first_name, last_name, password, email, date_of_birth, address, phone_number, role, jmbg)
VALUES
    ('Sarah', 'Wilson', 'managerpass', 'sarah@example.com', '1976-12-05', '101 Manager St, City', '1112223333', 'MANAGER', '1112223334445'),
    ('David', 'Thompson', 'davidpass', 'david@example.com', '1980-08-20', '202 Manager Ave, Town', '4445556666', 'MANAGER', '2223334445556'),
    ('Jennifer', 'Garcia', 'jenpass', 'jennifer@example.com', '1972-06-15', '303 Manager Rd, Village', '7778889999', 'MANAGER', '3334445556667'),
    ('Robert', 'Martinez', 'robpass', 'robert@example.com', '1965-04-03', '404 Manager Ln, County', '1234567890', 'MANAGER', '4445556667778'),
    ('Jessica', 'Lopez', 'jesspass', 'jessica@example.com', '1978-10-12', '505 Manager Hill, State', '9876543210', 'MANAGER', '5556667778889');

-- Insert data for travel categories
INSERT INTO TravelCategory (name, description)
VALUES
    ('SKIING', 'Enjoy skiing in beautiful mountain resorts.'),
    ('SUMMER_VACATION', 'Relax and unwind on a summer getaway.'),
    ('LAST_MINUTE', 'Grab last-minute deals for spontaneous trips.'),
    ('NEW_YEAR', 'Celebrate the New Year in exciting destinations.');

-- Insert data for trips
INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangement_price, total_seats, available_seats)
VALUES
    ('AIRPLANE', 'HOTEL', 'Hong Kong', 'hongkong.jpg', 1, '2024-03-15 08:00:00', '2024-03-20 18:00:00', 5, 1500.00, 100, 100),
    ('BUS', 'HOTEL', 'Novgorod', 'novgorod.jpg', 2, '2024-04-10 10:00:00', '2024-04-15 20:00:00', 5, 800.00, 50, 50),
    ('PERSONAL', 'APARTMENT', 'Moskva', 'moskva.jpg', 3, '2024-05-05 12:00:00', '2024-05-10 22:00:00', 5, 1200.00, 80, 80),
    ('PERSONAL', 'HOTEL', 'Washington', 'washington.jpg', 4, '2024-06-20 14:00:00', '2024-06-25 23:00:00', 5, 2000.00, 120, 120),
    ('PERSONAL', 'HOTEL', 'Shenzhen', 'shenzhen.jpg', 4, '2024-06-20 14:00:00', '2024-06-25 23:00:00', 5, 2000.00, 120, 120),
    ('BUS', 'HOTEL', 'Jerusalem', 'jerusalim.jpg', 4, '2024-07-15 08:00:00', '2024-07-20 18:00:00', 5, 1800.00, 90, 90);
