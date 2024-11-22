import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DB_1230350_1241456 {

    // ------ CONSTANTS ------
    static final int FULL_BATTERY = 100;
    static final int PREVENTION_DAY = 0; // day that the user wants to choose for exercise J
    static final int EXTRA_COLUMN = 1; // because the first column is the vehicle number
    static final double COST_PER_RECHARGE = 5.5;

    // ------ MAIN ------
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input.txt");
        Scanner scanner = new Scanner(file);

        double[][] voltDeiMatrix = matrixBuilder(scanner); // creates the matrix, with the first column being the vehicle number

        planningMatrix(voltDeiMatrix); // a)
        totalTraveledDistance(voltDeiMatrix); // b)
        double[][] batteryRechargeMatrix = batteryRecharge(voltDeiMatrix); // print exercise c) and return matrix
        double[][] dailyChargeArray = dailyCharge(voltDeiMatrix); //print exercise d) and return matrix
        double[][] averageKmMatrix = averageDayCarsKm(voltDeiMatrix); // print exercise e) and return matrix
        vehiclesWithAnHigherAverage(voltDeiMatrix, averageKmMatrix); // f)
        vehiclesConsecutiveRecharges(batteryRechargeMatrix); // g)
        latestDayWithMoreCharges(voltDeiMatrix, dailyChargeArray); // h)
        rechargesCost(batteryRechargeMatrix); // i)
        preventionVehicle(voltDeiMatrix, dailyChargeArray, PREVENTION_DAY); // j)

        scanner.close();
    }


    //------------ EXERCISES  ------------
    //------ EXERCISE A ------
    public static void planningMatrix(double[][] voltDeiMatrix) {

        System.out.println("a) planeamento (km/dia/veículo)");

        final int FORMAT = 1;

        printMatrix(voltDeiMatrix, FORMAT, true, true, "");
    }

    //------ EXERCISE B ------
    public static void totalTraveledDistance(double[][] voltDeiMatrix) {

        System.out.println("\nb) total de km a percorrer");

        final int FORMAT = 1;

        int totalDistance = 0;
        int numberOfRows = voltDeiMatrix.length;
        double[][] totalDistanceArray = new double[numberOfRows][2];


        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            for (int columnNumber = 1; columnNumber < voltDeiMatrix[rowNumber].length; columnNumber++) {
                totalDistance += (int) voltDeiMatrix[rowNumber][columnNumber];
            }

            totalDistanceArray[rowNumber][1] = totalDistance;

            totalDistance = 0;
        }

        printMatrix(totalDistanceArray, FORMAT, false, false, "km");
    }

    //------ EXERCISE C ------
    public static double[][] batteryRecharge(double[][] voltDeiMatrix) {

        System.out.println("\nc) recargas das baterias");

        final int FORMAT = 1;

        int rechargeCounter;
        int batteryInicialCharge;

        double[][] batteryRechargeMatrix = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        // same size as the voltDeiMatrix, with the first column being the vehicle number

        double[][] voltDeiMatrixCopy = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        //copy the matrix to not change the original

        for (int i = 0; i < voltDeiMatrixCopy.length; i++) {
            for (int j = 0; j < voltDeiMatrixCopy[i].length; j++) {
                voltDeiMatrixCopy[i][j] = voltDeiMatrix[i][j];
            }
        }

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

        printMatrix(batteryRechargeMatrix, FORMAT, true, true, "");
        return batteryRechargeMatrix;
    }


    //------------ EXERCISE D ------------
    public static double[][] dailyCharge(double[][] voltDeiMatrix) {

        System.out.println("\nd) carga das baterias");

        final int FORMAT = 2;

        double[][] dailyChargeArray = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        double partialBattery;
        double remainderBattery;
        double totalBattery;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            remainderBattery = 0;
            for (int columns = 1; columns < voltDeiMatrix[0].length; columns++) {


                partialBattery = (remainderBattery - (voltDeiMatrix[rows][columns]));

                while (partialBattery <= 0) {
                    partialBattery += FULL_BATTERY;
                }

                totalBattery = partialBattery;

                if (totalBattery > FULL_BATTERY) {
                    totalBattery -= FULL_BATTERY;
                }

                dailyChargeArray[rows][columns] = totalBattery;
                remainderBattery = partialBattery;
            }
        }

        printMatrix(dailyChargeArray, FORMAT, true, true, "");
        return dailyChargeArray;
    }


    //------ EXERCISE E ------
    public static double[][] averageDayCarsKm(double[][] voltDeiMatrix) {
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
        return averageKmMatrix;
    }


    //------ EXERCISE F ------
    public static void vehiclesWithAnHigherAverage(double[][] voltDeiMatrix, double[][] averageKmMatrix) {
        System.out.print("\nf) deslocações sempre acima da média diária");

        int[] carOverAverageArray = new int[voltDeiMatrix.length];
        int howManyVehicles = 0;
        int counter;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            counter = 0;
            for (int column = 1; column < voltDeiMatrix[0].length; column++) {
                if (voltDeiMatrix[rows][column] > averageKmMatrix[0][column]) {
                    counter++;
                }
            }
            if (counter == averageKmMatrix[0].length - EXTRA_COLUMN) {
                carOverAverageArray[howManyVehicles] = (int) voltDeiMatrix[rows][0];
                howManyVehicles++;
            }
        }

        System.out.printf("\n<%d> veículos : ", howManyVehicles);

        for (int iteratorToPrint = 0; iteratorToPrint < howManyVehicles; iteratorToPrint++) {
            System.out.printf("[V%d]", carOverAverageArray[iteratorToPrint]);
        }
        System.out.println();
    }


    //------ EXERCISE G ------
    public static void vehiclesConsecutiveRecharges(double[][] batteryRechargeMatrix) {
        System.out.print("\ng) veículos com mais dias consecutivas a necessitar de recarga :");

        boolean consecutiveRecharge;
        int actualConsecutiveDaysRecharge;
        int[] actualVehicle = new int[batteryRechargeMatrix.length];
        int maxConsecutiveDaysRecharge = 0;

        // initialize all positions of the array with -1
        for (int i = 0; i < actualVehicle.length; i++) {
            actualVehicle[i] = -1;
        }


        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            consecutiveRecharge = false;
            actualConsecutiveDaysRecharge = 0;

            for (int j = 1; j < batteryRechargeMatrix[0].length; j++) {
                if (batteryRechargeMatrix[i][j] > 0 && !consecutiveRecharge) {
                    consecutiveRecharge = true;
                    actualConsecutiveDaysRecharge++;

                } else if (batteryRechargeMatrix[i][j] > 0 && consecutiveRecharge) {
                    actualConsecutiveDaysRecharge++;

                } else if (batteryRechargeMatrix[i][j] == 0) {
                    consecutiveRecharge = false;

                    if (actualConsecutiveDaysRecharge > maxConsecutiveDaysRecharge) {
                        maxConsecutiveDaysRecharge = actualConsecutiveDaysRecharge;
                        actualVehicle[i] = i;
                    }
                    actualConsecutiveDaysRecharge = 0;
                }
            }

            if (actualConsecutiveDaysRecharge >= maxConsecutiveDaysRecharge) {
                maxConsecutiveDaysRecharge = actualConsecutiveDaysRecharge;
                actualVehicle[i] = i;
            }
        }

        if (maxConsecutiveDaysRecharge == 0) {
            System.out.print("\n- nenhum veiculo precisou de recarga!\n");
        } else {
            System.out.printf(" <%d> dias consecutivos, veículos :", maxConsecutiveDaysRecharge);

            for (int i = 0; i < actualVehicle.length; i++) {
                if (actualVehicle[i] > -1) {
                    System.out.printf(" [V%d] ", actualVehicle[i]);
                }
            }
            System.out.println();
        }
    }


    //------ EXERCISE H ------
    public static void latestDayWithMoreCharges(double[][] voltDeiMatrix, double[][] dailyChargeArray) {
        int numberOfCharges = 0;
        int numberOfMaxCharges = Integer.MAX_VALUE;
        int greaterColumn = 0;

        for (int columns = 1; columns < dailyChargeArray[0].length; columns++) {
            numberOfCharges = 0;
            for (int rows = 0; rows < dailyChargeArray.length; rows++) {

                if (dailyChargeArray[rows][columns] > 0) {
                    numberOfCharges++;
                }
            }
            if (numberOfMaxCharges > numberOfCharges) {
                numberOfMaxCharges = numberOfCharges;
                greaterColumn = columns + EXTRA_COLUMN;
            }
        }

        if (numberOfCharges == 0) {
            greaterColumn = -1;
        }

        System.out.printf("\nh) dia mais tardio em que todos os veículos necessitam de recarregar <%d>\n", greaterColumn);
    }

    //------ EXERCISE I ------
    public static void rechargesCost(double[][] batteryRechargeMatrix) {
        System.out.print("\ni) custo das recargas da frota :");

        double totalCost = 0;

        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            for (int j = 1; j < batteryRechargeMatrix[0].length; j++) {
                totalCost = totalCost + (batteryRechargeMatrix[i][j] * COST_PER_RECHARGE);
            }
        }

        if (totalCost == 0) {
            System.out.print("- não houveram recargas, logo não há custos!");
        } else {
            System.out.printf(" <%.2f €>", totalCost);
        }
    }


    //------ EXERCISE J ------
    public static void preventionVehicle(double[][] voltDeiMatrix, double[][] dailyChargeArray, int preventionDay) {

        int carCharge;
        int carKilometers;
        int lastCarCharge = Integer.MIN_VALUE;
        int lastcarKilometers = Integer.MAX_VALUE;
        int preventionVehicle = 0;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            carKilometers = (int) voltDeiMatrix[rows][preventionDay + EXTRA_COLUMN];
            carCharge = (int) dailyChargeArray[rows][preventionDay + EXTRA_COLUMN];

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
        voltDeiMatrix = new double[(int) quantityOfVehicles][(int) (quantityOfDays + 1)]; // +1 because the first column is the vehicle number

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }
        return voltDeiMatrix;
    }

    //------ AUX -> METHOD THAT PRINTS ANY MATRIX ------
    public static void printMatrix(double[][] matrix, int format, boolean barOnTop, boolean dayMessage, String sufix) {
        int quantityOfDays = matrix[0].length - EXTRA_COLUMN;

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

                } else if (j == 0) {
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
                    }
                }
            }
            System.out.println();
        }
    }
}
