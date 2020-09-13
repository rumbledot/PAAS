package productionApp;

import java.util.ArrayList;

public class Order {

    private String name;
    private ArrayList<Pizza> pizzas;
    private String status;
    private int ID;

    public Order(String name, String status, int ID) {
        setName(name);
        setStatus(status);
        setID(ID);
        pizzas = new ArrayList<>();
    }

    public void updateStatus() {

    }

    /**
     * GETTERS AND SETTERS
     *
     */

    public ArrayList<Pizza> getPizzas() {
        return pizzas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}