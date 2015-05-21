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

    public void saveTradition(ArrayList<Tradition> traditions, String direct) throws IOException;

    public void saveHolidays(List<Holiday> holidays, String direct) throws IOException;

    public void saveCountry(List<Country> countries, String direct) throws IOException;

    public ArrayList<Tradition> loadTradition(String direct) throws IOException, JDOMException, ClassNotFoundException, ParseException, SAXException;

    public LinkedList<Holiday> loadHoliday(String direct) throws IOException, JDOMException, ClassNotFoundException, ParseException, SAXException;

    public LinkedList<Country> loadCountry(String direct) throws IOException, JDOMException, ClassNotFoundException, SAXException;

    public void loadAll(ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays) throws ClassNotFoundException, IOException, JDOMException, ParseException, SAXException;

    public void saveAll(ArrayList<Tradition> traditions, List<Country> countries, List<Holiday> holidays) throws IOException;
}
