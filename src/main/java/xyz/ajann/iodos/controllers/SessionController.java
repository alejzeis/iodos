package xyz.ajann.iodos.controllers;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ajann.iodos.IodosServer;

/**
 * REST API Controller which handles opening and closing sessions.
 *
 * @author jython234
 */
@RestController
public class SessionController {

    @RequestMapping(value = IodosServer.ROOT_API_PATH + "/session/open")
    public ResponseEntity<String> openSession(@RequestParam("token") String userToken) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
    }
}
