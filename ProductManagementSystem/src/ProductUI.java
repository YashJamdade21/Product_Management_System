import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ProductUI extends Application {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Product Management System");


        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20));

        Label title = new Label("Product Management System");
        Button addButton = new Button("Add Product");
        Button viewButton = new Button("View Products");
        Button updateButton = new Button("Update Product");
        Button deleteButton = new Button("Delete Product");

        menuLayout.getChildren().addAll(title, addButton, viewButton, updateButton, deleteButton);


        Scene mainScene = new Scene(menuLayout, 400, 300);


        GridPane addLayout = createAddProductLayout(primaryStage, mainScene);


        VBox viewLayout = createViewProductsLayout(primaryStage, mainScene);


        GridPane updateLayout = createUpdateProductLayout(primaryStage, mainScene);


        GridPane deleteLayout = createDeleteProductLayout(primaryStage, mainScene);


        addButton.setOnAction(e -> primaryStage.setScene(new Scene(addLayout, 400, 300)));
        viewButton.setOnAction(e -> {
            refreshViewProducts(viewLayout);
            primaryStage.setScene(new Scene(viewLayout, 400, 300));
        });
        updateButton.setOnAction(e -> primaryStage.setScene(new Scene(updateLayout, 400, 300)));
        deleteButton.setOnAction(e -> primaryStage.setScene(new Scene(deleteLayout, 400, 300)));


        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private GridPane createAddProductLayout(Stage primaryStage, Scene mainScene) {
        GridPane addLayout = new GridPane();
        addLayout.setPadding(new Insets(20));
        addLayout.setHgap(10);
        addLayout.setVgap(10);

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField categoryField = new TextField();
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        addLayout.add(new Label("Name:"), 0, 0);
        addLayout.add(nameField, 1, 0);
        addLayout.add(new Label("Price:"), 0, 1);
        addLayout.add(priceField, 1, 1);
        addLayout.add(new Label("Category:"), 0, 2);
        addLayout.add(categoryField, 1, 2);
        addLayout.add(saveButton, 0, 3);
        addLayout.add(backButton, 1, 3);

        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();

                Product product = new Product(0, name, price, category);
                productDAO.addProduct(product);

                nameField.clear();
                priceField.clear();
                categoryField.clear();

                showMessage("Product added successfully!");
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        });

        return addLayout;
    }

    private VBox createViewProductsLayout(Stage primaryStage, Scene mainScene) {
        VBox viewLayout = new VBox(10);
        viewLayout.setPadding(new Insets(20));
        Button backButton = new Button("Back");
        viewLayout.getChildren().add(backButton);

        backButton.setOnAction(e -> primaryStage.setScene(mainScene));

        return viewLayout;
    }

    private void refreshViewProducts(VBox viewLayout) {
        viewLayout.getChildren().clear();

        List<Product> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            viewLayout.getChildren().add(new Label("No products available."));
        } else {
            for (Product product : products) {
                viewLayout.getChildren().add(new Label(product.toString()));
            }
        }
    }

    private GridPane createUpdateProductLayout(Stage primaryStage, Scene mainScene) {
        GridPane updateLayout = new GridPane();
        updateLayout.setPadding(new Insets(20));
        updateLayout.setHgap(10);
        updateLayout.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField categoryField = new TextField();
        Button updateButton = new Button("Update");
        Button backButton = new Button("Back");

        updateLayout.add(new Label("Product ID:"), 0, 0);
        updateLayout.add(idField, 1, 0);
        updateLayout.add(new Label("New Name:"), 0, 1);
        updateLayout.add(nameField, 1, 1);
        updateLayout.add(new Label("New Price:"), 0, 2);
        updateLayout.add(priceField, 1, 2);
        updateLayout.add(new Label("New Category:"), 0, 3);
        updateLayout.add(categoryField, 1, 3);
        updateLayout.add(updateButton, 0, 4);
        updateLayout.add(backButton, 1, 4);

        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
        updateButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();

                Product product = new Product(id, name, price, category);
                productDAO.updateProduct(product);

                idField.clear();
                nameField.clear();
                priceField.clear();
                categoryField.clear();

                showMessage("Product updated successfully!");
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        });

        return updateLayout;
    }

    private GridPane createDeleteProductLayout(Stage primaryStage, Scene mainScene) {
        GridPane deleteLayout = new GridPane();
        deleteLayout.setPadding(new Insets(20));
        deleteLayout.setHgap(10);
        deleteLayout.setVgap(10);

        TextField idField = new TextField();
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        deleteLayout.add(new Label("Product ID:"), 0, 0);
        deleteLayout.add(idField, 1, 0);
        deleteLayout.add(deleteButton, 0, 1);
        deleteLayout.add(backButton, 1, 1);

        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                productDAO.deleteProduct(id);

                idField.clear();
                showMessage("Product deleted successfully!");
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        });

        return deleteLayout;
    }

    private void showMessage(String message) {
        Stage messageStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        Label label = new Label(message);
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> messageStage.close());
        layout.getChildren().addAll(label, okButton);
        Scene scene = new Scene(layout, 300, 150);
        messageStage.setScene(scene);
        messageStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
