package Strassen;
import java.io.PrintWriter;
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
    //method to print the diagonal of a matrix one number per line
    public static void  print_matrix_diagonal(int [][] A, int sz){

        for(int i =0; i<sz; i++){
            System.out.printf("%d \n", A[i][i]);
        }
    }
    //Method to multiply two matrices
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

    //function to use when matrices are not a power of 2
    public static int [][] resize_matrix(int[][] A, int sz){
        int new_size= (int) Math.pow(2,Math.ceil(Math.log(sz)/Math.log(2)));

        if(new_size==sz) {
            return A;
        }
        else{
            int[][] newA= new int[new_size][new_size];
                for(int i=0; i<sz; i++)
                    for (int j=0; j<sz; j++)
                        newA[i][j]= A[i][j];
            return newA;
        }
    }
    //strassen function to handle all sizes
    public static int[][] strassen_all(int[][] Ap, int [][] Bp, int sz, int crossover){
        int[][] A = resize_matrix(Ap, sz);
        int[][] B = resize_matrix(Bp, sz);
        int[][] D = strassen_multiply(A,B,B.length, crossover);
        if(D.length== sz){
            return D;
        }
        else{
            int[][]new_D= new int [sz][sz];
            for(int i=0; i<sz; i++)
                for(int j=0; j<sz; j++)
                    new_D[i][j] = D[i][j];
            return new_D;
        }
    }
    //Strassen method to multiply two matrices that are powers of two
    public static int[][] strassen_multiply(int[][] A, int [][] B, int sz, int crossover){
        //change the size to next multiple of two
        int[][] D = new int[sz][sz];
        if(sz==1){
            D[0][0] = A[0][0]*B[0][0];
        }
        else if(sz<crossover){
            D= regular_multiply(A,B,sz);
        }
        else {
            int[][] blockAa = matrix_divider(A,sz, 0, 0);
            int[][] blockAb = matrix_divider(A,sz, 0, sz/2);
            int[][] blockAc = matrix_divider(A,sz, sz/2, 0);
            int[][] blockAd = matrix_divider(A,sz, sz/2, sz/2);

            int[][] blockBa = matrix_divider(B,sz, 0, 0);
            int[][] blockBb = matrix_divider(B,sz, 0, sz/2);
            int[][] blockBc = matrix_divider(B,sz, sz/2, 0);
            int[][] blockBd = matrix_divider(B,sz, sz/2, sz/2);

            //store the multiplication results and
            int[][] block_mult1 =
                    strassen_multiply(add(blockAa, blockAd, sz / 2), add(blockBa, blockBd, sz / 2), sz / 2, crossover);
            int[][] block_mult2 =
                    strassen_multiply(add(blockAc, blockAd, sz / 2), blockBa, sz / 2, crossover);
            int[][] block_mult3 =
                    strassen_multiply(blockAa, subtract(blockBb, blockBd, sz / 2), sz / 2,crossover);
            int[][] block_mult4 =
                    strassen_multiply(blockAd, subtract(blockBc, blockBa, sz / 2), sz / 2,crossover);
            int[][] block_mult5 =
                    strassen_multiply(add(blockAa, blockAb, sz / 2), blockBd, sz / 2,crossover);
            int[][] block_mult6 =
                    strassen_multiply(subtract(blockAc, blockAa, sz / 2), add(blockBa, blockBb, sz / 2), sz / 2,crossover);
            int[][] block_mult7 =
                    strassen_multiply(subtract(blockAb, blockAd, sz / 2), add(blockBc, blockBd, sz / 2), sz / 2,crossover);

            //Construct D blocks
            int[][] blockDa =
                    add(subtract(add(block_mult1,block_mult4,sz/2),block_mult5,sz/2),block_mult7,sz/2);
            int[][] blockDb =
                    add(block_mult3, block_mult5, sz / 2);
            int[][] blockDc =
                    add(block_mult2, block_mult4, sz / 2);
            int[][] blockDd =
                    add(subtract(add(block_mult1,block_mult3,sz/2),block_mult2,sz/2),block_mult6,sz/2);
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
    //returns an array of X numbers between 1 and sz for testing
    public static int[] testing_array(int num_tests, int max_num){
        int [] test = new int[num_tests];
        test[0] = 1;
        test[num_tests-1] = max_num;
        int factor = (int)Math.floor(max_num/num_tests);
        for(int i =1; i<num_tests-1; i++){
            test[i] = test[i-1] + factor;
        }
        return test;
    }
    public static void main(String[] args) throws FileNotFoundException {
        //Revise number of arguments

        if (args.length != 3) {
            System.out.println("Invalid number of arguments (./strassen 0 max_dimension filename)");
            return;
        }
        //Get argument values
        final Integer sz = Integer.valueOf(args[DIMENSION]);
        final String filename = args[NAME_FILE];
        ints_to_file(sz,filename);
        ArrayList<int[][]> matrices = fileToMatrices(filename, sz);
        int[][] matrix_A = matrices.get(0);
        int[][] matrix_B = matrices.get(1);
        //generate an array of X sizes for testing

        int [] arrray_test = testing_array(10,sz);
        //Regular matrix multiplication
        long start_time_regular =  System.nanoTime();
        int [][] matrix_C = regular_multiply(matrix_A, matrix_B, sz);
        long end_time_regular =  System.nanoTime();
        double regular_time = (end_time_regular-start_time_regular)/1000000000.0;
        System.out.println("Regular matrix result matrix result");
        System.out.printf("Time regular multiplication %f \n",regular_time );
        //testing for strassen
        double time_strassen = Double.MAX_VALUE;
        int i=1;
        int[][] matrix_D;
        while (time_strassen>regular_time) {
            long start_time_strassen = System.nanoTime();
            matrix_D = strassen_all(matrix_A, matrix_B, sz, i);
            long end_time_strassen = System.nanoTime();
//            print_matrix_diagonal(matrix_D,sz);
            time_strassen =(end_time_strassen-start_time_strassen)/1000000000.0;
            System.out.printf("Time Strassen multiplication %d %f \n", i,
                    time_strassen);
            i++;
        }
        System.out.printf("Time Strassen multiplication %d %f \n", i,
                time_strassen);
        //Print Matrix for debugging purposes
        System.out.println("Regular matrix result matrix result");
       // print_matrix_diagonal(matrix_C,sz);
        System.out.printf("Time regular multiplication %f \n",  (end_time_regular-start_time_regular)/1000000000.0);
        //Print Matrix for debugging purposes
        System.out.println("Strassen matrix result");
//        print_matrix_diagonal(matrix_D,sz);
//        System.out.printf("Time Strassen multiplication %f \n",  (end_time_strassen-start_time_strassen)/1000000000.0);

    }
}
