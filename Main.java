import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main
{
    public static List<String> argsL = null;
    public static final String RECURSION_FLAG = "-r", 
    TEST_INPUT_FLAG = "-t", DURATION_FLAG = "-d", PRINT_MATRIX_FLAG = "-pm";


    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        argsL = Arrays.asList(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        if (argsL.contains(TEST_INPUT_FLAG))
        {
            in = getTestInputReader(in);
            startTime = System.currentTimeMillis();
        }
        int test_cases = Integer.parseInt(in.readLine());
        List<Integer> results = new LinkedList<Integer>();
        for(int t = 0; t < test_cases; t++)
        {
            int numberOffers = Integer.parseInt(in.readLine());
            List<Offer> offers = new ArrayList<Offer>(numberOffers);
            for(int i = 0; i < numberOffers; i++)
            {
                String[] s = in.readLine().split(" ");
                offers.add(new Offer(Integer.parseInt(s[0]),
                                     Integer.parseInt(s[1]),
                                     Integer.parseInt(s[2])));
            }
            int result;
            if (argsL.contains(RECURSION_FLAG))
                result = new Drina(offers).solveR();
            else
                result = new Drina(offers).solveDP();
            results.add(result);
        }
        for(int r :results)
            System.out.println(r);
        long endTime = System.currentTimeMillis();
        if(argsL.contains(DURATION_FLAG))
            System.out.println("Duration: " + (endTime - startTime) + " ms");
    }   

    public static class Drina
    {
        private final List<Offer> offers;
        private int[] array;

        public Drina(List<Offer> offers)
        {
            this.offers = offers;
        }

        public int solveDP()
        {
            throw new Error("Not implemented");
        }

        public int solveR()
        {
            int result = Integer.MIN_VALUE;
            for (int i = 0; i < offers.size(); i++)
            {
                int r = solveRS(i);
                result = r > result ? r : result;
            }
            return result;
        }

        private int solveRS(int o)
        {   
            int maxValue = Integer.MIN_VALUE;
            Offer f = offers.get(o);
            for(int i = 0; i < offers.size(); i++)
            {
                int result = offers.get(o).price;
                if (f.startingTime + f.duration <= offers.get(i).startingTime && i != o)
                    result += solveRS(i);
                maxValue = result > maxValue ? result : maxValue;
            }
            return maxValue;
        }
    }

    public static class Offer
    {
        public final int duration;
        public final int startingTime;
        public final int price;

        public Offer(int startingTime, int duration, int price)
        {
            this.startingTime = startingTime;
            this.duration = duration;
            this.price = price;
        }
    }


    private static BufferedReader getTestInputReader(BufferedReader in) throws Exception
    {
        System.out.print("Insert test filename: ");
        String file = in.readLine();
        in.close();
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }
}