package xyz.ajann.iodos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ajann.iodos.IodosServer;
import xyz.ajann.iodos.UtilKt;

/**
 * Root REST API Controller, independent of
 * API version and is solely used to return server
 * information in JSON form.
 *
 * @author jython234
 */
@RestController
public class RootController {

    @RequestMapping(IodosServer.ROOT_PATH + "/queryServer")
    public ResponseEntity<String> queryServer() {
        return ResponseEntity.ok(UtilKt.getServerQueryJSON());
    }
}
