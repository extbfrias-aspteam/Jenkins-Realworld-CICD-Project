package net.cero.ahorro.spei.enviospei.servicioscero.base;

import lombok.extern.log4j.Log4j2;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Log4j2
public class BaseServicioWS {
    protected String http(String url, String body,String user,String pass) {
        log.info("url: " + url);
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient cliente = new OkHttpClient();
        String auth = Credentials.basic(user, pass);
        try {
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            return  cliente.newCall(request).execute().body().string();

        }catch(Exception e) {
            log.error("Error en http, se intento realiza una peticion a [" + url + "] El error fue: " + e.getMessage());
        }
        return "";
    }
}
