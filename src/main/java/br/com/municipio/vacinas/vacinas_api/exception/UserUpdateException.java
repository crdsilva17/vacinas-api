package br.com.municipio.vacinas.vacinas_api.exception;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message){
        super(message);
    }
}
