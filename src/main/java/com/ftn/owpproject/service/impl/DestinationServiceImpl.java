package com.ftn.owpproject.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.ftn.owpproject.model.Destination;
import com.ftn.owpproject.service.DestinationService;

public class DestinationServiceImpl implements DestinationService{
	
		private Map<Long, Destination> destinations = new HashMap<>();
		private long nextId = 1L;

		@Value("${destinations.pathToFile}")
		private String pathToFile;
		
		public DestinationServiceImpl() {
			String fileName = "destinations.txt";
			URL resource = getClass().getResource("/" + fileName);

			if (resource == null) {
			    System.err.println("File not found: " + fileName);
			} else {
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
						String city = tokens[1];
						String country = tokens[2];
						String continent = tokens[3];
						
						destinations.put(Long.parseLong(tokens[0]), new Destination(id,city, country , continent));
						
						if(nextId<id)
							nextId=id;
					}

			    } catch (URISyntaxException | IOException e) {
			        e.printStackTrace();
			    }
			}
			
		}

		public Destination findOne(Long id) {
			return destinations.get(id);
		}

		public List<Destination> findAll() {
			return new ArrayList<Destination>(destinations.values());
		}

		public Destination save(Destination destination) {
			if (destination.getId() == null) {
				destination.setId(++nextId);
			}
			destinations.put(destination.getId(), destination);
			return destination;
		}

		public List<Destination> save(List<Destination> destinations) {
			List<Destination> ret = new ArrayList<>();

			for (Destination d : destinations) {
				Destination saved = save(d);
				if (saved != null) {
					ret.add(saved);
				}
			}
			return ret;
		}

//		public Destination delete(Long id) {
//			if (!destinations.containsKey(id)) {
//				throw new IllegalArgumentException("tried to remove non existing destination");
//			}
//			Destination destination = destinations.get(id);
//			if (destination != null) {
//				destinations.remove(id);
//			}
//			return destination;
//		}
//
//		public void delete(List<Long> ids) {
//			for (Long id : ids) {
//				delete(id);
//			}
//		}
		
		public void delete(Long id) {
		    String fileName = "destinations.txt";
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
		                if (line.equals("") || line.indexOf('#') == 0)
		                    continue;

		                String[] tokens = line.split(";");
		                Long currentId = Long.parseLong(tokens[0]);

		                if (currentId.equals(id)) {
		                    // Skip the line to delete it
		                    continue;
		                }

		                newLines.add(line);
		            }

		            Files.write(path, newLines, Charset.forName("UTF-8"));

		            // Update the in-memory map
		            destinations.remove(id);

		        } catch (URISyntaxException | IOException e) {
		            e.printStackTrace();
		            // Handle the exception according to your application's requirements
		        }
		    }
		}


		@Override
		public Destination update(Destination destination) {
			// TODO Auto-generated method stub
			return null;
		}
	}


