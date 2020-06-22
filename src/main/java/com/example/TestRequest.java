package com.example;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

public class TestRequest {
    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] content;

    public TestRequest() {
    }

    public TestRequest(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
