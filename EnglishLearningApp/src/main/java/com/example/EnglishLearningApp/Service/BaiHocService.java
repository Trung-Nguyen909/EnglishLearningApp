package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Mapper.BaihocMapper;
import com.example.EnglishLearningApp.Model.BaiHoc;
import com.example.EnglishLearningApp.Repository.BaiHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaiHocService {

    private final BaiHocRepository baiHocRepository;
    private final BaihocMapper baihocMapper;

    public List<BaiHoc> getAllBaiHoc() {
        return baiHocRepository.findAll();
    }

    public Optional<BaiHoc> getBaiHocById(Integer id) {
        return baiHocRepository.findById(id);
    }

    public BaiHoc createBaiHoc(BaiHoc baiHoc) {
        return baiHocRepository.save(baiHoc);
    }

    public BaiHoc updateBaiHoc(Integer id, BaiHoc baiHocDetails) {
        return baiHocRepository.findById(id)
                .map(baiHoc -> {
                    if(baiHocDetails.getIdKhoaHoc() != null) baiHoc.setIdKhoaHoc(baiHocDetails.getIdKhoaHoc());
                    if(baiHocDetails.getTenBaiHoc() != null && !baiHocDetails.getTenBaiHoc().isEmpty()) baiHoc.setTenBaiHoc(baiHocDetails.getTenBaiHoc());
                    if(baiHocDetails.getMoTa() != null) baiHoc.setMoTa(baiHocDetails.getMoTa());
                    if(baiHocDetails.getNoiDung() != null) baiHoc.setNoiDung(baiHocDetails.getNoiDung());
                    if(baiHocDetails.getThuTuBaiHoc() != null) baiHoc.setThuTuBaiHoc(baiHocDetails.getThuTuBaiHoc());
                    if(baiHocDetails.getTrangThai() != null) baiHoc.setTrangThai(baiHocDetails.getTrangThai());
                    
                    return baiHocRepository.save(baiHoc);
                })
                .orElseThrow(() -> new RuntimeException("BaiHoc not found with id " + id));
    }

    public String updateBaiHocIcon(Integer id, org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        BaiHoc baiHoc = baiHocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BaiHoc not found with id " + id));

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new RuntimeException("Invalid file name");
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        java.util.List<String> allowed = java.util.Arrays.asList(".png", ".jpg", ".jpeg", ".webp");
        if (!allowed.contains(extension.toLowerCase())) throw new RuntimeException("File type not allowed");

        String fileName = baiHoc.getId() + "_icon" + extension;
        java.nio.file.Path path = java.nio.file.Paths.get("img_baihoc", fileName);
        java.nio.file.Files.createDirectories(path.getParent());
        java.nio.file.Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        
        baiHoc.setIconUrl(fileName);
        baiHocRepository.save(baiHoc);
        
        return fileName;
    }

    public void deleteBaiHoc(Integer id) {
        baiHocRepository.deleteById(id);
    }

    public List<BaiHoc> getBaiHocByKhoaHocId(Integer khoaHocId) {
        return baiHocRepository.findByIdKhoaHoc(khoaHocId);
    }
}
