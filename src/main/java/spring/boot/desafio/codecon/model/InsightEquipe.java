package spring.boot.desafio.codecon.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsightEquipe {

    private String equipe;
    private int totalMembros;
    private int lideres;
    private int projetosConcluidos;
    private double percentualAtivos;

}
