//package com.example.Autodrive.Configuration;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//@Configuration
//public class DocumentDBConfig extends AbstractMongoClientConfiguration {
//
//    public static final String KEY_STORE_TYPE = "db-certs/rds-truststore.jks";
//    public static final String DEFAULT_KEY_STORE_PASSWORD = "changeit"; // Default password for the truststore
//
//    @Value("${spring.data.mongodb.uri}")
//    private String mongoUri;
//
//    @Override
//    protected String getDatabaseName() {
//        // Extract database name from the connection string if needed,
//        // or hardcode it if it's always "autodrivedb"
//        return "autodrivedb";
//    }
//
//    @Bean
//    @Override
//    public MongoClientSettings mongoClientSettings() {
//        setSslProperties(); // Ensure SSL properties are set before building settings
//
//        return MongoClientSettings.builder()
//                .applyToSslSettings(builder -> builder.enabled(true))
//                .applyConnectionString(new ConnectionString(mongoUri)) // Apply the URI from application.properties
//                .build();
//    }
//
//    private static void setSslProperties() {
//        System.setProperty("javax.net.ssl.trustStore", KEY_STORE_TYPE);
//        System.setProperty("javax.net.ssl.trustStorePassword",
//                DEFAULT_KEY_STORE_PASSWORD);
//    }
//}
