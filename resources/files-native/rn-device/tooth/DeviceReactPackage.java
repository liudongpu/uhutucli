package com.[@config:appReact.workName].tooth;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by srnpr on 2017/6/15.
 */

public class DeviceReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return Collections.<NativeModule>singletonList(new DeviceReactModule(reactApplicationContext));
    }


    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        //return Collections.emptyList();
        return Arrays.<ViewManager>asList(
                new DeviceReactView()
        );
    }
}
