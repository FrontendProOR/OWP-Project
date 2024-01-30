package com.ftn.owpproject.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ftn.owpproject.model.enums.UserRole;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private LocalDateTime registrationDateTime;
    private UserRole role;
    private Long jmbg; 

    public User(String firstName, String lastName, String password, String emailAddress, LocalDate dateOfBirth,
                String address, String phoneNumber, LocalDateTime registrationDateTime, UserRole role, Long jmbg) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDateTime = registrationDateTime;
        this.role = role;
        this.jmbg = jmbg;
    }

    public User(Long id, String firstName, String lastName, String password, String emailAddress, LocalDate dateOfBirth,
                String address, String phoneNumber, LocalDateTime registrationDateTime, UserRole role, Long jmbg) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDateTime = registrationDateTime;
        this.role = role;
        this.jmbg = jmbg;
    }

    public User(Long id, String firstName, String lastName, String password, String emailAddress,
                LocalDate dateOfBirth, String address, String phoneNumber, Long jmbg) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registrationDateTime = LocalDateTime.now();
        this.role = UserRole.BUYER;
        this.jmbg = jmbg;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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

    public Long getJmbg() {
        return jmbg;
    }

    public void setJmbg(Long jmbg) {
        this.jmbg = jmbg;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDateTime=" + registrationDateTime +
                ", role=" + role +
                ", jmbg=" + jmbg +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return Objects.equals(getEmailAddress(), user.getEmailAddress()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmailAddress(), getPassword());
    }
}
