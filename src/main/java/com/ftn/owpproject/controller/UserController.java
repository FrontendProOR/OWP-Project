package com.ftn.owpproject.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.UserService;

@SuppressWarnings("unused")
@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static final String USER_KEY = "user";

    @Autowired
	private ServletContext servletContext;
	private  String bURL; 
    
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
	}
	
    @Autowired
    private UserService userService;

//    private String htmlFilePath = "src/main/resources/error.html";

    
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
            error = "Invalid credentials";

        if (!error.equals("")) {
            PrintWriter out;
            out = response.getWriter();
            File htmlFile = new File("src/main/resources/static/error.html");
            Document doc = Jsoup.parse(htmlFile, "UTF-8");

            Element body = doc.select("body").first();

            if (!error.equals("")) {
                Element divError = new Element(Tag.valueOf("div"), "").text(error);
                body.appendChild(divError);
            }

            Element loginForm = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "users/login");
            Element table = new Element(Tag.valueOf("table"), "");
            Element caption = new Element(Tag.valueOf("caption"), "").text("User Login");
            Element trEmail = new Element(Tag.valueOf("tr"), "");
            Element thEmail = new Element(Tag.valueOf("th"), "").text("Email:");
            Element tdEmail = new Element(Tag.valueOf("td"), "").appendChild(new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "email"));
            Element trPassword = new Element(Tag.valueOf("tr"), "");
            Element thPassword = new Element(Tag.valueOf("th"), "").text("Password:");
            Element tdPassword = new Element(Tag.valueOf("td"), "").appendChild(new Element(Tag.valueOf("input"), "").attr("type", "password").attr("name", "password"));
            Element trSubmit = new Element(Tag.valueOf("tr"), "");
            Element thSubmit = new Element(Tag.valueOf("th"), "");
            Element tdSubmit = new Element(Tag.valueOf("td"), "").appendChild(new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Login"));

            trEmail.appendChild(thEmail);
            trEmail.appendChild(tdEmail);
            trPassword.appendChild(thPassword);
            trPassword.appendChild(tdPassword);
            trSubmit.appendChild(thSubmit);
            trSubmit.appendChild(tdSubmit);

            table.appendChild(caption);
            table.appendChild(trEmail);
            table.appendChild(trPassword);
            table.appendChild(trSubmit);

            loginForm.appendChild(table);

            body.appendChild(loginForm);

            out.write(doc.html());
            return;
        }

        if (session.getAttribute(USER_KEY) != null)
            error = "User is already logged in. You must log out first<br/>";

        if (!error.equals("")) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out;
            out = response.getWriter();


            StringBuilder retVal = new StringBuilder();
            retVal.append("<!DOCTYPE html>\r\n");
            retVal.append("<html>\r\n");
            retVal.append("<head>\r\n");
            retVal.append("    <meta charset=\"UTF-8\">\r\n");
            retVal.append("    <base href=\"/owpproject/\">\r\n");
            retVal.append("    <title>User Login</title>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviTabela.css\"/>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/owpproject/css/StiloviHorizontalniMeni.css\"/>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n");
            retVal.append("</head>\r\n");
            retVal.append("<body>\r\n");
            retVal.append("    <ul>\r\n");
            retVal.append("        <li><a href=\"registration.html\">Register</a></li>\r\n");
            retVal.append("    </ul>\r\n");

            if (!error.equals("")) {
                retVal.append("    <div>" + error + "</div>\r\n");
            }

            retVal.append("    <a href=\"index.html\">Back</a>\r\n");
            retVal.append("    <br/>\r\n");
            retVal.append("</body>\r\n");
            retVal.append("</html>");


            out.write(retVal.toString());
            return;
        }

        session.setAttribute(USER_KEY, user);

        response.sendRedirect(bURL + "users");
    }

    
    @GetMapping(value = "/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute(USER_KEY);
        String error = "";

        if (user == null)
            error = "User not logged in<br/>";

        if (!error.equals("")) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out;
            out = response.getWriter();

            StringBuilder retVal = new StringBuilder();
            retVal.append("<!DOCTYPE html>\r\n");
            retVal.append("<html>\r\n");
            retVal.append("<head>\r\n");
            retVal.append("    <meta charset=\"UTF-8\">\r\n");
            retVal.append("    <base href=\"/FirstMavenWebProject/\">\r\n");
            retVal.append("    <title>User Login</title>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/FormStyles.css\"/>\r\n");
            retVal.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/HorizontalMenuStyles.css\"/>\r\n");
            retVal.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN\" crossorigin=\"anonymous\">");
            retVal.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL\" crossorigin=\"anonymous\"></script>");
            retVal.append("</head>\r\n");
            retVal.append("<body>\r\n");
            retVal.append("    <ul>\r\n");
            retVal.append("        <li><a href=\"registration.html\">Register</a></li>\r\n");
            retVal.append("    </ul>\r\n");

            if (!error.equals(""))
                retVal.append("    <div>" + error + "</div>\r\n");

            retVal.append("    <form method=\"post\" action=\"LoginLogout/Login\">\r\n");
            retVal.append("        <table>\r\n");
            retVal.append("            <caption>User Login</caption>\r\n");
            retVal.append("            <tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n");
            retVal.append("            <tr><th>Password:</th><td><input type=\"password\" value=\"\" name=\"password\" required/></td></tr>\r\n");
            retVal.append("            <tr><th></th><td><input type=\"submit\" value=\"Login\" /></td>\r\n");
            retVal.append("        </table>\r\n");
            retVal.append("    </form>\r\n");
            retVal.append("    <br/>\r\n");
            retVal.append("    <ul>\r\n");
            retVal.append("        <li><a href=\"users/logout\">Logout</a></li>\r\n");
            retVal.append("    </ul>\r\n");
            retVal.append("</body>\r\n");
            retVal.append("</html>");

            out.write(retVal.toString());
            return;
        }

        request.getSession().removeAttribute(USER_KEY);
        request.getSession().invalidate();
        response.sendRedirect(bURL + "users/login");
    }

    
    @PostMapping(value = "/registration")
    public void registration(@RequestParam(required = true) String email, 
                             @RequestParam(required = true) String password,
                             @RequestParam(required = true) String firstName,
                             @RequestParam(required = true) String lastName,
                             @RequestParam(required = true) String dateOfBirth,
                             @RequestParam(required = true) String address,
                             @RequestParam(required = true) String phoneNumber,
                             HttpSession session, HttpServletResponse response) throws IOException {
        
        UserRole role = UserRole.BUYER;  // Set the appropriate role for new users
        //LocalDateTime registrationDateTime = LocalDateTime.now();  // Set the current registration date and time

        User user = new User(firstName, lastName, password, email, dateOfBirth, address, phoneNumber, role);
        userService.save(user);

        response.sendRedirect(bURL + "users"); // Assuming "bURL" is defined in the class as the base URL
    }

    
    @GetMapping
    public ModelAndView getUsers(HttpSession session, HttpServletResponse response){
        List<User> users = userService.findAll();
        
        ModelAndView result = new ModelAndView("users");
        result.addObject("users", users);

        return result;
    }

    
    @GetMapping(value = "/userList")
    @ResponseBody
    public Map<String, Object> getUserList(HttpSession session, HttpServletResponse response){
        List<User> users = userService.findAll();
        
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", "ok");
        responseMap.put("users", users);
        return responseMap;
    }


    @PostMapping(value = "/delete")
    public void deleteUser(@RequestParam Long id, HttpServletResponse response) throws IOException {
        userService.delete(id);
        response.sendRedirect("/owpproject/users");
    }
}
