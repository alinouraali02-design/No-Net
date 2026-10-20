package com.nonet.app;
import android.content.Context;import androidx.annotation.NonNull;import androidx.work.Worker;import androidx.work.WorkerParameters;import org.json.JSONObject;
public class SubscriptionWorker extends Worker {
 public SubscriptionWorker(@NonNull Context c,@NonNull WorkerParameters p){super(c,p);}
 @NonNull @Override public Result doWork(){Context c=getApplicationContext();android.content.SharedPreferences prefs=c.getSharedPreferences("nonet",Context.MODE_PRIVATE);String id=prefs.getString("device_id","");if(id.isEmpty())return Result.success();try{JSONObject r=ApiClient.request(c,"GET","/api/subscription/status?deviceId="+id,null,null);JSONObject s=r.optJSONObject("subscription");boolean active=s!=null&&"active".equals(s.optString("status"));prefs.edit().putBoolean("subscription_active",active).putLong("last_server_check",System.currentTimeMillis()).apply();return Result.success();}catch(Exception e){/* Preserve the previous subscription state during network/server outages. */return Result.retry();}}
}
