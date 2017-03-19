package Strassen;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Magaly on 3/19/2017.
 */
public class Strassen {
    private final static int FREE_VAR = 0, DIMENSION = 1, NAME_FILE = 2;

    ///generates enough random numbers to fill out start matrices and puts them on the
    // specified file

    public static void ints_to_file(int sz, String filename) throws FileNotFoundException{
        PrintWriter tofile = new PrintWriter(filename +".txt");
        int number_ints = 2*sz*sz;
        for(int i=0; i<number_ints; i++) {
            Random rand = new Random();
            int n = rand.nextInt(10) + 1;
            tofile.write(n + "\n");
        }
        tofile.close();
    }
    //method to generate matrices from file
    public static ArrayList<int [][]> fileToMatrices(String file, int sz) throws FileNotFoundException {
        int [][] A = new int [sz][sz];
        int [][] B = new int[sz][sz];
        ArrayList<int[][]> matrices = new ArrayList<>();
        Scanner in = new Scanner(new FileReader(file + ".txt"));

        //fills in first matrix
        for(int i = 0; i<sz; i++){
            for(int j = 0; j<sz; j++){
                A[i][j] = Integer.valueOf(in.next());
            }
        }
        matrices.add(0,A);
        //fills in second matrix
        for(int i = 0; i<sz; i++){
            for(int j = 0; j<sz; j++){
                B[i][j] = Integer.valueOf(in.next());
            }
        }
        matrices.add(1,B);
        in.close();

        return matrices;
    }
    //Method to mutiply two matrices
    public static int[][] regular_multiply(int[][] A, int [][] B, int sz){
        int [][] C = new int[sz][sz];

        for(int i= 0; i<sz; i++){
            for(int j=0; j<sz; j++){
                for (int k=0; k<sz; k++) {
                    C[i][j] += (A[i][k]*B[k][j]);
                }
            }
        }
        return C;
    }
    //addition/subtraction functions
    public static int[][] add(int[][] A, int [][] B, int sz){
        int[][] C = new int [sz][sz];
        for(int i=0; i<sz; i++){
            for(int j=0; j<sz; j++){
                C[i][j]= A[i][j] + B[i][j];
            }
        }

        return C;
    }

    public static int [][] subtract(int[][] A, int [][] B, int sz){
        int[][] C = new int [sz][sz];
        for(int i=0; i<sz; i++){
            for(int j=0; j<sz; j++){
                C[i][j]= A[i][j] + B[i][j];
            }
        }
        return C;
    }
    //Method to divide a matrix into four blocks
    public static ArrayList<int[][]> matrix_divide(int[][] matrix, int sz){
        int[][] blockA = new int[sz/2][sz/2];
        int[][] blockB = new int[sz/2][sz/2];
        int[][] blockC = new int[sz/2][sz/2];
        int[][] blockD = new int[sz/2][sz/2];



        ArrayList<int [][]> blocks = new ArrayList<>();
        blocks.add(0,blockA);
        blocks.add(1,blockB);
        blocks.add(2,blockC);
        blocks.add(3,blockD);
        return blocks;
    }
    //Strassen method to multiply two matrices
    public static int[][] strassen_multiply(int[][] A, int [][] B, int sz){
        //handle if sz is not even


        int [][] C = new int[sz][sz];
        return C;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Revise number of arguments

        if (args.length != 3) {
            System.out.println("Invalid number of arguments (./strassen 0 dimension filename)");
            return;
        }
        //Get argument values
        final Integer sz = Integer.valueOf(args[DIMENSION]);
        final String filename = args[NAME_FILE];
        //just making sure string holds name of file
        System.out.println(filename);
        ints_to_file(sz,filename);

        ArrayList<int[][]> matrices = fileToMatrices(filename, sz);
        int[][] matrix_A = matrices.get(0);
        int[][] matrix_B = matrices.get(1);
        //Regular matrix multiplication
        int [][] matrix_C = regular_multiply(matrix_A, matrix_B, sz);

        //Print Matrix for debugging purposes
        for(int i = 0; i<sz; i++){
            for(int j =0; j<sz; j++){
                System.out.printf(" %d",matrix_C[i][j]);
            }
            System.out.print("\n");
        }


    }
}
