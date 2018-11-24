package com.wf.store.domain.dto;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class WarehouseItemDTO {

    @Key
    private long id;

    @Key
    private long quantity;
}
