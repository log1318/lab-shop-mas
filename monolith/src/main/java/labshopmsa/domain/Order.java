package labshopmsa.domain;

import labshopmsa.domain.OrderPlaced;
import labshopmsa.MonolithApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Order_table")
@Data

public class Order  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String productId;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private String customerId;
    
    
    
    
    
    private Double amount;

    @PostPersist
    public void onPostPersist(){

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        labshopmsa.external.DecreaseStockCommand decreaseStockCommand = new labshopmsa.external.DecreaseStockCommand();
        decreaseStockCommand.setQty(getQty()); //here
        // mappings goes here
        MonolithApplication.applicationContext.getBean(labshopmsa.external.InventoryService.class)
            .decreaseStock((Long.valueOf(getProductId())), decreaseStockCommand); //here



        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();

    }
    @PrePersist
    public void onPrePersist(){
    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = MonolithApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }






}
