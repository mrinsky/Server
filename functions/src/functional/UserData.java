package functional;
import model.*;
        import org.jdom2.JDOMException;
        import org.xml.sax.SAXException;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.math.BigInteger;
        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.List;

/**
 * Created by root on 05.04.15.
 */

public class UserData {
    public static int traditionCount = 0;
    public static int countryCount = 0;
    public static int holidayCount = 0;
    public static RSA rsa = new RSA();
    public static User currentUser;
    XmlFileWorking xmlFileWorking = new XmlFileWorking();

    public static ArrayList<User> users = new ArrayList<User>();


    public static void registration(String login, String pass1, String pass2,ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays
    ) throws IllegalArgumentException, IOException, JDOMException, SAXException, ParseException {
        if (pass1.equals(pass2)) {
            rsa.setModulus(new BigInteger("114300212443049308755638385038607092399228059171843074638659728066396329731870812301666900170326603999649607364454783561463395729169397992550553334308251756497995161575531048559625701582012129417669546314420880750128408561569822198960212709010390091463374475374736305384151906473683969549684741213893356703077"));
            BigInteger pass = new BigInteger(pass1.getBytes());
            User user = new User(login, pass, rsa.getPublicKey(),
                    rsa.getModulus(),rsa);
            users.add(user);
            loadData(login, pass1, traditions, countries, holidays);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String loadData(String login, String pass, ArrayList<Tradition> traditions, LinkedList<Country> countries, LinkedList<Holiday> holidays) throws JDOMException, SAXException, ParseException, IOException {

        if (authentication(login, pass)) {
            traditionCount = traditions.size();
            countryCount = countries.size();
            holidayCount = holidays.size();

            currentUser = users.get(Search.searchIndex(users, login));

            new XmlFileWorking().loadUser(traditions,countries,holidays);

            return "ok";

        }
        return "Error";
    }

    public static void logOut(ArrayList<Tradition> traditions, List<Country> countries, List<Holiday> holidays) throws IOException {

        ArrayList<Tradition> tr_list = new ArrayList<Tradition>();
        for (int i = traditionCount; i < traditions.size(); i++) {
            tr_list.add(traditions.get(i));
        }
        currentUser.setTraditionList(tr_list);
        Remove.removeListTradition(tr_list,traditions);
        LinkedList<Country> c_list = new LinkedList<Country>();
        for (int i = countryCount; i < countries.size(); i++) {
            c_list.add(countries.get(i));
        }
        currentUser.setCountryList(c_list);
        Remove.removeListCountry(c_list,countries);
        LinkedList<Holiday> h_list = new LinkedList<Holiday>();
        for (int i = holidayCount; i < holidays.size(); i++) {
            h_list.add(holidays.get(i));
        }
        currentUser.setHolidayList(h_list);
        Remove.removeListHoliday(h_list,holidays);

        new XmlFileWorking().saveUser(traditions,holidays,countries);
        traditionCount = 0;
        countryCount = 0;
        holidayCount = 0;
    }

    public static boolean authentication(String login, String pass) {
        int index = -1;
        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getLogin().equals(login)) {
                index = i;

            }
        }

        UserData.rsa.setModulus(new BigInteger("114300212443049308755638385038607092399228059171843074638659728066396329731870812301666900170326603999649607364454783561463395729169397992550553334308251756497995161575531048559625701582012129417669546314420880750128408561569822198960212709010390091463374475374736305384151906473683969549684741213893356703077"));
        BigInteger message = new BigInteger(pass.getBytes());
        BigInteger encrypt = UserData.rsa.encrypt(message);
        System.out.println(encrypt);
        System.out.println(users.get(index).getPass());
        return (index > -1) && encrypt.equals(users.get(index).getPass());
    }
}

