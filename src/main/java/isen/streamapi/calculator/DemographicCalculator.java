package isen.streamapi.calculator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import isen.streamapi.data.Person;
import isen.streamapi.data.Sex;
import isen.streamapi.util.PeopleFactory;
import isen.streamapi.util.StatPrinter;

public class DemographicCalculator {

	private StatPrinter printer = new StatPrinter();

	private List<Person> people;

	private BiConsumer<String, Number> mapEntryPrinter = (k, v) -> printer.print(k + " : " + v);

	public DemographicCalculator(String filePath) {
		people = PeopleFactory.getPeople(filePath);
	}

	private static int getAge(Person p) {
		return Period.between(p.getDateOfBirth(), LocalDate.of(2023, 01, 01)).getYears();
	}	

	public void printWomen() {
		people.stream()
		.filter(person -> person.getSex() == Sex.FEMALE)
		.forEach(person -> printer.print(person.toString()));
	}

	public void printAverageAge() {
        people.stream()
			.mapToInt(DemographicCalculator::getAge)  
			.average()  
			.ifPresent(avg -> printer.print("Average Age: " + avg));  
	}

	public void printCountByCity() {
        people.stream()
			.collect(Collectors.groupingBy(Person::getCity, Collectors.counting()))
			.forEach(mapEntryPrinter);
	}

	public void printMaxAge() {
        people.stream()
			.map(DemographicCalculator::getAge)  
			.max(Integer::compare)  
			.ifPresent(maxAge -> printer.print("Max Age: " + maxAge));  
	}

	public void printOldestPerson() {
        people.stream()
            .min(Comparator.comparing(Person::getDateOfBirth))
            .ifPresent(oldestPerson -> printer.print("Oldest Person: " + oldestPerson));
	}

	public void printAverageAgeOfMalesByCity() {
        people.stream()
            .filter(person -> person.getSex() == Sex.MALE)
            .collect(Collectors.groupingBy(Person::getCity, Collectors.averagingInt(DemographicCalculator::getAge)))
            .forEach(mapEntryPrinter);
	}


}
