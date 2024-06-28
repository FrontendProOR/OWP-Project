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
    arrangment_price DECIMAL(10, 2),
    original_price DECIMAL(10, 2),
    total_seats INT,
    available_seats INT,
    discount_percentage DOUBLE DEFAULT 0,
    discount_end_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (travel_category_id) REFERENCES TravelCategory(id) ON DELETE CASCADE
);

-- Create a table for reservations
CREATE TABLE Reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    travel_id BIGINT NOT NULL,
    reservation_date TIMESTAMP NOT NULL,
    reserved_seats INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (travel_id) REFERENCES Travel(id)
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

-- Insert data for future trips
INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangment_price, original_price, total_seats, available_seats, discount_percentage, discount_end_date)
VALUES
    ('AIRPLANE', 'HOTEL', 'Hong Kong', 'hongkong.jpg', 1, '2024-12-15 08:00:00', '2024-12-20 18:00:00', 5, 1500.00, 1500.00, 100, 100, 10.00, '2024-12-10 00:00:00'),
    ('BUS', 'HOTEL', 'Novgorod', 'novgorod.jpg', 2, '2024-11-10 10:00:00', '2024-11-15 20:00:00', 5, 800.00, 800.00, 50, 50, 0, NULL),
    ('PERSONAL', 'APARTMENT', 'Moskva', 'moskva.jpg', 3, '2024-10-05 12:00:00', '2024-10-10 22:00:00', 5, 1200.00, 1200.00, 80, 80, 5.00, '2024-09-30 00:00:00'),
    ('PERSONAL', 'HOTEL', 'Washington', 'washington.jpg', 4, '2024-09-20 14:00:00', '2024-09-25 23:00:00', 5, 2000.00, 2000.00, 120, 120, 0, NULL),
    ('PERSONAL', 'HOTEL', 'Shenzhen', 'shenzhen.jpg', 4, '2024-08-20 14:00:00', '2024-08-25 23:00:00', 5, 2000.00, 2000.00, 120, 120, 0, NULL);

-- Insert additional data for future trips
INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangment_price, original_price, total_seats, available_seats, discount_percentage, discount_end_date)
VALUES
    ('AIRPLANE', 'HOTEL', 'Barcelona', 'barcelona.jpg', 2, '2024-07-01 08:00:00', '2024-07-10 18:00:00', 9, 2500.00, 2500.00, 100, 100, 15.00, '2024-06-25 00:00:00'),
    ('BUS', 'HOTEL', 'Rome', 'rome.jpg', 1, '2024-08-15 10:00:00', '2024-06-20 20:00:00', 5, 900.00, 900.00, 60, 60, 10.00, '2024-06-10 00:00:00'),
    ('PERSONAL', 'APARTMENT', 'Istanbul', 'istanbul.jpg', 3, '2024-09-01 12:00:00', '2024-09-07 22:00:00', 6, 1300.00, 1300.00, 70, 70, 0, NULL),
    ('PERSONAL', 'HOTEL', 'New York', 'newyork.jpg', 4, '2024-08-15 14:00:00', '2024-08-25 23:00:00', 10, 3500.00, 3500.00, 120, 120, 20.00, '2024-08-10 00:00:00'),
    ('AIRPLANE', 'HOTEL', 'Dubai', 'dubai.jpg', 2, '2024-11-20 08:00:00', '2024-11-30 18:00:00', 10, 3000.00, 3000.00, 100, 100, 10.00, '2024-11-15 00:00:00'),
    ('PERSONAL', 'APARTMENT', 'Santorini', 'santorini.jpg', 3, '2024-07-20 12:00:00', '2024-07-27 22:00:00', 7, 1600.00, 1600.00, 80, 80, 0, NULL),
    ('PERSONAL', 'HOTEL', 'Miami', 'miami.jpg', 4, '2024-08-05 14:00:00', '2024-06-15 23:00:00', 10, 2400.00, 2400.00, 120, 120, 10.00, '2024-06-01 00:00:00'),
    ('AIRPLANE', 'HOTEL', 'Tokyo', 'tokyo.jpg', 2, '2024-09-15 08:00:00', '2024-09-25 18:00:00', 10, 2800.00, 2800.00, 100, 100, 15.00, '2024-09-10 00:00:00'),
    ('BUS', 'HOTEL', 'Vienna', 'vienna.jpg', 2, '2024-12-10 10:00:00', '2024-12-15 20:00:00', 5, 1100.00, 1100.00, 60, 60, 5.00, '2024-12-05 00:00:00');

    
