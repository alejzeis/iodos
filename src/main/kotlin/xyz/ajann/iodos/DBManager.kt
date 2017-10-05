package xyz.ajann.iodos

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import java.lang.Exception

class DBManager (private val server: IodosServer) {

    private val mongoClient: MongoClient
    private val db: MongoDatabase

    init {
        this.server.logger.info("Connecting to MongoDB database...")

        this.mongoClient = MongoClient(this.server.config.db.ip, this.server.config.db.port)
        this.db = this.mongoClient.getDatabase(this.server.config.db.name)

        try {
            mongoClient.address
        } catch(e: Exception) {
            e.printStackTrace()
            this.server.logger.error("Failed to connect to MongoDB database!")
            System.exit(1)
        }
    }
}