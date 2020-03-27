package Drina;

public class Offer
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