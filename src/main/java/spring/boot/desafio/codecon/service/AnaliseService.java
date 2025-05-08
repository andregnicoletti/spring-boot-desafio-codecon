package spring.boot.desafio.codecon.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.desafio.codecon.model.InsightEquipe;
import spring.boot.desafio.codecon.model.LogDeAcesso;
import spring.boot.desafio.codecon.model.Usuario;
import spring.boot.desafio.codecon.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnaliseService {

    private final UsuarioRepository usuarioRepository;

    // Retorna uma lista de "super usuários": score >= 900 e ativos
    public List<Usuario> superUsuarios() {
        return usuarioRepository.findAll().parallelStream() // percorre os usuários em paralelo
                .filter(usuario -> usuario.getScore() >= 900 && usuario.isAtivo()) // filtra por score e ativo
                .toList(); // converte em lista
    }

    // Retorna uma lista com os países e o total de usuários em cada país, ordenados do maior para o menor
    public List<Map<String, Object>> topPaises() {
        return usuarioRepository.findAll().parallelStream() // percorre usuários em paralelo
                .collect(Collectors.groupingBy(Usuario::getPais, Collectors.counting())) // agrupa por país e conta
                .entrySet().stream() // transforma em stream de Map.Entry
                .sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue).reversed()) // ordena por total decrescente
                .map(e -> Map.<String, Object>of("pais", e.getKey(), "total", e.getValue())) // transforma em mapa com campos "pais" e "total"
                .toList(); // converte para lista
    }

    // Retorna informações agregadas por equipe (InsightEquipe)
    public List<InsightEquipe> insightEquipes() {
        return usuarioRepository.findAll().parallelStream() // percorre os usuários em paralelo
                .collect(Collectors.groupingBy(u -> u.getEquipe().getNome())) // agrupa os usuários pelo nome da equipe
                .entrySet().stream() // transforma em stream de Map.Entry<String, List<Usuario>>
                .map(e -> { // para cada grupo de equipe
                    var usuarios = e.getValue(); // lista de usuários da equipe
                    int membros = usuarios.size(); // total de membros
                    int lideres = (int) usuarios.stream().filter(u -> u.getEquipe().isLider()).count(); // conta líderes
                    int concluidos = usuarios.stream() // para cada usuário
                            .flatMap(u -> u.getEquipe().getProjetos().stream()) // pega os projetos da equipe
                            .mapToInt(p -> p.isConcluido() ? 1 : 0).sum(); // soma os projetos concluídos
                    int ativos = (int) usuarios.stream().filter(Usuario::isAtivo).count(); // conta usuários ativos
                    double pct = membros == 0 ? 0 : ativos * 100.0 / membros; // calcula % de ativos
                    return new InsightEquipe(e.getKey(), membros, lideres, concluidos, pct); // cria objeto InsightEquipe
                }).toList(); // converte para lista
    }

    // Retorna um mapa com data do login e total de logins por dia (apenas acima do mínimo definido)
    public Map<LocalDate, Long> loginPorDia(int min) {
        return usuarioRepository.findAll().parallelStream() // percorre todos os usuários em paralelo
                .flatMap(u -> u.getLogs().stream()) // pega os logs de cada usuário
                .filter(l -> "login".equalsIgnoreCase(l.getAcao())) // filtra apenas os logs de login
                .collect(Collectors.groupingBy(LogDeAcesso::getData, Collectors.counting())) // agrupa por data e conta
                .entrySet().stream() // stream de entradas do mapa
                .filter(e -> e.getValue() >= min) // mantém apenas os dias com logins >= min
                .sorted(Map.Entry.comparingByKey()) // ordena por data
                .collect(Collectors.toMap( // coleta em LinkedHashMap mantendo a ordem
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

}
