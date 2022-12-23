//package com.example.apideliveryservice.service;
//
//import com.example.apideliveryservice.dto.GeneralMemberDto;
//import com.example.apideliveryservice.entity.GeneralMemberEntity;
//import com.example.apideliveryservice.exception.DeliveryServiceException;
//import com.example.apideliveryservice.exception.ExceptionMessage;
//import com.example.apideliveryservice.repository.GeneralMemberRepository;
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class GeneralMemberService {
//
//    @Value("${persistenceName:@null}")
//    private String persistenceName;
//    private final GeneralMemberRepository generalMemberRepository;
//
//    /**
//     * @param loginName, password, name
//     * @throws Exception
//     * @throws DeliveryServiceException-general member join fail due to DuplicatedLoginName
//     */
//    public void join(String loginName, String password, String name) throws Exception {
//        GeneralMemberEntity generalMemberEntity = getGeneralMember(loginName, password, name);
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            validateDuplicateLoginName(em, generalMemberEntity);
//            generalMemberRepository.create(em, generalMemberEntity);
//            tx.commit();
//        } catch (Exception e) {
//            tx.rollback();
//            throw e;
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }
//
//    private GeneralMemberEntity getGeneralMember(String loginName, String password, String name) {
//        GeneralMemberEntity generalMemberEntity = new GeneralMemberEntity(null, loginName, password, name,
//            false, new Timestamp(System.currentTimeMillis()));
//        return generalMemberEntity;
//    }
//
//    private void validateDuplicateLoginName(EntityManager em, GeneralMemberEntity generalMemberEntity) {
//        generalMemberRepository.findByLoginName(em, generalMemberEntity.getLoginName())
//            .ifPresent(m -> {
//                throw new DeliveryServiceException(
//                    ExceptionMessage.DeliveryExceptionDuplicatedName);
//            });
//    }
//
//    /**
//     * @return generalMemberList
//     * @throws Exception
//     */
//    public List<GeneralMemberDto> findAllMember() throws Exception {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            List<GeneralMemberEntity> allMemberEntity = generalMemberRepository.findAll(em);
//            tx.commit();
//            List<GeneralMemberDto> allMemberDto = changeAllMemberEntityToDto(allMemberEntity);
//            return allMemberDto;
//        } catch (Exception e) {
//            tx.rollback();
//            throw e;
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }
//
//    /**
//     * @param id
//     * @return findCompanyMember
//     * @throws Exception
//     * @throws DeliveryServiceException-general member findById fail due to
//     *                                          NonExistentMemberIdException
//     */
//    public GeneralMemberDto findById(String id) throws Exception {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            GeneralMemberEntity memberEntity = generalMemberRepository.findById(em, Long.parseLong(id))
//                .orElse(null);
//            if (memberEntity == null) {
//                throw new DeliveryServiceException(
//                    ExceptionMessage.DeliveryExceptionNonExistentMemberId);
//            }
//            tx.commit();
//            GeneralMemberDto memberDto = changeMemberEntityToDto(memberEntity);
//            return memberDto;
//        } catch (Exception e) {
//            tx.rollback();
//            throw e;
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }
//
//    private GeneralMemberDto changeMemberEntityToDto(GeneralMemberEntity memberEntity) {
//        GeneralMemberDto memberDto = new GeneralMemberDto(memberEntity.getId(),
//            memberEntity.getLoginName(), memberEntity.getName(), memberEntity.getCreatedAt());
//        return memberDto;
//    }
//
//    private List<GeneralMemberDto> changeAllMemberEntityToDto(List<GeneralMemberEntity> allMemberEntity) {
//        List<GeneralMemberDto> allMemberDto = allMemberEntity.stream().map(
//            m -> new GeneralMemberDto(m.getId(), m.getLoginName(), m.getName(),
//                m.getCreatedAt())).collect(Collectors.toList());
//        return allMemberDto;
//    }
//}
