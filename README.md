### README.md

# OWP Project

## Student: Ognjen Radić (SR16/2022)

## Introduction
This project is a travel reservation system developed using Spring Boot. It allows users to view, create, and manage travel reservations. The application includes functionality for both users (buyers) and managers.

## Controllers and Routes

### ReservationController

#### `@GetMapping("/reservations")`
- **Route:** `/reservations`
- **Description:** Displays the reservations of the logged-in user. Redirects to the login page if the user is not logged in.

#### `@GetMapping("/reservations/make")`
- **Route:** `/reservations/make`
- **Description:** Shows the form to make a new reservation. Only accessible to logged-in users who are not managers.

#### `@PostMapping("/reservations/make")`
- **Route:** `/reservations/make`
- **Description:** Handles the creation of a new reservation. Updates available seats for the travel and saves the reservation.

#### `@PostMapping("/reservations/deleteExpired")`
- **Route:** `/reservations/deleteExpired`
- **Description:** Deletes an expired reservation. Only accessible to logged-in users.

#### `@PostMapping("/reservations/cancel")`
- **Route:** `/reservations/cancel`
- **Description:** Cancels a reservation if the travel is more than 48 hours away. Updates available seats.

#### `@PostMapping("/reservations/cancelAll")`
- **Route:** `/reservations/cancelAll`
- **Description:** Cancels all reservations for the logged-in user if the travels are more than 48 hours away. Updates available seats.

#### `@PostMapping("/reservations/deleteAllExpired")`
- **Route:** `/reservations/deleteAllExpired`
- **Description:** Deletes all expired reservations for the logged-in user.

### TravelController

#### `@GetMapping({"/", "/index"})`
- **Route:** `/` or `/index`
- **Description:** Displays the index page with promotional, seasonal, and all other travels.

#### `@GetMapping("/travels")`
- **Route:** `/travels`
- **Description:** Displays the list of all travels. Accessible only to managers.

#### `@GetMapping("/travels/editTravel")`
- **Route:** `/travels/editTravel`
- **Description:** Shows the form to edit travel details. Accessible only to managers.

#### `@PostMapping("/travels/edit")`
- **Route:** `/travels/edit`
- **Description:** Handles the update of travel details. Accessible only to managers.

#### `@PostMapping("/travels/showEditForm")`
- **Route:** `/travels/showEditForm`
- **Description:** Shows the edit form for a specific travel.

#### `@GetMapping("/travels/addTravel")`
- **Route:** `/travels/addTravel`
- **Description:** Shows the form to add a new travel. Accessible only to managers.

#### `@PostMapping("/travels/add")`
- **Route:** `/travels/add`
- **Description:** Handles the creation of a new travel. Accessible only to managers.

#### `@GetMapping("/travels/details")`
- **Route:** `/travels/details`
- **Description:** Displays the details of a specific travel. Accessible to all logged-in users.

#### `@PostMapping("/travels/delete")`
- **Route:** `/travels/delete`
- **Description:** Deletes a travel if it has no reservations. Accessible only to managers.

### UserController

#### `@GetMapping("/users")`
- **Route:** `/users`
- **Description:** Displays the list of all users. Accessible only to logged-in users.

#### `@GetMapping("/users/add")`
- **Route:** `/users/add`
- **Description:** Shows the form to add a new user.

#### `@PostMapping("/users/add")`
- **Route:** `/users/add`
- **Description:** Handles the creation of a new user.

#### `@PostMapping("/users/delete")`
- **Route:** `/users/delete`
- **Description:** Deletes a user and logs them out.

#### `@GetMapping("/users/details")`
- **Route:** `/users/details`
- **Description:** Displays the details of a specific user. Accessible only to logged-in users.

#### `@PostMapping("/users/edit")`
- **Route:** `/users/edit`
- **Description:** Handles the update of user details. Accessible only to the logged-in user.

#### `@GetMapping("/users/login")`
- **Route:** `/users/login`
- **Description:** Displays the login form.

#### `@PostMapping("/users/login")`
- **Route:** `/users/login`
- **Description:** Handles user login.

#### `@GetMapping("/users/logout")`
- **Route:** `/users/logout`
- **Description:** Logs out the user.

#### `@GetMapping("/users/error")`
- **Route:** `/users/error`
- **Description:** Displays the error page.

## How to Run

1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn clean install`.
4. Start the application with `mvn spring-boot:run`.
5. Access the application at `http://localhost:8080/owpproject/`.

## Contact
For any questions or inquiries, please contact Ognjen Radić at radic.sr16.2022@uns.ac.rs