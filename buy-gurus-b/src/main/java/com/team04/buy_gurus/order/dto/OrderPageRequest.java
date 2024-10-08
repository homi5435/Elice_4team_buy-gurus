package com.team04.buy_gurus.order.dto;

import com.team04.buy_gurus.common.annotation.validation.ValueInList;
import lombok.Data;

public class OrderPageRequest {
    @Data
    public static class Pageable {
        private int page;
        private int size;

        Pageable(Integer page, Integer size) {
            this.page = (page == null || page <= 0) ? 1 : page;
            this.size = (size == null || size <= 0) ? 10 : size;
        }
    }

    @Data
    public static class Type {
        @ValueInList(values = {"s", "c"})
        private String type;
    }
}
