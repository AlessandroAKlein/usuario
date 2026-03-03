package com.javanauta.demo.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
        super(message);
    }


  public  ResourceNotFoundException(String mensagem, Throwable throwable){
    super(mensagem);
  }
}

