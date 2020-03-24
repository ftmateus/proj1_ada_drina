import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class Main
{

    private static final int MAX_TEST_CASES = 100;
    private static final int MAX_OFFERS = 10000;
    private static final int MAX_STARTING_INSTANT = 1000000000;
    private static final int MAX_DURATION = 2*10000;
    private static final int MAX_PRICE = 10000;

    public static List<String> argsL = null;
    public static final String RECURSION_FLAG = "-r", 
    TEST_INPUT_FLAG = "-t", DURATION_FLAG = "-d", PRINT_MATRIX_FLAG = "-pm",
    GENERATE_FILETEST_FLAG = "-g";


    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        argsL = Arrays.asList(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        if (argsL.contains(GENERATE_FILETEST_FLAG))
        {
            generateTestFile(in);
            in.close();
            return;
        }
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
            SortedMap<Integer, Offer> offers = new TreeMap<Integer, Offer>();
            for(int i = 0; i < numberOffers; i++)
            {
                String[] s = in.readLine().split(" ");
                int start = Integer.parseInt(s[0]);
                offers.put(start,
                new Offer(start,
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

    public static void generateTestFile(BufferedReader in) throws IOException
    {
        System.out.print("Test cases:");
        int test_cases = Integer.parseInt(in.readLine());
        System.out.print("Max offers: ");
        int max_offers = Integer.parseInt(in.readLine());
        System.out.print("Max start time: ");
        int max_start = Integer.parseInt(in.readLine());
        System.out.print("Max duration: ");
        int max_duration = Integer.parseInt(in.readLine());
        System.out.print("Max price: ");
        int max_price = Integer.parseInt(in.readLine());
        File f = new File("test" + Double.valueOf(1000*Math.random()).intValue() + ".txt");
        f.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        
        System.out.println(test_cases);
        writer.write(test_cases + "\n");
        for(int i = 0; i < test_cases; i++)
        {
            int offers = Double.valueOf(Math.random()*(max_offers - 1) + 1).intValue();
            writer.write(offers + "\n");
            for(int j = 0; j  < offers; j++)
            {
                int start = Double.valueOf(Math.random()*max_start).intValue();
                int duration = Double.valueOf(Math.random()*(max_duration - 1) + 1).intValue();
                int price = Double.valueOf(Math.random()*(max_price - 1) + 1).intValue();
                writer.write(String.format("%d %d %d\n", start, duration, price));
            }
        }
        writer.close();
        System.out.println("File generated");
    }


    public static class Drina
    {
        private final List<Offer> offers;
        private int[] array;

        public Drina(SortedMap<Integer, Offer> offers)
        {
            this.offers = new ArrayList<Offer>(offers.values());
            this.array = null;
        }

        public void buildArray()
        {
            array = new int[offers.size() + 1];
            for(int i = 0; i < array.length; i++)
                array[i] = 0;
        }

        // public int solveDP()
        // {
        //     int result = Integer.MIN_VALUE;
        //     buildArray();
        //     for(int i = 0; i < offers.size(); i++)
        //     {
        //         int maxValue = Integer.MIN_VALUE;
        //         Offer f = offers.get(i);
        //         for(int j = 0; j < offers.size(); j++)
        //         {
        //             int r = offers.get(i).price;
        //             if (f.startingTime + f.duration <= offers.get(j).startingTime && i != j)
        //             {
        //                 result += array[j];
        //             } 
        //             maxValue = r > maxValue ? r : maxValue;
        //         }
        //         array[i] = maxValue;
        //         result = array[i] > result ? array[i] : result;
        //     }
        //     return result;
        // }

        // public int solveR()
        // {
        //     int result = Integer.MIN_VALUE;
        //     for (int i = 0; i < offers.size(); i++)
        //     {
        //         int r = solveRS(i);
        //         result = r > result ? r : result;
        //     }
        //     return result;
        // }

        // private int solveRS(int o)
        // {   
        //     int maxValue = Integer.MIN_VALUE;
        //     Offer f = offers.get(o);
        //     for(int i = 0; i < offers.size(); i++)
        //     {
        //         int result = f.price;
        //         if (f.startingTime + f.duration <= offers.get(i).startingTime && i != o)
        //             result += solveRS(i);
        //         maxValue = result > maxValue ? result : maxValue;
        //     }
        //     return maxValue;
        // }

        public int solveDP()
        {
            throw new Error();
        }

        public int solveR()
        {
            return solveRS(offers.size());
        }

        public int solveRS(int i)
        {
            if (i == 0)
                return 0;
            if (compatible(i - 1, i))
                return offers.get(i - 1).price + solveRS(i - 1);
            else
            {
                int max = 0;
                for(int k = i - Math.min(i - 1, 2); k > 0; k--)
                {
                    int r = offers.get(i - 1).price + (compatible(k, i) ? solveRS(k) : 0);
                    max = r > max ? r : max;
                }
                return max;
            }
        }

        private boolean compatible(int i, int j)
        {
            return i == 0 || (offers.get(i - 1).startingTime + offers.get(i - 1).duration <= offers.get(j - 1).startingTime);
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