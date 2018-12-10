package io.codurance.pouch;

import io.restassured.RestAssured;

class RestAssuredConfiguration {

    static void configure(int serverPort) {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = serverPort;
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
    }
}