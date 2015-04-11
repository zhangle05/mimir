/**
 * 
 */
package com.jotunheim.mimir.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author zhangle
 *
 */
public abstract class BaseController {

    private static org.apache.log4j.Logger LOG = Logger
            .getLogger(BaseController.class);

    private static final ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();

    private static final ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

    public HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    public void setRequest(HttpServletRequest request) {
        requestLocal.set(request);
    }

    public void setResponse(HttpServletResponse response) {
        responseLocal.set(response);
    }

    public HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    protected void sendRawData(String data) {
        if (data != null) {
            LOG.info(data);
            HttpServletResponse response = getResponse();
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(data);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    protected void sendRawData(String data, HttpServletResponse response) {
        if (data != null) {
            LOG.info(data);
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(data);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
}
