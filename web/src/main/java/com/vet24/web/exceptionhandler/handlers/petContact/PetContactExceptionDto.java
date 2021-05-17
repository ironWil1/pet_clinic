package com.vet24.web.exceptionhandler.handlers.petContact;

//Класс, который вовзращает info, если было некорректное обращение к URL
public class PetContactExceptionDto {

    private String info;

    public PetContactExceptionDto() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
