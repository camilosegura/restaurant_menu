package co.camilosegura.restaurantmenu.util;

/*******************************************************************************
 EMPRESA:      Avantel S.A.
 PROYECTO:     Avantel Core
 ARCHIVO:      Conexion.java
 PROPOSITO:    Clase de conexion.
 HISTORIAL DE CAMBIOS:
 Version        Fecha        Autor           Descripcion
 ---------  ----------  ---------------------  ------------------------------------
 1.0                                           1. Creacion de la clase.
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * Clase para realizar conexiones HTTP GET
 */
public class Conexion {
	private static int timeOut = 30000;
	private static String TAG = "Conexion";

	public static void setTimeOut(int time) {
		timeOut = time;
	}

	/**
	 * 
	 * @param sUrl
	 * @return
	 */
	public static String get(String sUrl) {
		String respuesta = "-2";

		Log.d(TAG, "CX_get: " + sUrl);
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);

			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet request = new HttpGet();

			request.setURI(new URI(sUrl));
			HttpResponse response = client.execute(request);

			Log.d(TAG, "response: " + response);
			
			respuesta = readBuffer(response);
		} catch (Exception e) {
			respuesta = "-2";
			e.printStackTrace();
			Log.d(TAG, "errorSend: " + e.getMessage());
		} finally {

		}

		Log.d(TAG, "respuestaGeneral: " + respuesta);
		return respuesta;
	}

	/**
	 * 
	 * @param sUrl
	 * @param parametros
	 * @param valores
	 * @return
	 */
	public static String post(String sUrl, String[] parametros, String[] valores) {
		String respuesta = "-2";

		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(sUrl);

			// Add data to your post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);

			for (int i = 0; i < parametros.length; i++) {
				pairs.add(new BasicNameValuePair(parametros[i], valores[i]));
				Log.d(TAG, "ConexionPosParams: " + parametros[i] + ":"
						+ valores[i]);
			}

			httppost.setEntity(new UrlEncodedFormEntity(pairs));

			// Finally, execute the request
			HttpResponse response = httpclient.execute(httppost);

			respuesta = readBuffer(response);
		} catch (Exception e) {
			Log.d(TAG, "errorEnvioPOST: " + e.getMessage());
			respuesta = "-2";
		} finally {

		}

		return respuesta;
	}

	/**
	 * 
	 * @param URL
	 */
	public static String downloadImage(String URL, String name) {
		Bitmap bitmap = null;
		URI myURI = null;
		String imgPath = Environment.getExternalStorageDirectory()
				+ File.separator + name;
		
		File Directory = new File(imgPath.substring(0,imgPath.lastIndexOf("/")));
		
		

		if (!Directory.exists()) {
			Log.d(TAG, "Directory.exists");
			Directory.mkdirs();
		}
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
		Log.d(TAG, "downloadHeaderUrl: " + URL);
		try {
			myURI = new URI(URL);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpGet getMethod = new HttpGet(myURI);
			HttpResponse webServerResponse = null;
			webServerResponse = httpClient.execute(getMethod);

			HttpEntity httpEntity = webServerResponse.getEntity();

			if (httpEntity != null) {
				InputStream input = null;
				try {
					input = httpEntity.getContent();
					bitmap = BitmapFactory.decodeStream(input);
					if (bitmap.getHeight() > 0) {
						FileOutputStream out = new FileOutputStream(imgPath);
						bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
						try {
							out.close();
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d(TAG,"ErrorImg: "+e.getMessage());
				} finally {
					try {
						input.close();
					} catch (IOException e) {
					}
				}
			}
		} catch (Exception e) {
		}

		Log.d(TAG, "imgPath: " + imgPath);
		
		return imgPath;
	}

	private static String readBuffer(HttpResponse response) {
		String result = "";
		BufferedReader in = null;
		try {
			Log.d(TAG, "readBuffer: " + response);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "ISO-8859-1"));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
