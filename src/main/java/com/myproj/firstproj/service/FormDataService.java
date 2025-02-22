package com.myproj.firstproj.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class FormDataService {
    private final Map<String, Object> formData = new ConcurrentHashMap<>();

    public void storeData(String key, Object value) {
        formData.put(key, value);
    }

    public Object getData(String key) {
        return formData.get(key);
    }

    // Επιπλέον, μπορείτε να προσθέσετε μεθόδους για να καθαρίζετε τα δεδομένα ή να κάνετε άλλες λειτουργίες.
}
