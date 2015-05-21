package functional;

import model.Country;
import model.Holiday;
import model.Tradition;

import java.util.Date;

public class HolidayFactory {

    public Holiday createHoliday(String name) {
        return new Holiday(name);
    }

    public Holiday createHoliday(String name, String type) {
        return new Holiday(name,type);
    }

    public Holiday createHoliday(String name, Date start, String typeNum) {
        return new Holiday(name,start,typeNum);
    }

    public Holiday createHoliday(String name, Date start,  Date end, String typeNum) {
        return new Holiday(name,start,end,typeNum);
    }

    public Tradition createTradition(Holiday holiday, Country country) {
        return new Tradition(holiday,country);
    }

    public Tradition createTradition(Holiday holiday, Country country, String descr) {
        return new Tradition(holiday,country,descr);
    }
}
