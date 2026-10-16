package net.cero.ahorro.logica;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import net.cero.data.EnviaNotificacionReq;
import net.cero.data.Respuesta;
import net.cero.data.SMS;
import net.cero.seguridad.utilidades.ConstantesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Log4j2
public class EnviaNotificacionesSMS {
	public void enviarNotificacionSMS(SMS sms) {
		Respuesta resp = new Respuesta();
		
		try {
			Gson gson = new Gson();
			
			resp.setCodigo(0);
			resp.setMensaje("OK");

			// SMS sms = new SMS();
			EnviaNotificacionReq enreq = new EnviaNotificacionReq();
			enreq.setMensaje(sms);
			enreq.setTipoNotificacion(1);
			String body = gson.toJson(enreq);

			String wsUrl = ConstantesUtil.WS_CERO_NOTIF;
			String auth = Credentials.basic("ASP", "a5p2017$");
			MediaType media = MediaType.parse("application/json; charset=utf-8");
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url(wsUrl).post(okhttp3.RequestBody.create(media, body))
					.header("Authorization", auth).build();

			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onResponse(Call call, okhttp3.Response resp) throws IOException {
					if (resp.isSuccessful()) {
						String r = resp.body().string();
						log.info(r);
					} else {
						log.info("ERROR " + resp.code());
						log.error(resp.code());
					}
				}

				@Override
				public void onFailure(Call call, IOException e) {
					log.info("Ocurrio un error al enviar notificacion");
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			resp.setCodigo(1);
			resp.setMensaje("Fallo al enviar SMS");
		}
	}

}
