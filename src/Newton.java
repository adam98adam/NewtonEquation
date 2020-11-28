import java.util.ArrayList;
import java.lang.Math;
import java.util.List;
import java.util.Scanner;

class Container {

    private double x;
    private double y;

    Container(double x,double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}

public class Newton {

    ArrayList<Container> results = new ArrayList<Container>();
    double[][] matrix = new double[2][2];
    double[] fxy = new double[2];


    boolean checkInput(String x) {
        try {
            double value = Double.parseDouble(x.replace(',', '.'));
            return value > 0.0 && value < 0.5;
        } catch (Exception e) {
            return false;
        }
    }

    double derivativeF1X(double x, double y) {
        return Math.exp(x + y);
    }

    double derivativeF1Y(double x, double y) {
        return Math.exp(x + y) + 1;
    }

    double derivativeF2X(double x, double y) {
        return Math.exp(x - y) + 1;
    }

    double derivativeF2Y(double x, double y) {
        return -Math.exp(x - y);
    }

    double valueF1XY(double x, double y) {
        return Math.exp(x + y) + y;
    }

    double valueF2XY(double x, double y) {
        return Math.exp(x - y) + x;
    }

    void inverseMatrix(double[][] matrix) {
        double matrixdeterminant;
        double[][] values = {
                {matrix[0][0], matrix[0][1]},
                {matrix[1][0], matrix[1][1]}
        };

        matrix[0][0] = values[1][1];
        matrix[0][1] = -values[0][1];
        matrix[1][0] = -values[1][0];
        matrix[1][1] = values[0][0];

        matrixdeterminant = (values[0][0] * values[1][1]) - (values[0][1] * values[1][0]);

        matrix[0][0] = matrix[0][0] * 1 / matrixdeterminant;
        matrix[0][1] = matrix[0][1] * 1 / matrixdeterminant;
        matrix[1][0] = matrix[1][0] * 1 / matrixdeterminant;
        matrix[1][1] = matrix[1][1] * 1 / matrixdeterminant;
    }

    void matrixMultiplication(double[][] matrix, double[] fxy) {
        double[] values = new double[2];
        values[0] = (matrix[0][0] * fxy[0]) + (matrix[0][1] * fxy[1]);
        values[1] = (matrix[1][0] * fxy[0]) + (matrix[1][1] * fxy[1]);
        fxy[0] = values[0];
        fxy[1] = values[1];
    }

    void printxy(ArrayList<Container> results) {
        for (Container xy : results) {
            System.out.println("x = " + xy.getX() + "  y = " + xy.getY());
        }
    }

    void newton() {
        Scanner userInput = new Scanner(System.in);
        String s;
        double precision, x, y, valuef1, valuef2;

        System.out.println("Type a double-type number(greater than 0.0 and less than 0.5): ");
        s = userInput.nextLine();
        while (!checkInput(s)) {
            System.out.println("Sorry, try again...");
            System.out.println("Type a double-type number(greater than 0.0 and less than 0.5): ");
            s = userInput.nextLine();
        }
        precision = Double.parseDouble(s.replace(',', '.'));
        results.add(new Container(1.0, 1.0));

        do {
            x = results.get(results.size() - 1).getX();
            y = results.get(results.size() - 1).getY();
            matrix[0][0] = derivativeF1X(x, y);
            matrix[0][1] = derivativeF1Y(x, y);
            matrix[1][0] = derivativeF2X(x, y);
            matrix[1][1] = derivativeF2Y(x, y);
            fxy[0] = valueF1XY(x, y);
            fxy[1] = valueF2XY(x, y);
            inverseMatrix(matrix);
            matrixMultiplication(matrix, fxy);
            results.add(new Container(x - fxy[0], y - fxy[1]));
            //size = results.size();
            valuef1 = valueF1XY(results.get(results.size() - 1).getX(), results.get(results.size() - 1).getY());
            valuef2 = valueF2XY(results.get(results.size() - 1).getX(), results.get(results.size() - 1).getY());
        } while (!((Math.abs(valuef1) + Math.abs(valuef2)) < precision));

        results.remove(results.size()-1);
        printxy(results);
    }

    public static void main(String[] args) {
        Newton n = new Newton();
        n.newton();
    }
}
