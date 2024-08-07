package com.felipe.souls.dscatalog.services.validations;

import com.felipe.souls.dscatalog.controllers.exceptions.FieldMessage;
import com.felipe.souls.dscatalog.dto.UserInsertDTO;
import com.felipe.souls.dscatalog.entities.User;
import com.felipe.souls.dscatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());

        if (user.isEmpty()) {
            list.add(new FieldMessage("email", "This e-mail exist"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
