package com.huce.application.controller.admin;

import com.huce.application.model.mapper.CertificationMapper;
import com.huce.application.model.request.CreateCertificationRequest;
import com.huce.application.entity.Certification;
import com.huce.application.entity.User;
import com.huce.application.security.CustomUserDetails;
import com.huce.application.service.CertificationService;
import com.huce.application.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/certifications")
    public String homePage(Model model,
                           @RequestParam(defaultValue = "", required = false) String id,
                           @RequestParam(defaultValue = "", required = false) String name,
                           @RequestParam(defaultValue = "", required = false) String status,
                           @RequestParam(defaultValue = "1", required = false) Integer page) {

        //Lấy tất cả các anh của user upload
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> images = imageService.getListImageOfUser(user.getId());
        model.addAttribute("images", images);

        Page<Certification> certifications = certificationService.adminGetListCertifications(id, name, status, page);
        model.addAttribute("certifications", certifications.getContent());
        model.addAttribute("totalPages", certifications.getTotalPages());
        model.addAttribute("currentPage", certifications.getPageable().getPageNumber() + 1);
        return "admin/certification/list";
    }

    @PostMapping("/api/admin/certifications")
    public ResponseEntity<Object> createCertification(@Valid @RequestBody CreateCertificationRequest createCertificationRequest) {
        Certification certification = certificationService.createCertification(createCertificationRequest);
        return ResponseEntity.ok(CertificationMapper.toCertificationDTO(certification));
    }

    @PutMapping("/api/admin/certifications/{id}")
    public ResponseEntity<Object> updateCertification(@Valid @RequestBody CreateCertificationRequest createCertificationRequest, @PathVariable long id) {
        certificationService.updateCertification(createCertificationRequest, id);
        return ResponseEntity.ok("Sửa chứng nhận thành công!");
    }

    @DeleteMapping("/api/admin/certifications/{id}")
    public ResponseEntity<Object> deleteCertification(@PathVariable long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.ok("Xóa chứng nhận thành công!");
    }
    @GetMapping("/api/admin/certifications/{id}")
    public ResponseEntity<Object> getCertificationById(@PathVariable long id){
        Certification certification = certificationService.getCertificationById(id);
        return ResponseEntity.ok(certification);
    }
}
