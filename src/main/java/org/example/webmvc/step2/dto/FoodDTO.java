package org.example.webmvc.step2.dto;

public record FoodDTO(String name, int price) {
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
