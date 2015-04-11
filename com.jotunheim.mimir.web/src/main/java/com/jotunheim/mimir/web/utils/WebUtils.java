/**
 * 
 */
package com.jotunheim.mimir.web.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.jotunheim.mimir.common.utils.StringUtils;

/**
 * @author zhangle
 *
 */
public class WebUtils {

    private static org.apache.log4j.Logger LOG = Logger
            .getLogger(WebUtils.class);

    public static String getJsonStrFromUrl(String url) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            LOG.info("try get json, uri is:" + get.getURI());
            RequestConfig config = RequestConfig.custom()
                      .setSocketTimeout(4000)
                      .setConnectTimeout(2000)
                      .setConnectionRequestTimeout(2000)
                      .setStaleConnectionCheckEnabled(true)
                      .build();
            get.setConfig(config);
            String result = "";
            try {
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    /* 读返回数据 */
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                    }
                    if(!StringUtils.isEmpty(result)) {
                        LOG.info("result is:" + result);
                    }
                    else {
                        LOG.info("result is empty.");
                    }
                    return result;
                } else {
                    LOG.info("status error:" + response.getStatusLine().getStatusCode());
                    return "";
                }
            } catch (ClientProtocolException ex) {
                LOG.info("getJsonStrFromUrl done with exception" + ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                LOG.info("getJsonStrFromUrl done with exception" + ex);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            LOG.info("getJsonStrFromUrl done with exception" + ex);
            ex.printStackTrace();
        }
        LOG.info("getJsonStrFromUrl, error happened.");
        return "";
    }
}
