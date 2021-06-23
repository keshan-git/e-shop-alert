package restclient.response;

import lombok.Data;

import java.util.List;

@Data
public class PriceResponseWrapper {
    private String country;
    private List<PriceResponse> prices;
}
