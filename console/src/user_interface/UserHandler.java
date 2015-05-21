package user_interface;

import functional.*;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;

/**
 * Created by root on 15.03.15.
 */
public class UserHandler {
    protected static PrintWriter out = new PrintWriter(System.out, true);
    protected static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /*************************
     * Methods
     *************************/
    //Сам метод регистрации.
    private static void registration() {
        String login,
                pass1,
                pass2;

        try {
            while (true) {
                out.println(Resources.language.getLOGIN());
                login = reader.readLine();
                if (Registration.checkLogin(login)) {
                    throw new IllegalArgumentException(Resources.language.getLOGIN_EXCEPTION());
                }
                out.println(Resources.language.getPASS());
                pass1 = reader.readLine();
                out.println(Resources.language.getPASS());
                pass2 = reader.readLine();
                if (pass1.equals(pass2)==true){ Registration.registration(login, pass1, pass2);}
                else { throw new IllegalArgumentException(Resources.language.getWRONG_PASSWORD()); }
                break;
            }
        } catch (IllegalArgumentException e) {
            out.println(Resources.language.getWRONG_CHOICE());
        } catch (IOException e) {
            out.println(Resources.language.getIO_ERROR());
            MainMenu.mainMenu();
        } catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (SAXException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (ParseException e) {
            out.println(Resources.language.getPARSE_ERROR());
        }
    }
    //Автроизация пользователя.
    private static void authorization() throws JDOMException, SAXException, ParseException {
        String login,
                pass;
        while (true) {
            out.println(Resources.language.getLOGIN());
            try {
                login = reader.readLine();
                out.println(Resources.language.getPASS());
                pass = reader.readLine();
                loadUserData(login, pass);
            } catch (IOException exc) {
                out.println(Resources.language.getIO_ERROR());
            }
            MainMenu.mainMenu();
        }
    }
    //Загрузка даных пользователя.
    protected static void loadUserData(String login, String pass) throws JDOMException, SAXException, ParseException, IOException {
        String message = UserData.loadData(login, pass, Resources.traditions, Resources.countries,
                Resources.holidays);
        if (!message.isEmpty()) {
            out.println(Resources.language.getLOGIN_OR_PASS_EXCEPTION());
            authorization();
        }
        else out.println(Resources.language.getHELLO_USER() + login);
    }
    //Меню авторизации.
    protected static void logIn() {
        out.println(Resources.language.getENTER_MESSAGE());
        int choice;
        try {
            choice = Integer.parseInt(reader.readLine());
            switch (choice) {
                case 1:
                    authorization();
                    MainMenu.mainMenu();
                    break;
                case 2:
                    registration();
                    MainMenu.mainMenu();
                    break;
                case 3:
                    MainMenu.mainMenu();
                case 4:
                    MainMenu.exit();
                default:
                    out.println(Resources.language.getWRONG_CHOICE());
                    logIn();
                    break;
            }
        } catch (IOException e) {
            out.println(Resources.language.getIO_ERROR());
            logIn();
        } catch (JDOMException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (SAXException e) {
            out.println(Resources.language.getXML_ERROR());
        } catch (ParseException e) {
            out.println(Resources.language.getPARSE_ERROR());
        }
    }

}



