package Drina;
import java.util.Comparator;
public class OfferCompEndTime implements Comparator<Offer>
{
    public OfferCompEndTime(){}

    @Override
    public int compare(Offer o1, Offer o2) {
        if(o1.endTime < o2.endTime)
            return -1;
        if(o1.endTime > o2.endTime)
            return 1;
        if(o1.endTime == o2.endTime)
        {
            if(o1.startingTime < o2.startingTime)
                return -1;
            if(o1.startingTime > o2.startingTime)
                return 1;
            if(o1.startingTime == o2.startingTime)
                return o1.price < o2.price ? 0 : 1;
        }
        return 0;
    }
}