package com.example.apideliveryservice.repository;

import static com.example.apideliveryservice.entity.QOrderDetailEntity.orderDetailEntity;
import static com.example.apideliveryservice.entity.QOrderEntity.orderEntity;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.dto.QFoodPriceSumDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderStatisticRepository {

    private final JPAQueryFactory queryFactory;

    public List<FoodPriceSumDto> companyAllOfDay() {
        return queryFactory
            .select(new QFoodPriceSumDto(
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", orderEntity.registrationDate,
                    "%Y%m%d"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(Expressions.stringTemplate(
                "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m%d"))
            .orderBy(orderEntity.registrationDate.desc())
            .fetch();
    }

    public List<FoodPriceSumDto> companyAllOfMonth() {
        return queryFactory
            .select(new QFoodPriceSumDto(Expressions.stringTemplate(
                "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(Expressions.stringTemplate(
                "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m"))
            .orderBy(orderEntity.registrationDate.desc())
            .fetch();
    }

    public List<FoodPriceSumDto> companyMemberOfDay() {
        return  queryFactory
            .select(new QFoodPriceSumDto(
                orderDetailEntity.companyMemberEntity.id,
                orderDetailEntity.companyMemberEntity.name,
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", orderEntity.registrationDate,
                    "%Y%m%d"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()
            ))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(
                orderDetailEntity.companyMemberEntity.id,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m%d"))
            .orderBy(
                orderDetailEntity.companyMemberEntity.id.asc(),
                orderEntity.registrationDate.desc())
            .fetch();
    }

    public List<FoodPriceSumDto> companyMemberOfMonth() {
        return queryFactory
            .select(new QFoodPriceSumDto(
                orderDetailEntity.companyMemberEntity.id,
                orderDetailEntity.companyMemberEntity.name,
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", orderEntity.registrationDate,
                    "%Y%m"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()
            ))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(
                orderDetailEntity.companyMemberEntity.id,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m"))
            .orderBy(
                orderDetailEntity.companyMemberEntity.id.asc(),
                orderEntity.registrationDate.desc())
            .fetch();
    }

    public List<FoodPriceSumDto> generalMemberOfDay() {
        return queryFactory
            .select(new QFoodPriceSumDto(
                orderEntity.generalMemberEntity.id,
                orderEntity.generalMemberEntity.name,
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", orderEntity.registrationDate,
                    "%Y%m%d"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()
            ))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(
                orderEntity.generalMemberEntity.id,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m%d"))
            .orderBy(
                orderEntity.generalMemberEntity.id.asc(),
                orderEntity.registrationDate.desc())
            .fetch();
    }

    public List<FoodPriceSumDto> generalMemberOfMonth() {
        return queryFactory
            .select(new QFoodPriceSumDto(
                orderEntity.generalMemberEntity.id,
                orderEntity.generalMemberEntity.name,
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", orderEntity.registrationDate,
                    "%Y%m"),
                orderDetailEntity.foodPrice.multiply(orderDetailEntity.foodAmount).sum()
            ))
            .from(orderDetailEntity)
            .join(orderDetailEntity.orderEntity, orderEntity)
            .groupBy(
                orderEntity.generalMemberEntity.id,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0},{1})", orderEntity.registrationDate, "%Y%m"))
            .orderBy(
                orderEntity.generalMemberEntity.id.asc(),
                orderEntity.registrationDate.desc())
            .fetch();
    }
}
