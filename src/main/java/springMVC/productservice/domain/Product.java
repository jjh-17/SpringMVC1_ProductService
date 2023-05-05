package springMVC.productservice.domain;


import lombok.Data;

@Data
public class Product {

    private Long id;
    private String name;
    private Integer price; //null이 할당될 수 있으므로 int 대신 Integer
    private Integer quantity;

    public Product() {

    }

    public Product(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
