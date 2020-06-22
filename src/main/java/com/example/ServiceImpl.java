package com.example;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceImpl implements Service {

    @Override
    public Response exampleApi(TestRequest request) {
        MultipartFormDataOutput multipart = new MultipartFormDataOutput();
        multipart.addFormData("content", request.getContent(), MediaType.APPLICATION_OCTET_STREAM_TYPE);

        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };
        return Response.ok(entity, MediaType.MULTIPART_FORM_DATA).build();
    }
}
