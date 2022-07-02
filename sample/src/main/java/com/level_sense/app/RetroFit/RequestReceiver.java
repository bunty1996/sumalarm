/**
 * this interface exists just to allow the WebserviceHelper to make callback.
 */

package com.level_sense.app.RetroFit;

public interface RequestReceiver {
    void onSuccess(int requestCode, String fullResponse, Object dataObject);
    void onFailure(int requestCode, String errorCode, String message);
    void onNetworkFailure(int requestCode, String message);
}