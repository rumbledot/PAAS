import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Class to manage orders and pizzas
 */
public class ManageOrders {


    private DBConnector db;
    private VBox orderList;
    private GridPane pizzaList;

    /**
     *
     */
//    private int completeTime = (13*60) + 20;
    private int completeTime = 20;

    //Formatting values
    private String startedHex = "#c3e8c1";
    private String dueHex = "#edb2bf";
    private String dueTextHex = "#b50b30";

    private String lightHex = "#c7d7f2";
    private String darkestHex = "#2f4263";
    private String darkHex = "#415780";
    private String medHex = "#627ba8";

    private String selectedStyle = "-fx-background-color:" + lightHex + ";";
//    private String blankStyle = "-fx-background-color:" + lightHex + "; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px;";
    private String overdueStyle = "-fx-background-color:" + dueHex + ";";
    private String startedStyle = "-fx-background-color:" + startedHex + ";";
    private String unselectedStyle = "-fx-background-color: " + medHex + ";";
    private String pizzaBoxStyle = "-fx-background-color: " + lightHex + "; -fx-border-style: solid; -fx-border-color: black; -fx-border-width: 1px";

    private Font subHeaderFont = Font.font("Verdana", FontWeight.BOLD, 10);
    private Font midHeaderFont = Font.font("Verdana", FontWeight.BOLD, 15);
    private Font headerFont = Font.font("Verdana", FontWeight.BOLD, 20);
    private Border selectedBorder = new Border(new BorderStroke(Color.web(lightHex), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));
    private Border unselectedBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));

    /**
     * Creates new ManageOrders object
     * @param pizzaList the GridPane in the MainWindow that displays the pizza list
     * @param db the DBConnector started on the login screen
     */
    public ManageOrders(GridPane pizzaList, DBConnector db){
        this.db = db;
        orderList = new VBox();
        this.pizzaList = pizzaList;
//        System.out.println("Got to manageorders constructor");
        setPizzaView(0);

    }

    /**
     * get the list of current orders in a format usable by javafx
     * @return VBox containing all current orders
     */
    public VBox getOrderList(){
        displayIncompleteOrders();
        return orderList;
    }

    /**
     * Retrieve orders from the database and add each to the order view
     */
    public void displayIncompleteOrders() {
//        System.out.println("");
//        orderList.getChildren().add(new Text("Orders:"));
        ArrayList<Order> orders = db.getCurrentOrders();
        for (Order o : orders) {
            getOrderView(o);
        }
    }

    /**
     * fills the remaining unused boxes in the pizza view with blank boxes
     * @param i box number to start filling blank boxes
     */
    private void setPizzaView(int i){
        while (i < 6){
            VBox pizzaBlank = new VBox();
//            Text count = new Text  ("Box " + i);
//            pizzaBlank.getChildren().add(count);
            pizzaBlank.setStyle(unselectedStyle);
            pizzaBlank.setBorder(unselectedBorder);
            int col = 0;
            int row = 0;
            if(i%2 !=0){
                col = 1;
            }
            if(i > 3){
                row = 2;
            }
            else if(i > 1){
                row = 1;
            }
            pizzaList.add(pizzaBlank,col,row);
            i++;
        }
        pizzaList.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 2);
    }

    /**
     * Creates an order view for one order and adds to orderList view
     * @param o Order to be added
     */
    private void getOrderView(Order o) {

        VBox orderBox = new VBox();
        if(o.getStatus().equals("started")){
            orderBox.setStyle(startedStyle);
        }
        else {
            orderBox.setStyle(unselectedStyle);
        }
        orderBox.setBorder(unselectedBorder);
        orderBox.setPrefWidth(380);
        orderBox.setPadding(new Insets(10));
        orderBox.setAlignment(Pos.CENTER);
        orderBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for(int i = 0; i< orderList.getChildren().size(); i++){
                    VBox otherOrder = (VBox) orderList.getChildren().get(i);
                    boolean started = false;
                    for(int j = 2; j < otherOrder.getChildren().size(); j++){
                        HBox row = (HBox)otherOrder.getChildren().get(j);
                        Text status = (Text)row.getChildren().get(row.getChildren().size()-1);
                        if(status.getText().equals("started") || status.getText().equals("completed")){
                            started = true;
                        }
                    }
                    if(started) {
                        otherOrder.setStyle(startedStyle);
                    } else {
                        otherOrder.setStyle(unselectedStyle);
                    }
                    otherOrder.setBorder(unselectedBorder);
                }

                if(o.getStatus().equals("started")){
                    orderBox.setStyle(startedStyle);
                }
                else {
                    orderBox.setStyle(selectedStyle);
                }
                orderBox.setBorder(selectedBorder);
                displaySingleOrder(o);
//                System.out.println("Open pizza details on other side");
            }
        });
        VBox header = new VBox();
        header.setAlignment(Pos.TOP_CENTER);
        Text nameHeader = new Text("#" + o.getID() + ": " + o.getName());
        nameHeader.setFont(headerFont);
        HBox pizzaHeader = new HBox();
        pizzaHeader.setAlignment(Pos.CENTER);
        pizzaHeader.setPadding(new Insets(5));
        pizzaHeader.setSpacing(40);
        Text pizzaTitle = new Text("PIZZA");
        pizzaTitle.setFont(midHeaderFont);
        Text statusTitle = new Text("STATUS");
        statusTitle.setFont(midHeaderFont);
        pizzaHeader.getChildren().addAll(pizzaTitle,statusTitle);

        orderBox.getChildren().addAll(nameHeader,pizzaHeader);

        for(int i = 0; i< o.getPizzas().size(); i++){
            HBox pizzaRow = new HBox();
            pizzaRow.setPadding(new Insets(0,25,0,25));
            pizzaRow.setSpacing(50);
            Text pizza = new Text("Pizza " + o.getPizzas().get(i).getID());
            Text status = new Text(o.getPizzas().get(i).getStatus());
            Region buffer = new Region();
            pizzaRow.getChildren().addAll(pizza,buffer,status);
            pizzaRow.setHgrow(buffer, Priority.ALWAYS);
            pizzaRow.setAlignment(Pos.CENTER);
            orderBox.getChildren().add(pizzaRow);
        }
        HBox timer = new HBox();
        timer.setAlignment(Pos.BASELINE_CENTER);
        timer.setPadding(new Insets(10,10,5,10));
        Timestamp startTime = o.getSubmitTime();
