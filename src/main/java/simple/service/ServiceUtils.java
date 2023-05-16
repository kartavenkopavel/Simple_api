package simple.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceUtils {
    public static Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);

        return errorResponse;
    }
}
