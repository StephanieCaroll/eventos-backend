package br.com.ifpe.eventos.api.stand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;
import br.com.ifpe.eventos.modelo.stand.dto.StandBatchRequestDTO;
import br.com.ifpe.eventos.modelo.stand.dto.StandReservaDTO;
import br.com.ifpe.eventos.modelo.stand.dto.StandSelectionDTO;

@RestController
@RequestMapping("/api/stand-selection")
@CrossOrigin(origins = "http://localhost:3000")
public class StandSelectionController {

    @Autowired
    private StandService standService;

    // Endpoint de teste para debug
    @PostMapping("/debug")
    public ResponseEntity<?> debugEndpoint(@RequestBody Object payload) {
        try {
            System.out.println("Payload recebido: " + payload.toString());
            return ResponseEntity.ok(Map.of(
                "message", "Debug endpoint funcionando",
                "payload", payload.toString(),
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            System.err.println("Erro no debug endpoint: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }

    // Endpoint para listar todos os stands com status de disponibilidade
    @GetMapping("/grid")
    public ResponseEntity<List<StandSelectionDTO>> obterGridStands(
            @RequestParam(required = false) Long eventoId,
            @RequestParam(required = false) String userId) {
        try {
            List<StandSelectionDTO> stands = standService.listarStandsParaSelecao(eventoId, userId);
            return ResponseEntity.ok(stands);
        } catch (Exception e) {
            System.err.println("Erro ao obter grid de stands: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // Endpoint para selecionar/desselecionar stands
    @PostMapping("/toggle")
    public ResponseEntity<?> toggleStandSelection(@RequestBody StandToggleRequest request) {
        try {
            // Validação dos dados de entrada
            if (request.getUserId() == null || request.getStandId() == null) {
                return ResponseEntity.badRequest().body("UserId e StandId são obrigatórios");
            }

            StandReservaDTO reservaDTO = StandReservaDTO.builder()
                .userId(request.getUserId())
                .eventoId(request.getEventoId())
                .standIds(List.of(request.getStandId()))
                .descricaoReserva(request.getDescricao())
                .tipoOperacao(request.isSelecionar() ? "RESERVAR" : "LIBERAR")
                .build();

            List<Stand> resultado = standService.processarReservaStands(reservaDTO);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            System.err.println("Erro de negócio ao alternar seleção: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro interno ao alternar seleção de stand: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Endpoint para operações em lote (selecionar/liberar múltiplos stands)
    @PostMapping("/batch")
    public ResponseEntity<?> processarStandsEmLote(@RequestBody StandBatchRequestDTO batchRequest) {
        try {
            System.out.println("=== DEBUG BATCH OPERATION ===");
            System.out.println("Dados recebidos:");
            System.out.println("- userId: " + batchRequest.getUserId());
            System.out.println("- eventoId: " + batchRequest.getEventoId());
            System.out.println("- standIds: " + batchRequest.getStandIds());
            System.out.println("- tipoOperacao: " + batchRequest.getTipoOperacao());
            System.out.println("- descricaoReserva: " + batchRequest.getDescricaoReserva());
            
            // Validação dos dados de entrada
            if (batchRequest.getUserId() == null || batchRequest.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("UserId é obrigatório");
            }
            if (batchRequest.getStandIds() == null || batchRequest.getStandIds().isEmpty()) {
                return ResponseEntity.badRequest().body("Lista de stands não pode estar vazia");
            }
            if (batchRequest.getTipoOperacao() == null || batchRequest.getTipoOperacao().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tipo de operação é obrigatório");
            }

            // Converter para StandReservaDTO
            StandReservaDTO reservaDTO = batchRequest.toStandReservaDTO();
            
            System.out.println("Dados convertidos para processamento:");
            System.out.println("- userId: " + reservaDTO.getUserId());
            System.out.println("- eventoId: " + reservaDTO.getEventoId());
            System.out.println("- standIds: " + reservaDTO.getStandIds());
            System.out.println("- tipoOperacao: " + reservaDTO.getTipoOperacao());
            
            List<Stand> resultado = standService.processarReservaStands(reservaDTO);
            System.out.println("Operação concluída com sucesso. Stands processados: " + resultado.size());
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            System.err.println("Erro de negócio ao processar stands em lote: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro interno ao processar stands em lote: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Classe interna para requests de toggle
    public static class StandToggleRequest {
        private String userId;
        private Long eventoId;
        private Long standId;
        private String descricao;
        private boolean selecionar;

        // Getters e Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public Long getEventoId() { return eventoId; }
        public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
        public Long getStandId() { return standId; }
        public void setStandId(Long standId) { this.standId = standId; }
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public boolean isSelecionar() { return selecionar; }
        public void setSelecionar(boolean selecionar) { this.selecionar = selecionar; }
    }
}