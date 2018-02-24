package com.zhuhu.application_practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;
import com.zhuhu.application_practice.Location.Location;
import com.zhuhu.application_practice.zoom.ZoomImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    //显示
    private ImageView mMenuview;
    private ImageView mNavview;
    private ImageView mScanview;
    private ImageView mUpstairsview;
    private ImageView mDownstairsview;
    private View mElevatorview;
    private View mToiletview;
    private View mCleanview;
    private ImageView mGuideElevatorview;
    private ImageView mGuideToiletview;
    private ImageView mGuideToilet2view;
    private ZoomImageView mImageViews;
    private ImageView ArrowView;

    private RelativeLayout layout;
    private static RelativeLayout layout_main;

    //定位计算
    float point[] = new float[3];
    Location loc = new Location();

    //定位测量
    BeaconManagerListener beaconManagerListener;
    CopyOnWriteArrayList<Beacon> beacons;

    SharedPreferences sharedPreferences;
    NotificationManager notificationManager;
    public static final int NOTIFICATION_ID = 0;
    private int updateCount = 0;
    private int page_id = 0;

    SensoroManager sensoroManager;

    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;

    Handler handler = new Handler();
    Runnable runnable;
    String[] LengthList = new String[4];

    float[] avrg0 = new float[10];
    float[] avrg1 = new float[10];
    float[] avrg2 = new float[10];
    float[] avrg3 = new float[10];

    RelativeLayout relativeLayout;
    DisplayMetrics dm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initFlag();
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        initSensoro();
        initCtrl();
        initSensoroListener();
//        initRunnable();
    }

    ////定位模块
    @Override
    protected void onResume() {
        boolean isBTEnable = isBlueEnable();
        if (isBTEnable) {
            startSensoroService();
        }
        handler.post(runnable);
        super.onResume();
    }

    //设置蓝牙
    private boolean isBlueEnable() {
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        boolean status = bluetoothAdapter.isEnabled();
        if (!status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                }
            }).setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setTitle(R.string.ask_bt_open);
            builder.show();
        }

        return status;
    }

    private void initCtrl() {
        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);
        beacons = new CopyOnWriteArrayList<Beacon>();
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        ArrowView=(ImageView)findViewById(R.id.view_arrow);

    }

