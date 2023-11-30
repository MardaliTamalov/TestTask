package org.example;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CrptApiTest {

    @org.junit.jupiter.api.Test
    void createDocument() {
        CrptApi crptApi= new CrptApi(TimeUnit.MINUTES,3);
        crptApi.createDocument(CrptApi.Document.builder().build(),"");
        System.out.println("");
    }
}