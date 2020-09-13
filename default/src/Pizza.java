import java.util.ArrayList;

public class Pizza {
    private DBConnector db;

    private ArrayList<Ingredient> ingredients;
    private String status;
    private int ID;
    private Order order;

    public Pizza(String status, int ID, Order order, DBConnector db) {
        this.db = db;
        this.ingredients = new ArrayList<>();
        setID(ID);
        setStatus(status);
        setOrder(order);
    }

    /**
     * updates the status of the pizza and calls updateStatus on the connected order
     * @param status new value for status
     */
    public void updateStatus(String status) {
//        System.out.println("update Pizza status to " + status);
        setStatus(status);
        db.updatePizzaStatus(this,status);
        order.updateStatus(this,status);
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