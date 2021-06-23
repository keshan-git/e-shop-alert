package data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Title {
    private long id;
    private String name;
    private double regularPrice;
    private double discountPrice;
    private double discountAmount;
    private int discountPercent;
    private String currency;
    private LocalDateTime endDate;
}
