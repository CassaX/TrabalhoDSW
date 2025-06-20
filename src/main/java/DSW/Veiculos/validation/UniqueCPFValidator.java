package DSW.Veiculos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import DSW.Veiculos.dao.IClienteDAO;
import DSW.Veiculos.domain.Cliente;

@Component
public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    @Autowired
    private IClienteDAO dao;

    @Override
    public boolean isValid(String CPF, ConstraintValidatorContext context) {
        if (dao != null) {
            Cliente cliente = dao.findByCPF(CPF);
            return cliente == null;
        } else {
            // Durante a execução inicial, quando injeção não ocorreu ainda
            return true;
        }
    }
}
