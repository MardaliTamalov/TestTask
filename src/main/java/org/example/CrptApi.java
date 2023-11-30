package org.example;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private final BlockingQueue<Document> bucket;
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        bucket = new ArrayBlockingQueue<>(requestLimit);
        CompletableFuture.runAsync(() -> send(timeUnit.toMillis(1) / requestLimit));
    }

   @SneakyThrows
    public void createDocument(Document document, String signature) {
        bucket.put(document);
    }

    @SneakyThrows
    private void send(long interval) {
        while (true) {
            Document document = bucket.take();
            HttpPost httpPost = new HttpPost("https://ismp.crpt.ru/api/v3/lk/documents/create");
            Gson gson = new Gson();
            String jsonObject = gson.toJson(document, Document.class);
            StringEntity stringEntity = new StringEntity(jsonObject, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            Thread.sleep(interval);
        }
    }

    @Data
    @Builder
    public static class Document {
        private Description description;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private String productionDate;
        private String productionType;
        private List<Product> products;
        private String regDate;
        private String regNumber;
    }

    @Data
    public class Description {
        private String participantInn;
    }

    @Data
    public class Product {
        private String certificateDocument;
        private String certificateDocumentDate;
        private String certificateDocumentNumber;
        private String ownerInn;
        private String producerInn;
        private String productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;
    }
}
