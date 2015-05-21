package model;

import functional.DateLabelFormatter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Holiday implements Serializable, Comparable<Holiday> {

    private String name;
    private Date startDate;
    private Date endDate;
    private HolidayType type;

    public Holiday (String name) {
        this.name = name;
        this.startDate = new Date();
        this.endDate = this.startDate;
        this.type = HolidayType.OTHER;
    }

    public Holiday(String name, String type) {
        this.name = name;
        this.startDate = new Date();
        this.endDate = this.startDate;
        this.type = HolidayType.valueOf(type);
    }

    public Holiday(String name, Date start, String typeNum) {
        this.name = name;
        this.startDate = start;
        this.endDate = null;
        this.type = HolidayType.valueOf(typeNum);
    }

    public Holiday(String name, Date start,  Date end, String typeNum) {
        this.name = name;
        this.startDate = start;
        this.endDate = end;
        this.type = HolidayType.valueOf(typeNum);
    }

    public String getName() {
        return  this.name;
    }

    public String toString() {
        DateLabelFormatter formatter = new DateLabelFormatter();
        String s;
        if (endDate.equals(startDate)) s = formatter.dateFormat(startDate);
        else s = String.format("%s-%s", formatter.dateFormat(startDate), formatter.dateFormat(endDate));

        return String.format("%30s%15s%15s",name,s,type);
    }

    public String getStartDate() {
        DateLabelFormatter formatter = new DateLabelFormatter();
        return formatter.dateFormat(this.startDate);
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

    @Override
    public int compareTo(Holiday holiday) {
        if (holiday.getName().charAt(0) > this.getName().charAt(0)) return -1;
        else if (holiday.getName().charAt(0) < this.getName().charAt(0)) return 1;
        return 0;
    }
}
