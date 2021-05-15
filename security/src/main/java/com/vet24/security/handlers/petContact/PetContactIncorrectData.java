package com.vet24.security.handlers.petContact;

//Класс, который вовзращает info, если было некорректное обращение к URL
public class PetContactIncorrectData {

    private String info;

    public PetContactIncorrectData() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
