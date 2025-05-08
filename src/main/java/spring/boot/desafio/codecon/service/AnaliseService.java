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

    public List<Usuario> superUsuarios() {
        return usuarioRepository.findAll().parallelStream()
                .filter(usuario -> usuario.getScore() >= 900 && usuario.isAtivo())
                .toList();
    }

    public List<Map<String, Object>> topPaises() {
        return usuarioRepository.findAll().parallelStream()
                .collect(Collectors.groupingBy(Usuario::getPais, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue).reversed())
                .map(e -> Map.<String, Object>of("pais", e.getKey(), "total", e.getValue()))
                .toList();
    }

    public List<InsightEquipe> insightEquipes() {
        return usuarioRepository.findAll().parallelStream()
                .collect(Collectors.groupingBy(u -> u.getEquipe().getNome()))
                .entrySet().stream()
                .map(e -> {
                    var usuarios = e.getValue();
                    int membros = usuarios.size();
                    int lideres = (int) usuarios.stream().filter(u -> u.getEquipe().isLider()).count();
                    int concluidos = usuarios.stream()
                            .flatMap(u -> u.getEquipe().getProjetos().stream())
                            .mapToInt(p -> p.isConcluido() ? 1 : 0).sum();
                    int ativos = (int) usuarios.stream().filter(Usuario::isAtivo).count();
                    double pct = membros == 0 ? 0 : ativos * 100.0 / membros;
                    return new InsightEquipe(e.getKey(), membros, lideres, concluidos, pct);
                }).toList();
    }

    public Map<LocalDate, Long> loginPorDia(int min) {

        return usuarioRepository.findAll().parallelStream()
                .flatMap(u -> u.getLogs().stream())
                .filter(l -> "login".equalsIgnoreCase(l.getAcao()))
                .collect(Collectors.groupingBy(LogDeAcesso::getData, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() >= min)
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

}
