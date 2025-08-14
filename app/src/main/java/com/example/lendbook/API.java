package com.example.lendbook;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class API {

    private static final String BASE_URL = "http://10.240.72.69/comp2000/library/";
    private static final String TAG = "API";

    public interface UsersCallback {
        void onSuccess(List<Users> users);
        void onError(String errorMessage);
    }

    public interface APICallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    // Get all members
    public static void getAllMembers(Context context, UsersCallback callback) {
        String url = BASE_URL + "members";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Users> usersList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Users user = new Users();
                            user.setFirstname(obj.getString("firstname"));
                            user.setLastname(obj.getString("lastname"));
                            user.setEmail(obj.getString("email"));
                            user.setUsername(obj.getString("username"));
                            user.setContact(obj.optString("contact", ""));
                            user.setMembershipEndDate(obj.optString("membership_end_date", ""));
                            usersList.add(user);
                        }
                        callback.onSuccess(usersList);
                    } catch (JSONException e) {
                        Log.e(TAG, "Failed to parse members JSON", e);
                        callback.onError("Failed to parse members JSON: " + e.getMessage());
                    }
                },
                error -> callback.onError("Failed to get members: " + error.getMessage())
        );
        queue.add(request);
    }

    // Add a member
    public static void addMember(Context context, Users newUser, APICallback callback) {
        String url = BASE_URL + "members";
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("firstname", newUser.getFirstname());
            jsonBody.put("lastname", newUser.getLastname());
            jsonBody.put("email", newUser.getEmail());
            jsonBody.put("username", newUser.getUsername());
            jsonBody.put("contact", newUser.getContact());
            jsonBody.put("membership_end_date", newUser.getMembershipEndDate());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> callback.onSuccess(),
                    error -> {
                        String msg = error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e(TAG, "Error adding member", error);
                        callback.onError(msg);
                    }
            );

            queue.add(request);

        } catch (JSONException e) {
            Log.e(TAG, "JSON error adding member", e);
            callback.onError("JSON error: " + e.getMessage());
        }
    }

    // Update a member
    public static void updateMember(Context context, String username, Users updatedMember, APICallback callback) {
        try {
            String url = BASE_URL + "members/" + URLEncoder.encode(username, "UTF-8");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("firstname", updatedMember.getFirstname());
            jsonBody.put("lastname", updatedMember.getLastname());
            jsonBody.put("email", updatedMember.getEmail());
            jsonBody.put("contact", updatedMember.getContact());
            jsonBody.put("membership_end_date", updatedMember.getMembershipEndDate());

            Log.d("API", "Updating member URL: " + url);

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                    response -> {
                        Log.d("API", "Update success: " + response);
                        callback.onSuccess();
                    },
                    error -> {
                        String body = "Unknown network error";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        }
                        Log.e("API", "Error updating member: " + body, error);
                        callback.onError(body);
                    }
            );

            queue.add(request);

        } catch (Exception e) {
            Log.e("API", "Exception in updateMember", e);
            callback.onError(e.getMessage());
        }
    }

    // Delete a member
    public static void deleteMember(Context context, String username, APICallback callback) {
        try {
            String url = BASE_URL + "members/" + URLEncoder.encode(username, "UTF-8");
            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                    response -> {
                        Log.d("API", "Member deleted successfully");
                        if (callback != null) callback.onSuccess();
                    },
                    error -> {
                        String body = "Unknown network error";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        }
                        Log.e("API", "Error deleting member: " + body);
                        if (callback != null) callback.onError(body);
                    }
            );

            queue.add(request);

        } catch (Exception e) {
            Log.e("API", "Exception in deleteMember", e);
            if (callback != null) callback.onError(e.getMessage());
        }
    }
}
