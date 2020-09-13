import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {
    private DBConnector db;

    private String name;
    private ArrayList<Pizza> pizzas;
    private String status;
    private int ID;
    private Timestamp submitTime;

    public Order(String name, String status, int ID, Timestamp submitTime, DBConnector db) {
        this.db = db;
        setName(name);
        setStatus(status);
        setID(ID);
        setSubmitTime(submitTime);
        pizzas = new ArrayList<>();
    }

    /**
     * method to update status of a pizza, and then change order status as required
     * @param pizza the pizza that has changed status
     * @param status the status the pizza needs to change to
     */
    public void updateStatus(Pizza pizza, String status) {
        int i = pizzas.indexOf(pizza);
        pizzas.get(i).setStatus(status);
//        System.out.println("pizza status " + status);
        String newStatus = checkStatus(0,getStatus());
        setStatus(newStatus);
//        System.out.println("new status " + newStatus);
        db.updateOrderStatus(this,getStatus());
    }

    /**
     * recursive method to check all pizza statuses and determine new status for order
     * @param i index to check in pizzas array list
     * @param newStatus status determined by last pizza checked
     * @return correct status for order based on status of all pizzas
     */
    private String checkStatus(int i, String newStatus) {
//        System.out.println("check pizza " + i);
        if (i < pizzas.size()) {
            Pizza p = pizzas.get(i);
//            if (newStatus.equals(p.getStatus())) {
//                return checkStatus(i++, newStatus);
//            }
            if (p.getStatus().equals("started")) {
                return "started";
            } else if (p.getStatus().equals("completed")){
                return checkStatus(i+1,"completed");
            } else if(p.getStatus().equals("queued")){
                if(newStatus.equals("completed")){
                    return "started";
                }
            }
        }
        return newStatus;
    }


    /**
     * GETTERS AND SETTERS
     *
     */

    public ArrayList<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(ArrayList<Pizza> pizzas){ this.pizzas = pizzas;}

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

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }
}