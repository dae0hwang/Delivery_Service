package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.dto.RequestOrder;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import com.example.apideliveryservice.repository.OrderDetailRepository;
import com.example.apideliveryservice.repository.OrderRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CompanyFoodRepository companyFoodRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final CompanyMemberRepository companyMemberRepository;


    @Transactional
    public void addOrder(Long generalMemberId, List<RequestOrder> requestOrderList) {
        GeneralMemberEntity findGeneralMember = generalMemberRepository.findById(generalMemberId)
            .orElseThrow();
        OrderEntity saveOrderEntity = orderRepository.save(new OrderEntity(findGeneralMember));
        for (RequestOrder requestOrder : requestOrderList) {
            CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(
                requestOrder.getFoodId()).orElseThrow();
            CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(
                requestOrder.getCompanyMemberId()).orElseThrow();
            orderDetailRepository.save(
                new OrderDetailEntity(saveOrderEntity, findCompanyMember, findCompanyFood,
                    findCompanyFood.getPrice(), requestOrder.getFoodAmount()));
        }
    }
    @Transactional(readOnly = true)
    public List<GeneralMemberOrderDto> findOrderListByGeneralId(Long generalMemberId) {
        GeneralMemberEntity findGeneralMember = generalMemberRepository.findById(
            generalMemberId).orElseThrow();
        List<OrderEntity> findOrderList = orderRepository.findAllByGeneralMemberEntity(
            findGeneralMember);
        List<GeneralMemberOrderDto> generalMemberOrderDtoList = new ArrayList<>();

        for (OrderEntity orderEntity : findOrderList) {
            Timestamp registrationDate = orderEntity.getRegistrationDate();
            Long orderId = orderEntity.getId();
            Long findGeneralMemberId = orderEntity.getGeneralMemberEntity().getId();
            List<OrderDetailEntity> orderDetailEntityList = orderEntity.getOrderDetailEntityList();
            for (OrderDetailEntity orderDetail : orderDetailEntityList) {
                GeneralMemberOrderDto generalMemberOrderDto = new GeneralMemberOrderDto(
                    registrationDate, orderId, findGeneralMemberId,
                    orderDetail.getCompanyFoodEntity().getId(),
                    orderDetail.getCompanyFoodEntity().getName(), orderDetail.getFoodPrice(),
                    orderDetail.getFoodAmount(), orderDetail.getCompanyMemberEntity().getId());
                generalMemberOrderDtoList.add(generalMemberOrderDto);
            }
        }

        return generalMemberOrderDtoList;
    }

}
