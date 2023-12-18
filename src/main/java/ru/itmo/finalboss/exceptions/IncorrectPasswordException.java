package ru.itmo.finalboss.exceptions;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
