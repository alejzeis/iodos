package xyz.ajann.iodos.config;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import xyz.ajann.iodos.IodosServer;

@Controller
public class ServletConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> container.setPort(IodosServer.getInstance().getConfig().getNetwork().getBindPort()));
    }
}
