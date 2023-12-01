package com.ftn.owpproject.service.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.model.Korisnik;
import com.ftn.owpproject.service.KorisnikService;

@Service
@Qualifier("fajloviKorisnici")
public class KorisnikServiceImpl implements KorisnikService {

	@Value("${korisnici.pathToFile}")
	private String pathToFile;
	
	private Map<Long, Korisnik> readFromFile() {

    	Map<Long, Korisnik> korisnici = new HashMap<>();
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
				String ime = tokens[1];
				String prezime = tokens[2];
				String email = tokens[3];
				String lozinka = tokens[4];

				korisnici.put(Long.parseLong(tokens[0]), new Korisnik(id, ime, prezime, email, lozinka));

				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return korisnici;
    }
    
    private Map<Long, Korisnik> saveToFile(Map<Long, Korisnik> korisnici) {
    	
    	Map<Long, Korisnik> ret = new HashMap<>();
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();
			
			for (Korisnik k : korisnici.values()) {
				lines.add(k.toFileString());
				ret.put(k.getId(), k);
			}
			//pisanje svih redova za clanske karte
			Files.write(path, lines, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ret;
    }
    
    private Long nextId(Map<Long, Korisnik> korisnici) {
    	Long nextId = 1L;
    	for (Long id : korisnici.keySet()) {
    		if(nextId<id)
				nextId=id;
		}
    	return ++nextId;
    }
	
	@Override
	public Korisnik findOneById(Long id) {
		Map<Long, Korisnik> korisnici = readFromFile();
		return korisnici.get(id);
	}

	@Override
	public Korisnik findOne(String email) {
		Map<Long, Korisnik> korisnici = readFromFile();
		Korisnik found = null;
		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getEmail().equals(email)) {
				found = korisnik;
				break;
			}
		}
		
		return found;
	}

	@Override
	public Korisnik findOne(String email, String sifra) {
		Map<Long, Korisnik> korisnici = readFromFile();
		Korisnik found = null;
		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getEmail().equals(email) && korisnik.getLozinka().equals(sifra)) {
				found = korisnik;
				break;
			}
		}
		
		return found;
	}

	@Override
	public List<Korisnik> findAll() {
		Map<Long, Korisnik> korisnici = readFromFile();
		return new ArrayList<>(korisnici.values());
	}

	@Override
	public Korisnik save(Korisnik korisnik) {
		Map<Long, Korisnik> korisnici = readFromFile();	
		Long nextId = nextId(korisnici); 
		
		//u slučaju da korisnik nema id
		//tada treba da se dodeli id
		if (korisnik.getId() == null) {
			korisnik.setId(nextId++);
			
		}
		korisnici.put(korisnik.getId(), korisnik);
		saveToFile(korisnici);
		return korisnik;
	}

	@Override
	public Korisnik update(Korisnik korisnik) {
		Map<Long, Korisnik> korisnici = readFromFile();	
		
		korisnici.put(korisnik.getId(), korisnik);
		saveToFile(korisnici);
		return korisnik;
	}

	@Override
	public Korisnik delete(Long id) {
		Map<Long, Korisnik> korisnici = readFromFile();	
		
		if (!korisnici.containsKey(id)) {
			throw new IllegalArgumentException("tried to remove non existing book");
		}
		
		Korisnik korisnik = korisnici.get(id);
		if (korisnik != null) {
			korisnici.remove(id);
		}
		saveToFile(korisnici);
		return korisnik;
	}

}
