import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DB_1230350_1241456 {

    // ------ CONSTANTS ------
    static final int FULL_BATTERY = 100;

    static final int PREVENTION_DAY = 4;

    // ------ MAIN ------
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input.txt");
        Scanner scanner = new Scanner(file);

        double[][] voltDeiMatrix = matrixBuilder(scanner); // creates the matrix

        planningMatrix(voltDeiMatrix); // a)
        totalTraveledDistance(voltDeiMatrix); // b)
        batteryRecharge(voltDeiMatrix, true); // c)
        dailyCharge(voltDeiMatrix, true); //d)
        averageDayCarsKm(voltDeiMatrix, true); // e)
        vehiclesWithAnHigherAverage(voltDeiMatrix); // f)
        // g)
        latestDayWithMoreCharges(voltDeiMatrix); //h
        // i)
        preventionVehicle(voltDeiMatrix, PREVENTION_DAY);
      
        scanner.close();
    }


    //------------ EXERCISES  ------------
    //------ EXERCISE A ------
    public static void planningMatrix(double[][] voltDeiMatrix) {
        int format = 1;
        System.out.print("\na) planeamento (km/dia/veículo)\n");

        printMatrix(voltDeiMatrix, format, true, true, "");
    }

    //------ EXERCISE B ------
    public static void totalTraveledDistance(double[][] voltDeiMatrix) {

        int totalDistance = 0;
        int numberOfRows = voltDeiMatrix.length;
        double[][] totalDistanceArray = new double[(int) numberOfRows][2];

        System.out.println("\nb) total de km a percorrer");

        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            for (int columnNumber = 1; columnNumber < voltDeiMatrix[rowNumber].length; columnNumber++) {
                totalDistance += (int) voltDeiMatrix[rowNumber][columnNumber];
            }

            totalDistanceArray[rowNumber][1] = totalDistance;

            totalDistance = 0;
        }

        printMatrix(totalDistanceArray, 1, false, false, "km");
    }

    //------ EXERCISE C ------
    public static double[][] batteryRecharge(double[][] voltDeiMatrix) {
        int format = 1;
        System.out.println("\nc) recargas das baterias");

        //copy the matrix to not change the original
        double[][] voltDeiMatrixCopy = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        for (int i = 0; i < voltDeiMatrixCopy.length; i++) {
            for (int j = 0; j < voltDeiMatrixCopy[i].length; j++) {
                voltDeiMatrixCopy[i][j] = voltDeiMatrix[i][j];
            }
        }

        double[][] batteryRechargeMatrix = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        int rechargeCounter;
        int batteryInicialCharge;

        for (int i = 0; i < voltDeiMatrixCopy.length; i++) {
            batteryInicialCharge = FULL_BATTERY;
            for (int j = 1; j < voltDeiMatrixCopy[i].length; j++) {
                rechargeCounter = 0;

                while (voltDeiMatrixCopy[i][j] >= batteryInicialCharge) {
                    voltDeiMatrixCopy[i][j] -= batteryInicialCharge;
                    rechargeCounter++;
                    batteryInicialCharge = FULL_BATTERY;
                }

                batteryInicialCharge -= (int) voltDeiMatrixCopy[i][j];

                batteryRechargeMatrix[i][j] = rechargeCounter;
            }
        }
        printMatrix(batteryRechargeMatrix, format, true, true, "");

        return batteryRechargeMatrix;
    }


    //------------ EXERCISE D ------------
    public static double[][] dailyCharge(double[][] voltDeiMatrix, boolean needToPrint) {
        if (needToPrint)
            System.out.println("\nd) carga das baterias");
        double[][] dailyChargeArray = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];

        double partialBattery;
        double remainderBattery;
        double totalBattery;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            remainderBattery = 0;
            for (int columns = 1; columns < voltDeiMatrix[0].length; columns++) {


                partialBattery = (remainderBattery - (voltDeiMatrix[rows][columns]));

                while (partialBattery <= 0) {
                    partialBattery += 100;
                }

                totalBattery = partialBattery;

                if (totalBattery > 100) {
                    totalBattery -= 100;
                }

                dailyChargeArray[rows][columns] = totalBattery;
                remainderBattery = partialBattery;
            }
        }
        if (needToPrint)
            printMatrix(dailyChargeArray, 2, true, true, "");

        return dailyChargeArray;
    }


    //------ EXERCISE E ------
    public static void averageDayCarsKm(double[][] voltDeiMatrix) {
        final int FORMAT = 3;
        System.out.println("\ne) média de km diários da frota");

        double[][] averageKmMatrix = new double[1][voltDeiMatrix[0].length];
        double sumKilometers;
        double divisor = voltDeiMatrix.length;
        double average;

        for (int i = 1; i < voltDeiMatrix[0].length; i++) {
            sumKilometers = 0;
            for (int j = 0; j < voltDeiMatrix.length; j++) {
                sumKilometers += voltDeiMatrix[j][i];
            }
            average = sumKilometers / divisor;
            averageKmMatrix[0][i] = average;
        }

        printMatrix(averageKmMatrix, FORMAT, true, true, "");
    }


    //------ EXERCISE F ------
    public static void f() {
    }


    //------ EXERCISE G ------
    public static void vehiclesConsecutiveRecharges (double[][] batteryRechargeMatrix) {
        int format = 4;
        System.out.println("\ng) veículos com mais dias consecutivas a necessitar de recarga");

        double[][] vehiclesConsecutiveRecharges = new double[1][3]; //days, vehicle
        boolean consecutiveRecharge;
        int maxConsecutiveDaysRecharge = 0;
        int actualConsecutiveDaysRecharge;
        int actualVehicle = 999;

        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            consecutiveRecharge = false;
            actualConsecutiveDaysRecharge = 0;
            for (int j = 1; j < batteryRechargeMatrix[0].length; j++) {
                if (batteryRechargeMatrix[i][j] > 0 && !consecutiveRecharge){
                    consecutiveRecharge = true;
                    actualConsecutiveDaysRecharge++;
                } else if (batteryRechargeMatrix[i][j] > 0 && consecutiveRecharge) {
                    actualConsecutiveDaysRecharge++;
                } else if (batteryRechargeMatrix[i][j] == 0) {
                    consecutiveRecharge = false;
                }
            }
            if (actualConsecutiveDaysRecharge > maxConsecutiveDaysRecharge) {
                maxConsecutiveDaysRecharge = actualConsecutiveDaysRecharge;
                actualVehicle = i;
            }
        }

        // erro na matriz, print errado
        // erro no metodo printMatrix,
        vehiclesConsecutiveRecharges[0][1] = actualVehicle;
        vehiclesConsecutiveRecharges[0][2] = maxConsecutiveDaysRecharge;

        printMatrix(vehiclesConsecutiveRecharges, format, false, false, "dias consecutivos, veículos :");

    }


    //------ EXERCISE H ------
    public static void latestDayWithMoreCharges(double[][] voltDeiMatrix) {
        double[][] batteryRechargeArray = batteryRecharge(voltDeiMatrix, false);
        int numberOfCharges = 0;
        int numberOfMaxCharges = Integer.MAX_VALUE;
        int greaterColumn = 0;

        final int ERRORMARGIN = 1; //Might be deleted. However, used because our first column only contains the index used to count the cars.


        for (int columns = 1; columns < batteryRechargeArray[0].length; columns++) {
            numberOfCharges = 0;
            for (int rows = 0; rows < batteryRechargeArray.length; rows++) {

                if (batteryRechargeArray[rows][columns] > 0) {
                    numberOfCharges++;
                }
            }
            if (numberOfMaxCharges > numberOfCharges) {
                numberOfMaxCharges = numberOfCharges;
                greaterColumn = columns + ERRORMARGIN;
            }
        }

        if (numberOfCharges == 0) {
            greaterColumn = -1;
        }

        System.out.println();
        System.out.println();
        System.out.printf("h) dia mais tardio em que todos os veículos necessitam de recarregar <%d>", greaterColumn);
    }
  
    //------ EXERCISE I ------
    public static void i() {
    }


    //------ EXERCISE J ------
    public static void preventionVehicle(double[][] voltDeiMatrix, int preventionDay) {

        final int EXTRACOLUMN = 1;
        double[][] carChargeArray = dailyCharge(voltDeiMatrix, false);
        int carCharge;
        int carKilometers;
        int lastCarCharge = Integer.MIN_VALUE;
        int lastcarKilometers = Integer.MAX_VALUE;
        int preventionVehicle = 0;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            carKilometers = (int) voltDeiMatrix[rows][preventionDay + EXTRACOLUMN];
            carCharge = (int) carChargeArray[rows][preventionDay + EXTRACOLUMN];

            if (lastcarKilometers > carKilometers) {
                lastcarKilometers = carKilometers;
                preventionVehicle = rows;
            } else if (lastcarKilometers == carKilometers) {
                if (lastCarCharge < carCharge) {
                    lastCarCharge = carCharge;
                    preventionVehicle = rows;
                } else if (lastCarCharge == carCharge) {
                    preventionVehicle = rows - 1;
                }
            }
        }
        System.out.printf("%n%nj) veículo de prevenção no dia <%d> : V%d", preventionDay, preventionVehicle);
    }


