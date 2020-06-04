package com.example;

import javax.ws.rs.core.Response;

public class ServiceImpl implements Service {

    @Override
    public Response exampleApi() {
        return Response.ok().build();
    }
}
