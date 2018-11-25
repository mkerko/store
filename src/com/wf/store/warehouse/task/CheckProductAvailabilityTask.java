package com.wf.store.warehouse.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.store.domain.dto.WarehouseItemDTO;
import com.wf.store.exception.WarehouseServiceUnavailableException;
import com.wf.store.exception.WrongParameterException;
import com.wf.store.warehouse.WarehouseClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

@Component
public class CheckProductAvailabilityTask implements Callable<Boolean> {

    private static final String RETRIEVE_WAREHOUSE_ITEM_PATH = "/retrieveWarehouseItem?itemId=";

    @Setter
    @Getter
    private Long warehouseProductId;

    @Setter
    @Getter
    private Integer neededAmount;

    private final WarehouseClient warehouseClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public CheckProductAvailabilityTask(WarehouseClient warehouseClient, ObjectMapper objectMapper) {
        this.warehouseClient = warehouseClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Boolean call() throws WrongParameterException, IOException, WarehouseServiceUnavailableException {
        if (neededAmount == null || neededAmount == 0 || warehouseProductId == null) {
            throw new WrongParameterException("Warehouse product availability call parameters could not be null.");
        }
        InputStream inputStream = warehouseClient.makeWarehouseCall(RETRIEVE_WAREHOUSE_ITEM_PATH + warehouseProductId);
        WarehouseItemDTO item = objectMapper.readValue(inputStream, WarehouseItemDTO.class);
        return item.getQuantity() >= neededAmount;
    }
}
