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

    public Ingredient(String name, String type, int quantity, String supplierEmail) {
        setName(name);
        setType(type);
        setQuantity(quantity);
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
        if(name.length()>30){
            name = name.substring(0,29);
        }
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
        if(supplierEmail.length()>30){
            supplierEmail = supplierEmail.substring(0,29);
        }
        this.supplierEmail = supplierEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(type.length()>30){
            type = type.substring(0,29);
        }
        this.type = type;
    }
}