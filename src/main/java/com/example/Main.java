package com.example;

import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final List<Object> resources = new ArrayList<>();
        resources.add(new ServiceImpl());

        final ResteasyDeployment deployment = new ResteasyDeploymentImpl();
        deployment.setResources(resources);

        NettyJaxrsServer netty = new NettyJaxrsServer();
        netty.setDeployment(deployment);
        netty.setPort(8080);
        netty.setRootResourcePath("");
        netty.setSecurityDomain(null);

        System.out.println("Starting server");
        netty.start();
    }
}
