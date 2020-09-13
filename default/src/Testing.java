import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Testing {

    DBConnector db;

    Testing(){
        db = new DBConnector();
        db.connectDB("myuser", "mypass");
    }

    @Test
    void getCurrentOrders() {
        ArrayList<Order> orders = db.getCurrentOrders();
        for(int i = 0; i < orders.size(); i++) {
            System.out.println("Order: " + orders.get(i).getName() + ", Status: " + orders.get(i).getStatus());
            for (int j = 0; j < orders.get(i).getPizzas().size(); j++) {
                System.out.println("Pizza: " + orders.get(i).getPizzas().get(j).getID());
                for (int ii = 0; ii < orders.get(i).getPizzas().get(j).getIngredients().size(); ii++){
                    System.out.println("Ingredient: " + orders.get(i).getPizzas().get(j).getIngredients().get(ii).getName());
                }
            }
        }

        assertEquals(1, orders.size());
    }

    @Test
    void updateOrderStatus() {
        ArrayList<Order> currentOrders = db.getCurrentOrders();
        System.out.println(currentOrders.size());
        Order o = currentOrders.get(0);
        String s = o.getStatus();

        if (s.equals("queued")) {
           db.updateOrderStatus(o, "completed");

           assertEquals("completed", o.getStatus());
        } else if (s.equals("completed")) {
            db.updateOrderStatus(o, "queued");
            assertEquals("queued", o.getStatus());
        } else {
            fail("string wrong from the start. ");
        }
    }

    @Test
    void addIngredient() {
        Ingredient i = new Ingredient("gf base", "base", 7,"ggg@ggg.com");
        ArrayList<Ingredient> totalInventory = db.getInventory();
        db.addIngredient(i);
        ArrayList<Ingredient> newTotal = db.getInventory();
        assertEquals(totalInventory.size() + 1, newTotal.size());

    }

    @Test
    void createDBPizza() {

        ArrayList<Order> inven = db.getCurrentOrders();
        for (Order o :
    inven ) {
            System.out.println(o.getID());
        }
       // Pizza p = db.createPizza(1,"queued", 2,3,4,5,6,7,8,0, inven.get(0));
      //  assertEquals(p.getID(), 1);
    }

    @Test
    void getDBIngredients() {
        //@TODO
    }

    @Test
    void removeDBIngredient() {
        //
    }

    @Test
    void getPizzas() {
        //@TODO
    }

    @Test
    void updateIngredient() {
        //@TODO
    }

    @Test
    void updateDBOrderStatus() {
        //@TODO
    }

    @Test
    void updateDBPizzaStatus() {
        //@TODO
    }


    @Test
    void getInventory() {
        ArrayList<Ingredient> arrayOfIngredients = db.getInventory();
        int expectedResult = 1;
        int actualResult = arrayOfIngredients.size();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void createOrder(){
        Order o = db.createOrder("order1", "queued", 2, new Timestamp(System.currentTimeMillis()));
        assertEquals("order1", o.getName());
    }

    Order makeTestOrder(){
        Timestamp time = new Timestamp (System.currentTimeMillis());
        Order o = new Order("test order","queued",1111, time, db);
        Ingredient i = new Ingredient("test ing","base",1,1111,"testemail");
        Pizza p = new Pizza("queued",1111,o, db);
        Pizza p1 = new Pizza("queued",1111,o, db);
        Pizza p2 = new Pizza("queued",1111,o, db);
        p.getIngredients().add(i);
        p1.getIngredients().add(i);
        p2.getIngredients().add(i);
        ArrayList<Pizza> pizzas = new ArrayList<>();
        pizzas.add(p1);
        pizzas.add(p2);
        o.setPizzas(pizzas);
        return o;
    }

    @Test
    void updateStatusOrder(){
        Order o = makeTestOrder();
        String expectedResult = "queued";
        String actualResult = o.getStatus();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void updateStatusPizza1(){
        Order o = makeTestOrder();
        o.getPizzas().get(0).updateStatus("started");
        String expectedResult = "started";
        String actualResult = o.getStatus();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void updateStatusPizza2(){
        Order o = makeTestOrder();
        for(int i = 0; i< o.getPizzas().size(); i++) {
            o.getPizzas().get(i).updateStatus("completed");
        }
        String expectedResult = "completed";
        String actualResult = o.getStatus();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void updateStatusPizza3(){
        Order o = makeTestOrder();
        for(int i = 0; i< o.getPizzas().size()/2; i++) {
            o.getPizzas().get(i).updateStatus("completed");
        }
        String expectedResult = "started";
        String actualResult = o.getStatus();
        assertEquals(expectedResult,actualResult);
    }


}