package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.dto.RequestOrder;
import com.example.apideliveryservice.dto.ResponseOrderSuccess;
import com.example.apideliveryservice.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/delivery-service/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/member/order")
    public ResponseEntity orderFood(@Validated @RequestBody List<RequestOrder> requestOrderList) {
        Long generalMemberId = requestOrderList.get(0).getGeneralMemberId();
        orderService.addOrder(generalMemberId, requestOrderList);
        ResponseOrderSuccess success = new ResponseOrderSuccess(201, null, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }

    @GetMapping("/member/order/list")
    public ResponseEntity orderList(@RequestParam Long generalId) {
        List<GeneralMemberOrderDto> orderListByGeneralId = orderService.findOrderListByGeneralId(
            generalId);
        ResponseOrderSuccess success = new ResponseOrderSuccess(201, orderListByGeneralId, null);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
