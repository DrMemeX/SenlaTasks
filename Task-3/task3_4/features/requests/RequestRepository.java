package task3_4.features.requests;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository {

    private final List<Request> requestList = new ArrayList<>();

    public List<Request> findAllRequests() {
        return requestList;
    }

    public Request findRequestById(long id) {
        return requestList.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addRequest(Request request) {
        requestList.add(request);
    }

    public boolean deleteRequestById(long id) {
        return requestList.removeIf(r -> r.getId() == id);
    }

    public void restoreState(List<Request> requestState) {
        if (requestState != null) {
            requestList.clear();
            requestList.addAll(requestState);
        }
    }

}
