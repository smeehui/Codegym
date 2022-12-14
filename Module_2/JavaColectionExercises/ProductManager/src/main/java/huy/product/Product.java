package huy.product;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Product {
    private int amount;
    private String name;
    private double price;
    private String description;
    private int ID;
    private  String dateAdded;
    public Product(){}
    public Product(String name, int amount, double price, String description) {
        this.amount = amount;
        this.name = name;
        this.price = price;
        this.description = description.equals("")?"No description":description;
        this.dateAdded = generateDate();
        this.ID= generateID();
    }

    private String generateDate() {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of("UTC+7"));
        String strTime = time.toString().substring(11, 19) +
                         " " + time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + " ";
        return strTime;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int generateID() {
        return (int) (Math.random()*5);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return  getName() +"{" +
                "ID=" + getID() +
                ", amount=" + getAmount() +
                ", price=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                '}';
    }

}