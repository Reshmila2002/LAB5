package com.example.employeemanager.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("This email is already registered");
    }
}
