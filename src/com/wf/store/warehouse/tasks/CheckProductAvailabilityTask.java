package com.wf.store.warehouse.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.store.domain.dto.WarehouseItemDTO;
import com.wf.store.exceptions.WrongParameterException;
import com.wf.store.warehouse.WarehouseClient;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.Callable;

@Component
public class CheckProductAvailabilityTask implements Callable<Boolean> {

    private static final String PRODUCT_AVAILABILITY_PATH = "/checkProductAvailability?productId=";

    @Setter
    private Long warehouseProductId;

    private final WarehouseClient warehouseClient;

    @Autowired
    public CheckProductAvailabilityTask(WarehouseClient warehouseClient) {
        this.warehouseClient = warehouseClient;
    }

    @Override
    public Boolean call() throws Exception {
        if (warehouseProductId == null){
            throw new WrongParameterException("Warehouse Id could not be null.");
        }
        InputStream inputStream = warehouseClient.makeWarehouseCall(PRODUCT_AVAILABILITY_PATH + warehouseProductId);
        ObjectMapper objectMapper = new ObjectMapper();
        WarehouseItemDTO item = objectMapper.readValue(inputStream, WarehouseItemDTO.class);
        return item.getQuantity() >= 1;
    }
}
