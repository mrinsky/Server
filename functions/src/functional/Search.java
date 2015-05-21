package functional;

import model.*;

import javax.swing.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Search {

    public static ArrayList<Tradition> search(String request, ArrayList<Tradition> traditions) {
        ArrayList<Tradition> searchResult = new ArrayList<Tradition>();
        String countryName;
        String holidayName;
        String description;

        for (int i = 0; i < traditions.size(); i++) {
            countryName = traditions.get(i).getCountry().getName();
            holidayName = traditions.get(i).getHoliday().getName();
            description = traditions.get(i).getDescription();

            if (countryName.contains(request)) {
                searchResult.add(traditions.get(i));
                continue;
            }
            if (holidayName.contains(request)) {
                searchResult.add(traditions.get(i));
                continue;
            }
            if (description.contains(request)) {
                searchResult.add(traditions.get(i));
                continue;
            }
        }

        return searchResult;
    }

    public static ArrayList<Tradition> searchDate(String dateValue, LinkedList<Holiday> holidayList, ArrayList<Tradition> traditionList) {
        try {
            LinkedList<Holiday> traditionHolidays = new LinkedList<Holiday>();
            for (Tradition item : traditionList) {
                traditionHolidays.add(item.getHoliday());
            }
            Date date = Holiday.dateFormat.parse(dateValue);
            if (Search.getDateHolidays(date, traditionHolidays).size() != 0) {
                LinkedList<Holiday> holidays = Search.getDateHolidays(date, traditionHolidays);

                ArrayList<Tradition> traditions = new ArrayList<Tradition>();//Search.getTraditions(holidays.get(0), traditionList);
                for (Holiday item : holidays) {
                    for (Tradition tradition : Search.getTraditions(item, traditionList)) {
                        traditions.add(tradition);
                    }
                }
                traditionList = traditions;
            }
        } catch (IndexOutOfBoundsException exc) {
            JOptionPane.showMessageDialog(null, "IndexOutOfBoundsException");
        }
        catch (ParseException exc) {
            JOptionPane.showMessageDialog(null, "ParseException");
        }
        return traditionList;
    }

    public static ArrayList<Tradition> searchDate(ArrayList<String> dateValue, LinkedList<Holiday> holidayList, ArrayList<Tradition> traditionList) {
        try {
            LinkedList<Holiday> traditionHolidays = new LinkedList<Holiday>();
            for (Tradition item : traditionList) {
                traditionHolidays.add(item.getHoliday());
            }
            //ArrayList<Date> dates = new ArrayList<Date>();
            ArrayList<Tradition> traditions = new ArrayList<Tradition>();
            for (String item : dateValue) {
                Date date = Holiday.dateFormat.parse(item);
                if (Search.getDateHolidays(date, traditionHolidays).size() != 0) {
                    LinkedList<Holiday> holidays = Search.getDateHolidays(date, traditionHolidays);

                    for (Holiday holiday : holidays) {
                        for (Tradition tradition : Search.getTraditions(holiday, traditionList)) {
                            traditions.add(tradition);
                        }
                    }
                }
            }
            traditionList = traditions;
        } catch (IndexOutOfBoundsException exc) {
            JOptionPane.showMessageDialog(null, "IndexOutOfBoundsException");
        }
        catch (ParseException exc) {
            JOptionPane.showMessageDialog(null, "ParseException");
        }
        return traditionList;
    }

    public static ArrayList<Tradition> maskSearch(String holidayName, String countryName, String description, ArrayList<Tradition> traditions) { // Введите название -> Enter Введите страну -> Enter итд Если перенесем на форму будет удобнее
        ArrayList<Tradition> searchResult = new ArrayList<Tradition>();
        boolean holidayFound = false;
        boolean countryFound = false;
        ArrayList<Tradition> selectByHoliday = new ArrayList<Tradition>();
        ArrayList<Tradition> selectByCountry = new ArrayList<Tradition>();


        for (Tradition tradition : traditions) {
            if (holidayName.compareToIgnoreCase(tradition.getHoliday().getName()) == 0) {
                selectByHoliday.add(tradition);
                holidayFound = true;

            }
        }
        if (holidayFound == false) selectByHoliday = traditions;
        for (Tradition aSelectByHoliday : selectByHoliday) {
            if ((countryName.compareToIgnoreCase(aSelectByHoliday.getCountry().getName()) == 0)|(countryName=="")) {
                selectByCountry.add(aSelectByHoliday);
                countryFound = true;

            }
        }
        if (countryFound == true) {
            selectByHoliday = selectByCountry;
        }
        for (Tradition aSelectByHoliday : selectByHoliday) {
            if ((aSelectByHoliday.getDescription().contains(description) |(description==""))) {
                searchResult.add(aSelectByHoliday);
            }
        }


        return searchResult;
    }

    public static ArrayList<Tradition> regularSearch(String request, ArrayList<Tradition> traditions) throws PatternSyntaxException{
        ArrayList<Tradition> searchResult = new ArrayList<Tradition>();
        Matcher matcher;
        boolean found;
        Pattern pattern = Pattern.compile(request);

        for (int i = 0; i < traditions.size(); i++) {
            found = false;
            matcher = pattern.matcher(traditions.get(i).getHoliday().getName());
            found = matcher.matches();
            if (found) {
                searchResult.add(traditions.get(i));

                continue;
            }
            matcher = pattern.matcher(traditions.get(i).getCountry().getName());
            found = matcher.matches();
            if (found) {
                searchResult.add(traditions.get(i));
               continue;
            }
            matcher = pattern.matcher(traditions.get(i).getDescription());
            found = matcher.matches();
            if (found) {
                searchResult.add(traditions.get(i));

            }

        }

        return searchResult;
    }

    public static ArrayList<Tradition> getCountryTraditions(int country, List<Country> countries, List<Tradition> traditions) {
        ArrayList<Tradition> trad = new ArrayList<Tradition>();
        for (Tradition tradition : traditions) {
            if (tradition.getCountry().getName().equals(countries.get(country).getName()))
                trad.add(tradition);
        }
        return trad;
    }

    public static LinkedList<Holiday> getTypeHolidays(int type, LinkedList<Holiday> holidays) {
        LinkedList<Holiday> hols = new LinkedList<Holiday>();
        for (Holiday holiday : holidays) {
            if (holiday.getType().equals(HolidayType.values()[type])) hols.add(holiday);
        }
        return hols;
    }

    public static LinkedList<Holiday> getDateHolidays(Date date, LinkedList<Holiday> hol) {
        LinkedList<Holiday> holidays = new LinkedList<Holiday>();
        for (Holiday holiday : hol) {
            if (holiday.getStartDate().equals(Holiday.dateFormat.format(date))) holidays.add(holiday);}
        return holidays;
    }

    public static ArrayList<Tradition> getTraditions(Holiday holiday, List<Tradition> allTraditions) {
        ArrayList<Tradition> traditions = new ArrayList<Tradition>();
        for (Tradition tradition : allTraditions) {
            if (tradition.getHoliday().equals(holiday))
                traditions.add(tradition);}
        return traditions;
    }
    public static int searchIndex(ArrayList<User> users, String login){
        int index = 0;
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getLogin().equals(login)){
                index = i;
            }
        }
        return index;
    }

    public static int searchIndex(ArrayList<Tradition> tr, String country, String holiday) {
        int count = -1;
        for (Tradition tradition : tr){

            count++;
            if ((tradition.getHoliday().getName().equals(holiday))&&(tradition.getCountry().getName().equals(country)))
            {
                return count;
            }
        }
        return -1;
    }


}
