package com.lab5.resteventhub.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SendDataEventHubImpl implements SendDataService {

    public void sendAndLog(String url) throws IOException, EventHubException {
        final ConnectionStringBuilder connStr = new ConnectionStringBuilder()
                .setNamespaceName("labour5")//namespace
                .setEventHubName("eventinstance")//hub name
                /*Connection string–primary key*/
                .setSasKeyName("Endpoint=sb://labour5.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=4yHLGgOoCJ7Peygb+UGElqgCEn7qvYIRlrK4Yc5kWe8=")
                /*Primary key*/
                .setSasKey("4yHLGgOoCJ7Peygb+UGElqgCEn7qvYIRlrK4Yc5kWe8=");

        final Gson gson = new GsonBuilder().create();
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
        final EventHubClient ehClient = EventHubClient.createSync(connStr.toString(), executorService);

        try {
            URL data = new URL(url);
            HttpURLConnection con = (HttpURLConnection) data.openConnection();
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            showData(jsonArray, gson, ehClient);

            System.out.println(Instant.now() + ": Send Complete...");
            System.out.println("Press Enter to stop.");
            System.in.read();
        } finally {
            ehClient.closeSync();
            executorService.shutdown();
        }
    }

    public void showData(JSONArray jsonArray, Gson gson, EventHubClient ehClient) throws EventHubException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            System.out.println("Document: " + i);
            byte[] payloadBytes = gson.toJson(jsonObject).getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);

            ehClient.sendSync(sendEvent);
        }
    }
}
