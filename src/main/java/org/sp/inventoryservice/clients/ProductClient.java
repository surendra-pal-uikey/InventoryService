package org.sp.inventoryservice.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderClient {
}
