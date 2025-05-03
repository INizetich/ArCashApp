package com.EDJ.ArCash.Models.Exc;

import org.springframework.dao.DataIntegrityViolationException;

public class AlreadyExistCvu extends DataIntegrityViolationException {
    public AlreadyExistCvu(String message) {
        super(message);
    }
}
