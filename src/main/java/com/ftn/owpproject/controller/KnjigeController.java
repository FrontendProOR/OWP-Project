package com.ftn.owpproject.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.owpproject.model.Knjiga;
import com.ftn.owpproject.service.KnjigaService;

@Controller
@RequestMapping(value="/knjige")
public class KnjigeController implements ServletContextAware {

	public static final String KNJIGE_ZA_IZNAJMLJIVANJE = "knjige_za_iznajmljivanje";

	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private KnjigaService knjigaService;
	
	/** inicijalizacija podataka za kontroler */
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 
	
	/** pribavnjanje HTML stanice za prikaz svih entiteta, get zahtev */
	// GET: knjige
	@GetMapping
	@ResponseBody
	public void index(HttpServletResponse response) throws IOException {
		List<Knjiga> knjige = knjigaService.findAll();

		PrintWriter out;
		out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");

		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/owpproject/knjige").text("Knjige");

		liTagKnjige.appendChild(aTagKnjige);

		ulTag.appendChild(liTagKnjige);
		

		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Knjige");
		Element trZaglavlje = new Element(Tag.valueOf("tr"), "");
		Element thRedniBroj = new Element(Tag.valueOf("th"), "").text("R. br.");
		Element thDetails = new Element(Tag.valueOf("th"), "").text("Details");
		Element thNaziv = new Element(Tag.valueOf("th"), "").text("Naziv");
		Element thRBPrimerka = new Element(Tag.valueOf("th"), "").text("Registarski broj primerka");
		Element thJezik = new Element(Tag.valueOf("th"), "").text("Jezik");
		Element thBrStranica = new Element(Tag.valueOf("th"), "").text("Broj stranica");

		trZaglavlje.appendChild(thRedniBroj);
		trZaglavlje.appendChild(thDetails);
		trZaglavlje.appendChild(thNaziv);
		trZaglavlje.appendChild(thRBPrimerka);
		trZaglavlje.appendChild(thJezik);
		trZaglavlje.appendChild(thBrStranica);

		table.appendChild(caption);
		table.appendChild(trZaglavlje);

		for (int i=0; i < knjige.size(); i++) {
			if (!knjige.get(i).isIzdata()) {
				Element trKnjiga = new Element(Tag.valueOf("tr"), "");
				Element tdRedniBroj = new Element(Tag.valueOf("td"), "").text(String.valueOf(i + 1));
				Element tdDetails = new Element(Tag.valueOf("td"), "");
				Element aDetails = new Element(Tag.valueOf("a"), "").attr("href","knjige/details?id="+knjige.get(i).getId()).text(knjige.get(i).getNaziv());
				Element tdNaziv = new Element(Tag.valueOf("td"), "").text(knjige.get(i).getNaziv());
				Element tdRBPrimerka = new Element(Tag.valueOf("td"), "").text(knjige.get(i).getRegistarskiBrojPrimerka());
				Element tdJezik = new Element(Tag.valueOf("td"), "").text(knjige.get(i).getJezik());
				Element tdBrStranica = new Element(Tag.valueOf("td"), "").text(String.valueOf(knjige.get(i).getBrojStranica()));

				tdDetails.appendChild(aDetails);
				trKnjiga.appendChild(tdRedniBroj);
				trKnjiga.appendChild(tdDetails);
				trKnjiga.appendChild(tdNaziv);
				trKnjiga.appendChild(tdRBPrimerka);
				trKnjiga.appendChild(tdJezik);
				trKnjiga.appendChild(tdBrStranica);

				Element tdForm = new Element(Tag.valueOf("td"), "");
				Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "knjige/delete");
				Element inputHidden = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(knjige.get(i).getId()));
				Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Obrisi");
				form.appendChild(inputHidden);
				form.appendChild(inputSubmit);
				tdForm.appendChild(form);
				trKnjiga.appendChild(tdForm);
				table.appendChild(trKnjiga);
			}
		}

		Element ulTagDodajKnjigu = new Element(Tag.valueOf("ul"), "");
		Element liTagDodajKnjigu = new Element(Tag.valueOf("li"), "");
		Element aTagDodajKnjigu = new Element(Tag.valueOf("a"), "").attr("href", "knjige/add").text("Dodaj knjigu");
		liTagDodajKnjigu.appendChild(aTagDodajKnjigu);
		ulTagDodajKnjigu.appendChild(liTagDodajKnjigu);

		body.appendChild(ulTag);
		body.appendChild(table);
		body.appendChild(ulTagDodajKnjigu);

		out.write(doc.html());
		return;
	}
	
	/** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
	// GET: knjige/dodaj
	@GetMapping(value="/add")
	@ResponseBody
	public void create(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");
		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/owpproject/knjige").text("Knjige");

		liTagKnjige.appendChild(aTagKnjige);

		ulTag.appendChild(liTagKnjige);

		Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "add");
		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Knjiga");

		Element trNaziv = new Element(Tag.valueOf("tr"), "");
		Element thNaziv = new Element(Tag.valueOf("th"), "").text("Naziv");
		Element tdNaziv = new Element(Tag.valueOf("td"), "");
		Element inputNaziv = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "naziv");

		tdNaziv.appendChild(inputNaziv);
		trNaziv.appendChild(thNaziv);
		trNaziv.appendChild(tdNaziv);

		Element trRBPrimerka = new Element(Tag.valueOf("tr"), "");
		Element thRBPrimerka = new Element(Tag.valueOf("th"), "").text("Redni broj primerka");
		Element tdRBPrimerka = new Element(Tag.valueOf("td"), "");
		Element inputRBPrimerka = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "registarskiBrojPrimerka");

		tdRBPrimerka.appendChild(inputRBPrimerka);
		trRBPrimerka.appendChild(thRBPrimerka);
		trRBPrimerka.appendChild(tdRBPrimerka);

		Element trJezik = new Element(Tag.valueOf("tr"), "");
		Element thJezik = new Element(Tag.valueOf("th"), "").text("Jezik");
		Element tdJezik = new Element(Tag.valueOf("td"), "");
		Element inputJezik = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "jezik");

		tdJezik.appendChild(inputJezik);
		trJezik.appendChild(thJezik);
		trJezik.appendChild(tdJezik);

		Element trBrStranica = new Element(Tag.valueOf("tr"), "");
		Element thBrStranica = new Element(Tag.valueOf("th"), "").text("Broj stranica");
		Element tdBrStranica = new Element(Tag.valueOf("td"), "");
		Element inputBrStranica = new Element(Tag.valueOf("input"), "").attr("type", "number").attr("name", "brojStranica").attr("min", "1");

		tdBrStranica.appendChild(inputBrStranica);
		trBrStranica.appendChild(thBrStranica);
		trBrStranica.appendChild(tdBrStranica);

		Element trSubmit = new Element(Tag.valueOf("tr"), "");
		Element thSubmit = new Element(Tag.valueOf("th"), "");
		Element tdSubmit = new Element(Tag.valueOf("td"), "");
		Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Dodaj");

		tdSubmit.appendChild(inputSubmit);
		trSubmit.appendChild(thSubmit);
		trSubmit.appendChild(tdSubmit);

		table.appendChild(caption);

		table.appendChild(trNaziv);
		table.appendChild(trRBPrimerka);
		table.appendChild(trJezik);
		table.appendChild(trBrStranica);
		table.appendChild(trSubmit);

		form.appendChild(table);

		body.appendChild(ulTag);
		body.appendChild(form);

		out.write(doc.html());
		return;
	}
	
	/** obrada podataka forme za unos novog entiteta, post zahtev */
	// POST: knjige/add
	@PostMapping(value="/add")
	public void create(@RequestParam String naziv, @RequestParam String registarskiBrojPrimerka, 
			@RequestParam String jezik, @RequestParam int brojStranica, HttpServletResponse response) throws IOException {		
//		Knjiga knjiga = new Knjiga(naziv, registarskiBrojPrimerka, jezik, brojStranica);
//		Knjiga saved = knjigaService.save(knjiga);
		response.sendRedirect(bURL+"knjige");
	}
	
	/** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
	// POST: knjige/edit
	@PostMapping(value="/edit")
	public void Edit(@ModelAttribute Knjiga knjigaEdited , HttpServletResponse response) throws IOException {	
		Knjiga knjiga = knjigaService.findOne(knjigaEdited.getId());
		if(knjiga != null) {
			if(knjigaEdited.getNaziv() != null && !knjigaEdited.getNaziv().trim().equals(""))
				knjiga.setNaziv(knjigaEdited.getNaziv());
			if(knjigaEdited.getJezik() != null && !knjigaEdited.getJezik().trim().equals(""))
				knjiga.setJezik(knjigaEdited.getJezik());
			if(knjigaEdited.getBrojStranica() > 0)
				knjiga.setBrojStranica(knjigaEdited.getBrojStranica());
		}
//		Knjiga saved = knjigaService.update(knjiga);
		response.sendRedirect(bURL+"knjige");
	}
	
	/** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
	// POST: knjige/delete
	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {		
//		Knjiga deleted = knjigaService.delete(id);
		response.sendRedirect(bURL+"knjige");
	}
	
	/** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
	// GET: knjige/details?id=1
	@GetMapping(value="/details")
	@ResponseBody
	public void details(@RequestParam Long id, HttpServletResponse response) throws IOException {
		Knjiga knjiga = knjigaService.findOne(id);

		PrintWriter out;
		out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");

		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/owpproject/knjige").text("Knjige");

		liTagKnjige.appendChild(aTagKnjige);

		ulTag.appendChild(liTagKnjige);

		Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "edit");
		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Knjiga");

		Element inputHidden = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(knjiga.getId()));

		Element trNaziv = new Element(Tag.valueOf("tr"), "");
		Element thNaziv = new Element(Tag.valueOf("th"), "").text("Naziv");
		Element tdNaziv = new Element(Tag.valueOf("td"), "");
		Element inputNaziv = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "naziv").attr("value", knjiga.getNaziv());

		tdNaziv.appendChild(inputNaziv);
		trNaziv.appendChild(thNaziv);
		trNaziv.appendChild(tdNaziv);

		Element trRBPrimerka = new Element(Tag.valueOf("tr"), "");
		Element thRBPrimerka = new Element(Tag.valueOf("th"), "").text("Redni broj primerka");
		Element tdRBPrimerka = new Element(Tag.valueOf("td"), "");
		Element inputRBPrimerka = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "registarskiBrojPrimerka").attr("value", knjiga.getRegistarskiBrojPrimerka());

		tdRBPrimerka.appendChild(inputRBPrimerka);
		trRBPrimerka.appendChild(thRBPrimerka);
		trRBPrimerka.appendChild(tdRBPrimerka);

		Element trJezik = new Element(Tag.valueOf("tr"), "");
		Element thJezik = new Element(Tag.valueOf("th"), "").text("Jezik");
		Element tdJezik = new Element(Tag.valueOf("td"), "");
		Element inputJezik = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "jezik").attr("value", knjiga.getJezik());

		tdJezik.appendChild(inputJezik);
		trJezik.appendChild(thJezik);
		trJezik.appendChild(tdJezik);

		Element trBrStranica = new Element(Tag.valueOf("tr"), "");
		Element thBrStranica = new Element(Tag.valueOf("th"), "").text("Broj stranica");
		Element tdBrStranica = new Element(Tag.valueOf("td"), "");
		Element inputBrStranica = new Element(Tag.valueOf("input"), "").attr("type", "number").attr("name", "brojStranica").attr("min", "1").attr("value", String.valueOf(knjiga.getBrojStranica()));

		tdBrStranica.appendChild(inputBrStranica);
		trBrStranica.appendChild(thBrStranica);
		trBrStranica.appendChild(tdBrStranica);

		Element trSubmit = new Element(Tag.valueOf("tr"), "");
		Element thSubmit = new Element(Tag.valueOf("th"), "");
		Element tdSubmit = new Element(Tag.valueOf("td"), "");
		Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Izmeni");

		tdSubmit.appendChild(inputSubmit);
		trSubmit.appendChild(thSubmit);
		trSubmit.appendChild(tdSubmit);

		table.appendChild(caption);

		table.appendChild(trNaziv);
		table.appendChild(trRBPrimerka);
		table.appendChild(trJezik);
		table.appendChild(trBrStranica);
		table.appendChild(trSubmit);

		form.appendChild(inputHidden);
		form.appendChild(table);

		Element formBrisanje = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "delete");
		Element inputSubmitBrisanje = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Obrisi");
		Element inputHiddenBrisanje = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "id").attr("value", String.valueOf(knjiga.getId()));;
		formBrisanje.appendChild(inputHiddenBrisanje);
		formBrisanje.appendChild(inputSubmitBrisanje);

		body.appendChild(ulTag);
		body.appendChild(form);
		body.appendChild(formBrisanje);

		if (!knjiga.isIzdata()) {
			Element formDodajUZeljene = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "zeljene/dodaj");

			Element inputHiddenDodajUZeljene = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "idKnjige").attr("value", String.valueOf(knjiga.getId()));

			Element tableDodajUZeljene = new Element(Tag.valueOf("table"), "");
			Element captionDodajUZeljene = new Element(Tag.valueOf("caption"), "").text("Dodaj u zeljene knjige");

			Element trSubmitDodajUZeljene = new Element(Tag.valueOf("tr"), "");
			Element thSubmitDodajUZeljene = new Element(Tag.valueOf("th"), "");
			Element tdSubmitDodajUZeljene = new Element(Tag.valueOf("td"), "");
			Element inputSubmitDodajUZeljene = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Dodaj");

			tdSubmitDodajUZeljene.appendChild(inputSubmitDodajUZeljene);
			trSubmitDodajUZeljene.appendChild(thSubmitDodajUZeljene);
			trSubmitDodajUZeljene.appendChild(tdSubmitDodajUZeljene);

			tableDodajUZeljene.appendChild(captionDodajUZeljene);
			tableDodajUZeljene.appendChild(trSubmitDodajUZeljene);

			formDodajUZeljene.appendChild(inputHiddenDodajUZeljene);
			formDodajUZeljene.appendChild(tableDodajUZeljene);

			body.appendChild(formDodajUZeljene);
		}

		out.write(doc.html());
		return;
	}
	
	// POST: knjige/zeljene/dodaj
	@SuppressWarnings("unchecked")
	@PostMapping(value="/zeljene/dodaj")
	@ResponseBody
	public void dodajUZeljene(@RequestParam Long idKnjige, HttpSession session, HttpServletResponse response) throws IOException {
		List<Knjiga> zaIznajmljivanje = (List<Knjiga>) session.getAttribute(KNJIGE_ZA_IZNAJMLJIVANJE);	
		Knjiga knjiga = knjigaService.findOne(idKnjige);
		zaIznajmljivanje.add(knjiga);
		
		response.sendRedirect(bURL+"knjige/zeljene");
	}
		
	// GET: knjige/zeljene
	@SuppressWarnings("unchecked")
	@GetMapping(value="/zeljene")
	@ResponseBody
	public String dodajZeljene(HttpSession session) throws IOException {
		List<Knjiga> zaIznajmljivanje = (List<Knjiga>) session.getAttribute(KNJIGE_ZA_IZNAJMLJIVANJE);	

		StringBuilder retVal = new StringBuilder();
		retVal.append(
				"<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"	<meta charset=\"UTF-8\">\r\n" + 
	    		"	<base href=\""+bURL+"\">" + 
				"	<title>Knjige</title>\r\n" + 
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" + 
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"+
				"</head>\r\n" + 
				"<body> "+
				"	<ul>\r\n" + 
				"		<li><a href=\"knjige\">Knjige</a></li>\r\n" + 

				"		<li><a href=\"korisnici\">Korisnici</a></li>\r\n" + 
				"	</ul>\r\n" + 
				"		<table>\r\n" + 
				"			<caption>Zeljene njige</caption>\r\n" + 
				"			<tr>\r\n" + 
				"				<th>Naziv</th>\r\n" + 
				"				<th>Registarski broj primerka</th>\r\n" + 
				"				<th>Jezik</th>\r\n" +
				"				<th>Broj stranica</th>\r\n" +
				"				<th></th>\r\n" +
				"			</tr>\r\n");
		
		for (int i=0; i < zaIznajmljivanje.size(); i++) {
			Knjiga knjiga = zaIznajmljivanje.get(i);
			retVal.append(
				"			<tr>\r\n" + 
				"				<td>"+ knjiga.getNaziv() +"</td>\r\n" +
				"				<td>"+ knjiga.getRegistarskiBrojPrimerka() +"</td>\r\n" +
				"				<td>"+ knjiga.getJezik() +"</td>\r\n" +
				"				<td class=\"broj\">"+ knjiga.getBrojStranica() +"</td>\r\n" +
				"				<td>" + 
				"					<form method=\"post\" action=\"knjige/zeljene/ukloni\">\r\n" + 
				"						<input type=\"hidden\" name=\"idKnjige\" value=\""+ knjiga.getId()+"\">\r\n" + 
				"						<input type=\"submit\" value=\"Ukloni iz zeljenih\"></td>\r\n" + 
				"					</form>\r\n" +
				"				</td>" +
				"			</tr>\r\n");
		}
		retVal.append(
				"		</table>\r\n");
		
		retVal.append("	<br/>\r\n" + 

				"		<table>\r\n" + 
				"			<caption>Izdaj knjige</caption>" +	 
				"			<tr><th></th><td><input type=\"submit\" value=\"Izdaj\"></td>\r\n" + 
				"		</table>\r\n" + 
				"	</form>\r\n");
	
		return retVal.toString();
	}
	
	// POST: knjige/zeljene/dodaj
	@SuppressWarnings("unchecked")
	@PostMapping(value="/zeljene/ukloni")
	@ResponseBody
	public void ukloniIzZeljenih(@RequestParam Long idKnjige, HttpSession session, HttpServletResponse response) throws IOException {
		List<Knjiga> zaIznajmljivanje = (List<Knjiga>) session.getAttribute(KNJIGE_ZA_IZNAJMLJIVANJE);	

		for (Knjiga knjiga : zaIznajmljivanje) {
			if (knjiga.getId().equals(idKnjige)) {
				zaIznajmljivanje.remove(knjiga);
				break;
			}
		}
		response.sendRedirect(bURL+"knjige/zeljene");
	}
}
