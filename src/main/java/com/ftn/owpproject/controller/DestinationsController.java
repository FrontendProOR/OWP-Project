package com.ftn.owpproject.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.owpproject.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.owpproject.model.Destination;
import com.ftn.owpproject.service.DestinationService;
import com.ftn.owpproject.service.impl.DestinationServiceImpl;

@Controller
@RequestMapping(value = "/destinations")
public class DestinationsController {

    public static final String DESTINATIONS_KEY = "destinations";

    @Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private ApplicationMemory memorijaAplikacije;
	
    @PostConstruct
    public void init() {
    	bURL = servletContext.getContextPath()+"/";
//		memorijaAplikacije = (ApplicationMemory)applicationContext.getBean(ApplicationMemory.class);
	    DestinationServiceImpl destinationServiceImpl = new DestinationServiceImpl(); 
		memorijaAplikacije.put(DestinationsController.DESTINATIONS_KEY, destinationServiceImpl);
    }

    @GetMapping
    @ResponseBody
    public String index(HttpServletResponse response) throws IOException {
        DestinationService destinationService = (DestinationService) memorijaAplikacije.get(DestinationsController.DESTINATIONS_KEY);

        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<table>");
        htmlBuilder.append("<caption>Destinacije</caption>");
        htmlBuilder.append("<thead><tr><th>Redni broj</th><th>City</th><th>Country</th><th>Continent</th></tr></thead>");
        htmlBuilder.append("<tbody>");

        List<Destination> listaDestinacija = destinationService.findAll();
        for (int i = 0; i < listaDestinacija.size(); i++) {
            Destination destinacija = listaDestinacija.get(i);

            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(i + 1).append("</td>");
            htmlBuilder.append("<td>").append(destinacija.getCity()).append("</td>");
            htmlBuilder.append("<td>").append(destinacija.getCountry()).append("</td>");
            htmlBuilder.append("<td>").append(destinacija.getContinent()).append("</td>");

            htmlBuilder.append("<td>");
            htmlBuilder.append("<form method=\"post\" action=\"destinations/delete\">");
            htmlBuilder.append("<input type=\"hidden\" name=\"id\" value=\"").append(destinacija.getId()).append("\"/>");
            htmlBuilder.append("<input type=\"submit\" value=\"Obrisi\"/>");
            htmlBuilder.append("</form>");
            htmlBuilder.append("</td>");

            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</tbody></table>");
        htmlBuilder.append("<a href='destinations/add'>Add destination</a>");

        return htmlBuilder.toString();
    }


    @GetMapping(value = "/add")
    @ResponseBody
    public void create(HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<form method=\"post\" action=\"add\">");
        htmlBuilder.append("<table>");

        // Row for City
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<th>City</th>");
        htmlBuilder.append("<td><input type=\"text\" name=\"city\"></td>");
        htmlBuilder.append("</tr>");

        // Row for Country
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<th>Country</th>");
        htmlBuilder.append("<td><input type=\"text\" name=\"country\"></td>");
        htmlBuilder.append("</tr>");

        // Row for Continent
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<th>Continent</th>");
        htmlBuilder.append("<td><input type=\"text\" name=\"continent\"></td>");
        htmlBuilder.append("</tr>");

        // Submit button
        htmlBuilder.append("<tr>");
        htmlBuilder.append("<th></th>");
        htmlBuilder.append("<td><input type=\"submit\" value=\"Add\"></td>");
        htmlBuilder.append("</tr>");

        htmlBuilder.append("</table>");
        htmlBuilder.append("</form>");

        out.append(htmlBuilder.toString());
    }


    @PostMapping(value = "/add")
    public void create( @RequestParam String city, @RequestParam String country,
            @RequestParam String continent, HttpServletResponse response) throws IOException {
    	//obriso @RequestParam Long id, iz parametara
        DestinationServiceImpl destinations = (DestinationServiceImpl) memorijaAplikacije.get(DestinationsController.DESTINATIONS_KEY);
        
        
        Destination destination = new Destination(city, country, continent);

        destinations.save(destination);
        response.sendRedirect(bURL + "destinations");
    }

    
    @PostMapping(value = "/delete")
    public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
    	DestinationServiceImpl destinations = (DestinationServiceImpl) memorijaAplikacije.get(DestinationsController.DESTINATIONS_KEY);
		
		destinations.delete(id);
		response.sendRedirect(bURL+"destinacije");
    }

//    @GetMapping(value = "/details")
//    @ResponseBody
//    public void details(@RequestParam Long id) {
//        // Existing code...
//    }

	
}
