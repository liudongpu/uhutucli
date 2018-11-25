package com.[@config:appReact.workName].device;

import java.io.OutputStream;

/**
 * Created by srnpr on 2017/6/20.
 */

public interface IMt {



   public ICallBack upCallBack();

   public void inCallBack(ICallBack callBack);


   public void write(byte[] buf, int count,
                     OutputStream pOutputStream)throws Exception;





   public byte[] upCommand();


}
