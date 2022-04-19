package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import java.time.Duration;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class EmployeeTest {

    static Stream<String> getLastNames(){
        return Stream.of("Al4d", "B3nson","$mith","@lford");
    }

    static Employee firstEmployee;
    static double salary;
    static Employee thirdEmployee;
    static GregorianCalendar gc;

    @BeforeAll
    static void initEmployee(){
        salary =45000;
        firstEmployee=new Employee("Amy", "4Cruz",1001,'F',
                "Developer",salary,"Permanent");

        gc=new GregorianCalendar();

        thirdEmployee=null;
    }

    @ParameterizedTest
    //@RepeatedTest(5)
    @DisplayName("Salary Update")
    @ValueSource(doubles = {500, 1000, 1300})
    void salaryUpdate(double salaryIncrement){


        firstEmployee.adjustSalary(salaryIncrement);
        salary+=salaryIncrement;

        assertEquals(salary,firstEmployee.getSalary(),
                "Test salary update");
    }

    @ParameterizedTest(name="Test #{index} - Last name: {0}")
    @DisplayName("Name check")
    @MethodSource("getLastNames")
    @Tag("DEV")
    void nameTest(String lName){

        Employee employee = new Employee("Brian",lName,1011,'M',
                "Tester",50000,"Contract");

        assertThrows(InvalidNameException.class,
                employee::validateName,
                "Throws Exception test");
    }

    @ParameterizedTest
    @ValueSource(strings = {"developer", "tester", "release engineer","storage engineer"})
    @DisplayName("Role Check")
    @Tag("TEST")
    void ConverterTest(@ConvertWith(RoleConverter.class)String role){

        Employee employee = new Employee("Brian","Alford",1011,'M',
                role,50000,"Contract");

        System.out.println("Value of role: " + role);

        assertEquals(role,employee.getRole());
    }

    /*@Test
    void assumptionTest(){
        System.out.println("Current hour: " + gc.get(Calendar.HOUR_OF_DAY));

        Assumptions.assumingThat(gc.get(Calendar.HOUR_OF_DAY)<10,
                ()->{firstEmployee.adjustSalary(5000);
        assertEquals(85000,firstEmployee.getSalary());
            System.out.println("The assumption was satisfied and the test was run");});

        System.out.println("\nAfter the invocation of assumingThat");
    }*/

    @Test
    void assertTest(){
        Employee firstShallowCopy=firstEmployee;
        Employee firstSeparateCopy= new Employee(firstEmployee.getFirstName(),
                firstEmployee.getLastName(), firstEmployee.getId(),
                firstEmployee.getGender(), firstEmployee.getRole(),
                firstEmployee.getSalary(), firstEmployee.getType());

        assertSame(firstEmployee.getRole(),firstSeparateCopy.getRole(),
                "Test an object and a copy for sameness");
    }

    @Test
    void assertTestTime(){
        assertTimeout(Duration.ofSeconds(5),
                ()->{firstEmployee.adjustSalary(0);
        Employee secondEmployee=
        new Employee("Brian","Alford",1002,
                'M',"Developer",45000,"Contract");secondEmployee.adjustSalary(3000);});
    }

    @Test
    void assertTestNull(){
        assertNotNull(firstEmployee);
    }

    @Test
    void assertTestThrows(){
        assertThrows(InvalidNameException.class,
                ()->{firstEmployee.validateName();},"Throws Exception test");
    }

    @Test
    void assertTest2(){

        firstEmployee.adjustSalary(4000);

        assertAll("Test Employee",
                ()->assertNotNull(firstEmployee.getFirstName()),
                ()->assertThrows(NamingException.class,
                        ()->{firstEmployee.validateName();},
                        "Thrwos Exception test"));
    }

    @Test
    void assertTest3(){

        firstEmployee.addProject("Blue Fame");
        firstEmployee.addProject("Black Box");

        LinkedList<String> compareList = new LinkedList<>();
        compareList.add("Blue Fame");
        compareList.add("Black Box");

        assertIterableEquals(compareList, firstEmployee.getProjects());
    }
}
