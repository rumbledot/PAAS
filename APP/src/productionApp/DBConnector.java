package productionApp;

import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBConnector {
    private ArrayList<String> ingredientTypes;
    private Connection con;

    DBConnector(){
        try {
            String dbUser = "myuser";
            String usrPass = "mypass";
            Class.forName("org.mariadb.jdbc.Driver");
            //TODO confirm IP address for DB
            String url = "jdbc:mysql://localhost/mydbTesting";

            con = DriverManager.getConnection(url, dbUser, usrPass);

        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            System.out.println("Error in DBSaver constructor");
        }
    }

    public ArrayList<Order> getCurrentOrders(){
        ArrayList<Order> orderList = new ArrayList<>();
        //TODO SQL query to get orders
        return orderList;
    }

    private Order createOrder(Result r){
        //TODO Create new Order from SQL Result
        return null;
    }

    private ArrayList<Pizza> getPizzas(Order o){
        //TODO SQL Query to get all pizzas matching Order ID
        return null;
    }

    private Pizza createPizza(Result r){
        //TODO Create new Pizza from SQL Result
        return null;
    }

    private ArrayList<Ingredient> getIngredients(Pizza p){
        //TODO SQL Query to get ingredients matching Ingredient ID from Pizza
        return null;
    }

    public boolean updateOrderStatus(Order o,String status){
        //TODO SQL to update order status in DB
        return false;
    }

    public boolean updatePizzaStatus(Pizza p,String status){
        //TODO SQL to update pizza status in DB
        return false;
    }

    public ObservableList<Ingredient> getInventory(){
        //TODO SQL query to get all ingredients
        return null;
    }

    private Ingredient createIngredient(Result r){
        //TODO create new Ingredient from SQL Result
        return null;
    }

    private boolean updateIngredient(Ingredient i){
        //TODO SQL to update ingredient in DB
        return false;
    }


}
