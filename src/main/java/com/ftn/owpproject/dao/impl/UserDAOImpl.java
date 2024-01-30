package com.ftn.owpproject.dao.impl;

import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.owpproject.dao.UserDAO;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.UserRole;

@SuppressWarnings("unused")
@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class UserRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, User> users = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			String firstName = resultSet.getString(index++);
			String lastName = resultSet.getString(index++);	
			String password = resultSet.getString(index++);
			String email = resultSet.getString(index++);
//			LocalDate dateOfBirth = resultSet.getTimestamp(index++).toLocalDateTime().toLocalDate();
			LocalDate dateOfBirth = resultSet.getDate(index++).toLocalDate();
			String address = resultSet.getString(index++);
			String phoneNumber = resultSet.getString(index++);
			
			
			User user = users.get(id);
			if (user == null) {
				user = new User(id,firstName,lastName,password,email,dateOfBirth,address,phoneNumber);
				users.put(user.getId(), user);
			}
		}

		public List<User> getUsers() {
			return new ArrayList<>(users.values());
		}

	}	

    @Override
    public User findOne(Long id) {
        String sql = "SELECT * FROM User WHERE id = ?";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public User findOne(String email) {
    	try {
        String sql = "SELECT *" +
                     "FROM User WHERE email = ?";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email);

        return rowCallbackHandler.getUsers().get(0);
    	}catch (Exception e) {
			return null;
		}
    }

    @Override
    public User findOne(String email, String password) {
        String sql = "SELECT * " +
                     "FROM User WHERE email = ? AND password = ?";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email, password);

//        if (rowCallbackHandler.getUsers().size() == 0) {
//            return null;
//        }

        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * " +
                     "FROM User ";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getUsers();
    }

    @Transactional
    @Override
    public int save(User user) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO User (email, password, first_name, last_name, date_of_birth, address, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, user.getEmailAddress());
                preparedStatement.setString(index++, user.getPassword());
                preparedStatement.setString(index++, user.getFirstName());
                preparedStatement.setString(index++, user.getLastName());
                LocalDate dateOfBirth = user.getDateOfBirth();
                Date dateOfBirthSQL = Date.valueOf(dateOfBirth);
                preparedStatement.setDate(index++, dateOfBirthSQL);
                preparedStatement.setString(index++, user.getAddress());
                preparedStatement.setString(index++, user.getPhoneNumber());
                preparedStatement.setString(index++, user.getRole().name());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }


    @Transactional
    @Override
    public int update(User user) {
        String sql = "UPDATE User SET first_name = ?, last_name = ?, email = ?, password = ?, date_of_birth = ?, address = ?, phone_number = ?, role = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getPassword(), user.getDateOfBirth(), user.getAddress(), user.getPhoneNumber(), user.getRole().name(), user.getId()) == 1;

        return success ? 1 : 0;
    }


    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM User WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
