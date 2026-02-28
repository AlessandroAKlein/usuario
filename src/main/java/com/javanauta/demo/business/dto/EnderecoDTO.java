package com.javanauta.demo.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class EnderecoDTO {

    private String rua;
    private Long numero;
    private String cidade;
    private String estado;
    private String cep;

}
