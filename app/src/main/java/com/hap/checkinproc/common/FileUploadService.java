package com.hap.checkinproc.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    Disposable mDisposable;
    /**
     * Unique job ID for this service.
     */
    public enum MIMEType {
        IMAGE("image/*"), VIDEO("video/*");
        public String value;

        MIMEType(String value) {
            this.value = value;
        }
    }
    private static final int JOB_ID = 102;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String mFilePath = intent.getStringExtra("mFilePath");
        String mSF = intent.getStringExtra("SF");
        String FileName=intent.getStringExtra("FileName");
        String Mode=intent.getStringExtra("Mode");
        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Flowable<Double> fileObservable = Flowable.create(emitter -> {
            apiInterface.onFileUpload(mSF,FileName,Mode,
                    createMultipartBody(mFilePath, emitter)).blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);
        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> onProgress(progress), throwable -> onErrors(throwable),
                        () -> onSuccess());
    }
    private void onErrors(Throwable throwable) {
        //sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }
    private void onProgress(Double progress) {
        //sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }
    private void onSuccess() {
        sendBroadcastMeaasge("File uploading successful ");
        Log.i(TAG, "onSuccess: File Uploaded");

    }

    public void sendBroadcastMeaasge(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
     //   Intent localIntent = new Intent("my.own.broadcast");
     //   localIntent.putExtra("result", message);
     //   LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }
    private RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
    }
    /**
     * return multi part body in format of FlowableEmitter
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("file", file.getName(),
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }
    private RequestBody createCountingRequestBody(File file, String mimeType,
                                                  FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}