package user_interface;

import functional.XmlFileWorking;
import lang.Strings_EN;
import model.Country;
import model.Holiday;
import model.Tradition;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by 1 on 19.04.2015.
 */
public class HolidayHandler {

    protected static PrintWriter out = new PrintWriter(System.out, true);
    protected static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static XmlFileWorking xml = new XmlFileWorking();

    private static LinkedList<Country> defaultCountries = new LinkedList<Country>();
    private static LinkedList<Holiday> defaultHolidays = new LinkedList<Holiday>();
    private static ArrayList<Tradition> defaultTraditions = new ArrayList<Tradition>();

    protected static void holidayMainMenu() {

        try {

            if (defaultCountries.isEmpty()) initArrays();

            int choice;
            out.println(Resources.language.getHOLIDAY_MENU());
            choice = Integer.parseInt(reader.readLine());
            switch (choice) {
                case 1:
                    PrintHandler.showMenu(defaultTraditions, defaultHolidays, defaultCountries);
                    break;
                case 2:
                    SearchHandler.searchMenu(defaultTraditions,defaultCountries,defaultHolidays);
                    break;
                case 3:
                    AddHandler.addMenu(defaultTraditions, defaultCountries, defaultHolidays);
                    saveArrays();
                    holidayMainMenu();
                    break;
                case 4:
                    changeMenu();
                    holidayMainMenu();
                    break;
                case 5:
                    removeMenu();
                    holidayMainMenu();
                    break;
                case 6:
                    defaultCountries.clear();
                    defaultTraditions.clear();
                    defaultHolidays.clear();
                    MainMenu.mainMenu();
                    break;
            }
        } catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (SAXException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (ParseException e) {
            out.println(Resources.language.getPARSE_ERROR());
        } catch (IOException e) {
            out.println(Resources.language.getIO_ERROR());
        }
    }

    private static void initArrays() throws JDOMException, SAXException, ParseException, IOException {
        if (Resources.language instanceof Strings_EN) {
            defaultCountries = xml.loadCountry(XmlFileWorking.XML_COUNTRY_PATCH_EN);
            defaultHolidays = xml.loadHoliday(XmlFileWorking.XML_HOLIDAY_PATCH_EN);
            defaultTraditions = xml.loadTradition(XmlFileWorking.XML_TRADITION_PATCH_EN);
        } else {
            defaultCountries = xml.loadCountry(XmlFileWorking.XML_COUNTRY_PATCH_RU);
            defaultHolidays = xml.loadHoliday(XmlFileWorking.XML_HOLIDAY_PATCH_RU);
            defaultTraditions = xml.loadTradition(XmlFileWorking.XML_TRADITION_PATCH_RU);
        }
    }

    private static void saveArrays() throws IOException {
        if (Resources.language instanceof Strings_EN) {
            xml.saveCountry(defaultCountries, XmlFileWorking.XML_COUNTRY_PATCH_EN);
            xml.saveHolidays(defaultHolidays, XmlFileWorking.XML_HOLIDAY_PATCH_EN);
            xml.saveTradition(defaultTraditions, XmlFileWorking.XML_TRADITION_PATCH_EN);
        } else {
            xml.saveCountry(defaultCountries, XmlFileWorking.XML_COUNTRY_PATCH_RU);
            xml.saveHolidays(defaultHolidays, XmlFileWorking.XML_HOLIDAY_PATCH_RU);
            xml.saveTradition(defaultTraditions, XmlFileWorking.XML_TRADITION_PATCH_RU);
        }
    }

    private static void changeMenu() throws IOException {
        out.printf("%s\n1 - %s\n2 - %s\n3 - %s\n4 - %s", Resources.language.getCHANGE(), Resources.language.getCOUNTRY_ITEM(),
                Resources.language.getHOLIDAY_ITEM(), Resources.language.getTRADITION_ITEM(), Resources.language.getBACK());
        int choice = Integer.parseInt(reader.readLine());
        switch (choice) {
            case 1:
                ChangeHandler.countryChanger(defaultCountries, defaultTraditions);
                saveArrays();
                holidayMainMenu();
                break;
            case 2:
                ChangeHandler.holidayChanger(defaultHolidays, defaultTraditions);
                saveArrays();
                holidayMainMenu();
                break;
            case 3:
                ChangeHandler.changeTradition(defaultCountries, defaultHolidays, defaultTraditions);
                saveArrays();
                holidayMainMenu();
                break;
            case 4:
                holidayMainMenu();
                break;
            default:
                out.println(Resources.language.getWRONG_CHOICE());
                break;
        }
    }

    private static void removeMenu() throws IOException {
        out.printf("%s\n1 - %s\n2 - %s\n3 - %s\n4 - %s", Resources.language.getREMOVE(), Resources.language.getCOUNTRY_ITEM(),
                Resources.language.getHOLIDAY_ITEM(), Resources.language.getTRADITION_ITEM(), Resources.language.getBACK());
        int choice = Integer.parseInt(reader.readLine());
        switch (choice) {
            case 1:
                RemoveHandler.countryRemover(defaultCountries, defaultTraditions);
                saveArrays();
                break;
            case 2:
                RemoveHandler.holidayRemover(defaultHolidays, defaultTraditions);
                saveArrays();
                break;
            case 3:
                RemoveHandler.removeTradition(defaultTraditions);
                saveArrays();
                break;
            case 4:
                holidayMainMenu();
                break;
            default:
                out.println(Resources.language.getWRONG_CHOICE());
                break;
        }
    }
}
