import java.util.Scanner;

public class DB_1230350_1241456 {
    public static void main(String[] args) {
        planningMatrix();
    }

//a)
public static void planningMatrix (){
        Scanner scanner = new Scanner(System.in);

        int quantityOfVehicles = scanner.nextInt();
        int quantityOfDays = scanner.nextInt();

        int[][] voltDeiMatrix;
        voltDeiMatrix = new int[quantityOfVehicles][quantityOfDays+1];


    System.out.print("dia:");
    for (int i = 0; i < quantityOfDays; i++) {
        System.out.printf("%2d|", i);
    }
    System.out.println();

    for (int i = 0; i < voltDeiMatrix.length; i++) {
        voltDeiMatrix[i][0] = (i);
        for (int j = 0; j < voltDeiMatrix[i].length ; j++) {

            System.out.printf("%2d ", voltDeiMatrix[i][j]);
        }
        System.out.println();
    }

    System.out.println(voltDeiMatrix);
}

//b)
public static void totalTraveledDistance (){

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
