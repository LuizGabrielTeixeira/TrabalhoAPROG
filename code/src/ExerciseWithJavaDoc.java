import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Classe ExerciseJavaDoc que demonstra o uso de recargas de veículos elétricos,
 * analisando dados de uso armazenados num arquivo de entrada.
 */
public class ExerciseWithJavaDoc {

    /**
     * Valor constante representando a carga total da bateria (em percentage).
     */
    static final int FULL_BATTERY = 100;

    /**
     * Dia específico usado para realizar análise de prevenção no exercício J.
     */
    static final int PREVENTION_DAY = 4; // dia que o usuário escolhe para o exercício J

    /**
     * Número de colunas extras na matriz (uma coluna extra para armazenar o número do veículo).
     */
    static final int EXTRA_COLUMN = 1; // porque a primeira coluna é o número do veículo

    /**
     * Custo por recarga da bateria (em unidades monetárias).
     */
    static final double COST_PER_RECHARGE = 5.5;

    /**
     * Método principal que lê um arquivo de entrada e chama diferentes métodos *ExerciseJavaDoc*
     * para analisar e processar os dados de recarga dos veículos.
     *
     * @param args Argumentos passados pela linha de comando (não utilizados neste contexto).
     */
    public static void main(String[] args) {
        File file = new File("input.txt");
        Scanner scanner;
        double[][] voltDeiMatrix;

        try {
            scanner = new Scanner(file);

            try {
                voltDeiMatrix = matrixBuilder(scanner);
                // cria a matriz, com a primeira coluna sendo o número do veículo
            } catch (NoSuchElementException e) {
                System.out.println("Erro na leitura do ficheiro, algo está mal formatado!");
                // lança um erro se a entrada estiver mal formatada
                scanner.close();
                return;
            }

        } catch (FileNotFoundException e) {
            System.out.printf("O seguinte ficheiro não foi encontrado: '%s'.\n", file);
            // lança um erro se o arquivo não for encontrado
            return;
        }

        planningMatrix(voltDeiMatrix); // a)
        totalTraveledDistance(voltDeiMatrix); // b)
        double[][] batteryRechargeMatrix = batteryRecharge(voltDeiMatrix); // c) imprime e retorna a matriz
        double[][] dailyChargeArray = dailyCharge(voltDeiMatrix); // d) imprime e retorna a matriz
        double[][] averageKmMatrix = averageDayCarsKm(voltDeiMatrix); // e) imprime e retorna a matriz
        vehiclesWithAnHigherAverage(voltDeiMatrix, averageKmMatrix); // f)
        vehiclesConsecutiveRecharges(batteryRechargeMatrix); // g)
        latestDayWithMoreCharges(voltDeiMatrix, dailyChargeArray); // h)
        rechargesCost(batteryRechargeMatrix); // i)
        preventionVehicle(voltDeiMatrix, dailyChargeArray, PREVENTION_DAY); // j)

        scanner.close();
    }

    /**
     * Imprime a matriz de planeamento para os veículos, mostrando o deslocamento em km por dia por veículo.
     *
     * @param voltDeiMatrix Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                      e as colunas representam os quilómetros percorridos em cada dia.
     */
    public static void planningMatrix(double[][] voltDeiMatrix) {
        System.out.println("a) planeamento (km/dia/veículo)");

        final int FORMAT = 1;
        printMatrix(voltDeiMatrix, FORMAT, true, true, "");
    }

    /**
     * Calcula e imprime a distância total percorrida por cada veículo.
     *
     * @param voltDeiMatrix Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                      e as colunas representam os quilómetros percorridos em cada dia.
     */
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

