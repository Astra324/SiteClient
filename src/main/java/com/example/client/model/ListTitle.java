package com.example.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ListTitle {
    String name;
    String title;

    public ListTitle(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ListTitle{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