//        System.out.println("Order submitted: " + o.getSubmitTime().toString());
        Text timerText = new Text();
        timerText.setFont(midHeaderFont);
        Text due = new Text("Due: ");
        due.setFont(midHeaderFont);
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long dueTime = startTime.getTime() + (completeTime*60000);
                long countdown = (dueTime - System.currentTimeMillis());
                long countmin = TimeUnit.MILLISECONDS.toMinutes(countdown);
                long countsec = TimeUnit.MILLISECONDS.toSeconds(countdown-(countmin*60000));
                DecimalFormat format = new DecimalFormat("#.00");
                String secs = "0" + Math.abs(countsec);
                if(secs.length()>2){
                    secs = secs.substring(1);
                }
                timerText.setText(Math.abs(countmin) + ":" + secs);
                if(countdown<0){
                    due.setText("OVERDUE: ");
                    orderBox.setStyle(overdueStyle);
                    timerText.setFill(Color.web(dueTextHex));
                }
            }
        };
        animationTimer.start();



        timer.getChildren().addAll(due,timerText);

        orderBox.getChildren().add(timer);
        orderList.getChildren().add(orderBox);
    }

    /**
     * Dills the Pizza view with the pizzas for a selected order
     * @param order Order to be displayed
     */
    public void displaySingleOrder(Order order) {
//        ColumnConstraints pizzaCols = new ColumnConstraints();
//        pizzaCols.setHgrow(Priority.ALWAYS);
//        pizzaList.getColumnConstraints().addAll(pizzaCols, pizzaCols);
        int col = 0;
        int row = 0;
        int count = 0;
        int i = 0;
        while (count < 6 && i < order.getPizzas().size()){
                Pizza p = order.getPizzas().get(i);
                if(!p.getStatus().equals("completed")){
                    VBox pizzaBox = getPizzaBox(p);
                    pizzaList.add(pizzaBox, col, row);
                    count++;
                    if (col == 0) {
                        col = 1;
                    } else {
                        col = 0;
                        row++;
                    }
                }
                i++;
            }
            setPizzaView(count);

    }

    /**
     * Creates a box with the details for a given Pizza
     * @param p Pizza to be displayed
     * @retur VBox containing the pizza information
     */
    private VBox getPizzaBox(Pizza p){
        int inset = 5;
        int headSpacing = 30;
        VBox pizzaBox = new VBox();
        pizzaBox.setPrefWidth(250);
        if(p.getStatus().equals("started")){
            pizzaBox.setStyle(startedStyle);
            pizzaBox.setBorder(unselectedBorder);
        }
        else{
            pizzaBox.setStyle(pizzaBoxStyle);
            pizzaBox.setBorder(unselectedBorder);
        }
//        pizzaBox.setAlignment(Pos);
        HBox header = new HBox();
        header.setPadding(new Insets(inset));
        header.setAlignment(Pos.CENTER);
        header.setSpacing(headSpacing);
        Text headerText = new Text("Pizza " + p.getID());
        headerText.setFont(midHeaderFont);
        Button pizzaBtn = new Button();
        if(p.getStatus().equals("queued")){
            pizzaBtn.setText("Start Pizza");
        }
        else if (p.getStatus().equals("started")){
            pizzaBtn.setText("Complete");
        }
        pizzaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(pizzaBtn.getText().equals("Start Pizza")){
                    startPizza(p);
                    pizzaBox.setStyle(startedStyle);
                    pizzaBox.setBorder(unselectedBorder);
                    pizzaBtn.setText("Complete");
                }
                else if(pizzaBtn.getText().equals("Complete")){
                    completePizza(p);
                    pizzaBtn.setText("Already done");
                }
                refresh();
                displaySingleOrder(p.getOrder());
            }
        });
        Region buffer1 = new Region();
        buffer1.setPrefWidth(110);
        Region buffer2 = new Region();
        header.getChildren().addAll(buffer1,headerText,buffer2,pizzaBtn);
