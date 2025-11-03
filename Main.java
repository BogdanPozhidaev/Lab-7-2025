import functions.*;
import functions.basic.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Тестирование итератора
            System.out.println("=== Тестирование итератора ===");
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16});
            System.out.println("ArrayTabulatedFunction points:");
            for (FunctionPoint point : arrayFunc) {
                System.out.println(point);
            }

            TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16});
            System.out.println("\nLinkedListTabulatedFunction points:");
            for (FunctionPoint point : listFunc) {
                System.out.println(point);
            }

            // Тестирование фабричного метода
            System.out.println("\n=== Тестирование фабричного метода ===");
            Function cos = new Cos();
            TabulatedFunction tf;

            tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
            System.out.println("Default factory: " + tf.getClass().getSimpleName());

            TabulatedFunctions.setTabulatedFunctionFactory(
                    new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
            tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
            System.out.println("LinkedList factory: " + tf.getClass().getSimpleName());

            TabulatedFunctions.setTabulatedFunctionFactory(
                    new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
            tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
            System.out.println("Array factory: " + tf.getClass().getSimpleName());

            // Тестирование рефлексии
            System.out.println("\n=== Тестирование рефлексии ===");
            TabulatedFunction f;

            f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
            System.out.println("Array via reflection: " + f.getClass().getSimpleName());
            System.out.println(f);

            f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                    0, 10, new double[]{0, 5, 10});
            System.out.println("LinkedList via reflection: " + f.getClass().getSimpleName());
            System.out.println(f);

            f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
            System.out.println("Tabulate with reflection: " + f.getClass().getSimpleName());
            System.out.println(f);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}