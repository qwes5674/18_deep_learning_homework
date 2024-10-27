package com.ohgiraffers.homework_back.controller;

import com.ohgiraffers.homework_back.dto.RequestDTO;
import com.ohgiraffers.homework_back.dto.ResponseDTO;
import com.ohgiraffers.homework_back.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/analyze")
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "이미지 분석하기 ", description = "분석할 이미지를 올려주세요.")
    public ResponseDTO analyzeFile(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file")MultipartFile file){

        // RequestDTO 객체 생성
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFile(file); // MultipartFile 설정

        // 이미지 분석 요청
        ResponseDTO responseDTO = imageService.analyzeimage(requestDTO);
        return responseDTO;

    }

}