//    //2秒后运行runable里面的东西
//    private void initRunnable() {
//        runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                handler.postDelayed(this, 2000);
//            }
//        };
//    }

    //初始化sensoro服务
    private void startSensoroService() {
        // set a tBeaconManagerListener.
        sensoroManager.setBeaconManagerListener(beaconManagerListener);
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSensoroListener() {

        beaconManagerListener = new BeaconManagerListener() {

            @Override
            public void onUpdateBeacon(final ArrayList<Beacon> arg0) {
                beacons.clear();
                for (Beacon beacon : arg0) {
                    if (beacons.contains(beacon)) {
                        continue;
                    }
                    beacons.add(beacon);
                }

                final float[] avrgList = new float[4];
                for (int i = 0; i < beacons.size(); i++) {
                    int index = updateCount % 10;
                    float item = Float.parseFloat(updateView(beacons.get(i)));
                    switch (i) {
                        case 0:
                            if (updateCount >= 10) {
                                float avr = getAvrgOfArr(avrg0);
                                avrgList[0] = avr;
                                if (item < avr * 2) {
                                    avrg0[index] = item;
                                }
                            } else {
                                avrg0[index] = item;
                                avrgList[0] = item;
                            }
                            break;
                        case 1:
                            if (updateCount >= 10) {
                                float avr = getAvrgOfArr(avrg1);
                                avrgList[1] = avr;
                                if (item < avr * 2) {
                                    avrg1[index] = item;
                                }
                            } else {
                                avrg1[index] = item;
                                avrgList[1] = item;
                            }
                            break;
                        case 2:
                            if (updateCount >= 10) {
                                float avr = getAvrgOfArr(avrg2);
                                avrgList[2] = avr;
                                if (item < avr * 2) {
                                    avrg2[index] = item;
                                }
                            } else {
                                avrg2[index] = item;
                                avrgList[2] = item;
                            }
                            break;
                        case 3:
                            if (updateCount >= 10) {
                                float avr = getAvrgOfArr(avrg3);
                                avrgList[3] = avr;
                                if (item < avr * 2) {
                                    avrg3[index] = item;
                                }
                            } else {
                                avrg3[index] = item;
                                avrgList[3] = item;
                            }
                            break;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < beacons.size(); i++) {
                            LengthList[i] = avrgList[i] + "";
                        }
                        draw();
                    }
                });

                updateCount++;
            }

            @Override
            public void onNewBeacon(Beacon arg0) {
                /*
                 * A new beacon appears.
				 */
                String key = getKey(arg0);
                boolean state = sharedPreferences.getBoolean(key, false);
                if (state) {
					/*
					 * show notification
					 */

                    showNotification(arg0, true);
                }

            }

            @Override
            public void onGoneBeacon(Beacon arg0) {
				/*
				 * A beacon disappears.
				 */
                String key = getKey(arg0);
                boolean state = sharedPreferences.getBoolean(key, false);
                if (state) {
					/*
					 * show notification
					 */

                    showNotification(arg0, false);
                }
            }
        };
    }

    //取平均值函数
    private float getAvrgOfArr(float[] arr) {
        float sum = 0;
        for (float anArr : arr) sum += anArr;
        return sum / arr.length;
    }

    public String getKey(Beacon beacon) {
        if (beacon == null) {
            return null;
        }
        String key = beacon.getProximityUUID() + beacon.getMajor() + beacon.getMinor() + beacon.getSerialNumber();

        return key;

    }

    private void showNotification(final Beacon beacon, final boolean isIn) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                String context = null;
                if (isIn) {
                    context = String.format("IN:%s", beacon.getSerialNumber());
                } else {
                    context = String.format("OUT:%s", beacon.getSerialNumber());
                }
                builder.setTicker(context);
                builder.setContentText(context);
                builder.setWhen(System.currentTimeMillis());
                builder.setAutoCancel(true);
                builder.setContentTitle(getString(R.string.app_name));
                builder.setDefaults(Notification.DEFAULT_SOUND);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                Notification notification = builder.build();

                notificationManager.notify(NOTIFICATION_ID, notification);

            }
        });

    }

    //更新距离
    private String updateView(Beacon beacon) {
        if (beacon == null) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("#");
        String distance = format.format(beacon.getAccuracy() * 100);
        return distance;//距离单位为cm
    }

    //初始化Sensoro
    private void initSensoro() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.setCloudServiceEnable(false);
        sensoroManager.addBroadcastKey("01Y2GLh1yw3+6Aq0RsnOQ8xNvXTnDUTTLE937Yedd/DnlcV0ixCWo7JQ+VEWRSya80yea6u5aWgnW1ACjKNzFnig==");
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    //获得测量坐标
    private float[] getLocation() {
        try {
            //获得坐标
            point[0] = initCoordinates(1)[0];
            point[1] = initCoordinates(1)[1];
            point[2] = 0;
            loc.set_point(point, 1);

            point[0] = initCoordinates(2)[0];
            point[1] = initCoordinates(2)[1];
            point[2] = 0;
            loc.set_point(point, 2);

            point[0] = initCoordinates(3)[0];
            point[1] = initCoordinates(3)[1];
            point[2] = 0;
            loc.set_point(point, 3);

            point[0] = initCoordinates(4)[0];
            point[1] = initCoordinates(4)[1];
            point[2] = 200;
            loc.set_point(point, 4);

            //distance
            loc.set_distance(Float.valueOf(LengthList[0]), 1);
            loc.set_distance(Float.valueOf(LengthList[1]), 2);
            loc.set_distance(Float.valueOf(LengthList[2]), 3);
            loc.set_distance(Float.valueOf(LengthList[3]), 4);

            final float x[] = loc.calc();

            if (x != null) {
                return x;
            }
            return null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    //输入测量坐标，输出屏幕坐标
    private float[] changeLocation(float array[]) {
        dm = getResources().getDisplayMetrics();
//        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）
        if (array != null && getLocation() != null) {
            float arrayX = array[0];
            float arrayY = array[1];
            array[0] = arrayX / initCoordinates(4)[0] * screenWidth;
            array[1] = arrayY / initCoordinates(4)[1] * screenHeight;
            return array;
        } else {
            return new float[2];
        }
    }

    //绘制箭头
    private void draw() {
        if (getLocation() != null) {
            float arr[];
            arr = getLocation();

            final float[] tmp = changeLocation(arr);
            if (tmp != null && tmp.length > 1) {
                float x,y;
                x=tmp[0];
                y=tmp[1];
                relativeLayout.removeView(ArrowView);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ArrowView.getLayoutParams());
                params.setMargins((int)x,(int)y,0,0);
                relativeLayout.addView(ArrowView, params);
            }
        }
    }

    //初始化云子位置坐标
    private float[] initCoordinates(int n) {
        float array[] = new float[2];
        switch (n) {
            case 1:
                array[0] = 0;
                array[1] = 0;
                break;
            case 2:
                array[0] = 0;
                array[1] = 900;
                break;
            case 3:
                array[0] = 1600;
                array[1] = 0;
                break;
            case 4:
                array[0] = 1600;
                array[1] = 900;
                break;
        }
        return array;
    }


    private void initViews() {
        mMenuview = (ImageView) findViewById(R.id.view_menu);
        mNavview = (ImageView) findViewById(R.id.view_nav);
        mScanview = (ImageView) findViewById(R.id.view_scan);
        mUpstairsview = (ImageView) findViewById(R.id.view_upstairs);
        mDownstairsview = (ImageView) findViewById(R.id.view_downstairs);
        mElevatorview = findViewById(R.id.view_elevator);
        mToiletview = findViewById(R.id.view_toilet);
        mCleanview = findViewById(R.id.view_clean);
        mGuideElevatorview = (ImageView) findViewById(R.id.view_guide_elevator);
        mGuideToiletview = (ImageView) findViewById(R.id.view_guide_toilet);
        mGuideToilet2view = (ImageView) findViewById(R.id.view_guide_toilet2);
        mImageViews = (ZoomImageView) findViewById(R.id.id_zoomimageview);

        layout = (RelativeLayout) findViewById(R.id.layout_nav);
        layout_main = (RelativeLayout) findViewById(R.id.activity_main);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setListeners() {

        mNavview.setOnClickListener(new NoDoubleViewClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });
        mElevatorview.setOnClickListener(new NoDoubleViewClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                layout.setVisibility(View.GONE);
                //显示电梯图片
                mGuideElevatorview.setVisibility(View.VISIBLE);
            }
        });
        mToiletview.setOnClickListener(new NoDoubleViewClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                layout.setVisibility(View.GONE);
                //显示厕所图片
                mGuideToiletview.setVisibility(View.VISIBLE);
                mGuideToilet2view.setVisibility(View.VISIBLE);
            }
        });
        mCleanview.setOnClickListener(new NoDoubleViewClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                layout.setVisibility(View.GONE);
                //全部导览图标隐藏
                mGuideElevatorview.setVisibility(View.GONE);
                mGuideToiletview.setVisibility(View.GONE);
                mGuideToilet2view.setVisibility(View.GONE);
            }
        });
        //跳转到菜单
        mMenuview.setOnClickListener(new NoDoubleViewClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent it = new Intent();
                it.setAction("my_action");
                it.addCategory("my_category");
                startActivity(it);
            }
        });
        //跳转到扫描说明页面
        mScanview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, Scan_instructionActivity.class);
                startActivity(it);

            }
        });
        //打算换成跳转到新地图
        mUpstairsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_id==0) {
                    mImageViews.setImageDrawable(getResources().getDrawable(R.mipmap.pic_floor1));
                    page_id++;
                }
                else if(page_id==1) {
                    mImageViews.setImageDrawable(getResources().getDrawable(R.mipmap.pic_floor2));
                    page_id++;
                }

            }
        });
        mDownstairsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent map = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(map);
                if(page_id==1) {
                    mImageViews.setImageDrawable(getResources().getDrawable(R.mipmap.pic_main));
                    page_id--;
                }
                else if(page_id==2) {
                    mImageViews.setImageDrawable(getResources().getDrawable(R.mipmap.pic_floor1));
                    page_id--;
                }
            }
        });
        //地图伸缩回调
        mImageViews.setOnScaleEndCallback(new ZoomImageView.OnScaleEndCallback() {
            @Override
            public void onHide() {
                setVisibleOfView(mMenuview, false);
                setVisibleOfView(mScanview, false);
                setVisibleOfView(mNavview, false);
                setVisibleOfView(mUpstairsview,false);
                setVisibleOfView(mDownstairsview,false);
                setVisibleOfView(ArrowView,false);
            }

            @Override
            public void onShow() {
                setVisibleOfView(mMenuview, true);
                setVisibleOfView(mScanview, true);
                setVisibleOfView(mNavview, true);
                setVisibleOfView(mUpstairsview,true);
                setVisibleOfView(mDownstairsview,true);
                setVisibleOfView(ArrowView,true);
            }
        });

    }

//    //设置状态栏
//    private void initFlag() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
//    }

    private void setVisibleOfView(View view, boolean flag) {
        if (view != null)
            view.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")// TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //设置按返回键两次退出app
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出APP",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
