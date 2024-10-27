package com.ohgiraffers.controller;

import com.ohgiraffers.dto.RequestDTO;
import com.ohgiraffers.dto.ResponseDTO;
import com.ohgiraffers.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/analyze")
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 분석하기", description = "분석할 이미지를 올려주세요.")
    public ResponseDTO analyzeFile(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestParam("file") MultipartFile file) {

        // RequestDTO 객체 생성
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFile(file); // MultipartFile 설정

        // 이미지 분석 요청
        ResponseDTO responseDTO = imageService.analyzeimage(requestDTO);
        return responseDTO;
    }
}
