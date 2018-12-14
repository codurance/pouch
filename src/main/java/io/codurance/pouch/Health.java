package io.codurance.pouch;

import org.springframework.data.annotation.Id;

class Health {

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
