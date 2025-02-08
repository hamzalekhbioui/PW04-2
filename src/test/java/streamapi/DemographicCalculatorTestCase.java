package streamapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import isen.streamapi.calculator.DemographicCalculator;
import isen.streamapi.data.Person;
import isen.streamapi.data.Sex;
import isen.streamapi.util.PeopleFactory;
import isen.streamapi.util.StatPrinter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.mockito.Mockito.*;

public class DemographicCalculatorTestCase {

	private static StatPrinter printerMock;

	private static DemographicCalculator calculator;

	private static List<Person> people;

	private static BigDecimal getAge(Person p) {
		return BigDecimal.valueOf(Period.between(p.getDateOfBirth(), LocalDate.of(2023, 01, 01)).getYears());
	}

	@BeforeAll
	public static void init() throws Exception {
		people = PeopleFactory.getPeople("fakenames.csv");

	}

	@BeforeEach
	public void initTest() throws Exception {
		printerMock = mock(StatPrinter.class);
		calculator = new DemographicCalculator("fakenames.csv");
		Field printerField = calculator.getClass().getDeclaredField("printer");
		printerField.setAccessible(true);
		printerField.set(calculator, printerMock);
	}

	@Test
	public void shouldPrintWomen() throws Exception {
		// GIVEN
		List<Person> women = new ArrayList<>();
		for (Person person : people) {
			if (person.getSex().equals(Sex.FEMALE)) {
				women.add(person);
			}
		}
		// WHEN
		calculator.printWomen();
		// THEN
		verify(printerMock, times(4964)).print(any());
		for (Person woman : women) {
			verify(printerMock).print(eq(woman));
		}
	}

	@Test
	public void shouldPrintAverageAge() throws Exception {
		// GIVEN
		// WHEN
		calculator.printAverageAge();
		// THEN
		verify(printerMock).print(eq(60.0376));
	}

	@Test
	public void shouldPrintAverageAgeOfMalesByCity() throws Exception {
		// GIVEN
		Map<String, List<BigDecimal>> agesByCity = new HashMap<>();
		Map<String, Double> averageByCity = new HashMap<>();
		for (Person person : people) {
			if (person.getSex().equals(Sex.MALE)) {
				if (!agesByCity.containsKey(person.getCity())) {
					agesByCity.put(person.getCity(), new ArrayList<>());
				}
				agesByCity.get(person.getCity()).add(getAge(person));
			}
		}
		for (Entry<String, List<BigDecimal>> entry : agesByCity.entrySet()) {
			BigDecimal average = BigDecimal.valueOf(0);
			for (BigDecimal age : entry.getValue()) {
				average = average.add(age);
			}
			if (entry.getValue().size() > 0) {
				average = BigDecimal.valueOf(average.doubleValue() / entry.getValue().size());
			}
			averageByCity.put(entry.getKey(), average.doubleValue());
		}
		// WHEN
		calculator.printAverageAgeOfMalesByCity();
		// THEN
		verify(printerMock, times(453)).print(any());
		for (Entry<String, Double> average : averageByCity.entrySet()) {
			verify(printerMock).print(eq(average.getKey() + " : " + average.getValue()));
		}
	}

	@Test
	public void shouldPrintCountByCity() throws Exception {
		// GIVEN
		Map<String, Integer> countByCity = new HashMap<>();
		for (Person person : people) {
			if (!countByCity.containsKey(person.getCity())) {
				countByCity.put(person.getCity(), 0);
			}
			int total = countByCity.get(person.getCity());
			total++;
			countByCity.put(person.getCity(), total);
		}

		// WHEN
		calculator.printCountByCity();
		// THEN
		verify(printerMock, times(453)).print(any());
		for (Entry<String, Integer> average : countByCity.entrySet()) {
			verify(printerMock).print(eq(average.getKey() + " : " + average.getValue()));
		}
	}

	@Test
	public void shouldPrintMaxAge() throws Exception {
		// GIVEN
		// WHEN
		calculator.printMaxAge();
		// THEN
		verify(printerMock).print(eq(93));
	}

	@Test
	public void shouldPrintOldestPerson() throws Exception {
		// GIVEN
		// WHEN
		calculator.printOldestPerson();
		// THEN
		verify(printerMock).print(people.get(4035));
	}

}
