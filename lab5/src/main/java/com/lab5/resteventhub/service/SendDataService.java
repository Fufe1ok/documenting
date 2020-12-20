package com.lab5.resteventhub.service;

import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;

public interface SendDataService {
    void sendAndLog(String url) throws IOException, EventHubException;
}
