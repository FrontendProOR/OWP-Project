package com.ftn.owpproject.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.model.Destination;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.UserService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("filesUsers")
public class UserServiceImpl implements UserService {
	
    @Value("${users.pathToFile}")
    private String pathToFile;

    private Map<Long, User> readFromFile() {
        Map<Long, User> users = new HashMap<>();
        Long nextId = 1L;
        String fileName = "users.txt";
		URL resource = getClass().getResource("/" + fileName);
        if(resource == null) {
        	System.err.println("File not found:"+fileName);
        }
        try {
            Path path = Paths.get(resource.toURI());
            System.out.println(path.toAbsolutePath());
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            for (String line : lines) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;

                String[] tokens = line.split(";");
                Long id = Long.parseLong(tokens[0]);
                String firstName = tokens[1];
                String lastName = tokens[2];
                String username = tokens[3];
                String password = tokens[4];
                String email = tokens[5];
                String dateOfBirth = tokens[6];
                String address = tokens[7];
                String phoneNumber = tokens[8];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

                // Parse the input string to LocalDateTime
                LocalDateTime parsedDateTime = LocalDateTime.parse(tokens[9], formatter);
                LocalDateTime registrationDateTime = parsedDateTime;
                
                
                UserRole role = UserRole.PASSENGER;

                users.put(id, new User(id, firstName,lastName,username,password,email,dateOfBirth,address,phoneNumber,registrationDateTime,role));

                if (nextId < id)
                    nextId = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private Map<Long, User> saveToFile(Map<Long, User> users) {
        Map<Long, User> ret = new HashMap<>();

        try {
            Path path = Paths.get(pathToFile);
            System.out.println(path.toFile().getAbsolutePath());
            List<String> lines = new ArrayList<>();

            for (User user : users.values()) {
                lines.add(user.toFileString());
                ret.put(user.getId(), user);
            }
            Files.write(path, lines, Charset.forName("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private Long nextId(Map<Long, User> users) {
        Long nextId = 1L;
        for (Long id : users.keySet()) {
            if (nextId < id)
                nextId = id;
        }
        return ++nextId;
    }

    @Override
    public User findOneById(Long id) {
        Map<Long, User> users = readFromFile();
        return users.get(id);
    }

    @Override
    public User findOne(String email) {
        Map<Long, User> users = readFromFile();
        User found = null;
        for (User user : users.values()) {
            if (user.getEmailAddress().equals(email)) {
                found = user;
                break;
            }
        }
        return found;
    }

    @Override
    public User findOne(String email, String password) {
        List<User> users = findAll();
//        System.out.println(users);
        User found = null;
        for (User user : users) {
//        	System.out.println(user.getEmailAddress());
            if (user.getEmailAddress().equals(email) && user.getPassword().equals(password)) {
                found = user;
                break;
            }
        }
        return found;
    }

    @Override
    public List<User> findAll() {
        Map<Long, User> users = readFromFile();
        return new ArrayList<>(users.values());
    }

    public Long getNextAvailableId() {
        String fileName = "users.txt";
        URL resource = getClass().getResource("/" + fileName);

        if (resource == null) {
            System.err.println("File not found: " + fileName);
            return null;
        }

        try {
            Path path = Paths.get(resource.toURI());
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            if (lines.isEmpty()) {
                return 1L;
            }

            Long maxId = Long.MIN_VALUE;

            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    try {
                        Long userId = Long.parseLong(parts[0].trim());
                        maxId = Math.max(maxId, userId);
                    } catch (NumberFormatException ignored) {
                        // Ignore lines where the ID is not a valid number
                    }
                }
            }

            Long newId = maxId + 1;
            return newId;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static void saveStringToFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(User user) {
    	String filePath = "src/main/resources/users.txt"; 
    	if (user.getId() == null) {

            user.setId(getNextAvailableId());
        }
        String stringToSave = user.toFileString();
        saveStringToFile(filePath, stringToSave);
    }

    @Override
    public User update(User user) {
        Map<Long, User> users = readFromFile();
        users.put(user.getId(), user);
        saveToFile(users);
        return user;
    }

    public void delete(Long id) {
    	Map<Long, User> users = readFromFile();
    	final String fileName = "users.txt";
        URL resource = getClass().getResource("/" + fileName);

        if (resource == null) {
            System.err.println("File not found: " + fileName);
        } else {
            try {
                Path path = Paths.get(resource.toURI());
                System.out.println(path.toAbsolutePath());
                List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
                List<String> newLines = new ArrayList<>();

                for (String line : lines) {
                    line = line.trim();
                    if (line.equals("") || line.startsWith("#"))
                        continue;

                    String[] tokens = line.split(";");
                    Long currentId = Long.parseLong(tokens[0]);

                    if (currentId.equals(id)) {
                        continue;
                    }
                    newLines.add(line);
                }
                Files.write(path, newLines, Charset.forName("UTF-8"));
                users.remove(id);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        }
    
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public User findOneById(int id) {
		 Map<Long, User> users = readFromFile();
	        return users.get(id);
	}

	@Override
	public Map<Long, User> findAllAsMap() {
	    Map<Long, User> users = readFromFile();
	    return new HashMap<>(users); 
	}
	
}
