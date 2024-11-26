import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A classe ExerciseJavaDoc foi criada para o trabalho de APROG, documentada por curiosidade da dupla sobre javaDoc e GitHub Pages.
 * Segue formatações do curso, sem POO ou conceitos avançados, usando boas práticas, arrays, lógica e modularização.
 * A documentação dessa classe está em inglês e português. Cada método inclui um comentário em ambas as línguas,
 * seguido da descrição na mesma linha: primeiro em português e depois em inglês.
 * <p>
 * The ExerciseJavaDoc class was created for the APROG assignment, documented out of the duo's curiosity about javaDoc and GitHub Pages.
 * It follows the course's formatting, without OOP or advanced concepts, using best practices, arrays, logic, and modularization.
 * This class's documentation is provided in both English and Portuguese. Each method includes comments in both languages,
 * followed by the description on the same line: first in Portuguese and then in English.
 */

public class ExerciseJavaDoc {

    // ------ CONSTANTS ------
    static final int FULL_BATTERY = 100;
    static final int PREVENTION_DAY = 1; // day that the user wants to choose for exercise J
    static final int EXTRA_COLUMN = 1; // because the first column is the vehicle number
    static final double COST_PER_RECHARGE = 5.5;

    /**
     * O método principal que executa todos os exercícios.
     * [EN] The main method that executes all exercises.
     *
     * @param args Argumentos da linha de comando.
     *             [EN] Command-line arguments.
     */
    // ------ MAIN ------
    public static void main(String[] args) {
        File file = new File("input.txt");
        Scanner scanner;
        double[][] voltDeiMatrix;

        try {
            scanner = new Scanner(file);

            try {
                voltDeiMatrix = matrixBuilder(scanner);
                // creates the matrix, with the first column being the vehicle number
            } catch (NoSuchElementException e) {
                System.out.println("Erro na leitura do ficheiro, algo está mal formatado!");
                // throws an error if the input is not correct
                scanner.close();
                return;
            }

        } catch (FileNotFoundException e) {
            System.out.printf("O seguinte ficheiro não foi encontrado: '%s'.\n", file);
            // throws an error if the file is not found
            return;
        }

        planningMatrix(voltDeiMatrix); // a)
        totalTraveledDistance(voltDeiMatrix); // b)
        double[][] batteryRechargeMatrix = batteryRecharge(voltDeiMatrix); // print c) and return matrix
        double[][] dailyChargeArray = dailyCharge(voltDeiMatrix); //print d) and return matrix
        double[][] averageKmMatrix = averageDayCarsKm(voltDeiMatrix); // print e) and return matrix
        vehiclesWithAnHigherAverage(voltDeiMatrix, averageKmMatrix); // f)
        vehiclesConsecutiveRecharges(batteryRechargeMatrix); // g)
        latestDayWithMoreCharges(batteryRechargeMatrix); // h)
        rechargesCost(batteryRechargeMatrix); // i)
        preventionVehicle(voltDeiMatrix, dailyChargeArray, PREVENTION_DAY); // j)

        scanner.close();
    }


    //------------ EXERCISES  ------------
    //------ EXERCISE A ------
    /**
     * Imprime a matriz de planejamento (km/dia/veículo).
     * [EN] Prints the planning matrix (km/day/vehicle).
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     */
    public static void planningMatrix(double[][] voltDeiMatrix) {
        final int FORMAT = 1;

        System.out.println("a) planeamento (km/dia/veículo)");

        printMatrix(voltDeiMatrix, FORMAT, true, true, "");
    }

