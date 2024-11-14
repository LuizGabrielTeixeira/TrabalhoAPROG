import java.util.Scanner;

public class DB_1230350_1241456 {
    public static void main(String[] args) {
        planningMatrix();
    }

    //a)
    public static void planningMatrix() {
        Scanner scanner = new Scanner(System.in);

        int quantityOfVehicles = scanner.nextInt();
        int quantityOfDays = scanner.nextInt();

        int[][] voltDeiMatrix;
        voltDeiMatrix = new int[quantityOfVehicles][quantityOfDays+1];

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < quantityOfDays; i++) {
            if (i == 0) {
                System.out.print("a) planeamento (km/dia/veículo)\ndia : ");
            }
            System.out.printf("%8d ", i);
        }

        System.out.print("\n----|--------|--------|--------|--------|--------|--------|\n");

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            voltDeiMatrix[i][0] = (i);
            for (int j = 0; j < voltDeiMatrix[i].length; j++) {
                if (j == 0) {
                    System.out.printf("V%-3d:", voltDeiMatrix[i][j]);
                } else {
                    System.out.printf("%8d ", voltDeiMatrix[i][j]);
                }
            }
            System.out.println();
        }

    }

    //b)
    public static void totalTraveledDistance (int numberOfRows, int[][]voltDeiMatrix){

        int totalDistance = 0;
        int[][] totalDistanceArray = new int[numberOfRows][2];

        for (int rowNumber = 1; rowNumber < voltDeiMatrix.length; rowNumber++) {
            for (int columnNumber = 1; columnNumber < voltDeiMatrix[numberOfRows].length; columnNumber++) {
                totalDistance += voltDeiMatrix[rowNumber][columnNumber];
            }

            totalDistanceArray[rowNumber - 1][2] = totalDistance;
        }
    }
    //c)
    public static void c (){}


    //d)
    public static void d (){}


    //e)
    public static void e (){}


    //f)
    public static void f (){}


    //g)
    public static void g (){}


    //h)
    public static void h (){}


    //i)
    public static void i (){}


    //j)
    public static void j (){}


}
