package functional;

import model.Country;
import model.Holiday;
import model.Tradition;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
* Created by Михаил on 09.03.2015.
*/

public interface DataSaveLoad {
    public final static String XML_TRADITION_DEFAULT_RU = "resources/xml/default_save/traditionSave.xml";
    public final static String XML_HOLIDAY_DEFAULT_RU = "resources/xml/default_save/holidaySave.xml";
    public final static String XML_COUNTRY_DEFAULT_RU = "resources/xml/default_save/countrySave.xml";


    public final static String XML_TRADITION_PATCH_RU = "resources/xml/traditionSave.xml";
    public final static String XML_HOLIDAY_PATCH_RU = "resources/xml/holidaySave.xml";
    public final static String XML_COUNTRY_PATCH_RU = "resources/xml/countrySave.xml";

    public final static String XML_USERS = "resources/userList/user.xml";

    public final static String HOLIDAY_PATCH_RU = "resources/bin/holidays_ru.bin";
    public final static String COUNTRY_PATCH_RU = "resources/bin/country_ru.bin";
    public final static String TRADITION_PATCH_RU = "resources/bin/tradition_ru.bin";

    public final static String TRADITION_XSD = "resources/XSD_Schems/TraditionXSD.xsd";
    public final static String HOLIDAY_XSD = "resources/XSD_Schems/HolidayXSD.xsd";
    public final static String COUNTRY_XSD = "resources/XSD_Schems/CountryXSD.xsd";
    public final static String USERS_XSD = "resources/XSD_Schems/UsersXSD.xsd";
    DataSaveLoad xmlSaveLoad = new XmlFileWorking();
//    DataSaveLoad serSaveLoad = new SerFileWorking();


    public void saveTradition(ArrayList<Tradition> traditions, String direct) throws IOException;

    public void saveHolidays(List<Holiday> holidays, String direct) throws IOException;

    public void saveCountry(List<Country> countries, String direct) throws IOException;

    public ArrayList<Tradition> loadTradition(String direct) throws IOException, JDOMException, ClassNotFoundException, ParseException, SAXException;

    public LinkedList<Holiday> loadHoliday(String direct) throws IOException, JDOMException, ClassNotFoundException, ParseException, SAXException;

    public LinkedList<Country> loadCountry(String direct) throws IOException, JDOMException, ClassNotFoundException, SAXException;

    public void loadAll(ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays) throws ClassNotFoundException, IOException, JDOMException, ParseException, SAXException;

    public void saveAll(ArrayList<Tradition> traditions, List<Country> countries, List<Holiday> holidays) throws IOException;


}
