package functional;

import model.Country;
import model.Holiday;
import model.Tradition;

import java.util.Date;
import java.util.List;

public class Add {

    // Проверка на уникальность объекта
    public static boolean isUnique(Object value, List<?> list) {
        for (Object item : list) {
            if (value.toString().equals(item.toString())) {
                return false;
            }
        }
        return true;
    }

    //region Добавление страны
    public static List<Country> addCountry(String name, List<Country> list) {
        Country country = new Country(name);
        if (isUnique(country, list)) {
            list.add(country);
        }
        return list;
    }

    public static List<Country> addCountry(Country country, List<Country> list) {
        if (isUnique(country, list)) {
            list.add(country);
        }
        return list;
    }
    //endregion

    //region Добавление праздника
    public static List<Holiday> addHoliday(String name, List<Holiday> list) {
        Holiday holiday = new Holiday(name);
        if (isUnique(holiday, list)) {
            list.add(holiday);
        }
        return list;
    }

    public static List<Holiday> addHoliday(String name, int typeNum, List<Holiday> list) {
        Holiday holiday = new Holiday(name, typeNum);
        if (isUnique(holiday, list)) {
            list.add(holiday);
        }
        return list;
    }

    public static List<Holiday> addHoliday(String name, Date start, int typeNum, List<Holiday> list) {
        Holiday holiday = new Holiday(name, start, typeNum);
        if (isUnique(holiday, list)) {
            list.add(holiday);
        }
        return list;
    }

    public static List<Holiday> addHoliday(String name, Date start, Date end, int typeNum,
                                           List<Holiday> list) {
        Holiday holiday = new Holiday(name, start, end, typeNum);
        if (isUnique(holiday, list)) {
            list.add(holiday);
        }
        return list;
    }
    //endregion

    //region Добавление традиции
    public static List<Tradition> addTradition(Holiday holiday, Country country, List<Tradition> list) {
        Tradition tradition = new Tradition(holiday, country);
        if (isUnique(tradition, list)) {
            list.add(tradition);
        }
        return list;
    }

    public static List<Tradition> addTradition(Holiday holiday, Country country, String description, List<Tradition> list) {
        Tradition tradition = new Tradition(holiday,country,description);
        if (isUnique(tradition, list)) {
            list.add(tradition);
        }
        return list;
    }
    //endregion
}
