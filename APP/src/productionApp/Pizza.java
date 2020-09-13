package productionApp;

import java.util.ArrayList;

public class Pizza {

    private ArrayList<Ingredient> ingredients;
    private String status;
    private int ID;
    private Order order;

    public Pizza(ArrayList<Ingredient> ingredients, String status, int ID, Order order) {
        this.ingredients = new ArrayList<>();
        setID(ID);
        setStatus(status);
        setOrder(order);
    }


    public void updateStatus() {

    }




    /**
     * GETTERS AND SETTERS
     *
     */

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}