package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать объект класса TabulatedFunctions");
    }

    // Фабричные методы

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // Методы с рефлексией

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass,
                                                            double leftX, double rightX, int pointsCount) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass,
                                                            double leftX, double rightX, double[] values) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    // методы бинарного чтения/записи

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush();
    }

    // Старая версия
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }

    // Новая версия с указанием класса через рефлексию
    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем рефлексию для создания объекта указанного класса
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IOException("Error creating tabulated function from stream", e);
        }
    }

    // методы символьного чтения/записи

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);

        writer.print(function.getPointsCount());
        writer.print(' ');

        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(' ');
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(' ');
            }
        }

        writer.flush();
    }

    // Старая версия
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X точки " + (i + 1));
            }
            double x = tokenizer.nval;

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y точки " + (i + 1));
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }

    // указание класса через рефлексию
    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X точки " + (i + 1));
            }
            double x = tokenizer.nval;

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y точки " + (i + 1));
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }


        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IOException("Error creating tabulated function from reader", e);
        }
    }

    // етод tabulate
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    // Перегруженный метод tabulate с рефлексией
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function,
                                             double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }
}