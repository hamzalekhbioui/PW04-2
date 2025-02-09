package isen.streamapi;

import isen.streamapi.calculator.DemographicCalculator;

public class Application {

    public static void main(String[] args) {
        
        // Uncomment one of the following lines to have your code tested. You can
        // uncomment one method at a time
        
        DemographicCalculator calculator = new DemographicCalculator("fakenames.csv");
        // calculator.printWomen();
        calculator.printAverageAge();
        // calculator.printCountByCity();
        // calculator.printAverageAgeOfMalesByCity();
        // calculator.printMaxAge();
        // calculator.printOldestPerson();

    }
}
