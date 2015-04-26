package functional;

import model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;
import sun.security.util.Resources;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/*
Created by Михаил on 09.03.2015.
*/

public class XmlFileWorking implements DataSaveLoad {


    public final String ROOT = "resources/users/";
    public final String TRADITION_FILE = "/traditionSave.xml";
    public final String HOLIDAY_FILE = "/holidaySave.xml";
    public final String COUNTRY_FILE = "/countrySave.xml";
    private final String TEMP_XML = "/resources/temp/temp.xml";
    private SAXBuilder builder = new SAXBuilder();

    public void saveUser(ArrayList<Tradition> traditions, List<Holiday> holidays, List<Country> countries) throws IOException {
        this.saveTradition(traditions, ROOT + UserData.currentUser.getLogin() + TRADITION_FILE);
        this.saveHolidays(holidays, ROOT + UserData.currentUser.getLogin() + HOLIDAY_FILE);
        this.saveCountry(countries, ROOT + UserData.currentUser.getLogin() + COUNTRY_FILE);
    }

    public void loadUser(ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays) throws JDOMException, SAXException, ParseException, IOException {
        System.out.println("load");
        System.out.println("loadData" + UserData.currentUser.getLogin()+"пусто");
        for (Tradition item : loadTradition(ROOT + UserData.currentUser.getLogin() + TRADITION_FILE)) {

            traditions.add(item);
        }

        for (Country item : loadCountry(ROOT + UserData.currentUser.getLogin() + COUNTRY_FILE)) {
            countries.add(item);
        }
        for (Holiday item : loadHoliday(ROOT + UserData.currentUser.getLogin() + HOLIDAY_FILE)) {
            holidays.add(item);
        }
    }

    @Override
    public void saveTradition(ArrayList<Tradition> traditions, String direct) throws IOException {
        Element root = new Element("traditionSave");
        Document doc = new Document(root);
        for (Tradition tradition : traditions) {

            Element traditionElement = new Element("tradition");
            traditionElement.setAttribute("description", tradition.getDescription());

            Element holidayElement = new Element("holiday");

            Element holidayName = new Element("holidayName");
            holidayName.setText(tradition.getHoliday().getName());
            holidayElement.addContent(holidayName);

            Element holidayStartDate = new Element("holidayStartDate");
            holidayStartDate.setText(tradition.getHoliday().getStartDate());
            holidayElement.addContent(holidayStartDate);

            /*Element holidayEndDate = new Element("holidayEndDate");
            holidayEndDate.setText((String)tradition.getHoliday().getEndDate());
            holidayElement.addContent(holidayEndDate);
*/
            Element holidayType = new Element("holidayType");
            holidayType.setText(tradition.getHoliday().getType().toString());
            holidayElement.addContent(holidayType);

            traditionElement.addContent(holidayElement);

            Element elementCountry = new Element("country");

            Element countryName = new Element("countryName");
            countryName.setText(tradition.getCountry().getName());
            elementCountry.addContent(countryName);

            traditionElement.addContent(elementCountry);

            root.addContent(traditionElement);
            writeXml(doc, direct);


        }

    }

    @Override
    public ArrayList<Tradition> loadTradition(String direct) throws IOException, JDOMException, ParseException, SAXException {
        ArrayList<Tradition> traditions = new ArrayList<Tradition>();
       /* if (!((new File(direct)).exists())) {
            direct = XML_TRADITION_DEFAULT_RU;
        }
        if (validationXSD(direct, TRADITION_XSD) == false) {throw new SAXException();}
*/
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List traditionElem = root.getChildren();

        for (Iterator traditionIterator = traditionElem.iterator(); traditionIterator.hasNext(); ) {
            Element traditionElement = (Element) traditionIterator.next();

            Tradition tradition = new Tradition();
            tradition.setDescription(traditionElement.getAttributeValue("description"));

            Element holidayElement = traditionElement.getChild("holiday");
            Holiday holiday = new Holiday(holidayElement.getChild("holidayName").getText());

            holiday.setStartDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayStartDate").getText()));
//              holiday.setEndDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayEndDate").getText()));
            holiday.setType(HolidayType.valueOf(holidayElement.getChild("holidayType").getText()));

            tradition.setHoliday(holiday);

            Element countryElement = traditionElement.getChild("country");

            Country country = new Country(countryElement.getChild("countryName").getText());

            tradition.setCountry(country);
            traditions.add(tradition);


        }


        return traditions;

    }

