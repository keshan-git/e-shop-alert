package restclient.response;

import lombok.Data;
import lombok.Setter;

@Data
public class TitleResponse {

    private long id;

    //@JsonProperty( "formal_name" )
    @Setter
    private String formal_name;

    public String getName(){
        return formal_name;
    }
}
