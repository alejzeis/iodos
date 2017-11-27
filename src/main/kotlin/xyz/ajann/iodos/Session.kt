package xyz.ajann.iodos

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.json.simple.JSONObject

private var nextSessionId = 0L

class Session(private val user: String) {

    private val token: String
    private val sessionId: Long = nextSessionId++

    init {
        val root = JSONObject()
        root.put("user", user)
        root.put("sessionId", sessionId)
        root.put("serverId", IodosServer.serverID)

        // Create the JWT token
        this.token = Jwts.builder()
                .setPayload(root.toJSONString())
                .signWith(SignatureAlgorithm.ES384, IodosServer.instance.config.security.privateKey)
                .compact()
    }
}