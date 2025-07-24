package br.com.ifpe.eventos.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;

@Component
public class DataLoader implements CommandLineRunner {

    private final StandService standService;

    public DataLoader(StandService standService) {
        this.standService = standService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem stands cadastrados
        if (standService.listarTodos().isEmpty()) {
            System.out.println("Criando stands iniciais para o sistema...");
            
            // Criar stands organizados em uma grade para facilitar seleção visual
            String[] standCodes = {
                // Fileira A (front row)
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                // Fileira B
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
                // Fileira C
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8",
                // Fileira D
                "D1", "D2", "D3", "D4", "D5", "D6",
                // Fileira E (premium)
                "E1", "E2", "E3", "E4",
                // Fileira F (premium)
                "F1", "F2", "F3", "F4",
                // Área G (especial)
                "G1", "G2", "G3", "G4", "G5",
                // Área H (especial)
                "H1", "H2", "H3", "H4", "H5",
                // Área I (lateral)
                "I1", "I2", "I3", "I4"
            };

            for (String code : standCodes) {
                Stand stand = Stand.builder()
                    .codigo(code)
                    .descricao("Stand " + code + " - Disponível para cadastro em eventos")
                    .evento(null) // Inicialmente sem vinculação a evento
                    .usuario(null) // Inicialmente sem usuário
                    .build();
                standService.save(stand);
                System.out.println("Stand '" + code + "' cadastrado com sucesso.");
            }
            
            System.out.println("Total de " + standCodes.length + " stands cadastrados no sistema.");
            System.out.println("Os stands estão disponíveis para serem vinculados a eventos pelo gerenciador.");
        } else {
            System.out.println("Stands já existem no sistema. Total: " + standService.listarTodos().size());
        }
    }
}