package DSW.Veiculos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DSW.Veiculos.DAO.IVeiculoDAO;
import DSW.Veiculos.domain.Loja;
import DSW.Veiculos.domain.Veiculo;
import DSW.Veiculos.service.spec.IVeiculoService;

@Service
@Transactional
public  class VeiculoService implements IVeiculoService{
    @Autowired
    private IVeiculoDAO veiculoDAO;

    @Override
    public void salvar(Veiculo veiculo) {
        veiculoDAO.save(veiculo);
    }

    @Override
    public List<Veiculo> buscarTodos() {
        return veiculoDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Veiculo buscarPorId(Long id) {
        return veiculoDAO.findById(id.longValue()).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        if (!veiculoDAO.existsById(id)) {
            throw new IllegalArgumentException("Veículo não encontrado com ID: " + id);
        }
        veiculoDAO.deleteById(id);
    }

    @Override
    public void editar(Veiculo veiculo) {
        if (!veiculoDAO.existsById(veiculo.getId())) {
            throw new IllegalArgumentException("Veículo não encontrado com ID: " + veiculo.getId());
        }
        veiculoDAO.save(veiculo);
    }

    @Override
    public List<Veiculo> buscarPorLoja(Loja loja) {
        return veiculoDAO.findByLoja(loja);
    }

    @Override
    public List<Veiculo> buscarPorModelo(String modelo) {
        return veiculoDAO.findByModelo(modelo);
    }

}
