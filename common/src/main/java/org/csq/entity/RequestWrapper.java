package org.csq.entity;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestWrapper extends HttpServletRequestWrapper {

    private String requestBody;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        // 在构造函数中读取请求体数据并保存在requestBody中
        try {
            requestBody = readRequestBody(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // 将保存的请求体数据包装成BufferedReader并返回
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 将保存的请求体数据转换成字节数组并返回
        final byte[] requestBodyBytes = requestBody.getBytes();
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                System.out.println(readListener);
            }

            private int index = 0;

            @Override
            public int read() {
                if (index < requestBodyBytes.length) {
                    return requestBodyBytes[index++];
                } else {
                    return -1;
                }
            }
        };
    }

    private String readRequestBody(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
