package com.wf.store.warehouse;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.wf.store.exception.WarehouseServiceUnavailableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Log4j2
public class WarehouseClient {

    private static final String WAREHOUSE_HOST_PROPERTY = "warehouse.host";
    private final HttpTransport netHttpTransport;
    private final JsonFactory jacksonFactory;
    private final Environment env;

    @Autowired
    public WarehouseClient(Environment env, HttpTransport netHttpTransport, JsonFactory jacksonFactory) {
        this.env = env;
        this.netHttpTransport = netHttpTransport;
        this.jacksonFactory = jacksonFactory;
    }

    public InputStream makeWarehouseCall(String warehousePath) throws IOException, WarehouseServiceUnavailableException {
        log.debug("Starting warehouse call...");
        HttpRequestFactory requestFactory
                = netHttpTransport.createRequestFactory(
                (HttpRequest request) -> request.setParser(new JsonObjectParser(jacksonFactory)));
        WarehouseURL url = new WarehouseURL(env.getProperty(WAREHOUSE_HOST_PROPERTY) + warehousePath);
        HttpRequest request = requestFactory.buildGetRequest(url);
        HttpResponse response = request.execute();
        log.info("Finished warehouse call with response " + response.getStatusCode());
        int responseStatusCode = response.getStatusCode();
        if (responseStatusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw new WarehouseServiceUnavailableException("Warehouse response code = " + responseStatusCode);
        }
        return response.getContent();
    }
}
