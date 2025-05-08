package spring.boot.desafio.codecon.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {

    private UUID id;
    private String nome;
    private int idade;
    private int score;
    private boolean ativo;
    private String pais;
    private Equipe equipe;
    private List<LogDeAcesso> logs;

}
