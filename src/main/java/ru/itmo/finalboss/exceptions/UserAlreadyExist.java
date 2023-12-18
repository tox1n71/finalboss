package ru.itmo.finalboss.exceptions;

public class UserAlreadyExist extends Exception{
    public UserAlreadyExist(String message) {
        super(message);
    }
}
