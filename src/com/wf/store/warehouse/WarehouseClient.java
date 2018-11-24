package com.wf.store.warehouse;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class WarehouseClient {

    private static final String WAREHOUSE_HOST_PROPERTY = "warehouse.host";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final Environment env;

    @Autowired
    public WarehouseClient(Environment env) {
        this.env = env;
    }

    public InputStream makeWarehouseCall(String warehousePath) throws IOException {
        HttpRequestFactory requestFactory
                = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
        WarehouseURL url = new WarehouseURL(env.getProperty(WAREHOUSE_HOST_PROPERTY) + warehousePath);
        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute().getContent();
    }
}