    /**
     * Calcula a quantidade de recargas de bateria necessárias para cada veículo com base na matriz de dados.
     *
     * @param voltDeiMatrix Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                      e as colunas representam os quilómetros percorridos em cada dia.
     * @return Uma matriz contendo o número de recargas necessárias para cada veículo em cada dia.
     */
    public static double[][] batteryRecharge(double[][] voltDeiMatrix) {
        System.out.println("\nc) recargas das baterias");

        final int FORMAT = 1;
        int rechargeCounter;
        int batteryInicialCharge;

        double[][] batteryRechargeMatrix = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        // mesma dimensão que a voltDeiMatrix, com a primeira coluna sendo o número do veículo

        double[][] voltDeiMatrixCopy = new double[voltDeiMatrix.length][voltDeiMatrix[0].length];
        // copia a matriz para não alterar a original

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

    /**
     * Calcula e imprime a carga de bateria necessária para cada veículo diariamente.
     *
     * @param voltDeiMatrix Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                      e as colunas representam os quilómetros percorridos em cada dia.
     * @return Uma matriz contendo a carga da bateria necessária para cada veículo a cada dia.
     */
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

    /**
     * Calcula a média diária de quilómetros percorridos por toda a frota de veículos.
     *
     * @param voltDeiMatrix Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                      e as colunas representam os quilómetros percorridos em cada dia.
     * @return Uma matriz contendo a média diária de quilómetros percorridos por cada veículo.
     */
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

    /**
     * Identifica e imprime os veículos que sempre tiveram deslocamentos acima da média diária.
     *
     * @param voltDeiMatrix   Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                        e as colunas representam os quilómetros percorridos em cada dia.
     * @param averageKmMatrix Matriz contendo a média diária de quilómetros percorridos por toda a frota.
     */
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

    /**
     * Identifica e imprime os veículos que precisaram de recargas consecutivas por mais dias
     * e o número máximo de dias consecutivos que esses veículos precisaram de recarga.
     *
     * @param batteryRechargeMatrix Matriz contendo os dados de recargas, onde cada linha representa
     *                              um veículo e as colunas indicam quantas recargas foram necessárias em cada dia.
     */
    public static void vehiclesConsecutiveRecharges(double[][] batteryRechargeMatrix) {
        System.out.print("\ng) veículos com mais dias consecutivas a necessitar de recarga :");

        boolean consecutiveRecharge;
        int actualConsecutiveDaysRecharge;
        int[] maxConsecutiveDaysPerVehicle = new int[batteryRechargeMatrix.length];
        int maxConsecutiveDaysRecharge = 0;

        for (int i = 0; i < batteryRechargeMatrix.length; i++) {
            consecutiveRecharge = false;
            actualConsecutiveDaysRecharge = 0;
            maxConsecutiveDaysRecharge = 0;

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
                        maxConsecutiveDaysPerVehicle[i] = maxConsecutiveDaysRecharge;
                    }

                    actualConsecutiveDaysRecharge = 0;
                }
            }

