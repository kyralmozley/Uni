package uk.ac.cam.km687.oop.tick1;
import java.lang.reflect.Method;

public class ClassMethodTest {
    public static void main(String[] args) {
        Method[] methods = ArrayLife.class.getDeclaredMethods();
        int nMethod = 1;
        System.out.println("1. List of all methods of Person class");
        for (Method method : methods) {
            System.out.printf("%d. %s", ++nMethod, method);
            System.out.println();
        }
        System.out.printf("%d. End - all  methods of Person class", ++nMethod);
    }
}