package isen.streamapi.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import isen.streamapi.data.Person;
import isen.streamapi.data.Sex;

public class PeopleFactory {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

	public static List<Person> getPeople(String filePath) {
		List<Person> people = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(getPath(filePath));
			for (String line : lines) {
				String[] record = line.split(",");
				Person person = new Person();
				person.setLastname(record[2]);
				person.setFirstname(record[4]);
				person.setCity(record[5]);
				person.setSex(Sex.valueOf(record[1].toUpperCase()));
				person.setDateOfBirth(LocalDate.parse(record[9], DATE_FORMAT));
				people.add(person);
			}
			return people;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Path getPath(String filePath) throws URISyntaxException {
		return Paths.get(PeopleFactory.class.getClassLoader().getResource(filePath).toURI());
	}
}
