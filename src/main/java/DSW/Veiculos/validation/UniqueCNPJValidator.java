package DSW.Veiculos.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import DSW.Veiculos.DAO.ILojaDAO;
import DSW.Veiculos.domain.Loja;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueCNPJValidator implements ConstraintValidator<UniqueCNPJ, String> {

    @Autowired
    private ILojaDAO dao;

    @Override
    public boolean isValid(String CNPJ, ConstraintValidatorContext context) {
        if (dao != null) {
            Loja loja = dao.findByCNPJ(CNPJ);
            return loja == null;
        } else {
            // Durante a execução da classe LivrariaMvcApplication
            // não há injeção de dependência
            return true;
        }

    }
}