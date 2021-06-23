package restclient.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceComp {
    private String amount;
    private String currency;
    private double raw_value;
    private LocalDateTime start_datetime;
    private LocalDateTime end_datetime;
}
