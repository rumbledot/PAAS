import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class to manage the inventory
 */

public class ManageInventory {

    DBConnector db;
    private ObservableList<Ingredient> ingredients;
    //Formatting values
    private javafx.scene.text.Font subHeaderFont = javafx.scene.text.Font.font("Verdana", FontWeight.BOLD, 10);
    private javafx.scene.text.Font midHeaderFont = javafx.scene.text.Font.font("Verdana", FontWeight.BOLD, 15);
    private javafx.scene.text.Font headerFont = Font.font("Verdana", FontWeight.BOLD, 20);

    TextField ingredNameText, ingredQuantityText, ingredSupplierText;
    ChoiceBox<String> ingredTypeList;

    /**
     * Create new ManageInventory object
     * @param ingredNameText TextField from MainWindow to view/update name
     * @param ingredQuantityText TextField from MainWindow to view/update quantity
     * @param ingredSupplierText TextField from MainWindow to view/update supplier email
     * @param ingredTypeList TextField from MainWindow to view/update type
     * @param db DBConnector established at login
     */
    ManageInventory (TextField ingredNameText, TextField ingredQuantityText, TextField ingredSupplierText, ChoiceBox<String> ingredTypeList, DBConnector db){
        this.db = db;
        this.ingredTypeList = ingredTypeList;
        this.ingredSupplierText = ingredSupplierText;
        this.ingredQuantityText = ingredQuantityText;
        this.ingredNameText = ingredNameText;
        ingredients = FXCollections.observableArrayList(db.getInventory());
    }

    /**
     * Set up the TableView to display the ingredients
     * @param table TableView from MainWindow
     */
    public void setInventoryTable(TableView table) {
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Ingredient,Double>();
            }
        };
        TableColumn <String, Ingredient> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn <Integer, Ingredient> countCol = new TableColumn("Quantity");
        countCol.setCellValueFactory(new PropertyValueFactory("quantity"));
        TableColumn <String, Ingredient> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));


        table.getColumns().addAll(nameCol,typeCol,countCol);


