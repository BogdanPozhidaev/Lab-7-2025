package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable {
    private double x;
    private double y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }



    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        // Сравнение чисел с плавающей точкой с учетом погрешности
        return Math.abs(that.x - x) < 1e-10 &&
                Math.abs(that.y - y) < 1e-10;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }

    @Override
    public Object clone() {
        try {
            // Создаем новый объект с теми же координатами
            return new FunctionPoint(this.x, this.y);
        } catch (Exception e) {
            throw new AssertionError("Ошибка при клонировании");
        }
    }
}