    //------ EXERCISE B ------
    /**
     * Calcula e imprime a distância total percorrida por cada veículo.
     * [EN] Calculates and prints the total distance traveled by each vehicle.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     */
    public static void totalTraveledDistance(double[][] voltDeiMatrix) {
        final int FORMAT = 1;

        System.out.println("\nb) total de km a percorrer");

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
    /**
     * Calcula e imprime a matriz de recargas das baterias.
     * [EN] Calculates and prints the battery recharge matrix.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     * @return A matriz de recargas das baterias.
     *         [EN] The battery recharge matrix.
     */
    public static double[][] batteryRecharge(double[][] voltDeiMatrix) {
        final int FORMAT = 1;

        System.out.println("\nc) recargas das baterias");

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

    //------ EXERCISE D ------
    /**
     * Calcula e imprime a matriz de carga diária das baterias.
     * [EN] Calculates and prints the daily battery charge matrix.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     * @return A matriz de carga diária das baterias.
     *         [EN] The daily battery charge matrix.
     */
    public static double[][] dailyCharge(double[][] voltDeiMatrix) {
        final int FORMAT = 2;

        System.out.println("\nd) carga das baterias");

        double totalBattery;
        double partialBattery;
        double remainderBattery;

        double[][] dailyChargeArray = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];

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
    /**
     * Calcula e imprime a média de km diários da frota.
     * [EN] Calculates and prints the fleet's daily average kilometers.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     * @return A matriz de média de km diários.
     *         [EN] The daily average kilometers matrix.
     */
    public static double[][] averageDayCarsKm(double[][] voltDeiMatrix) {
        final int FORMAT = 3;

        System.out.println("\ne) média de km diários da frota");

        double average;
        double sumKilometers;
        double divisor = voltDeiMatrix.length;

        double[][] averageKmMatrix = new double[1][voltDeiMatrix[0].length];


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
    /**
     * Imprime os veículos que sempre se deslocaram acima da média diária.
     * [EN] Prints the vehicles that always traveled above the daily average.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     * @param averageKmMatrix A matriz de média de km diários.
     *                        [EN] The daily average kilometers matrix.
     */
    public static void vehiclesWithAnHigherAverage(double[][] voltDeiMatrix, double[][] averageKmMatrix) {
        System.out.print("\nf) deslocações sempre acima da média diária");

        int counter;
        int howManyVehicles = 0;

        int[] carOverAverageArray = new int[voltDeiMatrix.length];


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

        if (howManyVehicles > 0) {
            System.out.printf("\n<%d> veículos : ", howManyVehicles);

            for (int iteratorToPrint = 0; iteratorToPrint < howManyVehicles; iteratorToPrint++) {
                System.out.printf("[V%d]", carOverAverageArray[iteratorToPrint]);
            }
            System.out.println();
        } else {
            System.out.println("\n- nenhum veículo fez deslocações sempre acima da média diária!");
        }
    }

    //------ EXERCISE G ------
    /**
     * Imprime os veículos com mais dias consecutivos necessitando de recarga.
     * [EN] Prints the vehicles with the highest consecutive days requiring recharge.
     *
     * @param batteryRechargeMatrix A matriz de recargas das baterias.
     *                              [EN] The battery recharge matrix.
     */
    public static void vehiclesConsecutiveRecharges(double[][] batteryRechargeMatrix) {
        System.out.print("\ng) veículos com mais dias consecutivas a necessitar de recarga :");

        int actualConsecutiveDaysRecharge;
        int maxConsecutiveDaysRechargePerVehicle;

        int[] maxConsecutiveDaysPerVehicle = new int[batteryRechargeMatrix.length];

        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            actualConsecutiveDaysRecharge = 0;
            maxConsecutiveDaysRechargePerVehicle = 0;

            for (int j = 1; j < batteryRechargeMatrix[0].length; j++) {
                if (batteryRechargeMatrix[i][j] > 0) {
                    actualConsecutiveDaysRecharge++;
                    if (actualConsecutiveDaysRecharge > maxConsecutiveDaysRechargePerVehicle) {
                        maxConsecutiveDaysRechargePerVehicle = actualConsecutiveDaysRecharge;
                    }
                } else {
                    actualConsecutiveDaysRecharge = 0;
                }
            }

            maxConsecutiveDaysPerVehicle[i] = maxConsecutiveDaysRechargePerVehicle;
        }

        int maxConsecutiveDaysRecharge = maxValueCalculator(maxConsecutiveDaysPerVehicle);

        if (maxConsecutiveDaysRecharge == 0) {
            System.out.print("\n- nenhum veiculo precisou de recarga!\n");

        } else {
            System.out.printf("\n<%d> dias consecutivos, veículos :", maxConsecutiveDaysRecharge);

            for (int i = 0; i < maxConsecutiveDaysPerVehicle.length; i++) {
                if (maxConsecutiveDaysPerVehicle[i] == maxConsecutiveDaysRecharge) {
                    System.out.printf("[V%d]", i);
                }
            }
            System.out.println();
        }
    }

    //------ EXERCISE H ------
    /**
     * Imprime o dia mais tardio em que todos os veículos necessitam de recarregar.
     * [EN] Prints the latest day when all vehicles need recharging.
     *
     * @param dailyChargeArray A matriz de carga diária das baterias.
     *                         [EN] The daily battery charge matrix.
     */
    public static void latestDayWithMoreCharges(double[][] dailyChargeArray) {

        int greaterColumn = -1;
        int numberOfCharges;

        for (int columns = 1; columns < dailyChargeArray[0].length; columns++) {
            numberOfCharges = 0;
            for (int rows = 0; rows < dailyChargeArray.length; rows++) {

                if (dailyChargeArray[rows][columns] > 0) {
                    numberOfCharges++;
                }
            }
            if (numberOfCharges == dailyChargeArray.length) {
                greaterColumn = columns - EXTRA_COLUMN;
            }
        }

        System.out.printf("\nh) dia mais tardio em que todos os veículos necessitam de recarregar <%d>\n", greaterColumn);
    }

    //------ EXERCISE I ------
    /**
     * Calcula e imprime o custo das recargas da frota.
     * [EN] Calculates and prints the fleet's recharge costs.
     *
     * @param batteryRechargeMatrix A matriz de recargas das baterias.
     *                              [EN] The battery recharge matrix.
     */
    public static void rechargesCost(double[][] batteryRechargeMatrix) {
        System.out.print("\ni) custo das recargas da frota :");

        double totalCost = 0;

        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            for (int j = 1; j < batteryRechargeMatrix[0].length; j++) {
                totalCost = totalCost + (batteryRechargeMatrix[i][j] * COST_PER_RECHARGE);
            }
        }

        if (totalCost == 0) {
            System.out.print("\n- não houveram recargas, logo não há custos!");
        } else {
            System.out.printf(" <%.2f €>", totalCost);
        }
    }

    //------ EXERCISE J ------
    /**
     * Determina e imprime o veículo de prevenção no dia solicitado.
     * [EN] Determines and prints the prevention vehicle for the requested day.
     *
     * @param voltDeiMatrix A matriz de dados dos veículos.
     *                      [EN] The vehicle data matrix.
     * @param dailyChargeArray A matriz de carga diária das baterias.
     *                         [EN] The daily battery charge matrix.
     * @param solicitedDay O dia solicitado para a prevenção.
     *                     [EN] The requested day for prevention.
     */
    public static void preventionVehicle(double[][] voltDeiMatrix, double[][] dailyChargeArray, int solicitedDay) {

        int preventionVehicle = -1;
        int maxChargeRegistered;
        int minKilometerRegistered;
        boolean vehicleFinder = false;
        int preventionDay = solicitedDay + EXTRA_COLUMN;

        int[] chargeArray = new int[voltDeiMatrix.length];
        int[] vehicleArray = new int[voltDeiMatrix.length];
        int[] kilometerArray = new int[voltDeiMatrix.length];

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            kilometerArray[i] = (int) voltDeiMatrix[i][preventionDay];
            chargeArray[i] = preventionVehicle;
            vehicleArray[i] = preventionVehicle;
        }

        minKilometerRegistered = minValueCalculator(kilometerArray);

        for (int i = 0; i < kilometerArray.length; i++) {
            if (kilometerArray[i] == minKilometerRegistered) {
                chargeArray[i] = (int) dailyChargeArray[i][preventionDay];
            }
        }

        maxChargeRegistered = maxValueCalculator(chargeArray);

        for (int i = 0; i < chargeArray.length; i++) {
            if (chargeArray[i] == maxChargeRegistered) {
                vehicleArray[i] = i;
            }
        }

        for (int i = 0; i < vehicleArray.length && !vehicleFinder; i++) {
            if (vehicleArray[i] != preventionVehicle) {
                preventionVehicle = vehicleArray[i];
                vehicleFinder = true;
            }
        }

        System.out.printf("%n%nj) veículo de prevenção no dia <%d> : V%d", PREVENTION_DAY, preventionVehicle);
    }


// ------ AUXILIARY METHODS ------

