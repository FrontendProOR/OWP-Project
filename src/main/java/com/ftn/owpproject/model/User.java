package com.ftn.owpproject.model;


import java.time.LocalDateTime;
import java.util.Objects;

import com.ftn.owpproject.model.enums.UserRole;

public class User {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String password;
	private String emailAddress;
	private String dateOfBirth;
	private String address;
	private String phoneNumber;
	private LocalDateTime registrationDateTime;
	private UserRole role;    
    
    public User( Long id, String firstName, String lastName, String password, String emailAddress,
            String dateOfBirth, String address, String phoneNumber, LocalDateTime registrationDateTime, UserRole role) {
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
	}


    // Empty Constructor
    public User() {
    }

    // Full Constructor without id
    public User( String firstName, String lastName, String password, String emailAddress,
                String dateOfBirth, String address, String phoneNumber, UserRole role) {
        
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
    public User(Long id,  String firstName, String lastName, String password, String emailAddress,
                String dateOfBirth, String address, String phoneNumber, UserRole role) {
        this.id = id;
        
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
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationDateTime = LocalDateTime.now();
        this.role = UserRole.BUYER;  // Assuming a default role for new users
    }
    public User(Long id, String username, String password, String email, String firstName) {
        this.id = id;
        this.password = password;
        this.emailAddress = email;
        this.firstName = firstName;
        
    }
    
    // Getters and Setters
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
                '}';
    }

    public String toFileString() {
        StringBuilder result = new StringBuilder();

        result.append(id).append(";");
        result.append(firstName).append(";");
        result.append(lastName).append(";");
        result.append(password).append(";");
        result.append(emailAddress).append(";");
        result.append(dateOfBirth).append(";");
        result.append(address).append(";");
        result.append(phoneNumber).append(";");
        result.append(registrationDateTime).append(";");
        result.append(role);

        return result.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return Objects.equals(getEmailAddress(), user.getEmailAddress()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(getEmailAddress(), getPassword());
//    }

    
//    public Long getNextAvailableId() {
//        String fileName = "users.txt";
//        URL resource = getClass().getResource("/" + fileName);
//
//        if (resource == null) {
//            System.err.println("File not found: " + fileName);
//            return null;
//        }
//
//        try {
//            Path path = Paths.get(resource.toURI());
//            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
//
//            if (lines.isEmpty()) {
//                return 1L;
//            }
//
//            Long maxId = Long.MIN_VALUE;
//
//            for (String line : lines) {
//                String[] parts = line.split(";");
//                if (parts.length > 0) {
//                    try {
//                        Long userId = Long.parseLong(parts[0].trim());
//                        maxId = Math.max(maxId, userId);
//                    } catch (NumberFormatException ignored) {
//                        // Ignore lines where the ID is not a valid number
//                    }
//                }
//            }
//
//            Long newId = maxId + 1;
//            return newId;
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}