    @Override
    public void saveHolidays(List<Holiday> holidays, String direct) throws IOException {
        Element root = new Element("holidaysSave");
        Document doc = new Document(root);
        for (Holiday holiday : holidays) {
            Element holidayElement = new Element("holiday");
            Element holidayName = new Element("holidayName");
            holidayName.setText(holiday.getName());
            holidayElement.addContent(holidayName);

            Element holidayStartDate = new Element("holidayStartDate");
            holidayStartDate.setText(holiday.getStartDate());
            holidayElement.addContent(holidayStartDate);

            /*Element holidayEndDate = new Element("holidayEndDate");
            holidayEndDate.setText((String)holiday.getEndDate());
            holidayElement.addContent(holidayEndDate);
*/
            Element holidayType = new Element("holidayType");
            holidayType.setText(holiday.getType().toString());
            holidayElement.addContent(holidayType);

            root.addContent(holidayElement);
            writeXml(doc, direct);


        }
    }

    @Override
    public LinkedList<Holiday> loadHoliday(String direct) throws IOException, JDOMException, ParseException, SAXException {

        LinkedList<Holiday> holidays = new LinkedList<Holiday>();
        if (!((new File(direct)).exists())) {
            direct = XML_HOLIDAY_DEFAULT_RU;
        }
        //if (validationXSD(direct, HOLIDAY_XSD) == false) {
          //  throw new SAXException();
        //}
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List holidayElem = root.getChildren();
        Iterator holidayIterator = holidayElem.iterator();
        while (holidayIterator.hasNext()) {
            Element holidayElement = (Element) holidayIterator.next();
            Holiday holiday = new Holiday();
            holiday.setName(holidayElement.getChild("holidayName").getText());
            holiday.setStartDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayStartDate").getText()));
            // holiday.setEndDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayEndDate").getText()));
            holiday.setType(HolidayType.valueOf(holidayElement.getChild("holidayType").getText()));
            holidays.add(holiday);
        }

        return holidays;
    }

    @Override
    public void saveCountry(List<Country> countries, String direct) throws IOException {
        Element root = new Element("countrySave");
        Document doc = new Document(root);
        for (Country country : countries) {
            Element countryElement = new Element("country");
            Element countryName = new Element("countryName");
            countryName.setText(country.getName());
            countryElement.addContent(countryName);
            root.addContent(countryElement);
            writeXml(doc, direct);

        }
    }

    @Override
    public LinkedList<Country> loadCountry(String direct) throws IOException, JDOMException {
        LinkedList<Country> countries = new LinkedList<Country>();
        if (!((new File(direct)).exists())) {
            direct = XML_COUNTRY_DEFAULT_RU;
        }
        //if (validationXSD(direct, COUNTRY_XSD) == false) {
        //    throw new SAXException();
        //}

        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List countryElem = root.getChildren();
        Iterator countryIterator = countryElem.iterator();
        while (countryIterator.hasNext()) {
            Element countryElement = (Element) countryIterator.next();
            Country country = new Country();
            country.setName(countryElement.getChild("countryName").getText());

            countries.add(country);
        }

        return countries;
    }

    public void saveUsers(ArrayList<User> users, String direct) throws IOException {
        Element root = new Element("userSave");
        Document doc = new Document(root);
        for (User user : users) {
            Element userElement = new Element("user");
            Element userName = new Element("userName");
            Element userPass = new Element("userPass");
            userName.setText(user.getLogin());
            userPass.setText(user.getPass().toString());
            userElement.addContent(userName);
            userElement.addContent(userPass);
            root.addContent(userElement);
            writeXml(doc, direct);
        }
    }

    public ArrayList<User> loadUsers(String direct) throws IOException, JDOMException, SAXException {

        ArrayList<User> users = new ArrayList<User>();

        if (!((new File(direct)).exists())) {}
        if (validationXSD(direct, USERS_XSD) == false) {
            throw new SAXException();
        }
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List userElem = root.getChildren();
        Iterator userIterator = userElem.iterator();
        while (userIterator.hasNext()) {
            Element userElement = (Element) userIterator.next();
            User user = new User(userElement.getChild("userName").getText(),
                    userElement.getChild("userPass").getText(), UserData.rsa);

            users.add(user);
        }

        return users;
    }


