package spring.boot.desafio.codecon.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Projeto {

    private String nome;
    private boolean concluido;

}
