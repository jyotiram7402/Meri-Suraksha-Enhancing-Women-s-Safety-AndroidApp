package com.example.merisuraksha.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class getRequest {
    private RequestQueue queue;
    private String APIkey = "O8AcxKhugh12o3SB";
    private String brainID = "173709";
    private String reply;
    private char[] illegalChars = {'#', '<', '>', '$', '+', '%', '!', '`', '&',
            '*', '\'', '\"', '|', '{', '}', '/', '\\', ':', '@'};

    public getRequest(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String reply);
    }

    private String formatMessage(String message){


        for(char illegalChar : illegalChars){
            message = message.replace(illegalChar, '-');
        }
        return message;
    }

    public void getResponse(String message, final VolleyResponseListener volleyResponseListener){
        message = formatMessage(message);
        String url = "http://api.brainshop.ai/get?bid=173709&key=O8AcxKhugh12o3SB&uid=[uid]&msg="+message;
        Log.d("URL", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            reply = response.getString("cnt");
                            Log.d("RESPONSE", reply);
                            volleyResponseListener.onResponse(reply);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            volleyResponseListener.onError("JSON Exception");
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                        volleyResponseListener.onError("Volley Error");
                    }
                });
        queue.add(request);
    }
}
