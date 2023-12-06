package com.ftn.owpproject.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.owpproject.model.User;
import com.ftn.owpproject.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController implements ServletContextAware {
	
	public static final String USER_KEY = "user";
	
	@Autowired
	private ServletContext servletContext;
	private String bURL; 
	
	@Autowired
	private UserService userService;
	
	/** Initialization of data for the controller */
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath() + "/";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 

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

		if (!error.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/owpproject/\">	\r\n" + "	<title>User Login</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Register</a></li>\r\n" + "	</ul>\r\n");
			if (!error.equals(""))
				retVal.append("	<div>" + error + "</div>\r\n");
			retVal.append("	<form method=\"post\" action=\"users/login\">\r\n" + "		<table>\r\n"
					+ "			<caption>User Login</caption>\r\n"
					+ "			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n"
					+ "			<tr><th>Password:</th><td><input type=\"password\" value=\"\" name=\"password\" required/></td></tr>\r\n"
					+ "			<tr><th></th><td><input type=\"submit\" value=\"Login\" /></td>\r\n"
					+ "		</table>\r\n" + "	</form>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		if (session.getAttribute(USER_KEY) != null)
			error = "User is already logged in; you must log out first<br/>";

		if (!error.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/PrviiMavenVebProjekat/\">	\r\n" + "	<title>User Login</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Register</a></li>\r\n" + "	</ul>\r\n");
			if (!error.equals(""))
				retVal.append("	<div>" + error + "</div>\r\n");
			retVal.append("	<a href=\"index.html\">Back</a>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		session.setAttribute(USER_KEY, user);

		response.sendRedirect(bURL + "books");
	}
	
	@GetMapping(value="/logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {	

		User user = (User) request.getSession().getAttribute(USER_KEY);
		String error = "";
		if (user == null)
			error = "User is not logged in<br/>";
		
		if (!error.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;	
			out = response.getWriter();
			
			StringBuilder retVal = new StringBuilder();
			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" +
					"	<meta charset=\"UTF-8\">\r\n" + 
					"	<base href=\"/owpproject/\">	\r\n" + 
					"	<title>User Login</title>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"registracija.html\">Register</a></li>\r\n" + 
					"	</ul>\r\n");
			if (!error.equals(""))
				retVal.append(
					"	<div>"+error+"</div>\r\n");
			retVal.append(
					"	<form method=\"post\" action=\"Login/Logout\">\r\n" + 
					"		<table>\r\n" + 
					"			<caption>User Login</caption>\r\n" + 
					"			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n" + 
					"			<tr><th>Password:</th><td><input type=\"password\" value=\"\" name=\"password\" required/></td></tr>\r\n" + 
					"			<tr><th></th><td><input type=\"submit\" value=\"Login\" /></td>\r\n" + 
					"		</table>\r\n" + 
					"	</form>\r\n" + 
					"	<br/>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"users/logout\">Logout</a></li>\r\n" + 
					"	</ul>" +
					"</body>\r\n" + 
					"</html>");
			
			out.write(retVal.toString());
			return;
		}
		
		request.getSession().removeAttribute(USER_KEY);
		request.getSession().invalidate();
		response.sendRedirect(bURL + "users/login");
	}
	
	@PostMapping(value = "/registration")
	public void registration(@RequestParam(required = true) String email, @RequestParam(required = true) String password,
			@RequestParam(required = true) String firstName, @RequestParam(required = true) String lastName,
			HttpSession session, HttpServletResponse response) throws IOException {
		User user = new User(firstName, lastName, email, password);
		userService.save(user);
		
		response.sendRedirect(bURL + "users");
	}
	
	@GetMapping
	@ResponseBody
	public String getUsers(HttpSession session, HttpServletResponse response) throws IOException {	
		StringBuilder retVal = new StringBuilder();

		List<User> users = userService.findAll();
		if(users.size() == 0) {
			// No users, TODO: Provide a message and redirect
		} else {
			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"	<meta charset=\"UTF-8\">\r\n" + 
		    		"	<base href=\"" + bURL + "\">" + 

					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"+
					"</head>\r\n" + 
					"<body> "+
					"	<ul>\r\n" + 
					"		<li><a href=\"books\">Books</a></li>\r\n" + 

					"		<li><a href=\"users\">Users</a></li>\r\n" + 
					"	</ul>\r\n" + 
					"		<table>\r\n" + 
					"			<caption>Users</caption>\r\n" + 
					"			<tr>\r\n" + 
					"				<th>First Name</th>\r\n" + 
					"				<th>Last Name</th>\r\n" + 
					"				<th>Email</th>\r\n" +
					"				<th></th>\r\n" +
					"			</tr>\r\n");
			
			for (User u: users) {
				retVal.append(
					"			<tr>\r\n" + 
					"				<td>"+ u.getFirstName() +"</td>\r\n" + 
					"				<td>"+ u.getLastName() +"</td>\r\n" +
					"				<td>"+ u.getEmailAddress() +"</td>\r\n" +
					"				<td>" + 
									"	<form method=\"post\" action=\"users/delete\">\r\n" + 
									"		<input type=\"hidden\" name=\"id\" value=\""+u.getId()+"\">\r\n" + 
									"		<input type=\"submit\" value=\"Delete\">\r\n" + 
									"	</form>\r\n" +
					"				</td>\r\n" +
					"			</tr>\r\n");
			}
			retVal.append("</table>\r\n");
			
			retVal.append(
					"</body>\r\n"+
					"</html>\r\n");		
		}
		
		return retVal.toString();
	}
	
	@PostMapping(value="/delete")
	public void deleteUser(@RequestParam Long id, HttpServletResponse response) throws IOException {				
		userService.delete(id);

		response.sendRedirect(bURL + "users");
	}

}
