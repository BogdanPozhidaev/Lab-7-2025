package functions;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] points;
    private int pointsCount;

    // Конструктор создания объекта через количество точек
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек < 2");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 5];

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }


    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < pointsCount;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more points available");
                }
                // Возвращаем копию точки для сохранения инкапсуляции
                return new FunctionPoint(points[currentIndex++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");
            }
        };
    }

    // Конструктор создания объекта через массив значений
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения >= правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше двух");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[values.length + 5];

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double x = leftX + step * i;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    // Конструктор создания объекта через массив точек
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше двух");
        }

        // Проверка упорядоченности точек по X
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по возрастанию X");
            }
        }

        this.pointsCount = points.length;
        this.points = new FunctionPoint[points.length + 5];

        // Создаем копии точек для обеспечения инкапсуляции
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Поиск интервала, содержащего x
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();

                // Линейная интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        if (x == points[pointsCount - 1].getX()) {
            return points[pointsCount - 1].getY();
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index > 0 && point.getX() <= points[index-1].getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < (pointsCount-1) && point.getX() >= points[index+1].getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index > 0 && x <= points[index-1].getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < (pointsCount-1) && x >= points[index+1].getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        points[index].setX(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        points[index].setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Невозможно удалить точку, так как кол-во точек меньше 3");
        }

        System.arraycopy(points, index+1, points, index, pointsCount-index-1);
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на дублирование X
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с таким х уже существует");
            }
        }

        // Проверка на необходимость расширения
        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        int pos = 0;
        while (pos < pointsCount && points[pos].getX() < point.getX()) {
            pos++;
        }

        // Сдвиг вправо
        System.arraycopy(points, pos, points, pos + 1, pointsCount - pos);

        // Добавление новой точки
        points[pos] = new FunctionPoint(point);
        pointsCount++;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            sb.append(points[i].toString());
            if (i < pointsCount - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction that = (TabulatedFunction) o;

        // Проверка количества точек
        if (this.getPointsCount() != that.getPointsCount()) {
            return false;
        }

        // Оптимизация для ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction arrayThat = (ArrayTabulatedFunction) o;

            // Прямое сравнение массивов точек
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(arrayThat.points[i])) {
                    return false;
                }
            }
        } else {
            // Общий случай для любого TabulatedFunction
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint thatPoint = that.getPoint(i);

                if (!thisPoint.equals(thatPoint)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = pointsCount;

        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }

        return hash;
    }

    @Override
    public Object clone() {
        // Глубокое клонирование - создаем новый массив только с нужными точками
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = new FunctionPoint(points[i].getX(), points[i].getY());
        }

        return new ArrayTabulatedFunction(clonedPoints);
    }
}

