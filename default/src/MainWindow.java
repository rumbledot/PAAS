import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class MainWindow {

    //FXML Connector
//    @FXML
//    TableView<Ingredient> ingredientsList;
//    @FXML
//    TreeView<Order> ordersList;
    @FXML
    Label status1Label, status2Label, status3Label;
    @FXML
    TextField ingredNameText, ingredQuantityText, ingredSupplierText;
    @FXML
    ChoiceBox<String> ingredTypeList;
    @FXML
    Button ingredUpdateButton, ingredRestockButton, ingredAddButton, ingredRemoveButton, ingredClearButton,
            ingredRefreshButton, refreshBtn;

    @FXML
    GridPane pizzasList;
//    @FXML
//    HBox orderHeader, pizzaHeader;

    @FXML
    ScrollPane orderList;

    @FXML
    TableView ingredientsList;

    @FXML
    Text orderTitle, pizzaTitle, invHeader, ingHeader;

    @FXML
    Tab orderTab, inventoryTab;

    @FXML
    Label labelName, labelType, labelQuantity, labelSupplier;

    @FXML
    TabPane tabPane;

    @FXML
    AnchorPane background;

    @FXML
    HBox invTabHeading;

    @FXML
    Region invHeadBuffer;

    //colours
    private String lightHex = "#c7d7f2";
    private String darkestHex = "#2f4263";
    private String darkHex = "#415780";
    private String medHex = "#627ba8";


    private ManageOrders manageOrders;
    private ManageInventory manageInventory;
    //Data vars
    private ObservableList<String> ingredTypes = FXCollections.observableArrayList( "", "Meat", "Veggies", "Cheese", "Base", "Sauce");
    private Font headerFont = Font.font("Verdana", FontWeight.BOLD, 20);
    private String unselectedTab = "-fx-background-color: " + medHex + "; -fx-base-text-color: black";
    private String selectedTab = "-fx-background-color: " + darkestHex + "; -fx-base-text-color: " + lightHex + "; -fx-focus-color: "+ lightHex +";";
    private String bgStyle = "-fx-background-color: " + darkestHex + ";";



    public void initialize() {
    }

    /**
     * Initializes the MainWindow using the passed in DBConnector. Establishes formatting and OnAction events
     * @param db DBConnector established at login
     */
    public void initData(DBConnector db, Stage stage){
        manageOrders = new ManageOrders(pizzasList,db);
        manageInventory = new ManageInventory(ingredNameText,ingredQuantityText,ingredSupplierText,ingredTypeList,db);
        ingredTypeList.setItems(ingredTypes);
        ingredTypeList.getSelectionModel().selectFirst();
        orderList.setContent(manageOrders.getOrderList());
        orderList.setPrefWidth(stage.getWidth()*.33);
        pizzasList.setPrefWidth(stage.getWidth()*.63);

        manageInventory.setInventoryTable(ingredientsList);
        setIngredientsTable();

        //Formatting
//        background.setStyle(bgStyle);
//        tabPane.setStyle(bgStyle);
        invTabHeading.setPadding(new Insets(10,170,10,100));
        invTabHeading.setSpacing(stage.getWidth()/6);
        invTabHeading.setHgrow(invHeadBuffer,Priority.ALWAYS);

        orderTitle.setFont(headerFont);
        orderTitle.setFill(Color.web(lightHex));
        pizzaTitle.setFont(headerFont);
        pizzaTitle.setFill(Color.web(lightHex));
        ingHeader.setFont(headerFont);
        ingHeader.setFill(Color.web(lightHex));
        invHeader.setFont(headerFont);
        invHeader.setFill(Color.web(lightHex));
        orderTab.setStyle(unselectedTab);
        inventoryTab.setStyle(unselectedTab);
        tabPane.getSelectionModel().getSelectedItem().setStyle(selectedTab);

        labelName.setTextFill(Color.web(lightHex));
        labelQuantity.setTextFill(Color.web(lightHex));
        labelSupplier.setTextFill(Color.web(lightHex));
        labelType.setTextFill(Color.web(lightHex));

        orderTab.setOnSelectionChanged(changeTabStyle());
        inventoryTab.setOnSelectionChanged(changeTabStyle());

        // Inventory buttons
        ingredUpdateButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                String response = "";
                if(ingredientsList.getSelectionModel().getSelectedItem()!=null) {
                    response = manageInventory.editIngredient((Ingredient) ingredientsList.getSelectionModel().getSelectedItem());
                }
                else {
                    response = "No item selected";
                }
                Dialog dialog = new Dialog();
                dialog.getDialogPane().setPadding(new Insets(20));
                dialog.getDialogPane().setContent(new Text(response));
                dialog.setTitle("Update Item");
                ButtonType okbtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okbtn);

                dialog.showAndWait();
                setIngredientsTable();
            }
        });

        ingredRestockButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Dialog dialog = new Dialog();
                dialog.getDialogPane().setPadding(new Insets(20));

                dialog.setTitle("Reorder Item");
                ButtonType okbtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okbtn, ButtonType.CANCEL);

                Ingredient ingr = (Ingredient) ingredientsList.getSelectionModel().getSelectedItem();

                if(ingr != null) {
                    GridPane grid = new GridPane();

                    Text header = new Text("Reorder " + ingr.getName());
                    TextField quantity = new TextField();
                    Text qLabel = new Text(" Reorder quantity: ");
                    grid.setPadding(new Insets(0,10,0,10));
                    grid.setVgap(15);
                    grid.setAlignment(Pos.CENTER);
                    grid.add(header,1,0);
                    grid.setColumnSpan(header,2);
                    grid.add(qLabel,0,1);
                    grid.add(quantity,2,1);

                    dialog.getDialogPane().setContent(grid);
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okbtn) {
                            try {
                                String quan = quantity.getText();
                                int q = Integer.parseInt(quan);
                                manageInventory.reorderIngredient(ingr, q);
                            } catch (NumberFormatException e1) {
                                Dialog dialog1 = new Dialog();
                                dialog1.getDialogPane().setContent(new Text("Invalid quantity, try again"));
                                ButtonType okbtn2 = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                dialog1.getDialogPane().getButtonTypes().addAll(okbtn2);
                                dialog1.showAndWait();
                                setIngredientsTable();
                            }
                        }
                        return null;
                    });
                }
                else {
                    dialog.getDialogPane().setContent(new Text("No item selected"));
                }
                dialog.showAndWait();
            }
        });

        ingredClearButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                ingredNameText.setText("");
                ingredQuantityText.setText("");
                ingredSupplierText.setText("");
                ingredTypeList.getSelectionModel().selectFirst();
            }
        });

        // Inventory additional buttons
        ingredAddButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                String response = manageInventory.addIngredient(ingredTypes);
                Dialog dialog = new Dialog();
                dialog.getDialogPane().setPadding(new Insets(10));
                dialog.getDialogPane().setContent(new Text(response));
                ButtonType okbtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okbtn);
                dialog.showAndWait();
                setIngredientsTable();
            }
        });

        ingredRemoveButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Dialog dialog = new Dialog();
                dialog.getDialogPane().setPadding(new Insets(20));

                dialog.setTitle("Remove Item");
                ButtonType okbtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okbtn, ButtonType.CANCEL);

                Ingredient ingr = (Ingredient) ingredientsList.getSelectionModel().getSelectedItem();
                String message = "";

                if(ingr != null) {
                    message = "Are you sure you want to delete " + ingr.getName() + "?";
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okbtn) {
                            manageInventory.removeIngredient(ingr);
                        }
                        return null;
                    });
                }
                else{
                    message = "No item selected";
                }

                dialog.getDialogPane().setContent(new Text(message));


                dialog.showAndWait();
                setIngredientsTable();
            }
        });

        ingredClearButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                ingredNameText.setText("");
                ingredQuantityText.setText("");
                ingredSupplierText.setText("");
                ingredTypeList.getSelectionModel().selectFirst();
            }
        });

        ingredRefreshButton.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                setIngredientsTable();
            }
        });

        //Orders button
        refreshBtn.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                manageOrders.refresh();
            }
        });
//        System.out.println("finished mainwindow initialize");
    }

    /**
     * Action to update tab style as applicable when tabs are clicked
     * @return EventHandler to attach to tabs
     */
    private EventHandler<Event> changeTabStyle() {
        return new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                orderTab.setStyle(unselectedTab);
                inventoryTab.setStyle(unselectedTab);
                tabPane.getSelectionModel().getSelectedItem().setStyle(selectedTab);
            }
        };
    }

    /**
     * Refresh the ingredients table
     */
    private void setIngredientsTable(){
        ingredientsList.getItems().clear();
        manageInventory.displayInventory(ingredientsList);
    }


}
