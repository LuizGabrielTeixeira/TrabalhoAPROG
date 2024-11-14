import java.util.Scanner;

public class DB_1230350_1241456 {
    public static void main(String[] args) {
        int[][] volDeiMatrix = matrixBuilder(); // creates the matrix

        planningMatrix(volDeiMatrix); // a)
        totalTraveledDistance(volDeiMatrix); // b)
        batteryRecharge(volDeiMatrix); // c)
    }

    //method that returns the matrix
    public static int[][] matrixBuilder() {
        Scanner scanner = new Scanner(System.in);

        int quantityOfVehicles = scanner.nextInt();
        int quantityOfDays = scanner.nextInt();

        int[][] voltDeiMatrix;
        voltDeiMatrix = new int[quantityOfVehicles][quantityOfDays + 1];

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }
        return voltDeiMatrix;
    }

    //method that prints the matrix
    public static void printMatrix(int[][] matrix, int format) {
        int quantityOfDays = matrix[0].length - 1;

        for (int i = 0; i < quantityOfDays; i++) {
            if (i == 0) {
                System.out.print("dia : ");
            }
            System.out.printf("%8d ", i);
        }
        System.out.print("\n----|--------|--------|--------|--------|--------|--------|\n");

        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = (i);
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0) {
                    System.out.printf("V%-3d:", matrix[i][j]);
                } else {
                    switch (format) {
                        case 1:
                            //only print the intenger number
                            System.out.printf("%8d ", matrix[i][j]);
                            break;
                        case 2:
                            // print the percentage with one decimal
                            System.out.printf("%7.1f%% ", (double) matrix[i][j]);
                            break;
                        case 3:
                            // print the average with one decimal
                            System.out.printf("%8.1f ", (double) matrix[i][j]);
                            break;
                    }
                }
            }
            System.out.println();
        }

    }

    //method that prints the matrix showing kilometers format
    public static void planningMatrix(int[][] voltDeiMatrix) {
        int format = 1;
        System.out.print("a) planeamento (km/dia/veículo)\n");

        printMatrix(voltDeiMatrix, format);
    }

    //method that calculates the total distance traveled by each vehicle in the period
    public static void totalTraveledDistance(int[][] voltDeiMatrix) {

        int totalDistance = 0;
        int numberOfRows = voltDeiMatrix.length;
        int[][] totalDistanceArray = new int[numberOfRows][2];

        for (int rowNumber = 1; rowNumber < voltDeiMatrix.length; rowNumber++) {
            for (int columnNumber = 1; columnNumber < voltDeiMatrix[rowNumber].length; columnNumber++) {
                totalDistance += voltDeiMatrix[rowNumber][columnNumber];
            }

            totalDistanceArray[rowNumber - 1][1] = totalDistance;
        }

        for (int i = 0; i < totalDistanceArray.length; i++) {
            for (int j = 0; j < totalDistanceArray[i].length; j++) {
                System.out.println(totalDistanceArray[i][j]);
            }
        }
    }

    //method that calculates the number of recharges needed for each vehicle in a day
    public static void batteryRecharge(int[][] voltDeiMatrix) {
        int format = 1;
        System.out.println("c) recargas das baterias");

        int[][] batteryRechargeMatrix = new int[voltDeiMatrix.length][voltDeiMatrix[0].length];
        int rechargeCounter;
        final int fullBattery = 100;
        int batteryInicialCharge;

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            batteryInicialCharge = fullBattery;
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                rechargeCounter = 0;

                while (voltDeiMatrix[i][j] >= batteryInicialCharge) {
                    voltDeiMatrix[i][j] -= batteryInicialCharge;
                    rechargeCounter++;
                    batteryInicialCharge = fullBattery;
                }

                batteryInicialCharge -= voltDeiMatrix[i][j];

                batteryRechargeMatrix[i][j] = rechargeCounter;
            }
        }

        printMatrix(batteryRechargeMatrix, format);
    }


    //d)
    public static void d() {
    }


    //e)
    public static void e() {
    }


    //f)
    public static void f() {
    }


    //g)
    public static void g() {
    }


    //h)
    public static void h() {
    }


    //i)
    public static void i() {
    }


    //j)
    public static void j() {
    }


}
