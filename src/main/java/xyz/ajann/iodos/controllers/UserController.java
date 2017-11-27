package xyz.ajann.iodos.controllers;

import com.mongodb.client.MongoCollection;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ajann.iodos.CryptoUtilKt;
import xyz.ajann.iodos.IodosServer;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST API Controller which handles user management,
 * such as creating and deleting users.
 *
 * @author jython234
 */
@RestController
public class UserController {
    @Getter private static UserController instance;

    private Map<String, String> userTokens = new ConcurrentHashMap<>();

    public UserController() {
        UserController.instance = this;
    }

    @RequestMapping(value = IodosServer.ROOT_API_PATH + "/user/create", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        // Check if there is already a user with that username
        if(IodosServer.getInstance().getDbManager().getDocFromCollection("username", username, "users") != null) {
            // User with that username exists, must return error
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with that username already exists!");
        }

        MongoCollection<Document> usersCollection = IodosServer.getInstance().getDbManager().getCollection("users");

        Document doc = new Document();
        doc.append("username", username);
        doc.append("password", CryptoUtilKt.computeSHA512(password));
        doc.append("lastLogin", "");
        doc.append("loggedIn", false);
        doc.append("sessions", Collections.singletonList(new Document()));

        usersCollection.insertOne(doc);

        IodosServer.getInstance().getLogger().info("Registered new user \"" + username + "\"");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Success.");
    }

    @RequestMapping(value = IodosServer.ROOT_API_PATH + "/user/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        // Check if there is a user with that username
        Document userDoc = IodosServer.getInstance().getDbManager().getDocFromCollection("username", username, "users");
        if(userDoc == null) {
            // User does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with that username found.");
        }

        // Check if they are already logged in
        if(userTokens.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("That user is already logged in!");
        }

        // Check the password
        if(!CryptoUtilKt.computeSHA512(password).equals(userDoc.getString("password"))) {
            // Passwords do not match
            IodosServer.getInstance().getLogger().warn("Failed login attempt for \"" + username + "\" from " + request.getRemoteAddr() + ", password incorrect.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Password incorrect!");
        }

        // Everything checks out

        JSONObject root = new JSONObject();
        root.put("username", username);
        root.put("serverID", IodosServer.serverID);

        String jwt = Jwts.builder()
                .setPayload(root.toJSONString())
                .signWith(SignatureAlgorithm.ES384, IodosServer.getInstance().getConfig().getSecurity().getPrivateKey()).compact();

        this.userTokens.put(username, jwt);

        IodosServer.getInstance().getLogger().info("User \"" + username + "\" logged in from " + request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }
}
