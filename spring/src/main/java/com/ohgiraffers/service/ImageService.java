package com.ohgiraffers.service;

import com.ohgiraffers.dto.RequestDTO;
import com.ohgiraffers.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

@Service
@Slf4j
public class ImageService {

    private final RestTemplate restTemplate;

    // 요청 보낼 url
    private final String FAST_API_SERVER_URL = "http://localhost:8000/analyze/upload/";

    public ImageService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public ResponseDTO analyzeimage(RequestDTO requestDTO) {
        // 이미지 파일을 MulitpartFile에서 바이너리로 변환
        MultipartFile file = requestDTO.getFile(); // 이미지 파일 가져오기
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try{
            byteArrayOutputStream.write(file.getBytes());
        } catch(IOException e){
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다.");
        }

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 요청 본문 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(byteArrayOutputStream.toByteArray()){
            @Override
            public String getFilename(){
                return file.getOriginalFilename(); // 파일 이름
            }
        });

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<ResponseDTO> response;
        try {
            response = restTemplate.exchange(
                    FAST_API_SERVER_URL, // 요청 url
                    HttpMethod.POST,     // HTTP 요청 메서드
                    entity,              // 요청 entity(헤더 + 본문)
                    ResponseDTO.class    // 응답 본문을 반환할 타입(JSON -> ResponseDTO)
            );

            log.info("=== FastAPI 서버 응답 메시지");
            log.info("응답 결과 : {}", response.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException("FastAPI 서버와의 통신 중 오류가 발생했습니다.", e);
        }

        return response.getBody();
    }
}
