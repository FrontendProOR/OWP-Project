package com.ftn.owpproject.model;


import java.time.LocalDateTime;


import com.ftn.owpproject.model.enums.UserRole;

public class User {
    private Long id;
    private String username;
    private String password;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private LocalDateTime registrationDateTime;
    private UserRole role;

    // Empty Constructor
    public User() {
    }

    // Full Constructor without id
    public User(String username, String password, String emailAddress, String firstName, String lastName,
                String dateOfBirth, String address, String phoneNumber, UserRole role) {
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDateTime = LocalDateTime.now();
        this.role = role;
    }

    // Full Constructor with id
    public User(Long id, String username, String password, String emailAddress, String firstName, String lastName,
                String dateOfBirth, String address, String phoneNumber, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDateTime = LocalDateTime.now();
        this.role = role;
    }

    public User(String firstName, String lastName, String emailAddress, String password) {
        this.username = generateDefaultUsername(firstName, lastName);
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationDateTime = LocalDateTime.now();
        this.role = UserRole.PASSENGER;  // Assuming a default role for new users
    }
    public User(Long id, String username, String password, String email, String firstName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = email;
        this.firstName = firstName;
        // Set other default values or perform additional initialization if needed
    }

    // Helper method to generate a default username based on first and last name
    private String generateDefaultUsername(String firstName, String lastName) {
        // Logic to generate a username, e.g., concatenating first and last name
        // This is just a placeholder; you may want to implement your own logic
        return firstName.toLowerCase() + "." + lastName.toLowerCase();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String toFileString() {
        StringBuilder result = new StringBuilder();

        result.append(id).append(";");
        result.append(username).append(";");
        result.append(password).append(";");
        result.append(emailAddress).append(";");
        result.append(firstName).append(";");
        result.append(lastName).append(";");
        result.append(dateOfBirth).append(";");
        result.append(address).append(";");
        result.append(phoneNumber).append(";");
        result.append(registrationDateTime).append(";");
        result.append(role);

        return result.toString();
    }
    
//    public String toString() {
//        return this.username + " " + this.lastName + " (" + this.emailAddress + ")";
//    }
    

}

