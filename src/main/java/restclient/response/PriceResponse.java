package restclient.response;

import lombok.Data;

@Data
public class PriceResponse {
    private long title_id;
    private String sales_status;
    private PriceComp regular_price;
    private PriceComp discount_price;
}