    public void writeXml(Document document, String direct) throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        FileWriter writer = new FileWriter(direct);
        outputter.output(document, writer);
        writer.close();

    }


    @Override
    public void loadAll(ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays) throws ClassNotFoundException, IOException, JDOMException, ParseException, SAXException {
        holidays = xmlSaveLoad.loadHoliday(XML_HOLIDAY_PATCH_RU);
        countries = xmlSaveLoad.loadCountry(XML_COUNTRY_PATCH_RU);
        traditions = xmlSaveLoad.loadTradition(XML_TRADITION_PATCH_RU);
        UserData.users = loadUsers(XML_USERS);
    }

    @Override
    public void saveAll(ArrayList<Tradition> traditions, List<Country> countries, List<Holiday> holidays) throws IOException {
        xmlSaveLoad.saveHolidays(holidays, XML_HOLIDAY_PATCH_RU);
        xmlSaveLoad.saveCountry(countries, XML_COUNTRY_PATCH_RU);
        xmlSaveLoad.saveTradition(traditions, XML_TRADITION_PATCH_RU);
        saveUsers(UserData.users, XML_USERS);
    }

    public boolean validationXSD(String directXML, String directXSD) throws IOException {
        try {


            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(directXSD));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(directXML));
            return true;

        } catch (SAXException ex) {
            return false;
        }
    }


    public String xmlToString(String direct) {
        String s = "";
        File file = new File(direct);

        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file))
            );

            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    //variable line does NOT have new-line-character at the end
                    s = s + line;
                }
                br.close();


            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден");
            }
        } catch (IOException e) {

        }

        return s;
    }
    public void stringToXML(String direct, String inputMessage) {
        PrintWriter printWriter;
        File file = new File(direct);
        try {
            file.createNewFile();

            printWriter = new PrintWriter(file.getAbsoluteFile());
            printWriter.print(inputMessage);
            printWriter.close();
        } catch (IOException e) {
            System.out.println("StringToXml ошибка");
        }

    }

    public String sendHolidaysToServer_ADD(Holiday holiday) throws IOException {
        Element root = new Element("addHoliday");
        Document doc = new Document(root);
            Element holidayElement = new Element("holiday");
            Element holidayName = new Element("holidayName");
            holidayName.setText(holiday.getName());
            holidayElement.addContent(holidayName);

            Element holidayStartDate = new Element("holidayStartDate");
            holidayStartDate.setText(holiday.getStartDate());
            holidayElement.addContent(holidayStartDate);

            /*Element holidayEndDate = new Element("holidayEndDate");
            holidayEndDate.setText((String)holiday.getEndDate());
            holidayElement.addContent(holidayEndDate);
*/
            Element holidayType = new Element("holidayType");
            holidayType.setText(holiday.getType().toString());
            holidayElement.addContent(holidayType);

            root.addContent(holidayElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);


    }
    public Holiday getHolidayFromClient_ADD(String direct) throws IOException, JDOMException, ParseException, SAXException {

        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List holidayElem = root.getChildren();
        Iterator holidayIterator = holidayElem.iterator();
            Element holidayElement = (Element) holidayIterator.next();
            Holiday holiday = new Holiday();
            holiday.setName(holidayElement.getChild("holidayName").getText());
            holiday.setStartDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayStartDate").getText()));
            // holiday.setEndDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayEndDate").getText()));
            holiday.setType(HolidayType.valueOf(holidayElement.getChild("holidayType").getText()));



        return holiday;
    }

    public String sendCountryToServer_ADD(Country country) throws IOException {
        Element root = new Element("addCountry");
        Document doc = new Document(root);
            Element countryElement = new Element("country");
            Element countryName = new Element("countryName");
            countryName.setText(country.getName());
            countryElement.addContent(countryName);
            root.addContent(countryElement);

        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);



    }
    public Country getCountryFromClient_ADD(String direct) throws IOException, JDOMException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List countryElem = root.getChildren();
        Iterator countryIterator = countryElem.iterator();
        Element countryElement = (Element) countryIterator.next();
            Country country = new Country();
            country.setName(countryElement.getChild("countryName").getText());


        return country;
    }

    public String sendTraditionToServer_ADD(Tradition tradition) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("addTradition");
        Document doc = new Document(root);
        Element traditionElement = new Element("tradition");
        traditionElement.setAttribute("description", tradition.getDescription());

        Element holidayElement = new Element("holiday");

        Element holidayName = new Element("holidayName");
        holidayName.setText(tradition.getHoliday().getName());
        holidayElement.addContent(holidayName);

        Element holidayStartDate = new Element("holidayStartDate");
        holidayStartDate.setText(tradition.getHoliday().getStartDate());
        holidayElement.addContent(holidayStartDate);

            /*Element holidayEndDate = new Element("holidayEndDate");
            holidayEndDate.setText((String)tradition.getHoliday().getEndDate());
            holidayElement.addContent(holidayEndDate);
*/
        Element holidayType = new Element("holidayType");
        holidayType.setText(tradition.getHoliday().getType().toString());
        holidayElement.addContent(holidayType);

        traditionElement.addContent(holidayElement);

        Element elementCountry = new Element("country");

        Element countryName = new Element("countryName");
        countryName.setText(tradition.getCountry().getName());
        elementCountry.addContent(countryName);

        traditionElement.addContent(elementCountry);

        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);


    } //отправляем традицию с клиента на сервер
    public Tradition getTraditionFromClient_ADD(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        List traditionElem = root.getChildren();
        Tradition tradition = new Tradition();

        for (Iterator traditionIterator = traditionElem.iterator(); traditionIterator.hasNext(); ) {
            Element traditionElement = (Element) traditionIterator.next();

            tradition.setDescription(traditionElement.getAttributeValue("description"));

            Element holidayElement = traditionElement.getChild("holiday");
            Holiday holiday = new Holiday(holidayElement.getChild("holidayName").getText());

            holiday.setStartDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayStartDate").getText()));
//              holiday.setEndDate(Holiday.dateFormat.parse(holidayElement.getChild("holidayEndDate").getText()));
            holiday.setType(HolidayType.valueOf(holidayElement.getChild("holidayType").getText()));

            tradition.setHoliday(holiday);

            Element countryElement = traditionElement.getChild("country");

            Country country = new Country(countryElement.getChild("countryName").getText());

            tradition.setCountry(country);


        }


        return tradition;

    } //получаем традицию и кидаем в метод добавления


    public String sendRequestToServer_Search(String request) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("search");
        Document doc = new Document(root);
        Element traditionElement = new Element("searchRequest");
        traditionElement.setAttribute("request", request);
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }
    public String getRequestFromClient_Search(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("searchRequest").getAttributeValue("request");

    }

    public String sendRequestToServer_regularSearch(String request) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("regularSearch");
        Document doc = new Document(root);
        Element traditionElement = new Element("regularSearchRequest");
        traditionElement.setAttribute("request", request);
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }
    public String getRequestFromClient_regularSearch(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("regularSearchRequest").getAttributeValue("request");

    }

    public String sendDateToServer_dateSearch(Date date) {
        Element root = new Element("searchByDate");
        Document doc = new Document(root);
        Element traditionElement = new Element("dateSearchDate");
        traditionElement.setAttribute("holidayStartDate", Holiday.dateFormat.format(date));
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }

    public String getDateFromClient_dateSearch(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("dateSearchDate").getAttributeValue("holidayStartDate");
    }

    public String sendRequestToServer_maskSearch(String name,String country,String description) throws IOException {
        Element root = new Element("maskSearch");
        Document doc = new Document(root);
        Element traditionElement = new Element("maskSearchRequest");
        traditionElement.setAttribute("holidayName", name);
        traditionElement.setAttribute("countryName",country);
        traditionElement.setAttribute("description",description);
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }
    public String getRequestFromClient_maskSearchHolidayName(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("maskSearchRequest").getAttributeValue("holidayName");
        }
    public String getRequestFromClient_maskSearchCountryName(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("maskSearchRequest").getAttributeValue("countryName");
    }
    public String getRequestFromClient_maskSearchDescriptionName(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("maskSearchRequest").getAttributeValue("description");
    }

    public String sendIdToServer_Remove(int id) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("remove");
        Document doc = new Document(root);
        Element traditionElement = new Element("traditionId");
        traditionElement.setAttribute("id", String.valueOf(id));
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }
    public String getIdFromClient_Remove(String direct) throws JDOMException, IOException, ParseException {
        Document document = builder.build(direct);
        Element root = document.getRootElement();
        return root.getChild("traditionId").getAttributeValue("id");

    }
    public String sendDataRegToServer_Registration(String login,String pass1, String pass2) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("registration");
        Document doc = new Document(root);
        Element traditionElement = new Element("dataReg");
        traditionElement.setAttribute("login", login);
        traditionElement.setAttribute("pass1",pass1);
        traditionElement.setAttribute("pass2",pass2);
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }
    public String sendDataLogInToServer_LogIn(String login,String pass1) throws IOException { //пользователь делает xml для отправки на сервер
        Element root = new Element("logIn");
        Document doc = new Document(root);
        Element traditionElement = new Element("dataLogIn");
        traditionElement.setAttribute("login", login);
        traditionElement.setAttribute("pass1",pass1);
        root.addContent(traditionElement);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(doc);
    }




}


