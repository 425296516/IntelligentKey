package com.aigo.router.bussiness;

import android.content.Context;
import android.util.Log;

import com.aigo.router.bussiness.bean.ExecuteRecords;
import com.aigo.router.bussiness.bean.Message;
import com.aigo.router.bussiness.bean.NetBehaviour;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.NetDeviceList;
import com.aigo.router.bussiness.bean.NetDeviceType;
import com.aigo.router.bussiness.bean.NetGetVerifyCode;
import com.aigo.router.bussiness.bean.NetLinkman;
import com.aigo.router.bussiness.bean.NetScene;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.MD5Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class SceneModule {

    private static final String TAG = SceneModule.class.getSimpleName();
    private static SceneModule mManager;
    private static Context mContext;

    public static SceneModule getInstance() {
        if (mManager == null)
            throw new NullPointerException("CarInsuranceModule is not init");
        return mManager;
    }

    //初始化1
    public static SceneModule init(Context context) {
        mContext = context;
        if (mManager == null)
            mManager = new SceneModule();
        return mManager;
    }

    //1.用户绑定设备(app端调用)：UserBindDevice.json
    public Observable<ResultObject> UserBindDevice(final String username, final String deviceSN
            , final String deviceName, final String deviceType, final String remarks) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/UserBindDevice.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&deviceSN=").append(deviceSN)
                        .append("&deviceName=").append(deviceName)
                        .append("&deviceTypeId=").append(deviceType)
                        .append("&remarks=").append(remarks)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);

            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //2.用户解绑设备(app端调用)：UserUnbindDevice.json
    public Observable<ResultObject> UserUnbindDevice(final String username, final String deviceSN) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/UserUnbindDevice.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&deviceSN=").append(deviceSN)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }


    //3.用户修改设备备注名(app端调用)：modifyRemark.json
    public Observable<ResultObject> modifyRemark(final String username, final String deviceSN
            , final String remarks) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/modifyRemark.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&deviceSN=").append(deviceSN)
                        .append("&remarks=").append(remarks)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);

            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //4.获取所有设备类型(app端调用)：getAllDeviceType.json
    public Observable<NetDeviceType> getAllDeviceType(final String type) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getAllDeviceType.json?type=" + type;
                String urlStr = new StringBuffer(url).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetDeviceType>>() {
            @Override
            public Observable<NetDeviceType> call(String s) {

                Log.d(TAG, s);
                NetDeviceType result = new Gson().fromJson(s, NetDeviceType.class);

                return Observable.just(result);
            }
        });
    }


    //5.根据设备类型获取用户绑定设备列表(app端调用)：getUserBindDeviceList.json
    public Observable<NetBindDeviceList> getUserBindDeviceList(final String username, final String deviceType) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getUserBindDeviceList.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&deviceTypeId=").append(deviceType)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetBindDeviceList>>() {
            @Override
            public Observable<NetBindDeviceList> call(String s) {

                Log.d(TAG, s);
                NetBindDeviceList result = new Gson().fromJson(s, NetBindDeviceList.class);

                return Observable.just(result);
            }
        });
    }


    //6.获取用户设备列表根据设备类型分组(app端调用)：getAllUserDeviceList.json

    public Observable<NetDeviceList> getAllUserDeviceList(final String username) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getAllUserDeviceList.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetDeviceList>>() {
            @Override
            public Observable<NetDeviceList> call(String s) {

                Log.d(TAG, s);
                NetDeviceList result = new Gson().fromJson(s, NetDeviceList.class);

                return Observable.just(result);
            }
        });
    }

    //7.添加用户联系人(app端调用)：addUserLinkman.json
    public Observable<ResultObject> addUserLinkman(final String link_no, final String link_name, final String username) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/addUserLinkman.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&link_no=").append(link_no)
                        .append("&link_name=").append(link_name).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //8.获取用户联系人列表(app端调用)：getUserLinkman.json
    public Observable<NetLinkman> getUserLinkman(final String username) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getUserLinkman.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetLinkman>>() {
            @Override
            public Observable<NetLinkman> call(String s) {

                Log.d(TAG, s);
                NetLinkman result = new Gson().fromJson(s, NetLinkman.class);

                return Observable.just(result);
            }
        });
    }

    //9.修改用户联系人(App端调用)：modifyUserLinkman.json
    public Observable<ResultObject> modifyUserLinkman(final String link_id, final String link_no, final String link_name) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/modifyUserLinkman.json";
                String urlStr = new StringBuffer(url)
                        .append("?link_id=").append(link_id)
                        .append("&link_no=").append(link_no)
                        .append("&link_name=").append(link_name)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //10.删除用户联系人(app端调用)：deleteUserLinkman.json
    public Observable<ResultObject> deleteUserLinkman(final String link_id) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/deleteUserLinkman.json";
                String urlStr = new StringBuffer(url)
                        .append("?link_id=").append(link_id).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //11.用户添加场景(app端调用)：addScene.json
    public Observable<ResultObject> addScene(final String username, final String trigger_deviceSN,
                                             final String execute_deviceSN, final String triggerId, final String executeId,
                                             final String trigger_describe, final String execute_describe, final String link_id,
                                             final String message, final String isActivate, final String isPush) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/addScene.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&trigger_deviceSN=").append(trigger_deviceSN)
                        .append("&execute_deviceSN=").append(execute_deviceSN)
                        .append("&triggerId=").append(triggerId)
                        .append("&executeId=").append(executeId)
                        .append("&trigger_describe=").append(trigger_describe)
                        .append("&execute_describe=").append(execute_describe)
                        .append("&link_id=").append(link_id)
                        .append("&message=").append(message)
                        .append("&isActivate=").append(isActivate)
                        .append("&isPush=").append(isPush)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //12.获取用户绑定设备列表(app端调用)：modifyScene.json

    public Observable<ResultObject> modifyScene(final String sceneId, final String triggerSN,
                                                final String executeSN, final String deviceTriggerId,
                                                final String deviceExecuteId, final String trigger_describe,
                                                final String execute_describe, final String link_id,
                                                final String message, final String isActivate, final String isPush) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/modifyScene.json";
                String urlStr = new StringBuffer(url)
                        .append("?sceneId=").append(sceneId)
                        .append("&trigger_deviceSN=").append(triggerSN)
                        .append("&execute_deviceSN=").append(executeSN)
                        .append("&triggerId=").append(deviceTriggerId)
                        .append("&executeId=").append(deviceExecuteId)
                        .append("&trigger_describe=").append(trigger_describe)
                        .append("&execute_describe=").append(execute_describe)
                        .append("&link_id=").append(link_id)
                        .append("&message=").append(message)
                        .append("&isActivate=").append(isActivate)
                        .append("&isPush=").append(isPush)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }


    //13.用户删除场景(app端调用)：deleteScene.json
    public Observable<ResultObject> deleteScene(final List<String> sceneIdList) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/deleteScene.json";
                String urlStr = new StringBuffer(url)
                        .append("?sceneIdList=").append(sceneIdList).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    //14.获取用户场景列表(app端调用)：getUserScene.json
    public Observable<NetScene> getUserScene(final String username) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getUserScene.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username).toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetScene>>() {
            @Override
            public Observable<NetScene> call(String s) {

                Log.d(TAG, s);
                NetScene result = new Gson().fromJson(s, NetScene.class);

                return Observable.just(result);
            }
        });
    }


    //15.获取用户场景执行日志(app端调用)：getSceneLog.json
    public Observable<ExecuteRecords> getSceneLog(final String username, final int year, final int month) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getSceneLog.json";
                String urlStr = new StringBuffer(url)
                        .append("?username=").append(username)
                        .append("&year=").append(year)
                        .append("&month=").append(month)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ExecuteRecords>>() {
            @Override
            public Observable<ExecuteRecords> call(String s) {

                Log.d(TAG, s);
                ExecuteRecords result = new Gson().fromJson(s, ExecuteRecords.class);

                return Observable.just(result);
            }
        });
    }


    //16.根据设备类型以及行为类型获取行为列表(app端调用)：getBehaviorList.json
    public Observable<ResultObject> updateSceneStatus(final String sceneId, final int isActivate) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/updateSceneStatus.json";
                String urlStr = new StringBuffer(url)
                        .append("?sceneId=").append(sceneId)
                        .append("&isActivate=").append(isActivate)
                        .toString();

                Log.d(TAG, urlStr);

                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }


    //17.根据设备类型以及行为类型获取行为列表(app端调用)：getBehaviorList.json
    public Observable<NetBehaviour> getBehaviorList(final String deviceTypeId, final String behaviourType) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getBehaviorList.json";
                String urlStr = new StringBuffer(url)
                        .append("?behaviourType=").append(behaviourType)
                        .append("&deviceTypeId=").append(deviceTypeId)
                        .toString();

                Log.d(TAG, urlStr);
                return NetHelper.getData(urlStr);
            }
        }).flatMap(new Func1<String, Observable<NetBehaviour>>() {
            @Override
            public Observable<NetBehaviour> call(String s) {

                Log.d(TAG, s);
                NetBehaviour result = new Gson().fromJson(s, NetBehaviour.class);

                return Observable.just(result);
            }
        });
    }

    //18.
    public Observable<NetGetVerifyCode> getPhoneVerifyCode(final String phoneNumber) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.life.aigo.com/sendVerifyCode.html";

                StringBuffer urlStr = new StringBuffer(url)
                        .append("?action=").append("sendVerifyCode")
                        .append("&phone=").append(phoneNumber);

                String sign = MD5Util.createSignParameter(urlStr.toString(), "sendVerifyCode");
                urlStr.append("&sign=").append(sign);

                Log.d(TAG, urlStr.toString());

                return NetHelper.getData(urlStr.toString());
            }
        }).flatMap(new Func1<String, Observable<NetGetVerifyCode>>() {
            @Override
            public Observable<NetGetVerifyCode> call(String s) {

                Log.d(TAG, s);
                NetGetVerifyCode result = new Gson().fromJson(s, NetGetVerifyCode.class);

                return Observable.just(result);
            }
        });
    }


    public Observable<ResultObject> checkVerifyCode(final String phone, final String identifier, final String verifyCode) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/checkVerifyCode.json";

                StringBuffer urlStr = new StringBuffer(url)
                        .append("?phone=").append(phone)
                        .append("&identifier=").append(identifier)
                        .append("&verifyCode=").append(verifyCode);

                Log.d(TAG, urlStr.toString());

                return NetHelper.getData(urlStr.toString());
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    public Observable<ResultObject> updateScenePush(final String sceneId, final String isPush) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/updateScenePush.json";

                StringBuffer urlStr = new StringBuffer(url)
                        .append("?sceneId=").append(sceneId)
                        .append("&isPush=").append(isPush);

                Log.d(TAG, urlStr.toString());

                return NetHelper.getData(urlStr.toString());
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }

    public Observable<Message> getMessageTemplate() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/device/getMessageTemplate.json";

                Log.d(TAG, url);

                return NetHelper.getData(url);
            }
        }).flatMap(new Func1<String, Observable<Message>>() {
            @Override
            public Observable<Message> call(String s) {

                Log.d(TAG, s);
                Message result = new Gson().fromJson(s, Message.class);

                return Observable.just(result);
            }
        });
    }


    public Observable<ResultObject> checkDeviceOnline(final String device, final String token) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/cloud/checkDeviceOnline.json";

                String deviceSN = device.replace(":", "").toLowerCase();

                StringBuffer urlStr = new StringBuffer(url)
                        .append("?deviceSN=").append(deviceSN)
                        .append("&token=").append(token);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("deviceSN", deviceSN);
                hashMap.put("token", token);

                String sign = null;
                try {
                    sign = MD5Util.createDeviceOnlineSignParameter(hashMap, "5d8d32ba-4c55-43d0-b772-9607b1f3e109");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                urlStr.append("&sign=").append(sign);

                Log.d(TAG, urlStr.toString());

                return NetHelper.getData(urlStr.toString());
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }


    public Observable<ResultObject> ble_light(final String deviceSN, final String dtype,
                                              final String data, final String source) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                String url = "http://api.aigolife.com/service/ble_light.json";

                StringBuffer urlStr = new StringBuffer(url)
                        .append("?deviceSN=").append(deviceSN)
                        .append("&dtype=").append(dtype)
                        .append("&data=").append(data)
                        .append("&source=").append(source);

                Log.d(TAG, urlStr.toString());

                return NetHelper.getData(urlStr.toString());
            }
        }).flatMap(new Func1<String, Observable<ResultObject>>() {
            @Override
            public Observable<ResultObject> call(String s) {

                Log.d(TAG, s);
                ResultObject result = new Gson().fromJson(s, ResultObject.class);

                return Observable.just(result);
            }
        });
    }


}
