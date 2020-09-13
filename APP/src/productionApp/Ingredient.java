package productionApp;

public class Ingredient {

    private String name;
    private int quantity;
    private int ID;
    private String supplierEmail;
    private String type;


    public Ingredient(String name, String type, int quantity, int ID, String supplierEmail) {
        setName(name);
        setType(type);
        setQuantity(quantity);
        setID(ID);
        setSupplierEmail(supplierEmail);
    }

    public void update() {

    }

    public void reorder() {

    }


    /**
     * GETTERS AND SETTERS
     *
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}