    //------ AUX -> METHOD TO BUILD A MATRIX ------
    /**
     * Constrói uma matriz a partir dos dados fornecidos pelo scanner.
     * [EN] Builds a matrix from the data provided by the scanner.
     *
     * @param scanner O scanner para ler os dados.
     *                [EN] The scanner to read the data.
     * @return A matriz construída.
     *         [EN] The constructed matrix.
     */
    public static double[][] matrixBuilder(Scanner scanner) {

        String text = scanner.nextLine();
        double quantityOfVehicles = scanner.nextDouble();
        double quantityOfDays = scanner.nextDouble();

        double[][] voltDeiMatrix;
        voltDeiMatrix = new double[(int) quantityOfVehicles][(int) (quantityOfDays + 1)];
        // +1 because the first column is the vehicle number

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }
        return voltDeiMatrix;
    }

    //------ AUX -> METHOD THAT CALCULATES THE HIGHEST NUMBER OF RECHARGES
    /**
     * Calcula o maior valor em uma matriz.
     * [EN] Calculates the largest value in a matrix.
     *
     * @param matrix A matriz de inteiros.
     *               [EN] The integer matrix.
     * @return O maior valor encontrado.
     *         [EN] The largest value found.
     */
    public static int maxValueCalculator(int[] matrix) {
        int maxValue = 0;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] > maxValue) {
                maxValue = matrix[i];
            }
        }

        return maxValue;
    }

    //------ AUX -> METHOD THAT CALCULATES THE LOWEST NUMBER OF RECHARGES
    /**
     * Calcula o menor valor em uma matriz.
     * [EN] Calculates the smallest value in a matrix.
     *
     * @param matrix A matriz de inteiros.
     *               [EN] The integer matrix.
     * @return O menor valor encontrado.
     *         [EN] The smallest value found.
     */
    public static int minValueCalculator(int[] matrix) {
        int minValue = Integer.MAX_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] < minValue) {
                minValue = matrix[i];
            }
        }

        return minValue;
    }

    //------ AUX -> METHOD THAT PRINTS ANY MATRIX ------
    /**
     * Imprime qualquer matriz fornecida. Diversos parâmetros servem para que o método consiga ser reutilizado por qualquer outro método que necessite imprimir uma tabela.
     * [EN] Prints any given matrix. Several parameters allow the method to be reused by other methods needing to print a table.
     *
     * @param matrix A matriz a ser impressa.
     *               [EN] The matrix to be printed.
     * @param format O formato de impressão.
     *               [EN] The print format.
     * @param barOnTop Se deve imprimir uma barra no topo.
     *                 [EN] Whether to print a bar on top.
     * @param dayMessage Se deve imprimir a mensagem do dia.
     *                   [EN] Whether to print the day message.
     * @param sufix O sufixo a ser adicionado aos valores.
     *              T[EN] he suffix to be added to the values.
     */
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
            matrix[i][0] = (i); // vehicle number (first column)

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