package com.lab5.resteventhub;

import com.lab5.resteventhub.service.SendDataConsoleImpl;
import com.lab5.resteventhub.service.SendDataEventHubImpl;
import com.lab5.resteventhub.service.SendDataService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class LogService {

    private Map<String, SendDataService> logServiceMap;

    public LogService() {
        logServiceMap = createStrategies();
    }

    public SendDataService getService(String repositoryName) {
        return logServiceMap.get(repositoryName);
    }

    public Set<String> getAllNames() {
        return logServiceMap.keySet();
    }

    public Collection<SendDataService> getAllServices(){
        return logServiceMap.values();
    }

    private Map<String, SendDataService> createStrategies(){
        Map<String, SendDataService> strategies = new HashMap<>();
        strategies.put("eventHub", new SendDataEventHubImpl());
        strategies.put("redis", new SendDataConsoleImpl());
        return strategies;
    }
}