            if (actualConsecutiveDaysRecharge > maxConsecutiveDaysRecharge) {
                maxConsecutiveDaysRecharge = actualConsecutiveDaysRecharge;
                maxConsecutiveDaysPerVehicle[i] = maxConsecutiveDaysRecharge;
            }
        }

        if (maxConsecutiveDaysRecharge == 0) {
            System.out.print("\n- nenhum veiculo precisou de recarga!\n");
        } else {
            maxConsecutiveDaysRecharge = maxConsecutiveDaysCalculator(maxConsecutiveDaysPerVehicle);

            System.out.printf("\n<%d> dias consecutivos, veículos :", maxConsecutiveDaysRecharge);

            for (int i = 0; i < maxConsecutiveDaysPerVehicle.length; i++) {
                if (maxConsecutiveDaysPerVehicle[i] == maxConsecutiveDaysRecharge) {
                    System.out.printf(" [V%d] ", i);
                }
            }
            System.out.println();
        }
    }

    /**
     * Determina o dia mais tardio em que todos os veículos precisaram de recarga.
     *
     * @param voltDeiMatrix    Matriz contendo os dados dos veículos.
     * @param dailyChargeArray Matriz contendo as cargas diárias necessárias para cada veículo.
     */
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

    /**
     * Calcula e imprime o custo total das recargas da frota de veículos.
     *
     * @param batteryRechargeMatrix Matriz contendo os dados de recargas, onde cada linha representa
     *                              um veículo e as colunas indicam quantas recargas foram necessárias em cada dia.
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

    /**
     * Determina o veículo de prevenção para um dia específico, levando em consideração
     * os quilómetros percorridos e a carga da bateria.
     *
     * @param voltDeiMatrix    Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     *                         e as colunas representam os quilómetros percorridos em cada dia.
     * @param dailyChargeArray Matriz contendo as cargas diárias necessárias para cada veículo.
     * @param preventionDay    Dia específico para o qual o veículo de prevenção será determinado.
     */
    public static void preventionVehicle(double[][] voltDeiMatrix, double[][] dailyChargeArray, int preventionDay) {
        int carCharge;
        int carKilometers;
        int lastCarCharge = Integer.MIN_VALUE;
        int lastCarKilometers = Integer.MAX_VALUE;
        int preventionVehicle = 0;

        for (int rows = 0; rows < voltDeiMatrix.length; rows++) {
            carKilometers = (int) voltDeiMatrix[rows][preventionDay + EXTRA_COLUMN];
            carCharge = (int) dailyChargeArray[rows][preventionDay + EXTRA_COLUMN];

            if (lastCarKilometers > carKilometers) {
                lastCarKilometers = carKilometers;
                preventionVehicle = rows;
            } else if (lastCarKilometers == carKilometers) {
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

    /**
     * Constrói uma matriz a partir dos dados fornecidos por um Scanner.
     *
     * @param scanner Scanner contendo os dados para construção da matriz.
     * @return Matriz contendo os dados dos veículos, onde cada linha representa um veículo
     * e as colunas representam os quilómetros percorridos em cada dia.
     */
    public static double[][] matrixBuilder(Scanner scanner) {
        String text = scanner.nextLine();
        double quantityOfVehicles = scanner.nextDouble();
        double quantityOfDays = scanner.nextDouble();

        double[][] voltDeiMatrix;
        voltDeiMatrix = new double[(int) quantityOfVehicles][(int) (quantityOfDays + 1)];
        // +1 porque a primeira coluna é o número do veículo

        for (int i = 0; i < voltDeiMatrix.length; i++) {
            for (int j = 1; j < voltDeiMatrix[i].length; j++) {
                voltDeiMatrix[i][j] = scanner.nextInt();
            }
        }
        return voltDeiMatrix;
    }

    /**
     * Calcula o maior número de dias consecutivos em que um veículo precisou de recarga.
     *
     * @param maxConsecutiveDaysPerVehicle Array contendo o número máximo de dias consecutivos de recarga por veículo.
     * @return O maior número de dias consecutivos de recarga entre todos os veículos.
     */
    public static int maxConsecutiveDaysCalculator(int[] maxConsecutiveDaysPerVehicle) {
        int maxConsecutiveDaysRecharge = 0;

        for (int i = 0; i < maxConsecutiveDaysPerVehicle.length; i++) {
            if (maxConsecutiveDaysPerVehicle[i] > maxConsecutiveDaysRecharge) {
                maxConsecutiveDaysRecharge = maxConsecutiveDaysPerVehicle[i];
            }
        }

        return maxConsecutiveDaysRecharge;
    }

    /**
     * Imprime uma matriz conforme o formato especificado.
     *
     * @param matrix     Matriz a ser impressa.
     * @param format     Formato da impressão:
     *                   1 - número inteiro,
     *                   2 - percentage com uma casa decimal,
     *                   3 - média com uma casa decimal.
     * @param barOnTop   Se deve imprimir uma barra no topo da matriz.
     * @param dayMessage Se deve imprimir os dias como cabeçalho.
     * @param sufix      Sufixo a ser adicionado aos valores impressos.
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
            matrix[i][0] = (i); // número do veículo (primeira coluna)

            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0 && format == 3) {
                    String text = "km";
                    System.out.printf("%-3s :", text);
                } else if (j == 0) {
                    System.out.printf("V%-3d:", (int) matrix[i][j]);
                } else {
                    switch (format) {
                        case 1:
                            // imprime apenas o número inteiro
                            System.out.printf("%8d %s", (int) matrix[i][j], sufix);
                            break;
                        case 2:
                            // imprime a percentage com uma casa decimal
                            System.out.printf("%7.1f%% %s", matrix[i][j], sufix);
                            break;
                        case 3:
                            // imprime a média com uma casa decimal
                            System.out.printf("%8.1f %s", matrix[i][j], sufix);
                            break;
                    }
                }
            }
            System.out.println();
        }
    }
}

