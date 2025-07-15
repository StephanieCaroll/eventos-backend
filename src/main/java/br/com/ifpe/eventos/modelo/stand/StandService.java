package br.com.ifpe.eventos.modelo.stand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StandService {
     @Autowired
    private StandRepository repository;
    @Transactional //Quando colocada em cima de uma função, ela abre um escopo de transação no banco para a mesma (ou tudo funciona, ounada vai ser rodado)
   public Stand save(Stand stand) {

       stand.setHabilitado(Boolean.TRUE);
      
       Stand s = repository.save(stand); // chama a função salvar, gravando no banco. retorna o objeto que acabou de ser gravado no banco
       
       return s;
   }
    //select * from clienteS
   public List<Stand> listarTodos() {
  
        return repository.findAll();
    }
   //select * from cliente where
    public Stand obterPorID(Long id) {

        return repository.findById(id).get();
    }

     @Transactional
   public void update(Long id, Stand standAlterado) {

      Stand stand = repository.findById(id).get(); //vai colsultar o cliente no banco
      stand.setCodigo(standAlterado.getCodigo());

	//acima é onde se faz a alteração
      repository.save(standAlterado);//como o cliente 
  }

   @Transactional
   public void delete(Long id) {

       Stand stand = repository.findById(id).get();
       stand.setHabilitado(Boolean.FALSE);

       repository.save(stand);
   }


}