//        System.out.println("table width: " + ingredientsList.getPrefWidth());
        nameCol.setPrefWidth(table.getPrefWidth()*.49);
        countCol.setPrefWidth(table.getPrefWidth()*.25);
        typeCol.setPrefWidth(table.getPrefWidth()*.25);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            System.out.println(newSelection);
            if (newSelection != null) {
                Ingredient selected = (Ingredient) newSelection;
//                System.out.println("name: " + selected.getName());
                ingredNameText.setText(selected.getName());
                ingredQuantityText.setText("" + selected.getQuantity());
                ingredSupplierText.setText(selected.getSupplierEmail());
                boolean matched = false;
                for (int i = 0; i< ingredTypeList.getItems().size(); i++){
                    String s = ingredTypeList.getItems().get(i);
                    if(s.equalsIgnoreCase(selected.getType())) {
                        ingredTypeList.setValue(s);
                        ingredTypeList.getSelectionModel().clearAndSelect(i);
                        matched = true;
                    }
                }
                if(!matched){
                    ingredTypeList.getSelectionModel().selectFirst();
                }
                ingredTypeList.setValue(selected.getType());
            }
        });

    }

    /**
     * Retrieve inventory from database and display in inventory TableView
     * @param table
     */
    public void displayInventory(TableView table){
        ingredients = FXCollections.observableArrayList(db.getInventory());
        for(Ingredient i : ingredients){
            table.getItems().add(i);
        }

    }

    /**
     * Update the details for an ingredient already in the database
     * @param ingredient Ingredient to be updated
     * @return String confirming outcome of update
     */
    public String editIngredient(Ingredient ingredient) {
        ingredient.setName(ingredNameText.getText());
        ingredient.setType(ingredTypeList.getValue());
        String email = ingredSupplierText.getText();
        if(email.contains("@")) {
            ingredient.setSupplierEmail(email);
        } else{
//            System.out.println("need dialog to alert email incorrect");
            ingredSupplierText.setText("");
            return "Unable to update as email is invalid";
        }
        String quantity = ingredQuantityText.getText();
        try{
            int q = Integer.parseInt(quantity);
            ingredient.setQuantity(q);
        } catch (NumberFormatException e){
//            System.out.println("need dialog to alert quantity incorrect");
            ingredQuantityText.setText("");
            return "Unable to update as quantity is not a valid number";
        }
        if(db.updateIngredient(ingredient)){
            return "Item updated";
        }
        else{
            return "Unable to update database";
        }
    }

    /**
     * Send an email from an automatic template to reorder an ingredient
     * @param ingredient Ingredient to be reordered
     * @param quantity Amount to be ordered
     */
    public void reorderIngredient(Ingredient ingredient, int quantity) {
        Dialog dialog = new Dialog();
        dialog.getDialogPane().setPadding(new Insets(20));
        dialog.setTitle("Send Reorder Email");
        ButtonType sendBtn = new ButtonType("SEND", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();

        Text header = new Text("Reorder email template");
        Text emailLabel = new Text("Email: ");
        TextField emailTo = new TextField();
        emailTo.setText(ingredient.getSupplierEmail());
        TextArea emailBody = new TextArea();
        emailBody.setText("Please deliver " + quantity + " units of " + ingredient.getName() + " to PAAS Co tomorrow. Our usual invoicing process will apply. Thank you!");
        emailBody.setPrefColumnCount(2);
        emailBody.setPrefRowCount(4);
        emailBody.setWrapText(true);

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(0,10,0,10));
        grid.setVgap(15);
        grid.add(header,1,0);
        grid.setColumnSpan(header,2);
        grid.add(emailLabel,0,1);
        grid.add(emailTo,1,1);
        grid.add(emailBody,0,2);
        grid.setColumnSpan(emailBody,2);
        grid.setRowSpan(emailBody,4);


        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendBtn) {
                Dialog dialog1 = new Dialog();
                dialog1.getDialogPane().setContent(new Text("Email sent"));
                dialog1.getDialogPane().setPadding(new Insets(10));
                ButtonType okbtn2 = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog1.getDialogPane().getButtonTypes().addAll(okbtn2);
                dialog1.showAndWait();
            }
            return null;
        });

        dialog.showAndWait();

    }

    /**
     * Add a new ingredient to the database
     * @param types Options to populate type dropdown list
     * @return String confirming outcome of adding ingredient
     */
    public String addIngredient(ObservableList<String> types){
        Dialog <String>dialog = new Dialog();
        dialog.getDialogPane().setPadding(new Insets(20));
        dialog.setTitle("Confirm new ingredient details:");
        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();

        Text header = new Text("New Ingredient");
        Text nameLabel = new Text("Name: ");
        TextField name = new TextField();
        name.setText(ingredNameText.getText());
        ChoiceBox<String> type = new ChoiceBox<>();
        type.setItems(types);
        type.setValue(ingredTypeList.getValue());
        type.getSelectionModel().select(ingredTypeList.getSelectionModel().getSelectedIndex());
        Text typeLabel = new Text("Type: ");

        Text quantityLabel = new Text("Quantity: ");
        TextField quantity = new TextField();
        quantity.setText(ingredQuantityText.getText());
        Text supplierLabel = new Text("Supplier Email: ");
        TextField supplier = new TextField();
        supplier.setText(ingredSupplierText.getText());


        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(0,10,0,10));
        grid.setVgap(15);
        grid.add(header,1,0);
        grid.setColumnSpan(header,2);
        grid.add(nameLabel,0,1);
        grid.add(name,1,1);
        grid.add(typeLabel,0,2);
        grid.add(type,1,2);
        grid.add(quantityLabel,0,3);
        grid.add(quantity,1,3);
        grid.add(supplierLabel,0,4);
        grid.add(supplier,1,4);



        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okBtn) {
                String ingName = name.getText();
                if(ingName.length()>30){
                    ingName = ingName.substring(0,29);
                }
                String ingType = type.getValue();
                String email = supplier.getText();
                if(email.length()>30){
                    email = email.substring(0,29);
                }
                String qText = quantity.getText();
                try {
                    int q = Integer.parseInt(qText);
                    if (email.contains("@")) {
                        Ingredient i = new Ingredient(ingName, ingType, q, email);
                        if(db.addIngredient(i)){
//                        if (true){
                            return "Ingredient added";
                        } else {
                        return "Unable to add to database";
                        }
                    }
                    else{
                        return "Unable to save as email is invalid";
                    }
                } catch (NumberFormatException ex){
                    return "Unable to save as quanity is invalid";
                }

            }
            return "Add item cancelled";
        });

        dialog.showAndWait();
        return dialog.getResult();
    }

    /**
     * Remove an ingredient from the database
     * @param ingredient Ingredient to be removed
     * @return Boolean confirming ingredient removed
     */
    public boolean removeIngredient(Ingredient ingredient){
        Dialog dialog = new Dialog();
        dialog.getDialogPane().setPadding(new Insets(10));
        String message = "";
        boolean removed = db.removeIngredient(ingredient);
//        boolean removed = false;
            if(removed){
                message = ingredient.getName() + " has been removed";
            }
            else{
                message = "unable to remove " + ingredient.getName();
            }

            dialog.getDialogPane().setContent(new Text(message));
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        return removed;
    }


}