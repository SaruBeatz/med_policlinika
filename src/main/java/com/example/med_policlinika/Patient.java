package com.example.med_policlinika;

public class Patient {
    private int id; // Уникальный идентификатор пациента
    private String fullName; // Полное имя пациента
    private String telephone; // Номер телефона пациента
    private String gender; // Пол пациента
    private String birthday; // Дата рождения пациента
    private String address; // Адрес пациента

    // Конструктор класса Patient для инициализации всех полей
    public Patient(int id, String fullName, String telephone, String gender, String birthday, String address) {
        this.id = id;
        this.fullName = fullName;
        this.telephone = telephone;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
    }

    // Геттеры и сеттеры для всех полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}