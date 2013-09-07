package hr.android.petkovic.igoout.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

public class DefaultRequest extends JsonObjectRequest{

	public DefaultRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("encoding", "utf-8");
		return headers;
	}
	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HTTP.UTF_8);
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

}
