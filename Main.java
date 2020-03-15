import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main
{
    public static List<String> argsL = null;
    public static final String RECURSION_FLAG = "-r";

    public static final int STARTING_TIME_INDEX = 0;
    public static final int DURATION_INDEX = 1;
    public static final int PRICE_INDEX = 2;


    public static void main(String[] args) 
    {
        argsL = Arrays.asList(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int test_cases = Integer.parseInt(in.readLine());
        List<Integer> results = new LinkedList<Integer>();
        for(int t = 0; t < test_cases; t++)
        {
            int numberOffers = Integer.parseInt(in.readLine());
            int[][] offers = new int[numberOffers][3];
            for(int[] o : offers)
            {
                String[] s = in.readLine().split(" ");
                offers[t][STARTING_TIME_INDEX] = Integer.parseInt(s[STARTING_TIME_INDEX]);
                offers[t][DURATION_INDEX] = Integer.parseInt(s[DURATION_INDEX]);
                offers[t][PRICE_INDEX] = Integer.parseInt(s[PRICE_INDEX]);
            }
            // results.add(1);
        }
    }   

    public static int drinaProfitR(int[][] offers, int currentOffer)
    {
        int endingTime = offers[currentOffer][STARTING_TIME_INDEX] + offers[currentOffer][DURATION_INDEX];
        for(int i = 0; i < offers.length; i++)
        {
            if (i != currentOffer)
            {
                
            }
        }
    }

    public static int drinaProfitDP(int[][][][] offers)
    {
        
    }
}