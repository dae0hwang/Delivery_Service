package com.example.apideliveryservice.exception;

public final class ExceptionMessage {

    //companyFoodController message
    public static final String DuplicatedFoodNameException = "company food add fail due to "
        + "duplicated name";
    public static final String NonExistentFoodIdException = "find food fail due to no exist food "
        + "id";
    public static final String RequestCompanyFoodDtoMemberId = "requestCompanyFood memberId must "
        + "not be blank";
    public static final String RequestCompanyFoodDtoName = "requestCompanyFood name must not be "
        + "blank";
    public static final String RequestCompanyFoodDtoPrice = "requestCompanyFood price must be "
        + "digit";
    public static final String RequestCompanyFoodPriceDtoFoodId = "requestCompanyFoodPrice foodId"
        + " must not be blank";
    public static final String RequestCompanyFoodPriceDtoFoodPrice = "requestCompanyFoodPrice "
        + "price must be digit";

    //companyMemberController
    public static final String DuplicatedLoginNameException = "Company member join fail due to "
        + "DuplicatedLoginName";
    public static final String NonExistentMemberIdException = "Company member find fail due to no"
        + " exist member Id";
    public static final String RequestCompanyMemberDtoLoginName = "requestCompanyMember loginName"
        + " is 8 to 20 lowercase letters and numbers";
    public static final String RequestCompanyMemberDtoPassword = "requestCompanyMember password "
        + "is At least 8 characters, at least 1 uppercase, lowercase, number, and special "
        + "character each";
    public static final String RequestCompanyMemberDtoName = "requestCompanyMember name must not "
        + "be blank";

    //generalMemberController
    public static final String DeliveryExceptionDuplicatedName = "general member join fail due to"
        + " DuplicatedLoginName";
    public static final String DeliveryExceptionNonExistentMemberId = "general member findById "
        + "fail due to NonExistentMemberIdException";
    public static final String RequestGeneralMemberDtoLoginName = "requestGeneralMember loginName"
        + " is 8 to 20 lowercase letters and numbers";
    public static final String RequestGeneralMemberDtoPassword = "password is At least 8 "
        + "characters, at least 1 uppercase, lowercase, number, and special character each";
    public static final String RequestGeneralMemberDtoName = "requestGeneralMember name must not "
        + "be blank";
    public static final String RequestPurchaseListDtoGeneralMemberId = "requestPurchaseListDto "
        + "generalMemberId must be digit";
    public static final String RequestPurchaseListDtoCompanyMemberId = "requestPurchaseListDto "
        + "companyMemberId must be digit";
    public static final String RequestPurchaseListDtoGeneralFoodId = "requestPurchaseListDto "
        + "foodId must be digit";
    public static final String RequestPurchaseListDtoGeneralFoodPrice = "requestPurchaseListDto "
        + "foodPrice must be digit";




}
