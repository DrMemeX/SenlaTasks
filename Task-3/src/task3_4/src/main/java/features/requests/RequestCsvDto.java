package features.requests;

import java.util.List;

public class RequestCsvDto {
    public long id;
    public long bookId;
    public boolean resolved;
    public List<Long> waitingOrderIds;
}
