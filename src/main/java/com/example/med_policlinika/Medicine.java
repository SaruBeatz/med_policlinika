package com.example.med_policlinika;

public class Medicine {
    private String drugName;
    private String medDescription;
    private Integer medPrice;

    public Medicine(String drugName, String medDescription, Integer medPrice) {
        this.drugName = drugName;
        this.medDescription = medDescription;
        this.medPrice = medPrice;
    }

    // Геттеры (getters)
    public String getDrugName() {
        return drugName;
    }

    public String getMedDescription() {
        return medDescription;
    }

    public Integer getMedPrice() {
        return medPrice;
    }

    // Сеттеры (setters)
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public void setMedDescription(String medDescription) {
        this.medDescription = medDescription;
    }

    public void setMedPrice(Integer medPrice) {
        this.medPrice = medPrice;
    }

    public String printMedicine() {
        return "Лекарство: " + drugName + ", Описание: " + medDescription + ", Цена: " + medPrice;
    }
}