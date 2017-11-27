package xyz.ajann.iodos;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class IodosServer {
    public static final String SOFTWARE = "Iodos-Server";
    public static final String SOFTWARE_VERSION = "0.1.0-SNAPSHOT";

    public static final int API_VERSION_MAJOR = 1;
    public static final int API_VERISON_MINOR = 0;

    public static final String ROOT_PATH = "/iodos/api";
    public static final String ROOT_API_PATH = ROOT_PATH + "/v/" + API_VERSION_MAJOR + "/" + API_VERISON_MINOR;

    public static final long serverID = new Random().nextLong();

    @Getter protected static IodosServer instance;

    @Getter protected static ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Getter protected Logger logger;
    @Getter protected IodosServerConfiguration config;
    @Getter protected DBManager dbManager;

    protected IodosServer() {
        if(IodosServer.instance != null) return;

        // Add BouncyCastle as a Java Security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        IodosServer.instance = this;

        // Stop MongoDB from logging all the DEBUG info unless an environment variable is set
        UtilKt.setMongoLogLevels();

        // Setup our ThreadPool, initalize to same amount of threads as cores, and then max size set to amount of cores * 2
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        threadPoolTaskExecutor.setQueueCapacity(16);
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
        threadPoolTaskExecutor.initialize();

        try {
            System.out.println(UtilKt.getResourceContents("header.txt"));
        } catch(IOException e) {
            // Don't worry about failing to print the header logo
        }

        this.logger = LoggerFactory.getLogger("IodosServer");

        this.logger.info("Starting " + SOFTWARE + " " + SOFTWARE_VERSION);

        try {
            this.loadConfig();
            logger.info("Loaded configuration.");
        } catch(IOException e) {
            logger.error("Failed to load configuration! IOException");
            e.printStackTrace(System.err);
            System.exit(1);
        }

        this.dbManager = new DBManager(this);

        logger.info("Initialization Complete.");
    }

    private void loadConfig() throws IOException {
        File configFile = new File(UtilKt.getConfigurationDirectory() + File.separator + "server.ini");
        if(!configFile.exists() || !configFile.isFile()) {
            UtilKt.copyResourceTo("defaultConfig.ini", configFile);
        }

        this.config = IodosServerConfigurationKt.loadConfiguration(configFile);
    }

    public static void main(String[] args) {
        new IodosServer();
        SpringApplication.run(IodosServer.class, args);
    }
}
