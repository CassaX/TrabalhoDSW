package DSW.Veiculos.service.spec;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DSW.Veiculos.DAO.IVeiculoDAO;
import DSW.Veiculos.domain.Veiculo;

@Service
public class VeiculoService {

    @Autowired
    private IVeiculoDAO dao;

    public void salvar(Veiculo veiculo) {
        dao.save(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return dao.findAll();
    }
}
