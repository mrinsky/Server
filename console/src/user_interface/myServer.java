package user_interface;

//import functional.XmlFileWorking;

import functional.*;
import model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class myServer implements Runnable {
    static int clientCount = 0;
    Socket connection;
    String input;
    String name = "";
    String temp = "";
    XmlFileWorking xmlFileWorking = new XmlFileWorking();
    private final String TEMP_FOLDER = "resources/temp/";
    private final String TEMP_XML = "temp.xml";
    private SAXBuilder builder = new SAXBuilder();
    ArrayList<Tradition> traditions = new ArrayList<Tradition>();
    LinkedList<Country> countries = new LinkedList<Country>();
    LinkedList<Holiday> holidays = new LinkedList<Holiday>();
    Resources resources = new Resources();



    public myServer(Socket socket) {
        this.connection = socket;
        clientCount++;
    }

    public static void serverInit() throws IOException {
        try {
            Resources.sw = new FileWriter(Resources.serverLogDirect, true);
            Resources.sw.write("\n" + "Session started: " + new java.util.Date().toString() + "\n");
            Resources.sw.close();
        } catch (IOException e) {
            System.out.println("Ошибка в создании при работе с Log файлом!");
        }

        ServerSocket servers = null;

        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Невозможно запустить сервер, порт занят!");
            System.exit(-1);
        }
        //Ждем подключения клиента, запускаем поток на каждое подключение
        while (true) {
            try {
                Socket connection = servers.accept();
                Runnable runnable = new myServer(connection);
                Thread thread = new Thread(runnable);
                thread.start();
            } catch (IOException e) {
                System.out.println("Ошибка при создании потока!");
                System.exit(-1);
            }
        }
    }

    @Override
    public void run() {
        name = "";
        try {
            BufferedReader inServer = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            PrintWriter outServer = new PrintWriter(connection.getOutputStream(), true);
            //считываем имя клиента
            if ((input = inServer.readLine()) != null) {
                if ("closeSystem".equals(input)) {
                    //Thread.currentThread().interrupt();
                } else if (!"initSystem".equals(input)) {
                    name = input;

                    try {
                        Resources.sw = new FileWriter(Resources.serverLogDirect, true);

                        Resources.sw.write(("\n Client " + name + " connected        " + new Date()));
                        System.out.println("Client " + name + "  connected        " + new Date());
                        Resources.sw.close();
                        clientCount++;
                    }

                catch(IOException e){
                    System.out.println("Ошибка в создании при работе с Log файлом!");
                }
                }
            }

                //чистаем сообщения клиента
                while (true) {
                    if ((input = inServer.readLine()) != null) {
                        System.out.println(input);
                        try {
                            if ("getCountry".equals(input)) {
                                xmlFileWorking.saveCountry(countries, TEMP_FOLDER + name + TEMP_XML);
                                outServer.println(xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML));
                                input = null;
                            } else if ("getHoliday".equals(input)) {
                                xmlFileWorking.saveHolidays(holidays, TEMP_FOLDER + name + TEMP_XML);
                                outServer.println(xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML));
                                input = null;
                            } else if ("getTradition".equals(input)) {
                                xmlFileWorking.saveTradition(traditions, TEMP_FOLDER + name + TEMP_XML);
                                outServer.println(xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML));
                                input = null;
                            } else if ("loadAllData".equals(input)){
                                try {
                                    System.out.println("Дошло до LoadAll");

                                    xmlFileWorking.loadUser(traditions,countries,holidays);
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if ("logOut".equals(input)){Thread.currentThread().stop();}
                            else{

                                temp = getMethodFromClient(input);
                                outServer.println(temp);
                                input = null;
                                temp = null;
                            }

                        } catch (JDOMException e) {
                            //ошибки в хмле
                        } catch (ParseException e) {

                        }

                        //Здесь расшифровываем сообщения и вызываем нужные методы
                        //после чего отправляем результат обратно

                    }
                }
            }catch(IOException e){

            }

        }

    public String getMethodFromClient(String inputMessage) throws IOException, JDOMException, ParseException {
        PrintWriter printWriter;
        File file = new File(TEMP_FOLDER + name + TEMP_XML);
        file.createNewFile();
        printWriter = new PrintWriter(file.getAbsoluteFile());
        printWriter.print(inputMessage);
        printWriter.close();
        Document document = builder.build(file);
        Element root = document.getRootElement();
        if ("addTradition".equals(root.getName())) {
            Add.addTradition(xmlFileWorking.getTraditionFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getHoliday(),
                    xmlFileWorking.getTraditionFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getCountry(),
                    xmlFileWorking.getTraditionFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getDescription(),
                    traditions);
            xmlFileWorking.saveTradition(traditions, (TEMP_FOLDER + name + TEMP_XML));
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("addHoliday".equals(root.getName())) {
            try {
                Holiday holiday = new Holiday(xmlFileWorking.getHolidayFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getName());
                holiday.setStartDate(Holiday.dateFormat.parse(xmlFileWorking.getHolidayFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getStartDate()));
                holiday.setType(xmlFileWorking.getHolidayFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getType());
                holidays.add(holiday);
                xmlFileWorking.saveHolidays(holidays, (TEMP_FOLDER + name + TEMP_XML));
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("addCountry".equals(root.getName())) {
            Add.addCountry(xmlFileWorking.getCountryFromClient_ADD(TEMP_FOLDER + name + TEMP_XML).getName(),
                    countries);
            xmlFileWorking.saveCountry(countries, (TEMP_FOLDER + name + TEMP_XML));
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("search".equals(root.getName())) {
            xmlFileWorking.saveTradition(Search.search(xmlFileWorking.getRequestFromClient_Search(TEMP_FOLDER + name + TEMP_XML),
                    traditions), TEMP_FOLDER + name + TEMP_XML);
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);

        }
        if ("searchByDate".equals(root.getName())) {
            xmlFileWorking.saveTradition(Search.searchDate(xmlFileWorking.getDateFromClient_dateSearch(TEMP_FOLDER + name + TEMP_XML),
                    holidays, traditions), TEMP_FOLDER + name + TEMP_XML);
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("regularSearch".equals(root.getName())) {
            xmlFileWorking.saveTradition(Search.regularSearch(xmlFileWorking.getRequestFromClient_regularSearch(TEMP_FOLDER + name + TEMP_XML),
                    traditions), TEMP_FOLDER + name + TEMP_XML);
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("maskSearch".equals(root.getName())) {
            xmlFileWorking.saveTradition(Search.maskSearch(xmlFileWorking.getRequestFromClient_maskSearchHolidayName(TEMP_FOLDER + name + TEMP_XML),
                    xmlFileWorking.getRequestFromClient_maskSearchCountryName(TEMP_FOLDER + name + TEMP_XML),
                    xmlFileWorking.getRequestFromClient_maskSearchDescriptionName(TEMP_FOLDER + name + TEMP_XML), traditions), TEMP_FOLDER + name + TEMP_XML);
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("remove".equals(root.getName())) {
            Remove.removeTraditionGui(Integer.parseInt(xmlFileWorking.getIdFromClient_Remove(TEMP_FOLDER + name + TEMP_XML)), traditions);
            xmlFileWorking.saveTradition(traditions, TEMP_FOLDER + name + TEMP_XML);
            return xmlFileWorking.xmlToString(TEMP_FOLDER + name + TEMP_XML);
        }
        if ("change".equals(root.getName())) {
        }
        if ("registration".equals(root.getName())) {
            System.out.println("Дошло до регистрации");
            if ((Registration.checkLogin(root.getChild("dataReg").getAttributeValue("login")) == true) &
                    (!(root.getChild("dataReg").getAttributeValue("pass1")
                            .equals(root.getChild("dataReg").getAttributeValue("pass2"))))) {
                return "Error";
            } else try {

                Registration.registration(root.getChild("dataReg").getAttributeValue("login"),
                        root.getChild("dataReg").getAttributeValue("pass1"),
                        root.getChild("dataReg").getAttributeValue("pass2"));
                System.out.println("Запара в load data ");
                UserData.loadData(root.getChild("dataReg").getAttributeValue("login"),
                        root.getChild("dataReg").getAttributeValue("pass1"), traditions, countries, holidays);
                System.out.println("регистрация прошла успешно");

                // Resources.traditions, Resources.countries, Resources.holidays передаем пользователю
                //Загужаем традиции
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }
        if ("logIn".equals(root.getName())) {
             if (UserData.authentication(root.getChild("dataLogIn").getAttributeValue("login"),
                        root.getChild("dataLogIn").getAttributeValue("pass1"))){
                 UserData.currentUser = UserData.users.get(Search.searchIndex(UserData.users,root.getChild("dataLogIn").getAttributeValue("login")));
                 return "ok";} else return "Error";

        }


        if ("countrySave".equals(root.getName())) {

            Resources.countries = xmlFileWorking.loadCountry(TEMP_FOLDER + name + TEMP_XML);
            File f1 = new File(TEMP_FOLDER + name + TEMP_XML);
            File f2 = new File(xmlFileWorking.ROOT + name + xmlFileWorking.COUNTRY_FILE);

            User.copyFile(f1, f2);
        }
        if ("holidaysSave".equals(root.getName())) {
            try {
                Resources.holidays = xmlFileWorking.loadHoliday(TEMP_FOLDER + name + TEMP_XML);
                File f1 = new File(TEMP_FOLDER + name + TEMP_XML);
                File f2 = new File(xmlFileWorking.ROOT + name + xmlFileWorking.HOLIDAY_FILE);

                User.copyFile(f1, f2);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        if ("traditionSave".equals(root.getName())) {
            try {
                Resources.traditions = xmlFileWorking.loadTradition(TEMP_FOLDER + name + TEMP_XML);
                File f1 = new File(TEMP_FOLDER + name + TEMP_XML);
                File f2 = new File(xmlFileWorking.ROOT + name + xmlFileWorking.TRADITION_FILE);

                User.copyFile(f1, f2);
            } catch (SAXException e) {
                e.printStackTrace();
            }


        }


        return null;


    }
}