package spring.boot.desafio.codecon.controller;

// Importações necessárias para o controlador funcionar
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.desafio.codecon.model.Usuario;
import spring.boot.desafio.codecon.repository.UsuarioRepository;
import spring.boot.desafio.codecon.service.AnaliseService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

// Define esta classe como um controlador REST
@RestController
// Gera automaticamente um construtor com os campos finais (injeção de dependência)
@RequiredArgsConstructor
public class UsuarioController {

    // Repositório que armazena os usuários em memória
    private final UsuarioRepository usuarioRepository;

    // Serviço com lógica de análise dos dados dos usuários
    private final AnaliseService analiseService;

    // Método utilitário para medir o tempo de execução de uma operação e retornar junto com os dados
    private <T> Map<String, Object> timed(Supplier<T> supplier) {
        long start = System.currentTimeMillis(); // Marca início da execução
        T data = supplier.get(); // Executa a função passada
        long exec = System.currentTimeMillis() - start; // Calcula o tempo decorrido

        // Retorna um mapa com timestamp atual, tempo de execução e os dados obtidos
        return Map.of(
                "timestamp", Instant.now(),
                "execution_time_ms", exec,
                "data", data
        );
    }

    private <I, R> Map<String, Object> timed(Function<I, R> function, I input) {
        long start = System.currentTimeMillis(); // Marca início
        R data = function.apply(input);          // Executa função com argumento
        long exec = System.currentTimeMillis() - start; // Tempo de execução

        return Map.of(
                "timestamp", Instant.now(),
                "execution_time_ms", exec,
                "data", data
        );
    }



    // Endpoint POST /users - recebe uma lista de usuários e salva no repositório
    @PostMapping("/users")
    public Map<String, Object> carregar(@RequestBody List<Usuario> usuarios) {
        return timed(() -> { // Mede o tempo da operação
            usuarioRepository.saveAll(usuarios); // Salva todos os usuários
            return Map.of(
                    "mensagem", "Arquivo recebido com sucesso",
                    "total_usuarios", usuarioRepository.findAll().size() // Retorna o total armazenado
            );
        });
    }

    // Endpoint GET /superusers - retorna superusuários com score >= 900 e ativos
    @GetMapping("/superusers")
    public Map<String, Object> superUsuarios() {
        return timed(analiseService::superUsuarios); // Mede o tempo da chamada do serviço
    }

    // Endpoint GET /top-countries - retorna os 5 países com mais superusuários
    @GetMapping("/top-countries")
    public Map<String, Object> topPaises() {
        return timed(analiseService::topPaises);
    }

    // Endpoint GET /team-insights - retorna estatísticas por equipe
    @GetMapping("/team-insights")
    public Map<String, Object> insightsEquipe() {
        return timed(analiseService::insightEquipes);
    }

    // Endpoint GET /active-users-per-day - retorna número de logins por dia
    @GetMapping("/active-users-per-day")
    public Map<String, Object> logins(@RequestParam(defaultValue = "0") int min) {
//        return timed(() -> analiseService.loginPorDia(min)); // Executa com filtro mínimo (min)
        return timed(analiseService::loginPorDia, min);
    }

}
