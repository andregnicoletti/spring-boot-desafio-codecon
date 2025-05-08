package spring.boot.desafio.codecon.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogDeAcesso {

    private LocalDate data;
    private String acao;

}