-- Insert data for past trips
INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangment_price, original_price, total_seats, available_seats, discount_percentage, discount_end_date)
VALUES
    ('BUS', 'HOTEL', 'Jerusalem', 'jerusalim.jpg', 4, '2023-07-15 08:00:00', '2023-07-20 18:00:00', 5, 1800.00, 1800.00, 90, 90, 0, NULL),
    ('AIRPLANE', 'HOTEL', 'Tokyo', 'tokyo.jpg', 2, '2023-05-10 10:00:00', '2023-05-15 20:00:00', 5, 2200.00, 2200.00, 70, 70, 0, NULL),
    ('PERSONAL', 'APARTMENT', 'Paris', 'paris.jpg', 3, '2023-04-05 12:00:00', '2023-04-10 22:00:00', 5, 1300.00, 1300.00, 60, 60, 0, NULL),
    ('PERSONAL', 'HOTEL', 'Berlin', 'berlin.jpg', 1, '2023-03-20 14:00:00', '2023-03-25 23:00:00', 5, 1700.00, 1700.00, 80, 80, 0, NULL),
    ('PERSONAL', 'HOTEL', 'London', 'london.jpg', 4, '2023-02-20 14:00:00', '2023-02-25 23:00:00', 5, 2100.00, 2100.00, 90, 90, 0, NULL);

-- Insert reservations for buyers (2 in the future and 2 in the past for each buyer)
INSERT INTO Reservation (user_id, travel_id, reservation_date, reserved_seats, total_price)
VALUES
    -- John Doe's reservations
    (1, 1, CURRENT_TIMESTAMP, 2, 2700.00),
    (1, 2, CURRENT_TIMESTAMP, 2, 1600.00),
    (1, 6, CURRENT_TIMESTAMP, 2, 3600.00),
    (1, 7, CURRENT_TIMESTAMP, 2, 4400.00),
    -- Alice Smith's reservations
    (2, 3, CURRENT_TIMESTAMP, 2, 2280.00),
    (2, 4, CURRENT_TIMESTAMP, 2, 3200.00),
    (2, 8, CURRENT_TIMESTAMP, 2, 4400.00),
    (2, 9, CURRENT_TIMESTAMP, 2, 1600.00),
    -- Bob Johnson's reservations
    (3, 5, CURRENT_TIMESTAMP, 2, 3000.00),
    (3, 1, CURRENT_TIMESTAMP, 2, 2700.00),
    (3, 10, CURRENT_TIMESTAMP, 2, 3600.00),
    (3, 6, CURRENT_TIMESTAMP, 2, 3600.00),
    -- Emily Brown's reservations
    (4, 2, CURRENT_TIMESTAMP, 2, 1600.00),
    (4, 3, CURRENT_TIMESTAMP, 2, 2280.00),
    (4, 7, CURRENT_TIMESTAMP, 2, 4400.00),
    (4, 8, CURRENT_TIMESTAMP, 2, 4400.00),
    -- Michael Lee's reservations
    (5, 4, CURRENT_TIMESTAMP, 2, 3200.00),
    (5, 5, CURRENT_TIMESTAMP, 2, 3000.00),
    (5, 9, CURRENT_TIMESTAMP, 2, 1600.00),
    (5, 10, CURRENT_TIMESTAMP, 2, 3600.00);
