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
        
        if (standService.listarTodos().isEmpty()) {
            String[] standCodes = {
                "A1", "A2", "A3", "A4",
                "B1", "B2", "B3", "B4",
                "C1", "C2", "C3", "C4",
                "D1", "D2",
                "E1", "E2",
                "F1", "F2",
                "G1", "G2", "G3",
                "H1", "H2", "H3",
                "I1", "I2", "I3"
            };

            for (String code : standCodes) {
                Stand stand = Stand.builder().codigo(code).build();
                standService.save(stand);
                System.out.println("Stand '" + code + "' cadastrado.");
            }
        }
    }
}