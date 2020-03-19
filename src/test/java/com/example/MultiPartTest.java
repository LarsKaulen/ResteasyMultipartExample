package com.example;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiPartTest {

    public static void main(String[] args) throws Exception     {
        String body = "ABC123";
        String bodyEncoded = Base64.getEncoder().encodeToString(body.getBytes());
        String input = "URLSTR: file:/Users/billburke/jboss/resteasy-jaxrs/resteasy-jaxrs/src/test/test-data/data.txt\r\n"
                + "--B98hgCmKsQ-B5AUFnm2FnDRCgHPDE3\r\n"
                + "Content-Disposition: form-data; name=\"data.txt\"; filename=\"data.txt\"\r\n"
                + "Content-Type: application/octet-stream\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + bodyEncoded + "\r\n"
                + "--B98hgCmKsQ-B5AUFnm2FnDRCgHPDE3--";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("boundary", "B98hgCmKsQ-B5AUFnm2FnDRCgHPDE3");
        MediaType contentType = new MediaType("multipart", "form-data", parameters);
        MultipartInputImpl multipart = new MultipartInputImpl(contentType, null);
        multipart.parse(bais);

        for (InputPart part : multipart.getParts()) {
            InputStream inputStream = ((MultipartInputImpl.PartImpl) part).getBody();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            System.out.println("Expected: " + body + ", received: " + new String(bytes));
        }
    }
}
