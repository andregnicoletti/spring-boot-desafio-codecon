package spring.boot.desafio.codecon.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Equipe {

    private String nome;
    private boolean lider;
    private List<Projeto> projetos;

}
