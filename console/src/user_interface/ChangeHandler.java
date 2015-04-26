package user_interface;

import functional.Change;
import functional.Remove;
import functional.UserData;
import model.Country;
import model.Holiday;
import model.Tradition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChangeHandler {

    // Меню изменения праздника
    protected static void holidayChanger() {
        try {
            // Выбор праздника для изменения
            MainMenu.out.println(Resources.language.getID_REQUEST());
            PrintHandler.printArrayHolidays(Resources.holidays, 0);
            int choice = Integer.parseInt(MainMenu.reader.readLine());
            if (choice < Resources.holidays.size()) {
                changeHoliday(choice);
            } else {
                throw new IndexOutOfBoundsException();
            }
            PrintHandler.showMenu();
        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        } catch (IndexOutOfBoundsException e) {
            MainMenu.out.println(Resources.language.getWRONG_CHOICE());
        }

    }

    // Меню изменения страны
    protected static void countryChanger() {
        try {
            MainMenu.out.println(Resources.language.getCOUNTRY_CHOICE());
            PrintHandler.printArrayCountries(Resources.countries);
            // Выбор страны для изменения
            int choice = Integer.parseInt(MainMenu.reader.readLine());
            if (choice < UserData.countryCount) {
                changeCountry(choice);
            } else {
                throw new IndexOutOfBoundsException();
            }
            PrintHandler.showMenu();
        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        } catch (IndexOutOfBoundsException e) {
            Resources.language.getWRONG_CHOICE();
        }
    }

    // Изменение праздника
    private static void changeHoliday(int id) throws IOException {
        Holiday holiday = Resources.holidays.get(id);
        Holiday newHoliday = AddHandler.addHoliday();
        // Изменение самого праздника
        Resources.holidays = (LinkedList<Holiday>) Change.editHoliday(id, newHoliday, Resources.holidays);
        Remove.removeHoliday(Resources.holidays.size() - 1, Resources.holidays, Resources.traditions);
        // Изменение традиций, связанных с этим праздником
        Resources.traditions = (ArrayList<Tradition>) Change.editTradition(holiday, newHoliday, Resources.traditions);
    }

    // Изменение страны
    private static void changeCountry(int id) throws IOException {
        Country country = Resources.countries.get(id);
        MainMenu.out.println(Resources.language.getCOUNTRY_REQUEST());
        String newCountryName = MainMenu.reader.readLine();
        // Изменение традиций со страной
        Resources.traditions = (ArrayList<Tradition>) Change.editTradition(newCountryName, id, 4, Resources.traditions, Resources.countries);
        // Изменение самой страны
        Resources.countries = (LinkedList<Country>) Change.editCountry(country, newCountryName, Resources.countries);
    }

    // Изменение традиции
    protected static void changeTradition() {
        MainMenu.out.println(Resources.language.getCHANGE_TRADITION());
        try {
            int choice = Integer.parseInt(MainMenu.reader.readLine());
            switch (choice) {
                case 1:
                    // Изменение описания
                    changeDescription();
                    break;
                case 2:
                    // Изменение страны для конкретной традиции
                    changeCountryTradition();
                    break;
                case 3:
                    // Изменение праздника для конкретной традиции
                    changeHolidayTradition();
                    break;
                default:
                    MainMenu.out.println(Resources.language.getWRONG_CHOICE());
                    break;
            }
        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        }
    }

    protected static void changeHolidayTradition() {
        try {
            MainMenu.out.println(Resources.language.getID_REQUEST());
            int choice = Integer.parseInt(MainMenu.reader.readLine());

            MainMenu.out.println(Resources.language.getHOLIDAY_REQUEST());
            PrintHandler.printArrayHolidays(Resources.holidays, 0);

            int description = Integer.parseInt(MainMenu.reader.readLine());
            if (choice < Resources.holidays.size()) {
                Resources.traditions = (ArrayList<Tradition>)
                        Change.editTradition(Resources.holidays.get(description).getName(), choice, 3,
                                Resources.traditions, Resources.countries);
            } else throw new IndexOutOfBoundsException();
            MainMenu.out.println(Resources.language.getREADY());
        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        } catch (IndexOutOfBoundsException e) {
            Resources.language.getWRONG_CHOICE();
        }
    }

    protected static void changeCountryTradition() {
        try {
            MainMenu.out.println(Resources.language.getID_REQUEST());
            int choice = Integer.parseInt(MainMenu.reader.readLine());

            MainMenu.out.println(Resources.language.getCOUNTRY_CHOICE());
            PrintHandler.printArrayCountries(Resources.countries);

            int description = Integer.parseInt(MainMenu.reader.readLine());
            if (choice < Resources.countries.size()) {
                Resources.traditions = (ArrayList<Tradition>)
                        Change.editTradition(Resources.countries.get(description).getName(), choice, 2,
                                Resources.traditions, Resources.countries);
            } else throw new IndexOutOfBoundsException();
            MainMenu.out.println(Resources.language.getREADY());
        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        } catch (IndexOutOfBoundsException e) {
            Resources.language.getWRONG_CHOICE();
        }
    }

    protected static void changeDescription() {
        try {
            MainMenu.out.println(Resources.language.getID_REQUEST());
            int choice = Integer.parseInt(MainMenu.reader.readLine());
            MainMenu.out.println(Resources.language.getENTER_DESCRIPTION());

            String description = MainMenu.reader.readLine();

            if (choice > Resources.traditions.size()) {
                Resources.traditions = (ArrayList<Tradition>)
                        Change.editTradition(description, choice, 1, Resources.traditions, Resources.countries);
            } else throw new IndexOutOfBoundsException();
            MainMenu.out.println(Resources.language.getREADY());

        } catch (IOException e) {
            MainMenu.out.println(Resources.language.getIO_ERROR());
        } catch (IndexOutOfBoundsException e) {
            Resources.language.getWRONG_CHOICE();
        }
    }
}
