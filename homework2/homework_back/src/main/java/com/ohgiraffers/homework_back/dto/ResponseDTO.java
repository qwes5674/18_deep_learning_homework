package com.ohgiraffers.homework_back.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDTO {

    private int person_count;
    private String congestion;
    private String img;

}
