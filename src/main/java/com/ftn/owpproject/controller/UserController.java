package com.ftn.owpproject.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static final String USER_KEY = "user";

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public void getLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String password,
            HttpSession session, HttpServletResponse response) throws IOException {
        postLogin(email, password, session, response);
    }
    
    @PostMapping(value = "/login")
    @ResponseBody
    public void postLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String password,
            HttpSession session, HttpServletResponse response) throws IOException {

        User user = userService.findOne(email, password);
        String error = "";
        if (user == null)
            error = "Invalid credentials<br/>";

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out;
        out = response.getWriter();

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\r\n");
        htmlBuilder.append("<html>\r\n");
        htmlBuilder.append("<head>\r\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\r\n");
        htmlBuilder.append("    <title>User Login</title>\r\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviForma.css\"/>\r\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviHorizontalniMeni.css\"/>\r\n");
        htmlBuilder.append("</head>\r\n");
        htmlBuilder.append("<body>\r\n");
        htmlBuilder.append("    <ul>\r\n");
        htmlBuilder.append("        <li><a href=\"/owpproject/index.html\">Homepage</a></li>\r\n");
        htmlBuilder.append("        <li><a href=\"/owpproject/registration.html\">Register</a></li>\r\n");
        htmlBuilder.append("        <li><a href=\"/owpproject/users/logout\">Logout</a></li>\r\n");
        htmlBuilder.append("    </ul>\r\n");

        if (!error.equals("")) {
            htmlBuilder.append("    <div>" + error + "</div>\r\n");
        } else if (session.getAttribute(USER_KEY) != null) {
            error = "User is already logged in; you must log out first<br/>";
            htmlBuilder.append("    <div>" + error + "</div>\r\n");
            htmlBuilder.append("    <a href=\"/owpproject/index.html\">Back</a>\r\n");
            htmlBuilder.append("    <br/>\r\n");
        } else {
            session.setAttribute(USER_KEY, user);
            response.sendRedirect("/owpproject/users");
            return;
        }

        htmlBuilder.append("    <form method=\"post\" action=\"/owpproject/users/login\">\r\n");
        htmlBuilder.append("        <table>\r\n");
        htmlBuilder.append("            <caption>User Login</caption>\r\n");
        htmlBuilder.append("            <tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n");
        htmlBuilder.append("            <tr><th>Password:</th><td><input type=\"password\" value=\"\" name=\"password\" "
                + " required/></td></tr>\r\n");
        htmlBuilder.append("            <tr><th></th><td><input type=\"submit\" value=\"Login\" /></td>\r\n");
        htmlBuilder.append("        </table>\r\n");
        htmlBuilder.append("    </form>\r\n");
        htmlBuilder.append("    <br/>\r\n");
        htmlBuilder.append("</body>\r\n");
        htmlBuilder.append("</html>\r\n");

        out.write(htmlBuilder.toString());
    }

    @GetMapping(value = "/logout")
    @ResponseBody
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {

        User user = (User) session.getAttribute(USER_KEY);
        String error = "";
        if (user == null)
            error = "User is not logged in<br/>";

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out;
        out = response.getWriter();

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\r\n");
        htmlBuilder.append("<html>\r\n");
        htmlBuilder.append("<head>\r\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\r\n");
        htmlBuilder.append("    <title>User Logout</title>\r\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviForma.css\"/>\r\n");
        htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviHorizontalniMeni.css\"/>\r\n");
        htmlBuilder.append("</head>\r\n");
        htmlBuilder.append("<body>\r\n");
        htmlBuilder.append("    <ul>\r\n");
        htmlBuilder.append("        <li><a href=\"/owpproject/registracija.html\">Register</a></li>\r\n");
        htmlBuilder.append("        <li><a href=\"/owpproject/index.html\">Homepage</a></li>\r\n");
        htmlBuilder.append("    </ul>\r\n");

        if (!error.equals("")) {
            htmlBuilder.append("    <div>" + error + "</div>\r\n");
        } else {
            session.removeAttribute(USER_KEY);
            session.invalidate();
            response.sendRedirect("/owpproject/users/login");
            return;
        }
        
        htmlBuilder.append("    <form method=\"post\" action=\"/owpproject/users/login\">\r\n");
        htmlBuilder.append("        <table>\r\n");
        htmlBuilder.append("            <caption>User Sign in after logout</caption>\r\n");
        htmlBuilder.append("            <tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n");
        htmlBuilder.append("            <tr><th>Password:</th><td><input type=\"password\" value=\"\" name=\"password\" required/></td></tr>\r\n");
        htmlBuilder.append("            <tr><th></th><td><input type=\"submit\" value=\"Login\" /></td>\r\n");
        htmlBuilder.append("        </table>\r\n");
        htmlBuilder.append("    </form>\r\n");
        htmlBuilder.append("    <br/>\r\n");
        htmlBuilder.append("</body>\r\n");
        htmlBuilder.append("</html>\r\n");

        out.write(htmlBuilder.toString());
    }

    @PostMapping(value = "/registration")
    public void registration(
                             @RequestParam(required = true) String firstName, 
                             @RequestParam(required = true) String lastName,
                             @RequestParam(required = true) String username, 
                             @RequestParam(required = true) String password,
                             @RequestParam(required = true) String emailAddress,
                             @RequestParam(required = true) String dateOfBirth,
                             @RequestParam(required = true) String address,
                             @RequestParam(required = true) String phoneNumber,
                             HttpSession session, HttpServletResponse response) throws IOException {
        
        // Assuming UserRole is a predefined enum for user roles
        UserRole role = UserRole.BUYER;  // Set the appropriate role for new users

        LocalDateTime registrationDateTime = LocalDateTime.now();  // Set the current registration date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        // Format the LocalDateTime using the defined formatter
        String formattedDateTimeString = registrationDateTime.format(formatter);
        
        LocalDateTime formattedDateTime = LocalDateTime.parse(formattedDateTimeString, formatter);
        
        Long idRandom = new Random().nextLong();
        
        User user = new User( idRandom,firstName, lastName, password, emailAddress,
                             dateOfBirth, address, phoneNumber, formattedDateTime, role);
        
        userService.save(user);

        response.sendRedirect("/owpproject/users");
    }


    @GetMapping
    @ResponseBody
    public void getUsers(HttpSession session, HttpServletResponse response) throws IOException {
        List<User> users = userService.findAll();

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out;
        out = response.getWriter();

        StringBuilder htmlBuilder = new StringBuilder();
        if (users.size() == 0) {
            // No users, TODO: Provide a message and redirect
        } else {
            htmlBuilder.append("<!DOCTYPE html>\r\n");
            htmlBuilder.append("<html>\r\n");
            htmlBuilder.append("<head>\r\n");
            htmlBuilder.append("    <meta charset=\"UTF-8\">\r\n");
            htmlBuilder.append("    <title>User List</title>\r\n");
            htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviTabela.css\"/>\r\n");
            htmlBuilder.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviHorizontalniMeni.css\"/>\r\n");
            htmlBuilder.append("</head>\r\n");
            htmlBuilder.append("<body>\r\n");
            htmlBuilder.append("    <ul>\r\n");
