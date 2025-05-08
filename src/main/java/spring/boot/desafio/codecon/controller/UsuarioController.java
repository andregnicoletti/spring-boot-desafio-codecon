package spring.boot.desafio.codecon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.desafio.codecon.model.Usuario;
import spring.boot.desafio.codecon.repository.UsuarioRepository;
import spring.boot.desafio.codecon.service.AnaliseService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final AnaliseService analiseService;

    private <T> Map<String, Object> timed(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T data = supplier.get();
        long exec = System.currentTimeMillis() - start;
        return Map.of(
                "timestamp", Instant.now(),
                "execution_time_ms", exec,
                "data", data
        );
    }

    @PostMapping("/users")
    public Map<String, Object> carregar(@RequestBody List<Usuario> usuarios) {
        return timed(() -> {
            usuarioRepository.saveAll(usuarios);
            return Map.of(
                    "mensagem", "Arquivo recebido com sucesso",
                    "total_usuarios", usuarioRepository.findAll().size()
            );
        });
    }

    @GetMapping("/superusers")
    public Map<String, Object> superUsuarios() {
        return timed(analiseService::superUsuarios);
    }

    @GetMapping("/top-countries")
    public Map<String, Object> topPaises() {
        return timed(analiseService::topPaises);
    }

    @GetMapping("/team-insights")
    public Map<String, Object> insightsEquipe() {
        return timed(analiseService::insightEquipes);
    }

    @GetMapping("/active-users-per-day")
    public Map<String, Object> logins(@RequestParam(defaultValue = "0") int min) {
        return timed(()-> analiseService.loginPorDia(min));
    }

}