//        header.setHgrow(buffer1, Priority.ALWAYS);
        header.setHgrow(buffer2, Priority.ALWAYS);

        HBox baseSauce = new HBox();
        baseSauce.setSpacing(15);
        baseSauce.setAlignment(Pos.BASELINE_CENTER);
        baseSauce.setPadding(new Insets(0,0,5,0));
        HBox baseBox = new HBox();

        Text baseTitle = new Text("Base: ");
        baseTitle.setFont(subHeaderFont);
        baseBox.getChildren().add(baseTitle);
        baseBox.setAlignment(Pos.BASELINE_CENTER);
        HBox sauceBox = new HBox();
        Text sauceTitle = new Text("Sauce: ");
        sauceTitle.setFont(subHeaderFont);
        sauceBox.getChildren().add(sauceTitle);
        sauceBox.setAlignment(Pos.BASELINE_CENTER);
        GridPane topBox = new GridPane();
        topBox.setAlignment(Pos.CENTER);
        topBox.setVgap(5);
        topBox.setHgap(15);
        Text toppings = new Text("Toppings:");
        toppings.setFont(subHeaderFont);
        toppings.setTextAlignment(TextAlignment.CENTER);
        topBox.add(toppings,0,0);
        topBox.setColumnSpan(toppings,2);
        topBox.setHalignment(toppings, HPos.CENTER);
        int row = 1;
        int col = 0;
        for (int j = 0; j < p.getIngredients().size(); j++){
            Text ingredient = new Text(p.getIngredients().get(j).getName());
            ingredient.setTextAlignment(TextAlignment.CENTER);
            if(p.getIngredients().get(j).getType().equalsIgnoreCase("base")){
                if(baseBox.getChildren().size()==1) {
                    baseBox.getChildren().add(ingredient);
                }
            }
            else if (p.getIngredients().get(j).getType().equalsIgnoreCase("sauce")){
                if (sauceBox.getChildren().size() == 1) {
                    sauceBox.getChildren().add(ingredient);
                }
            }
            else {
                topBox.add(ingredient,col,row);
                topBox.setHalignment(ingredient,HPos.CENTER);
                if(col == 0){
                    col = 1;
                }
                else{
                    col = 0;
                    row++;
                }
            }
        }
        baseSauce.getChildren().setAll(baseBox, sauceBox);
        pizzaBox.getChildren().addAll(header, baseSauce, topBox);
        return pizzaBox;
    }

    /**
     * Updates a pizza's status to "started"
     * @param pizza The pizza to be updated
     */
    public void startPizza(Pizza pizza) {
            pizza.updateStatus("started");
    }
    /**
     * Updates a pizza's status to "completed"
     * @param pizza The pizza to be updated
     */
    public void completePizza(Pizza pizza) {
            pizza.updateStatus("completed");
    }

    /**
     * Updates the orders screen to the most current information
     */
    public void refresh() {
        orderList.getChildren().clear();
        displayIncompleteOrders();
        setPizzaView(0);
    }


}