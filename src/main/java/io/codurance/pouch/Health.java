package io.codurance.pouch;

import org.springframework.data.annotation.Id;

class Health {

    @Id
    Integer id;
    String status;
}
