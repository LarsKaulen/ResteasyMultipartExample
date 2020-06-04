package com.example;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmptyHostHeaderTest {

    @Test
    public void emptyHost() throws IOException {
        Thread thread = new Thread(() -> {
            final List<Object> resources = new ArrayList<>();
            resources.add(new ServiceImpl());

            final ResteasyDeployment deployment = new ResteasyDeployment();
            deployment.setResources(resources);

            NettyJaxrsServer netty = new NettyJaxrsServer();
            netty.setDeployment(deployment);
            netty.setPort(8080);
            netty.setRootResourcePath("");
            netty.setSecurityDomain(null);
            netty.start();
        });
        thread.start();

        String uri = "/service/example";
        System.out.println("uri: " + uri);

        try (Socket client = new Socket("127.0.0.1", 8080)) {
            try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                out.printf(Locale.US, "GET %s HTTP/1.1\nHost: \n\n", uri);
                String response = new BufferedReader(new InputStreamReader(client.getInputStream())).lines().collect(Collectors.joining("\n"));
                System.out.println("response: " + response);
                Assert.assertNotNull(response);
                Assert.assertTrue(response.contains("HTTP/1.1 200 OK"));
                Assert.assertTrue(response.contains("uriInfo: http://127.0.0.1:8080" + uri));
            }
        }
    }
}
