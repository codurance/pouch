package io.codurance.pouch;

import io.restassured.RestAssured;

public class RestAssuredConfiguration {

    public static void configure(int serverPort) {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = serverPort;
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if(basePath==null){
            basePath = "/api/";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
    }
}