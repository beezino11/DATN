package com.huce.application.service.impl;

import com.huce.application.exception.BadRequestException;
import com.huce.application.exception.InternalServerException;
import com.huce.application.exception.NotFoundException;
import com.huce.application.model.mapper.CertificationMapper;
import com.huce.application.model.request.CreateCertificationRequest;
import com.huce.application.repository.CertificationRepository;
import com.huce.application.entity.Certification;
import com.huce.application.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.huce.application.config.Constants.LIMIT_CERTIFICATION;

@Component
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public Page<Certification> adminGetListCertifications(String id, String name, String status, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_CERTIFICATION, Sort.by("created_at").descending());
        return certificationRepository.adminGetListCertifications(id, name, status, pageable);

    }

    @Override
    public List<Certification> getListCertification() {
        return certificationRepository.findAll();
    }

    @Override
    public Certification createCertification(CreateCertificationRequest createCertificationRequest) {
        Certification certification = certificationRepository.findByName(createCertificationRequest.getName());
        if (certification != null) {
            throw new BadRequestException("Tên chứng nhận đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }
        certification = CertificationMapper.toCertification(createCertificationRequest);
        certificationRepository.save(certification);
        return certification;
    }

    @Override
    public void updateCertification(CreateCertificationRequest createCertificationRequest, Long id) {
        Optional<Certification> certification = certificationRepository.findById(id);
        if (certification.isEmpty()) {
            throw new NotFoundException("Tên chứng nhận không tồn tại!");
        }
        Certification certification1 = certificationRepository.findByName(createCertificationRequest.getName());
        if (certification1 != null) {
            if (!createCertificationRequest.getId().equals(certification1.getId()))
                throw new BadRequestException("Tên chứng nhận " + createCertificationRequest.getName() + " đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }
        Certification rs = certification.get();
        rs.setId(id);
        rs.setName(createCertificationRequest.getName());
        rs.setDescription(createCertificationRequest.getDescription());
        rs.setThumbnail(createCertificationRequest.getThumbnail());
        rs.setStatus(createCertificationRequest.isStatus());
        rs.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        try {
            certificationRepository.save(rs);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi chỉnh sửa chứng nhận");
        }
    }

    @Override
    public void deleteCertification(long id) {
        Optional<Certification> certification = certificationRepository.findById(id);
        if (certification.isEmpty()) {
            throw new NotFoundException("Tên chứng nhận không tồn tại!");
        }
        try {
            certificationRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa nhãn hiệu!");
        }
    }

    @Override
    public Certification getCertificationById(long id) {
        Optional<Certification> certification = certificationRepository.findById(id);
        if (certification.isEmpty()) {
            throw new NotFoundException("Tên chứng nhận không tồn tại!");
        }
        return certification.get();
    }

    @Override
    public long getCountCertification() {
        return certificationRepository.count();
    }
}
