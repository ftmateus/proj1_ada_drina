import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import Drina.*;

public class Main
{

    /**
     * List of the program arguments.
     */
    public static List<String> argsL = null;
    /**
     * Flags
     */
    public static final String 
    RECURSION_FLAG = "-r", 
    TEST_INPUT_FLAG = "-t",
    DURATION_FLAG = "-d",
    GENERATE_TESTFILE_FLAG = "-g";

    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        argsL = Arrays.asList(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        if (argsL.contains(GENERATE_TESTFILE_FLAG))
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
            SortedSet<Offer> offers = new TreeSet<Offer>(new OfferCompEndTime());
            for(int i = 0; i < numberOffers; i++)
            {
                String[] s = in.readLine().split(" ");
                int start = Integer.parseInt(s[0]);
                int duration = Integer.parseInt(s[1]);
                int price = Integer.parseInt(s[2]);
                offers.add(new Offer(start, duration, price));
            }
            long result;
            if (argsL.contains(RECURSION_FLAG))
                result = new Drina(offers).solveR();
            else
                result = new Drina(offers).solveDP();
            results.add(result);
            System.out.println(result);
        }
        in.close();
        // for(long r : results)
        //     System.out.println(r);
        long endTime = System.currentTimeMillis();
        if(argsL.contains(DURATION_FLAG))
        {
            Runtime r = Runtime.getRuntime();
            System.out.println("Duration: " + (endTime - startTime) + " ms");
            System.out.println((r.totalMemory()  - r.freeMemory())/1024);
        }
            
    }   

    private static BufferedReader getTestInputReader(BufferedReader in) throws Exception
    {
        System.out.print("Insert test filename: ");
        String file = in.readLine();
        in.close();
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    private static void generateTestFile(BufferedReader in) throws IOException
    {
        System.out.print("Test cases: ");
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
        System.out.println("File generated!");
    }
}