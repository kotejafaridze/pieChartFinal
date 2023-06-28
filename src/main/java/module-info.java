module com.example.java_konstantine_japaridze {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;



    opens com.example.java_konstantine_japaridze to javafx.fxml;
    exports com.example.java_konstantine_japaridze;
}