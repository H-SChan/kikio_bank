package com.kakao.bank.service.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation = Paths.get("static/").toAbsolutePath().normalize();

    /**
     * 파일 저장
     * @return fileName
     */
    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()
                || (!Objects.equals(FilenameUtils.getExtension(file.getOriginalFilename()), "png"))
                && (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("jpg"))
                && (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("jpeg"))
                && (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("gif"))
                && (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("svg"))) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류");
        }
        String fileName = StringUtils.cleanPath(
                UUID.randomUUID().toString()
                        + "-"
                        + Objects.nonNull(file.getOriginalFilename()) + ".jpg"
        );
        try {
            Path makeFile = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), makeFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public Resource loadFile(String fileName) {
        Resource resource = null;
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resource != null && resource.exists()) {
            return resource;
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 파일");
        }
    }

}