// ------ AUXILIARY METHODS ------

    //------ AUX -> METHOD TO BUILD A MATRIX ------
    public static double[][] matrixBuilder(Scanner scanner) {

        String text = scanner.nextLine();
        double quantityOfVehicles = scanner.nextDouble();
        double quantityOfDays = scanner.nextDouble();

        double[][] voltDeiMatrix;
        voltDeiMatrix = new double[(int) quantityOfVehicles][(int) (quantityOfDays + 1)];

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }
        return voltDeiMatrix;
    }

    //------ AUX -> METHOD THAT PRINTS ANY MATRIX ------
    public static void printMatrix(double[][] matrix, int format, boolean barOnTop, boolean dayMessage, String sufix) {
        int quantityOfDays = matrix[0].length - 1;

        if (dayMessage) {
            for (int i = 0; i < quantityOfDays; i++) {
                if (i == 0) {
                    System.out.print("dia : ");
                }
                System.out.printf("%8d ", i);
            }

        }

        if (barOnTop) {
            System.out.print("\n----|");
            while (quantityOfDays > 0) {
                System.out.print("--------|");
                quantityOfDays--;
            }
            System.out.println();
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = (i);
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0 && format == 3) {
                    String text = "km";
                    System.out.printf("%-3s :", text);
                } else if (j == 0 && format != 4) {
                    System.out.printf("V%-3d:", (int) matrix[i][j]);
                } else {
                    switch (format) {
                        case 1:
                            //only print the integer number
                            System.out.printf("%8d %s", (int) matrix[i][j], sufix);
                            break;
                        case 2:
                            // print the percentage with one decimal
                            System.out.printf("%7.1f%% %s", matrix[i][j], sufix);
                            break;
                        case 3:
                            // print the average with one decimal
                            System.out.printf("%8.1f %s", matrix[i][j], sufix);
                            break;
                        case 4:
                            // print for ex F and G
                            System.out.printf("<%d> %s [V%d]", (int) matrix[0][2], sufix ,(int) matrix[0][1]);
                            j = matrix[i].length;
                            break;

                    }
                }
            }
            System.out.println();
        }

    }

}