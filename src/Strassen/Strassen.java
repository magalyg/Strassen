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
            int n = rand.nextInt(5) + 1;
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
    //method to print the diagonal of a matrix;
    public static void  print_matrix_diagonal(int [][] A, int sz){

        for(int i =0; i<sz; i++){
            System.out.printf("%d, ", A[i][i]);
        }
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
                C[i][j]= A[i][j] - B[i][j];
            }
        }
        return C;
    }
    //to add zeros to handle odd numbers
    public  static int[][] addZeroes(int [][] matrix, int sz){
        int [][] newMatrix = new int[sz+1][sz+1];
        //matrix is automatically initialize with zeroes so we only have to move the things from smaller matrix
        for(int i = 0; i<sz; i++){
            for(int j = 0; j<sz; j++){
                newMatrix[i][j] = matrix[i][j];
            }
        }
        return  newMatrix;
    }

    public  static int[][] removeZeroes(int [][] even_matrix, int sz){
        int [][] odd_matrix = new int[sz+1][sz+1];
        //matrix is automatically initialize with zeroes so we only have to move the things from smaller matrix
        for(int i = 0; i<sz; i++){
            for(int j = 0; j<sz; j++){
                odd_matrix[i][j] = even_matrix[i][j];
            }
        }
        return  odd_matrix;
    }
    //new matrix divide
    public static int[][] matrix_divider(int [][] matrix, int sz, int row_start, int column_start){
        int[][] matrix_block= new int[sz/2][sz/2];
        for(int i = 0; i<sz/2; i++){
            for(int j=0;j<sz/2; j++){
                matrix_block[i][j] = matrix[i+row_start][j+column_start];
            }
        }
        return  matrix_block;
    }
    //Method to divide a matrix into four blocks
    public static ArrayList<int[][]> matrix_divide(int[][] matrix, int sz){
        int[][] blockA = new int[sz/2][sz/2];
        int[][] blockB = new int[sz/2][sz/2];
        int[][] blockC = new int[sz/2][sz/2];
        int[][] blockD = new int[sz/2][sz/2];

        for(int i = 0; i<sz/2; i++){
            for(int j=0;j<sz/2; j++){
                blockA[i][j] = matrix[i][j];
            }
        }

        for(int i = 0; i<sz/2; i++){
            for(int j=sz/2;j<sz; j++){
                blockB[i][j-sz/2] = matrix[i][j];
            }
        }
        for(int i = sz/2; i<sz; i++){
            for(int j=0;j<sz/2; j++){
                blockC[i-sz/2][j] = matrix[i][j];
            }
        }
        for(int i = sz/2; i<sz; i++){
            for(int j=sz/2;j<sz; j++){
                blockD[i-sz/2][j-sz/2] = matrix[i][j];
            }
        }

        ArrayList<int [][]> blocks = new ArrayList<>();
        blocks.add(0,blockA);
        blocks.add(1,blockB);
        blocks.add(2,blockC);
        blocks.add(3,blockD);
        return blocks;
    }
    //Strassen method to multiply two matrices
    public static int[][] strassen_multiply(int[][] A, int [][] B, int sz){
        int [][] D = new int[sz][sz];
        /*handle if sz is not even
        */
        //handle base case/ this can change when we fix the even size
        if(sz==1){
            D[0][0] = A[0][0]*B[0][0];
        }
        else {

            //Divide the matrices into two
            //ArrayList<int[][]> blocksA = matrix_divide(A, sz);
//            int[][] blockAa = blocksA.get(0);
//            int[][] blockAb = blocksA.get(1);
//            int[][] blockAc = blocksA.get(2);
//            int[][] blockAd = blocksA.get(3);

            int[][] blockAa = matrix_divider(A,sz, 0, 0);
            int[][] blockAb = matrix_divider(A,sz, 0, sz/2);
            int[][] blockAc = matrix_divider(A,sz, sz/2, 0);
            int[][] blockAd = matrix_divider(A,sz, sz/2, sz/2);

//            ArrayList<int[][]> blocksB = matrix_divide(B, sz);
//            int[][] blockBa = blocksB.get(0);
//            int[][] blockBb = blocksB.get(1);
//            int[][] blockBc = blocksB.get(2);
//            int[][] blockBd = blocksB.get(3);
            int[][] blockBa = matrix_divider(B,sz, 0, 0);
            int[][] blockBb = matrix_divider(B,sz, 0, sz/2);
            int[][] blockBc = matrix_divider(B,sz, sz/2, 0);
            int[][] blockBd = matrix_divider(B,sz, sz/2, sz/2);

            //store the multiplication results and
            int[][] block_mult1 = strassen_multiply(add(blockAa, blockAd, sz / 2), add(blockBa, blockBd, sz / 2), sz / 2);
            int[][] block_mult2 = strassen_multiply(add(blockAc, blockAd, sz / 2), blockBa, sz / 2);
            int[][] block_mult3 = strassen_multiply(blockAa, subtract(blockBb, blockBd, sz / 2), sz / 2);
            int[][] block_mult4 = strassen_multiply(blockAd, subtract(blockBc, blockBa, sz / 2), sz / 2);
            int[][] block_mult5 = strassen_multiply(add(blockAa, blockAb, sz / 2), blockBd, sz / 2);
            int[][] block_mult6 = strassen_multiply(subtract(blockAc, blockAa, sz / 2), add(blockBa, blockBb, sz / 2), sz / 2);
            int[][] block_mult7 = strassen_multiply(subtract(blockAb, blockAd, sz / 2), add(blockBc, blockBd, sz / 2), sz / 2);

            //Construct D blocks
            int[][] blockDa = add(subtract(add(block_mult1,block_mult4,sz/2),block_mult5,sz/2),block_mult7,sz/2);
            int[][] blockDb = add(block_mult3, block_mult5, sz / 2);
            int[][] blockDc = add(block_mult2, block_mult4, sz / 2);
            int[][] blockDd = add(subtract(add(block_mult1,block_mult3,sz/2),block_mult2,sz/2),block_mult6,sz/2);
            //place the blocks back together into D

            for (int i = 0; i < sz / 2; i++) {
                for (int j = 0; j < sz / 2; j++) {
                    D[i][j] = blockDa[i][j];
                }
            }

            for (int i = 0; i < sz / 2; i++) {
                for (int j = sz / 2; j < sz; j++) {
                    D[i][j] = blockDb[i][j - sz / 2];
                }
            }
            for (int i = sz / 2; i < sz; i++) {
                for (int j = 0; j < sz / 2; j++) {
                    D[i][j] = blockDc[i - sz / 2][j];
                }
            }
            for (int i = sz / 2; i < sz; i++) {
                for (int j = sz / 2; j < sz; j++) {
                    D[i][j] = blockDd[i - sz / 2][j - sz / 2];
                }
            }
        }

        return D;
    }
    public static void print_block(int[][] D, int sz){
        for(int i = 0; i<sz; i++){
            for(int j =0; j<sz; j++){
                System.out.printf(" %d",D[i][j]);
            }
            System.out.print("\n");
        }
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
        long start_time_regular =  System.nanoTime();
        int [][] matrix_C = regular_multiply(matrix_A, matrix_B, sz);
        long end_time_regular =  System.nanoTime();
        long start_time_strassen = System.nanoTime();
        int [][] matrix_D = strassen_multiply(matrix_A,matrix_B,sz);
        long end_time_strassen = System.nanoTime();
        //Print Matrix for debugging purposes
        System.out.println("Regular matrix result matrix result");
//        for(int i = 0; i<sz; i++){
//            for(int j =0; j<sz; j++){
//                System.out.printf(" %d",matrix_C[i][j]);
//            }
//            System.out.print("\n");
//        }
        print_matrix_diagonal(matrix_C,sz);
        System.out.printf("Time regular multiplication %f \n",  (end_time_regular-start_time_regular)/1000000000.0);
        //Print Matrix for debugging purposes
        System.out.println("Strassen matrix result");
//        for(int i = 0; i<sz; i++){
//            for(int j =0; j<sz; j++){
//                System.out.printf(" %d",matrix_D[i][j]);
//            }
//            System.out.print("\n");
//        }
        print_matrix_diagonal(matrix_D,sz);
        System.out.printf("Time Strassen multiplication %f \n",  (end_time_strassen-start_time_strassen)/1000000000.0);

    }
}
