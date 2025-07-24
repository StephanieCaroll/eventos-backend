package br.com.ifpe.eventos.api.stand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;
import br.com.ifpe.eventos.modelo.stand.dto.StandReservaDTO;
import br.com.ifpe.eventos.modelo.stand.dto.StandSelectionDTO;

@RestController
@RequestMapping("/api/stand") 
@CrossOrigin
public class StandController {

    @Autowired
    private StandService standService;

    @PostMapping
    public ResponseEntity<Stand> save(@RequestBody StandRequest request) {
        Stand stand = standService.save(request);
        return new ResponseEntity<>(stand, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Stand> listarTodos() {
        return standService.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<Stand> listarStandsDisponiveis(@RequestParam(required = false) Long eventId) {
      
        return standService.listarStandsDisponiveis(); 
    }

    @GetMapping("/usuario") 
    public ResponseEntity<List<Stand>> getStandsDoUsuarioPorEvento(
            @RequestParam String userId, 
            @RequestParam Long eventId) {
        try {
            List<Stand> stands = standService.findByUsuarioUsernameAndEventoId(userId, eventId);
            return new ResponseEntity<>(stands, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands do usuário por evento: " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping("/registered")
    public ResponseEntity<List<Stand>> getRegisteredStandsByUserId(@RequestParam String userId) {
        try {
            List<Stand> stands = standService.getRegisteredStandsByUserId(userId);
            return new ResponseEntity<>(stands, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands registrados do usuário: " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public Stand obterPorID(@PathVariable Long id) {
        return standService.obterPorID(id);
    }

    // Endpoint para obter informações detalhadas de um stand
    @GetMapping("/{id}/info")
    public ResponseEntity<StandSelectionDTO> obterInfoStand(
            @PathVariable Long id,
            @RequestParam(required = false) String userId) {
        try {
            StandSelectionDTO standInfo = standService.getStandInfo(id, userId);
            if (standInfo != null) {
                return ResponseEntity.ok(standInfo);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Erro ao obter info do stand: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stand> update(@PathVariable("id") Long id, @RequestBody StandRequest request) {
        standService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        standService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Novo endpoint para listar stands com informações de seleção
    @GetMapping("/selecao")
    public ResponseEntity<List<StandSelectionDTO>> listarStandsParaSelecao(
            @RequestParam(required = false) Long eventoId,
            @RequestParam(required = false) String userId) {
        try {
            List<StandSelectionDTO> stands = standService.listarStandsParaSelecao(eventoId, userId);
            return ResponseEntity.ok(stands);
        } catch (Exception e) {
            System.err.println("Erro ao listar stands para seleção: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // Endpoint para reservar/liberar múltiplos stands
    @PostMapping("/processar-reserva")
    public ResponseEntity<?> processarReservaStands(@RequestBody StandReservaDTO reservaDTO) {
        try {
            // Validação dos dados de entrada
            if (reservaDTO.getUserId() == null) {
                return ResponseEntity.badRequest().body("UserId é obrigatório");
            }
            if (reservaDTO.getStandIds() == null || reservaDTO.getStandIds().isEmpty()) {
                return ResponseEntity.badRequest().body("Lista de stands não pode estar vazia");
            }
            if (reservaDTO.getTipoOperacao() == null) {
                return ResponseEntity.badRequest().body("Tipo de operação é obrigatório");
            }

            List<Stand> standsProcessados = standService.processarReservaStands(reservaDTO);
            return ResponseEntity.ok(standsProcessados);
        } catch (RuntimeException e) {
            System.err.println("Erro de negócio ao processar reserva: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro interno ao processar reserva de stands: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // Endpoint alternativo para aceitar formato do frontend
    @PostMapping("/batch-operation")
    public ResponseEntity<?> batchOperation(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== BATCH OPERATION ALTERNATIVO ===");
            System.out.println("Payload completo: " + payload);
            
            // Extrair dados do payload
            String usuarioId = (String) payload.get("usuarioId");
            Object eventoIdObj = payload.get("eventoId");
            Long eventoId = eventoIdObj != null ? Long.valueOf(eventoIdObj.toString()) : null;
            
            @SuppressWarnings("unchecked")
            List<Integer> standIdsInt = (List<Integer>) payload.get("standIds");
            List<Long> standIds = standIdsInt != null ? 
                standIdsInt.stream().map(Long::valueOf).collect(java.util.stream.Collectors.toList()) : null;
            
            String operacao = (String) payload.get("operacao");
            String observacoes = (String) payload.get("observacoes");
            
            System.out.println("Dados extraídos:");
            System.out.println("- usuarioId: " + usuarioId);
            System.out.println("- eventoId: " + eventoId);
            System.out.println("- standIds: " + standIds);
            System.out.println("- operacao: " + operacao);
            System.out.println("- observacoes: " + observacoes);
            
            // Validações
            if (usuarioId == null || usuarioId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("usuarioId é obrigatório");
            }
            if (standIds == null || standIds.isEmpty()) {
                return ResponseEntity.badRequest().body("standIds não pode estar vazio");
            }
            if (operacao == null || operacao.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("operacao é obrigatória");
            }
            
            // Criar StandReservaDTO
            StandReservaDTO reservaDTO = StandReservaDTO.builder()
                .userId(usuarioId)
                .eventoId(eventoId)
                .standIds(standIds)
                .tipoOperacao(operacao)
                .descricaoReserva(observacoes)
                .build();
            
            List<Stand> resultado = standService.processarReservaStands(reservaDTO);
            System.out.println("Operação concluída. Stands processados: " + resultado.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.err.println("Erro no batch operation alternativo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }
}