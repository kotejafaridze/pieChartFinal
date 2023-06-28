package com.example.java_konstantine_japaridze;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelloApplication extends Application {
    GridPane gridPane;
    Label nameLabel;
    Label priceLabel;
    TextField nameField;
    TextField priceField;
    Connection connection;
    Button add;
    TableView<Product> tableView;
    PieChart pieChart;

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        String db_URL= "jdbc:mysql://localhost:3306/finalexam";
        String UserName = "root";
        String Password = null;
        connection = DriverManager.getConnection(db_URL,UserName, Password);
        tableView = new TableView<>();

        nameLabel = new Label("Name");
        nameField = new TextField();

        priceLabel = new Label("Price");
        priceField = new TextField();

        add = new Button("Add");

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getColumns().addAll(idColumn, nameColumn, priceColumn);
        fetchData();

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        formBox.getChildren().addAll(nameLabel, nameField, priceLabel, priceField, add);
        add.setOnAction(actionEvent -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product product = new Product(0, name, price);

            try {
                addProduct(product);
                clearForm();
                pieChart.setData(getProductQuantityChartData());

                fetchData();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        pieChart = new PieChart();
        pieChart.setPrefWidth(300);
        pieChart.setData(getProductQuantityChartData());

        gridPane = new GridPane();
        gridPane.add(tableView,0,0);
        gridPane.add(formBox,1,0);
        gridPane.add(pieChart, 0, 1);




        Scene scene = new Scene(gridPane, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }



    public static void main(String[] args) {
        launch();
    }

    private ObservableList<PieChart.Data> getProductQuantityChartData() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT price FROM products");

        List<Double> priceList = new ArrayList<>();

        while(rs.next()){
            double price = rs.getDouble("price");
            priceList.add(price);
        }

        rs.close();
        statement.close();

        Map<Double, Long> productQuantityMap = priceList.stream()
                .collect(Collectors.groupingBy(price -> Math.floor(price / 100) * 100, Collectors.counting()));

        ObservableList<PieChart.Data> data = productQuantityMap.entrySet().stream()
                .map(entry -> new PieChart.Data("$" + entry.getKey() + "-" + (entry.getKey() + 100), entry.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return data;

    }


    private void addProduct(Product product) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT into products(name, price) VALUES ('" + product.getName() +"', '" + product.getPrice() + "')");
        statement.close();

    }

    private void fetchData() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT id, name, price FROM products");
        ObservableList<Product> products = FXCollections.observableArrayList();
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            Product product = new Product(id, name, price);
            products.add(product);


        }
        rs.close();
        statement.close();
        tableView.setItems(products);




    }
    private void clearForm() {
        nameField.clear();
        priceField.clear();

    }
}

