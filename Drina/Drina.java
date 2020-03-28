package Drina;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Drina
{
    public static final int MAX_TEST_CASES = 100;
    public static final int MAX_OFFERS = 10000;
    public static final int MAX_STARTING_INSTANT = 1000000000;
    public static final int MAX_DURATION = 2*10000;
    public static final int MAX_PRICE = 10000;

    /**
     * List of offers ordered by their ending time.
     */
    private final List<Offer> offers;

    /**
     * Stores the maximum profit that is obtained 
     * with i offers in its respective array position.
     */
    private long[] maxProfit;

    public Drina(SortedSet<Offer> offers)
    {
        this.offers = new ArrayList<Offer>(offers);
        this.maxProfit = null;
    }


    /**
     * Initializes and builds {@link Drina#maxProfit max profit array} in
     * order to solve the problem with {@link Drina#solveDP() dynamic programming}.
     */
    public void buildArray()
    {
        maxProfit = new long[offers.size() + 1];
        for(int i = 0; i < maxProfit.length; i++)
            maxProfit[i] = 0;
    }

    /**
     * Solves the problem with the dynamic programming technique.
     * @return Result of solving the problem
     */
    public long solveDP()
    {
        buildArray();
        for(int i = 1; i <= offers.size(); i++)
        {
            long max = getOffer(i).price;
            for(int k = 1; compatible(k, i);  k++)
            {   
                long r = 0;
                if(k == 0 || maxProfit[k] > maxProfit[k-1])
                    r = getOffer(i).price + maxProfit[k];
                max = Math.max(max, r);
            }
            maxProfit[i] = Math.max( max, maxProfit[i-1]);
        }
        return maxProfit[offers.size()];
    }

    /**
     * Auxiliary method of {@link Drina#solveR() } 
     * to solve the problem with recursion.
     */
    private long solveRS(int i)
    {
        if (i == 0)
            return 0;
        long max = getOffer(i).price;
        for(int k = 1; compatible(k, i); k++)
        {
            long r = 0;
            long lastProfit = solveRS(k);
            if(k == 0 || lastProfit > solveRS(k-1))
                r = getOffer(i).price + lastProfit;
            max = Math.max(max, r);
        }
        return Math.max(max, solveRS(i - 1));
    }

    /**
     * Solves the problem with recursion.
     * The recursion flag must be passed to the
     * program in order to trigger this method.
     */
    public long solveR()
    {
        return solveRS(offers.size());
    }

    private Offer getOffer(int i)
    {
        return offers.get(i-1);
    }

    /**
     * Checks if 2 offers are compatible, that is,
     * if the ending time of offer i is less or 
     * equal to start time of offer j.
     */
    private boolean compatible(int i, int j)
    {
        return i == 0 || (getOffer(i).endTime <= getOffer(j).startingTime);
    }
}

    