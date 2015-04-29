package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Holiday implements Serializable {
    private String name;
    private Date startDate;
    private Date endDate;
    private HolidayType type;
    // а почему поле общедоступно? да и не место ему в классе с праздниками. Создайте какой-нибудь DateHandler и вынесите туда
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");

// Если все эти конструкторы действительно нужны, то надо загнать всё это добро в паттерн Builder (см. википедию, второй вариант реализации)
    public Holiday (String name) {
        this.name = name;
        this.startDate = new Date();
        this.endDate = null;
        // присваивать null очень опасно. Будут падать неконтролируемые NPE или же вам придется тащить кучу проверок на null
        // если endDate не задан, то можно по умолчанию сделать совпадающим с startDate
        this.type = HolidayType.OTHER;
    }

    public Holiday(String name, int typeNum) {
        this.name = name;
        this.startDate = new Date();
        this.endDate = null;
        // а зачем вам обращение к элементу перечисления по номеру? 
        // можно и именем обойтись. А если кто-то изменит порядок следования элементов, будет очень плохо. 
        // А главное - будет невероятно сложно понять, из-за чего всё упало
        this.type = HolidayType.values()[typeNum];
    }

    public Holiday(String name, Date start, int typeNum) {
        this.name = name;
        this.startDate = start;
        this.endDate = null;
        this.type = HolidayType.values()[typeNum];
    }

    public Holiday(String name, Date start,  Date end, int typeNum) {
        this.name = name;
        this.startDate = start;
        this.endDate = end;
        this.type = HolidayType.values()[typeNum];
    }

    public Holiday() {
        // должны быть значения полей по умолчанию
    }

    public String getName() {
        return  this.name;
    }

    public String toString() {
        String s;
        if (endDate == null) s = dateFormat.format(startDate);//(!startDate.equals(endDate)) s = startDate.toString();
        else s = String.format("%s-%s",dateFormat.format(startDate),dateFormat.format(endDate));

        return String.format("%30s%15s%15s",name,s,type);
    }

    public String getStartDate() {
        return dateFormat.format(this.startDate);
    }

    public HolidayType getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Holiday holiday = (Holiday) o;

        if (!name.equals(holiday.name)) return false;
        if (!startDate.equals(holiday.startDate)) return false;
        if (type != holiday.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
