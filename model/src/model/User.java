package model;

import javax.sound.midi.Patch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by root on 12.03.15.
 */
public class User {
    /*Задача класса сущности - описание сущности и ничего больше
    Никаких файловых операций в этом классе быть не должно даже близко.
    Для описания сущности нужно задать только поля, методы доступа, конструктор(ы) и некоторые дополнительные методы
    (например, equals() или hashCode())
    Т.е. в классе сущности вы просто задаете характеристики этой сущности. Сущность могут поместить в файл, отправить по сети,
    записать в базу - да еще много чего сделать. Вы пытаетесь всё это упихать в класс на примере файловых операций.
    Очень грубая ошибка
    Более того, файловые операции, как я вам говорил, - вещь довольно однотипная. Поэтому в любом случае такие вещи надо выносить
    в отдельные классы*/
    private final String ROOT = "resources/users/";
    private final String TRADITION_FILE = "/traditionSave.xml";
    private final String HOLIDAY_FILE = "/holidaySave.xml";
    private final String COUNTRY_FILE = "/countrySave.xml";
    private final String HOLIDAY_DEFAULT = "resources/xml/holidaySave.xml";
    private final String COUNTRY_DEFAULT = "resources/xml/countrySave.xml";
    private final String TRADITION_DEFAULT = "resources/xml/traditionSave.xml";
    private String login;
    private BigInteger pass;
    private BigInteger modules;
    //private RSA rsa;
    
    /// А вот тут непонятно, что у вас с моделью происходит
    // у традиции уже есть ссылки на праздник и страну. Зачем пользователю списки стран и праздников?
    // Они легко получатся из списка традиций. Лишнее надо убрать.
    private static LinkedList<Country> countries = new LinkedList<Country>();
    private static LinkedList<Holiday> holidays = new LinkedList<Holiday>();
    private static ArrayList<Tradition> traditions = new ArrayList<Tradition>();

    // Почему BigInteger??? У вас настолько огромные данные?
    // Если так будете делать, то для создания очередного пользователя придется бежать за дополнительной планкой оперативы
    
    // RSA лучше сделать статическим и не передавать. Не обязательно сообщать другим разработчикам, что у вас используется шифрование и какое именно
    // это нарушение инкапсуляции
    // Соответственно когда уберете файловые операции, исчезнет выброс исключения из конструктора. Тоже не совсем хороший подход
    public User(String login, BigInteger pass, BigInteger key, BigInteger modules, RSA rsa) throws IOException {

        this.login = login;
        //rsa = new RSA();

        this.modules = modules;
        rsa.setModulus(modules);
        rsa.setPublicKey(key);

        this.pass = rsa.encrypt(pass);

       File folder = new File(ROOT + this.login);
        if (folder.mkdir()) {
            File tradition = new File(ROOT + this.login + TRADITION_FILE);
            File country = new File(ROOT + this.login + COUNTRY_FILE);
            File holiday = new File(ROOT + this.login + HOLIDAY_FILE);

            File traditionDef = new File(TRADITION_DEFAULT);
            File countryDef = new File(COUNTRY_DEFAULT);
            File holidayDef = new File(HOLIDAY_DEFAULT);
System.out.println("Дошло до копирования"); // такой отладочной информации в сущности быть не должно.
            // а вдруг там, где используется сущность, нет командной строки???
            copyFile(traditionDef, tradition);
            copyFile(countryDef, country);
            copyFile(holidayDef, holiday);
            System.out.println("Копирование прошло");

                //if (!(tradition.createNewFile() & country.createNewFile() & holiday.createNewFile()))
                  //  throw new IOException();
        }
    }

    public User(String login, String pass, RSA rsa) throws IOException {
        this.login = login;

        // откуда взяли такой огромный айдишник и зачем??? Даже для объемов данных неткрекера таких длинных id нет
        // к тому же явно в коде прописано
        this.modules = new BigInteger("114300212443049308755638385038607092399228059171843074638659728066396329731870812301666900170326603999649607364454783561463395729169397992550553334308251756497995161575531048559625701582012129417669546314420880750128408561569822198960212709010390091463374475374736305384151906473683969549684741213893356703077");
        rsa.setModulus(this.modules);
        rsa.setPublicKey(rsa.getPublicKey());
        BigInteger pass_value = new BigInteger(pass);
        this.pass = pass_value;


    }

    public LinkedList<Country> getCountryList() {
        return countries;
    }

    public LinkedList<Holiday> getHolidayList() {
        return holidays;
    }

    public static ArrayList<Tradition> getTraditionList() {
        return traditions;
    }

    public void setCountryList(LinkedList<Country> list) {
        countries = list;
    }

    public void setHolidayList(LinkedList<Holiday> list) {
        holidays = list;
    }

    public void setTraditionList(ArrayList<Tradition> list) {
        traditions = list;
    }

    public void setModules(String value, RSA rsa) {
        rsa.setModulus(new BigInteger(value));
    }

    public void setPass(String pass) {
        this.pass = new BigInteger(pass);
    }

    public BigInteger getPass() {
        return pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public BigInteger getModules() {
        return this.modules;
    }

    public boolean isAdmin() {
        // такие часто используемые строки должны быть вынесены в классы констант
        // а если стандартный логин у админа станет другим, вы будете искать эту сущность долго и упорно
        // да и поддерживать код можете не только вы
        return "admin".equals(login);
    }
    // вынести в классы с файловыми операциями
    public static void copyFile(File in, File out) throws IOException {

        byte buffer[] = new byte[100000000];
        // исходя из чего взят такой размер буфера?
        try {
            FileInputStream fileIn = new FileInputStream(in);
            int bytes = fileIn.read(buffer,0,100000000);
            fileIn.close();

            FileOutputStream fileOut = new FileOutputStream(out);
            fileOut.write(buffer,0,bytes);
            fileOut.close();
        }
        catch (Exception e) {
            // тут сразу две ошибки. найдите сами
        }

    }
    // вынести в классы с файловыми операциями
    public static void copy(File source, File dest) throws IOException {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        try {
            FileChannel destChannel = new FileOutputStream(dest).getChannel();
            try {
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            } finally {
                destChannel.close();
            }
        } finally {
            sourceChannel.close();
        }
    }

}
