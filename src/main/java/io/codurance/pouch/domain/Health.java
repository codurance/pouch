package io.codurance.pouch.domain;

import org.springframework.data.annotation.Id;

public class Health {

    @Id
    private Integer id;
    private String status;

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
