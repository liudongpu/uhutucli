package com.[@config:appReact.workName].device;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * Created by srnpr on 2017/6/21.
 */

public abstract class AMt implements  IMt{

    private ICallBack callNotice;


    private String dataObject;
    @Override
    public ICallBack upCallBack() {
        return this.callNotice;
    }

    @Override
    public void inCallBack(ICallBack callBack) {
        this.callNotice=callBack;

    }


    public void inDataObject(String sData){
        this.dataObject=sData;
    }

    public String upDataObject(){
        return this.dataObject;
    }


    public void callBack(JSONObject oJson){


        JSONObject jsonResult=new JSONObject();


        try {
            jsonResult.put("result",oJson);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        inDataObject(jsonResult.toString());
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                upCallBack().call(upDataObject());

            }
        });
    }
}
