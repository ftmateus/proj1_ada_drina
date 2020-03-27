package Drina;

import java.util.SortedSet;

public class Drina
{
    public static final int MAX_TEST_CASES = 100;
    public static final int MAX_OFFERS = 10000;
    public static final int MAX_STARTING_INSTANT = 1000000000;
    public static final int MAX_DURATION = 2*10000;
    public static final int MAX_PRICE = 10000;

    private final Offer[] offers;
    private long[] maxProfit;

    public Drina(SortedSet<Offer> offers)
    {
        this.offers = offers.toArray(new Offer[offers.size()]);
        this.maxProfit = null;
    }

    public void buildArray()
    {
        maxProfit = new long[offers.length + 1];
        for(int i = 0; i < maxProfit.length; i++)
            maxProfit[i] = 0;
    }

    public long solveDP()
    {
        buildArray();
        for(int i = 1; i <= offers.length; i++)
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
        return maxProfit[offers.length];
    }

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
        return max;
    }

    public long solveR()
    {
        return solveRS(offers.length);
    }

    private Offer getOffer(int i)
    {
        return offers[i-1];
    }

    private boolean compatible(int i, int j)
    {
        return i == 0 || (getOffer(i).endTime <= getOffer(j).startingTime);
    }
}

    