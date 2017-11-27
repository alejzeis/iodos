package xyz.ajann.iodos

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.apache.commons.codec.digest.DigestUtils
import org.bson.Document
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
        } catch (e: Exception) {
            e.printStackTrace()
            this.server.logger.error("Failed to connect to MongoDB database!")
            System.exit(1)
        }
    }

    fun getCollection(name: String): MongoCollection<Document> {
        return this.db.getCollection(name) ?: throw RuntimeException("Collection \"$name\" not found!")
    }

    fun getDocFromCollection(docKey: String, docValue: String, collectionName: String): Document? {
        val collection = this.db.getCollection(collectionName) ?: throw RuntimeException("Collection \"$collectionName\" not found!")
        return collection.find(Filters.eq(docKey, docValue)).firstOrNull()
    }
}