package com.ftn.owpproject.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.service.UserService;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        try {
            Path path = Paths.get(pathToFile);
            System.out.println(path.toFile().getAbsolutePath());
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            for (String line : lines) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;

                String[] tokens = line.split(";");
                Long id = Long.parseLong(tokens[0]);
                String username = tokens[1];
                String password = tokens[2];
                String email = tokens[3];
                String firstName = tokens[4];

                users.put(id, new User(id, username, password, email, firstName));

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
        Map<Long, User> users = readFromFile();
        User found = null;
        for (User user : users.values()) {
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

    @Override
    public User save(User user) {
        Map<Long, User> users = readFromFile();
        Long nextId = nextId(users);

        if (user.getId() == null) {
            user.setId(nextId++);
        }
        users.put(user.getId(), user);
        saveToFile(users);
        return user;
    }

    @Override
    public User update(User user) {
        Map<Long, User> users = readFromFile();
        users.put(user.getId(), user);
        saveToFile(users);
        return user;
    }

    @Override
    public User delete(Long id) {
        Map<Long, User> users = readFromFile();

        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Tried to remove a non-existing user");
        }

        User user = users.get(id);
        if (user != null) {
            users.remove(id);
        }
        saveToFile(users);
        return user;
    }

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public User findOneById(int id) {
		 Map<Long, User> users = readFromFile();
	        return users.get(id);
	}
	
}
