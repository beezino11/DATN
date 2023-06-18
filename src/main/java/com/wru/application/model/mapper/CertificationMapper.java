//package com.wru.application.model.mapper;
//
//import com.wru.application.model.request.CreateCertificationRequest;
//import com.wru.application.entity.Certification;
//import com.wru.application.model.dto.CertificationDTO;
//
//import java.sql.Timestamp;
//
//public class CertificationMapper {
//    public static CertificationDTO toCertificationDTO(Certification certification){
//        CertificationDTO certificationDTO = new CertificationDTO();
//        certificationDTO.setId(certificationDTO.getId());
//        certificationDTO.setName(certificationDTO.getName());
//        certificationDTO.setDescription(certificationDTO.getDescription());
//        certificationDTO.setThumbnail(certificationDTO.getThumbnail());
//        certificationDTO.setStatus(certificationDTO.isStatus());
//
//        return certificationDTO;
//    }
//
//    public static Certification toCertification(CreateCertificationRequest createCertificationRequest){
//        Certification certification= new Certification();
//        certification.setName(createCertificationRequest.getName());
//        certification.setDescription(createCertificationRequest.getDescription());
//        certification.setThumbnail(createCertificationRequest.getThumbnail());
//        certification.setStatus(createCertificationRequest.isStatus());
//        certification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//
//        return certification;
//    }
//}
