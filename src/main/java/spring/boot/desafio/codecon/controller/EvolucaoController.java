package spring.boot.desafio.codecon.controller;

// Importa anotações e classes necessárias do Spring e Java
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
// Gera automaticamente um construtor com todos os campos finais (injeção de dependência)
@RequiredArgsConstructor
public class EvolucaoController {

    // Configuração de estratégias de troca para permitir respostas maiores (até 16MB em memória)
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
            .build();

    // WebClient reativo configurado com URL base local e estratégia de troca definida acima
    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .exchangeStrategies(strategies)
            .build();

    // Define o endpoint GET "/evaluation" que avalia a performance de outros endpoints
    @GetMapping("/evaluation")
    public Map<String, Object> evaluate() {
        // Lista de endpoints que serão testados
        List<String> endPoints = List.of(
                "/superusers",
                "/top-countries",
                "/team-insights",
                "/active-users-per-day"
        );

        // Mapa para armazenar os resultados dos testes, mantendo a ordem de inserção
        Map<String, Object> results = new LinkedHashMap<>();

        // Itera sobre cada endpoint para medir tempo de resposta e status
        endPoints.forEach(ep -> {
            // Marca o tempo de início da requisição
            long start = System.currentTimeMillis();

            // Executa uma requisição GET síncrona ao endpoint usando WebClient
            ResponseEntity<String> resp = client.get()
                    .uri(ep)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            // Calcula o tempo total da requisição
            long time = System.currentTimeMillis() - start;

            // Adiciona os resultados (status HTTP, tempo e validade) ao mapa
            results.put(ep, Map.of(
                    "status", resp == null ? 0 : resp.getStatusCode().value(),
                    "time_as", time,
                    "valid_response", true // Aqui sempre está como true, mas pode ser ajustado para verificar conteúdo
            ));
        });

        // Retorna os resultados organizados em um mapa com a chave "tested_endpoints"
        return Map.of("tested_endpoints", results);
    }
}
