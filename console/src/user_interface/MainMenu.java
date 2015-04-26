package user_interface;

import functional.DataSaveLoad;
import functional.UserData;
import functional.XmlFileWorking;
import lang.Strings_EN;
import lang.Strings_RU;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.ParseException;

public class MainMenu {
    protected static PrintWriter out = new PrintWriter(System.out, true);
    protected static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    protected static DataSaveLoad xmlFiles = new XmlFileWorking();


    public static void init() {
        try {
            UserData.users = new XmlFileWorking().loadUsers(DataSaveLoad.XML_USERS);
            runServerInit();
            chooseLocale();
        } catch (IOException e) {
            out.println(Resources.language.getIO_ERROR());
        } catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (SAXException e) {
            out.println(Resources.language.getXML_ERROR());
        }
    }

    private static void chooseLocale() throws JDOMException {
        int N = 1024;
        UserData.rsa.init(N);
        int choice;
        while (true) {
            out.println(Resources.language.getSTART_CHOICE());
            try {
                // Выбор языка
                choice = Integer.parseInt(reader.readLine());
                switch (choice) {
                    case 1:
                        Resources.language = new Strings_RU();
                        readArrays();
                        mainMenu();
                        break;
                    case 2:
                        Resources.language = new Strings_EN();
                        readArrays();
                        mainMenu();
                        break;
                    case 3:
                        new XmlFileWorking().saveUsers(UserData.users, DataSaveLoad.XML_USERS);
                        System.exit(0);
                        break;
                    default:
                        out.println(Resources.language.getWRONG_CHOICE());
                        break;
                }
            } catch (IOException e) {
                out.println(Resources.language.getIO_ERROR());
            } catch (NumberFormatException ex) {
                out.println(Resources.language.getWRONG_CHOICE());
            }
        }
    }

    protected static void mainMenu() {

        out.println(Resources.language.getMAIN_MENU());
        int choice;
        try {
            choice = Integer.parseInt(reader.readLine());
            switch (choice) {
                case 1:
                    AddHandler.addMenu();
                    break;
                case 2:
                    SearchHandler.searchMenu();
                    break;
                case 3:
                    PrintHandler.showMenu();
                    break;
                case 4:
                    chooseLocale();
                    break;
                case 5:
                    exit();
                    break;
                default:
                    out.println(Resources.language.getWRONG_CHOICE());
                    mainMenu();
                    break;
            }
        } catch (NumberFormatException ex) {
            out.println(Resources.language.getWRONG_CHOICE());
            mainMenu();
        } catch (IOException e) {
            out.println(Resources.language.getIO_ERROR());
            mainMenu();
        } catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        }
    }

    protected static void exit() throws IOException {
        try {
            writeArrays();
            reader.close();
            out.close();
            System.exit(0);
        } catch (ParseException e) {
            out.println(Resources.language.getPARSE_ERROR());
        } catch (ClassNotFoundException e) {
            out.println(Resources.language.getCLASS_NOT_FOUND_ERROR());
        }
        catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        }
    }

    private static void readArrays() throws IOException {
        try {
            xmlFiles.loadAll(Resources.traditions, Resources.countries, Resources.holidays);
        } catch (ClassNotFoundException ex) {
            MainMenu.out.println(Resources.language.getNO_CLASS());
        }
        catch (JDOMException e) {
            MainMenu.out.println(Resources.language.getXML_ERROR());
        } catch (ParseException e) {
            MainMenu.out.println(Resources.language.getPARSE_ERROR());
        } catch (SAXException e) {
            MainMenu.out.println(Resources.language.getXML_ERROR());
        }
    }

    private static void writeArrays() throws IOException, JDOMException, ParseException, ClassNotFoundException {
        xmlFiles.saveAll(Resources.traditions, Resources.countries, Resources.holidays);
    }

    private static void runServerInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    myServer.serverInit();
                } catch (IOException e) {

                }

            }
        }).start();
    }
}
