import functions.*;
import functions.basic.*;
import java.io.*;

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

            TabulatedFunction originalFunc = new ArrayTabulatedFunction(
                    0, 10, new double[]{0, 1, 4, 9, 16, 25});

            // Тестирование бинарного ввода/вывода с фабрикой
            System.out.println("\n=== Бинарный ввод/вывод с фабрикой ===");

            // Записываем в байтовый поток
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(originalFunc, byteOut);

            // Читаем обратно с фабрикой по умолчанию (Array)
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction funcFromBinary = TabulatedFunctions.inputTabulatedFunction(byteIn);
            System.out.println("Из бинарного потока (фабрика): " + funcFromBinary.getClass().getSimpleName());

            // Читаем с указанием конкретного класса через рефлексию
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction funcFromBinaryReflection = TabulatedFunctions.inputTabulatedFunction(
                    LinkedListTabulatedFunction.class, byteIn);
            System.out.println("Из бинарного потока (рефлексия): " + funcFromBinaryReflection.getClass().getSimpleName());

            // Тестирование символьного ввода/вывода
            System.out.println("\n=== Символьный ввод/вывод ===");

            // Записываем в символьный поток
            StringWriter stringWriter = new StringWriter();
            TabulatedFunctions.writeTabulatedFunction(originalFunc, stringWriter);
            String serialized = stringWriter.toString();

            // Читаем обратно с фабрикой по умолчанию
            StringReader stringReader = new StringReader(serialized);
            TabulatedFunction funcFromText = TabulatedFunctions.readTabulatedFunction(stringReader);
            System.out.println("Из текстового потока (фабрика): " + funcFromText.getClass().getSimpleName());

            // Читаем с указанием конкретного класса через рефлексию
            stringReader = new StringReader(serialized);
            TabulatedFunction funcFromTextReflection = TabulatedFunctions.readTabulatedFunction(
                    LinkedListTabulatedFunction.class, stringReader);
            System.out.println("Из текстового потока (рефлексия): " + funcFromTextReflection.getClass().getSimpleName());

            // Проверяем, что данные сохранились корректно
            System.out.println("\n=== Проверка данных ===");
            System.out.println("Оригинал: " + originalFunc);
            System.out.println("Из бинарного: " + funcFromBinary);
            System.out.println("Из текстового: " + funcFromText);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}