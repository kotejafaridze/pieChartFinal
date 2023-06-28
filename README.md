# pieChartFinal
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.31</version>
</dependency>


private final static String db_URL= "jdbc:mysql://localhost:3306/derivative";
private final static String UserName = "root";
private final static String Password = null;


CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10,2)
);

INSERT INTO products (name, price) VALUES ('rdze', 3.90);



requires java.sql;
requires lombok;
