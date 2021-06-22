package restclient.response;

import lombok.Data;

import java.util.List;

@Data
public class TitleResponseWrapper {
    private List<TitleResponse> contents;
    private int length;
    private int offset;
    private int total;
}
