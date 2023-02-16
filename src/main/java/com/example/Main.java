package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.netty.RequestDispatcher;
import org.jboss.resteasy.plugins.server.netty.RequestHandler;
import org.jboss.resteasy.plugins.server.netty.RestEasyHttpRequestDecoder;
import org.jboss.resteasy.plugins.server.netty.RestEasyHttpResponseEncoder;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final List<Object> resources = new ArrayList<>();
        resources.add(new ServiceImpl());

        final ResteasyDeployment deployment = new ResteasyDeploymentImpl();
        deployment.setResources(resources);

        deployment.start();

        RequestDispatcher dispatcher = new RequestDispatcher((SynchronousDispatcher) deployment.getDispatcher(), deployment.getProviderFactory(), null);

        RestEasyHttpRequestDecoder restEasyHttpRequestDecoder = new RestEasyHttpRequestDecoder(dispatcher.getDispatcher(),
                "", RestEasyHttpRequestDecoder.Protocol.HTTP);
        RestEasyHttpResponseEncoder restEasyHttpResponseEncoder = new RestEasyHttpResponseEncoder();

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipe = ch.pipeline();
                        pipe.addLast(new HttpRequestDecoder())
                                .addLast(new HttpResponseEncoder())
                                .addLast(new HttpObjectAggregator(10 * 1024 * 1024))
                                .addLast(restEasyHttpRequestDecoder)
                                .addLast(restEasyHttpResponseEncoder)
                                .addLast(new NioEventLoopGroup(), new RequestHandler(dispatcher));
                    }
                }).childOption(ChannelOption.SO_KEEPALIVE, true);

        System.out.println("Starting server");
        serverBootstrap.bind(8080).syncUninterruptibly();
    }
}
