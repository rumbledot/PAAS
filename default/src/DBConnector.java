import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;



public class DBConnector {
    private ArrayList<String> ingredientTypes;
    private Connection con;

     DBConnector(){


//        printIncompleteOrderList();
    }

    public boolean connectDB(String dbUser, String usrPass){
        try {
//            String dbUser = "myuser";
//            String dbUser = "alien";
//            String usrPass = "mypass";
//            String usrPass = "Passw0rd";
            Class.forName("org.mariadb.jdbc.Driver");
            //TODO confirm IP address for DB
            String url = "jdbc:mariadb://10.140.155.18:3306/paasOne";
//            String url = "jdbc:mariadb://localhost/paasOne";
//            String url = "jdbc:mariadb://spacepizza.ceybgcu06f5n.us-west-2.rds.amazonaws.com:3306/spacepizza";

            con = DriverManager.getConnection(url, dbUser, usrPass);
            return true;
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            System.out.println("Error in DBSaver constructor");
            return false;
        }
    }


    /**
     * Gets all orders that are not 'complete' from database. Adds to arraylist and returns arraylist.
     *
     * @return orderList        arraylist of orders that do not have the status 'complete'
     */
    public ArrayList<Order> getCurrentOrders(){
        ArrayList<Order> orderList = new ArrayList<Order>();
        try {

            // System.out.println("GetCurrentOrders - Statement Created");

            String sql = "SELECT * FROM orders WHERE NOT order_status=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,"completed");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orderList.add(createOrder(rs.getString("name"), rs.getString("order_status"), rs.getInt("ID"), rs.getTimestamp("submit_time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return orderList;
    }

    /**
     * Test method to print orderlist array.
     */

    public void printIncompleteOrderList() {
        System.out.println("Started test");
        ArrayList<Order> orderList = getCurrentOrders();
       for (Order o : orderList) {
           System.out.println("Order: " + o.getName() + "; Pizzas: " + o.getPizzas().size());
       }
    }

    /**
     * Creates order object from database result.
     *
     * @param name     name from getCurrentOrders
     * @param order_status     orderStatus from getCurrentOrders
     * @param id     id from getCurrentOrders
     * @return order        Returns single order object once created.
     */
    protected Order createOrder(String name, String order_status, int id, Timestamp submit_time){
        Order order = new Order(name, order_status, id, submit_time, this);
        getPizzas(order);
        return order;
    }

    /**
     * Gets all the pizzas and associates them to specific order.
     * @param o     order that the pizzas pulled will be associated to.
     * @return  returns arraylist of pizzas that will be attached to an order.
     */
    private ArrayList<Pizza> getPizzas(Order o){

        try {

            // System.out.println("GetCurrentOrders - Statement Created");

            String sql = "SELECT * FROM pizzas WHERE order_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,o.getID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
//                System.out.println("found pizza");
               o.getPizzas().add(createPizza(rs.getInt("id"),rs.getString("status"), rs.getInt("base"), rs.getInt("sauce"), rs.getInt("t1"), rs.getInt("t2"), rs.getInt("t3"), rs.getInt("t4"), rs.getInt("t5"), rs.getInt("t6"), o));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
//        System.out.println("Total pizzas: " + o.getPizzas().size());
        return o.getPizzas();
    }

    /**
     * Creates a pizza object
     * @param status    initial status of the pizza
     * @param base      ingredient id being added to pizza
     * @param sauce     ingredient id being added to pizza
     * @param t1    ingredient id being added to pizza
     * @param t2    ingredient id being added to pizza
     * @param t3    ingredient id being added to pizza
     * @param t4    ingredient id being added to pizza
     * @param t5    ingredient id being added to pizza
     * @param t6    ingredient id being added to pizza
     * @param o     order the pizza is being attached to.
     * @return  pizza object.
     */
    private Pizza createPizza(int id, String status, int base, int sauce, int t1, int t2, int t3, int t4, int t5, int t6, Order o){

        Pizza p = new Pizza( status, id, o, this);
        getIngredients(p, base, sauce, t1, t2, t3, t4, t5, t6);
        return p;
    }

    /**
     * Gets the ingredients being attached to a pizza
     * @param p     pizza object ingredients are being attached to.
     * @param base      ingredient id being added to pizza
     * @param sauce     ingredient id being added to pizza
     * @param t1        ingredient id being added to pizza
     * @param t2        ingredient id being added to pizza
     * @param t3        ingredient id being added to pizza
     * @param t4        ingredient id being added to pizza
     * @param t5        ingredient id being added to pizza
     * @param t6        ingredient id being added to pizza
     * @return      Arraylist of pizzas being added to object.
     */
    private ArrayList<Ingredient> getIngredients(Pizza p, int base, int sauce, int t1, int t2, int t3, int t4, int t5, int t6){

        ArrayList<Integer> notNullIngredient = new ArrayList<>();
        if (base != 99) {
            notNullIngredient.add(base);
        }

        if (sauce != 99) {
            notNullIngredient.add(sauce);
        }

        if (t1 != 99) {
            notNullIngredient.add(t1);
        }

        if (t2 != 99) {
            notNullIngredient.add(t2);
        }

        if (t3 != 99) {
            notNullIngredient.add(t3);
        }
        if (t4 != 99) {
            notNullIngredient.add(t4);
        }
        if (t5 != 99) {
            notNullIngredient.add(t5);
        }
        if (t6 != 99) {
            notNullIngredient.add(t6);
        }

        try {
            Statement stmt = con.createStatement();
            // System.out.println("GetCurrentOrders - Statement Created");

            for (int i = 0; i < notNullIngredient.size(); i++) {
                String sql = "SELECT * FROM ingredients WHERE id='"+ notNullIngredient.get(i) +"'";

                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()) {
                    Ingredient ingr = createIngredient(rs.getString("name"), rs.getString("type"), rs.getInt("id"), rs.getInt("quantity"), rs.getString("supplier_email"));
                    p.getIngredients().add(ingr);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }

        return p.getIngredients();
    }

    /**
     * Creates the ingredient option
     * @param name      String, name of the ingredient.
     * @param type      String, type of the ingredient
     * @param id        ID of the ingredient in db
     * @param quan      int, quantity of the ingredient available.
     * @param supplierEmail     string, supplier email.
     * @return  returns ingredient object.
     */
    private Ingredient createIngredient(String name, String type, int id, int quan, String supplierEmail){
        Ingredient i = new Ingredient(name, type, quan, id, supplierEmail);
        return i;
    }

    /**
     * Updates the order status in the database.
     *
     * @param o     order object being updated
     * @param status        status string that is being updated in database.
     * @return      true if successful, false if not.
     */

    public boolean updateOrderStatus(Order o,String status){
        try {
            Statement stmt = con.createStatement();
            int orderID = o.getID();
            String sql = "UPDATE orders SET order_status='"+ status +"' WHERE id='"+ orderID +"'";

            ResultSet rs = stmt.executeQuery(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return false;
    }

    /**
     * Updates pizza cooking status. Queued, Started, Complete.
     * @param p     Pizza object that is being updated.
     * @param status    New Status string to be amended to pizza.
     * @return  true if successful; false if not.
     */
    public boolean updatePizzaStatus(Pizza p,String status){
        try {

            int pizzaID = p.getID();
            if(con==null){
                System.out.println("Connection gone?");
            }
            String sql = "UPDATE pizzas SET status= ? WHERE id= ?";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setString(1,status);
            prep.setInt(2,pizzaID);
//            Statement stmt = con.createStatement();
            ResultSet rs = prep.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return false;
    }

    /**
     * Populates an arraylist of every ingredient in database.
     *
     * @return totalInventory
     */

    public ArrayList<Ingredient> getInventory(){
       ArrayList<Ingredient> totalInventory = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
                String sql = "SELECT * FROM ingredients";

                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {
//                    System.out.println("Added ingredient " + rs.getString("name"));
                    totalInventory.add(createIngredient(rs.getString("name"), rs.getString("type"), rs.getInt("id"), rs.getInt("quantity"), rs.getString("supplier_email")));
                }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }

        return totalInventory;
    }

    /**
     * Updates an ingredient object in the database.
     *
     * @param i     Ingredient object to get data from and amend.
     * @return true if object updated in db; false if not.
     */
    public boolean updateIngredient(Ingredient i){
        try {

            int ingredientID = i.getID();
            String name = i.getName();
            String type = i.getType();
            int quantity = i.getQuantity();
            String supplieremail = i.getSupplierEmail();


            String sql = "UPDATE ingredients SET name=?, type=?, quantity=?, supplier_email=? WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,type);
            stmt.setInt(3,quantity);
            stmt.setString(4,supplieremail);
            stmt.setInt(5,ingredientID);

            ResultSet rs = stmt.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return false;

    }

    /**
     * Adds new ingredient to database.
     * @param i
     * @return true if successful, false if not.
     */

    public boolean addIngredient(Ingredient i) {
        try {

            int ingredientID = i.getID();
            String name = i.getName();
            String type = i.getType();
            int quantity = i.getQuantity();
            String supplieremail = i.getSupplierEmail();


            String sql = "INSERT INTO ingredients (name, type, quantity, supplier_email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,type);
            stmt.setInt(3,quantity);
            stmt.setString(4,supplieremail);

            ResultSet rs = stmt.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return false;


    }

    /**
     * Removes an ingredient from the database.
     * @param i     ingredient object to be removed.
     * @return true if successful, false if not.
     */
    public boolean removeIngredient(Ingredient i) {
        try {

            int ingredientID = i.getID();


            Statement stmt = con.createStatement();
            String sql = "DELETE FROM ingredients WHERE id='"+ingredientID+"'";

            ResultSet rs = stmt.executeQuery(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("");
        }
        return false;

    }





}


/**
 * METHOD TO INSERT EVERYthinG INTO DATABASE. ONLY USE ONCE>
 */


//    public void insertIngredientstoDB() {
//        //RUN ONLY ONCE
//        try {
//            Statement stmt = con.createStatement();
//
//                String sql = "INSERT INTO ingredients VALUES (id, 'base', 'base', 20, 'hello@bidvest.co.nz')";
//                ResultSet rs = stmt.executeQuery(sql);
//
//             sql = "INSERT INTO ingredients VALUES (id, 'sauce', 'sauce', 20, 'hello@bidvest.co.nz')";
//             rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'mozzarella', 'cheese', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'camembert', 'cheese', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'vegan mozzarella', 'cheese', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'salami', 'meat', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'chicken', 'meat', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'bacon', 'meat', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'vegan chicken', 'meat', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'spinach', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'mushrooms', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'tomato', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'olives', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'onions', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'capsicum', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'jalapenos', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//            sql = "INSERT INTO ingredients VALUES (id, 'pineapple', 'veggies', 20, 'hello@bidvest.co.nz')";
//            rs = stmt.executeQuery(sql);
//
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("");
//        }
//
//    }