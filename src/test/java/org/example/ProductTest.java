package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.CONCURRENT)
public class ProductTest {

    static Product tv;

    @BeforeAll
    static void init(){
        tv=new Product("Samsung","QLed",101,800,"Electronics");

        System.out.println("System properties");
        System.out.println("os.arch: " + System.getProperty("os.arch"));
        System.out.println("os.version: " + System.getProperty("os.version"));
        System.out.println("os.name: " + System.getProperty("os.name"));
        System.out.println("user.name: " + System.getProperty("user.name"));

        System.out.println();

        System.out.println("Environment variables: ");
        System.out.println("PWD: " + System.getenv("PWD"));
        System.out.println("USER: " + System.getenv("USERNAME"));
        System.out.println("ALL: " + System.getenv());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledForJreRange(min=JRE.JAVA_11,max = JRE.JAVA_18)
    @EnabledOnOs(OS.WINDOWS)
    @EnabledIfSystemProperty(named="os.arch",matches = "amd64")
    public @interface MyCustomTestConditions{
    }

    @Test
    @DisplayName("Brand Name Check")
    @Order(2)
    @Timeout(4)
    @EnabledOnOs(OS.WINDOWS)
    @EnabledOnJre(JRE.JAVA_18)
    @EnabledIfSystemProperty(named = "os.arch", matches = "amd64")
    @EnabledIfEnvironmentVariable(named = "USERNAME",matches = "goncalo.diogo")
    void brandNameTest() throws InterruptedException{

        Thread.sleep(2000);

        assertEquals("Samsung",tv.getBrandName(),
                "Brand Name Test");
    }

    @Test
    @DisplayName("Price Check")
    @Order(1)
    @Timeout(4)
    @DisabledOnOs(OS.LINUX)
    @DisabledOnJre(JRE.JAVA_10)
    @DisabledIfEnvironmentVariable(named = "LANG", matches = "de_De*")
    @DisabledForJreRange(min=JRE.JAVA_9, max=JRE.JAVA_16)
    @EnabledIfSystemProperty(named = "os.name", matches = "Windows 10")

    void priceTest() throws InterruptedException{

        Thread.sleep(3000);
        assertEquals(800,tv.getPrice(),
                "Price Test");
    }

    @Test
    @DisplayName("Category Check")
    @Order(3)
    void categoryTest()throws InterruptedException{


        assertEquals("Electronics",tv.getCategory(),
                "Category Test");
    }

}
