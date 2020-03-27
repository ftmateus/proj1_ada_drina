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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        List<Long> results = new LinkedList<Long>();
        for(int t = 0; t < test_cases; t++)
        {
            int numberOffers = Integer.parseInt(in.readLine());
            SortedSet<Offer> offers = new TreeSet<Offer>(new offerCompEndTime());
            for(int i = 0; i < numberOffers; i++)
            {
                String[] s = in.readLine().split(" ");
                int start = Integer.parseInt(s[0]);
                int duration = Integer.parseInt(s[1]);
                offers.add(
                        new Offer(start
                        , duration,
                        Integer.parseInt(s[2])));
            }
            long result;
            if (argsL.contains(RECURSION_FLAG))
                result = new Drina(offers).solveR();
            else
                result = new Drina(offers).solveDP();
            results.add(result);
        }
        for(long r :results)
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
        private final Offer[] offers;
        private long[] array;
        private long[] maxComb;

        public Drina(SortedSet<Offer> offers)
        {
            this.offers = offers.toArray(new Offer[offers.size()]);
            this.array = null;
        }

        public void buildArray()
        {
            array = new long[offers.length + 1];
            maxComb = new long[offers.length + 1];
            for(int i = 0; i < array.length; i++)
            {
                array[i] = 0;
                maxComb[i] = 0;
            }
                
        }

        public long solveDP()
        {
         
            // for(Offer o : offers)
            // {    System.out.println(o);
            // }
            //System.out.println();
            buildArray();
            for(int i = 1; i <= offers.length; i++)
            {
                long max = getOffer(i).price;
                for(int k = 1; k <= offers.length;  k++)
                {   
                    long r = 0;
                    if(compatible(k, i))
                    {
                        if (k == 0 || array[k] > array[k-1])
                        {
                            r = getOffer(i).price + array[k];
                        }
                        else
                        {
                            r = getOffer(i).price;
                        }
                    }
                    else
                    {
                        r = getOffer(i).price;
                    }
        
                    // long r = (compatible(k, i) && (k == 0 || array[k] > array[k-1])? 
                    // getOffer(i).price + array[k] 
                    // : array[k]);
                    max = Math.max(max, r);
                }
                // System.out.println(max + " " + array[i-1]);
                array[i] = Math.max( max, array[i-1]);
            }
            // System.out.println();
            // for(long r : array)
            // {
                // System.out.println(r);
            // }
            // System.out.println();
            return array[offers.length];
        }

        public int solveR()
        {
            return solveRS(offers.length);
        }

        private Offer getOffer(int i)
        {
            return offers[i-1];
        }

        public int solveRS(int i)
        {
            if (i == 0)
                return 0;
            // if (compatible(i - 1, i))
            //     return getOffer(i).price + solveRS(i - 1);
            // else
            // {
            int max = getOffer(i).price;
            for(int k = i - 1; k >= 0; k--)
            {
                int r = (compatible(k, i) ? 
                getOffer(i).price + solveRS(k) 
                : solveRS(k));
                
                max = r > max ? r : max;
            }
            return max;
            // }
        }

        private boolean compatible(int i, int j)
        {
            return i == 0 || (getOffer(i).endTime <= getOffer(j).startingTime);
        }

    }

    public static class Offer
    {
        public final int endTime;
        public final int startingTime;
        public final int price;

        public Offer(int startingTime, int duration, int price)
        {
            this.startingTime = startingTime;
            this.endTime = startingTime + duration;
            this.price = price;
        }

        public String toString()
        {
            return String.format("(%d %d %d)", startingTime, endTime, price);
        }
    }


    private static BufferedReader getTestInputReader(BufferedReader in) throws Exception
    {
        System.out.print("Insert test filename: ");
        String file = in.readLine();
        in.close();
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    private static class offerCompEndTime implements Comparator<Offer>
    {

        public offerCompEndTime(){}

        @Override
        public int compare(Offer o1, Offer o2) {
            if(o1.endTime < o2.endTime)
                return -1;
            if(o1.endTime > o2.endTime)
                return 1;
            if(o1.endTime == o2.endTime)
            {
                if(o1.startingTime < o2.startingTime)
                {
                    return -1;
                }
                if(o1.startingTime > o2.startingTime)
                {
                    return 1;
                }
                if(o1.startingTime == o2.startingTime)
                {
                    return o1.price < o2.price ? 0 : 1;
                }
            }
            return 0;
        }
        
    }
}