//            htmlBuilder.append("        <li><a href=\"/owpproject/books\">Books</a></li>\r\n");
            htmlBuilder.append("        <li><a href=\"/owpproject/index.html\">Homepage</a></li>\r\n");
            htmlBuilder.append("        <li><a href=\"/owpproject/users\">Users</a></li>\r\n");
            htmlBuilder.append("        <li><a href=\"/owpproject/registration.html\">Register</a></li>\r\n");
            htmlBuilder.append("        <li><a href=\"/owpproject/users/logout\">Logout</a></li>\r\n");
            htmlBuilder.append("    </ul>\r\n");
            htmlBuilder.append("        <table>\r\n");
            htmlBuilder.append("            <caption>Users</caption>\r\n");
            htmlBuilder.append("            <tr>\r\n");
            htmlBuilder.append("                <th>First Name</th>\r\n");
            htmlBuilder.append("                <th>Last Name</th>\r\n");
            htmlBuilder.append("                <th>Email</th>\r\n");
            htmlBuilder.append("				<th>Username</th>\r'n");
            htmlBuilder.append("				<th>Password</th>\r'n");
            htmlBuilder.append("				<th>Date Of Birth</th>\r'n");
            htmlBuilder.append("				<th>Address</th>\r'n");
            htmlBuilder.append("				<th>DateOfRegistration</th>\r'n");
            htmlBuilder.append("				<th>Role</th>\r'n");
            htmlBuilder.append("                <th></th>\r\n");
            htmlBuilder.append("            </tr>\r\n");

            for (User u : users) {
                htmlBuilder.append("            <tr>\r\n");
                htmlBuilder.append("                <td>" + u.getFirstName() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getLastName() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getPassword() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getEmailAddress() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getDateOfBirth() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getAddress() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getRegistrationDateTime().toString() + "</td>\r\n");
                htmlBuilder.append("                <td>" + u.getRole().toString() + "</td>\r\n");
                htmlBuilder.append("                <td>\r\n");
                htmlBuilder.append("    <form method=\"post\" action=\"/owpproject/users/delete\">\r\n");
                htmlBuilder.append("        <input type=\"hidden\" name=\"id\" value=\"" + u.getId() + "\">\r\n");
                htmlBuilder.append("        <input type=\"submit\" value=\"Delete\">\r\n");
                htmlBuilder.append("    </form>\r\n");
                htmlBuilder.append("                </td>\r\n");
                htmlBuilder.append("            </tr>\r\n");
            }
            htmlBuilder.append("        </table>\r\n");
            htmlBuilder.append("    </body>\r\n");
            htmlBuilder.append("</html>\r\n");
        }

        out.write(htmlBuilder.toString());
    }

    @PostMapping(value = "/delete")
    public void deleteUser(@RequestParam Long id, HttpServletResponse response) throws IOException {
        userService.delete(id);
        response.sendRedirect("/owpproject/users");
    }
}
