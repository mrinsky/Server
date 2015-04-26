package functional;

import model.Country;
import model.Holiday;
import model.Tradition;

import java.util.ArrayList;
import java.util.List;

public class Change {

    // Правка страны
    public static List<Country> editCountry(Country country, String newName, List<Country> list) {
        int index = list.indexOf(country);
        country.setName(newName);
        // Проверка на уникальность
        if (Add.isUnique(country, list)) {
            list.set(index, country);
        }
        return list;
    }

    // Правка праздника
    public static List<Holiday> editHoliday(int id, Holiday newHoliday, List<Holiday> list) {
        if (Add.isUnique(newHoliday,list)) {
            list.set(id, newHoliday);
        }
        return list;
    }

    //region Правка традиций
    public static List<Tradition> editTradition(String newStr, int id, int param, List<Tradition> list, List<Country> countries) {
        Tradition tradition = list.get(id);
        switch (param) {
            case 1:
                // Изменение описания традиции
                tradition.setDescription(newStr);
                break;
            case 2:
                // Изменение имени страны
                tradition.getCountry().setName(newStr);
                break;
            case 3:
                // Изменение имени праздника
                tradition.getHoliday().setName(newStr);
                break;
            case 4:
                list = editTradition(new Country(newStr), id, countries, list);
                break;
            default:
                break;
        }
        list.set(id, tradition);
        return list;
    }

    public static List<Tradition> editTradition(Country newCountry, int countryId, List<Country> countries, List<Tradition> list) {
        // Получаем массив традиций для определенной страны
        ArrayList<Tradition> traditions = Search.getCountryTraditions(countryId, countries, list);
        for (Tradition tradition : list) {
            for (int i = 0; i < traditions.size(); i++) {
                // Измененная традиция
                Tradition added = new Tradition(tradition.getHoliday(), newCountry, tradition.getDescription());
                // Проверка на уникальность и совпадение индексов
                if ((tradition.equals(traditions.get(i)))&&(Add.isUnique(added,list)))
                    list.set(list.indexOf(traditions.get(i)), added);
            }
        }
        return list;
    }

    public static List<Tradition> editTradition(Holiday holiday, Holiday newHoliday, List<Tradition> list) {
        // Получаем массив традиций для определенного праздника
        ArrayList<Tradition> traditions = Search.getTraditions(holiday, list);
        for (Tradition tradition : list) {
            for (int i = 0; i < traditions.size(); i++) {
                // Измененная традиция
                Tradition added = new Tradition(newHoliday, tradition.getCountry(), tradition.getDescription());
                // Проверка на уникальность и совпадение индексов
                if ((tradition.equals(traditions.get(i)))&&(Add.isUnique(added,list)))
                    list.set(list.indexOf(traditions.get(i)), added);
            }
        }
        return list;
    }

    public static List<Tradition> editTradition(int id, Holiday newHoliday, Country newCountry, String description, List<Tradition> list) {
        Tradition changing = list.get(id);
        changing.setHoliday(newHoliday);
        changing.setCountry(newCountry);
        changing.setDescription(description);
        if (Add.isUnique(changing,list)) list.set(id, changing);
        return list;
    }
    //endregion
}
