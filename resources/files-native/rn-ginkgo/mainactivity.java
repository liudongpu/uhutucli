    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateCheck updateCheck=new UpdateCheck();

        updateCheck.check(this);


        new GlobalCheck().init(this);


        if (this != null) {
            /*
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                }
            });
            */

            FrameLayout contentView =  (FrameLayout)this.findViewById(android.R.id.content);
            TextView b1 = new TextView(this);
            b1.setText("正在通话中……");


            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();



            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams (layoutParams.width,60);  //设置按钮的宽度和高度

            b1.setLayoutParams(btParams);
            b1.setClickable(true);
            b1.setBackgroundColor(Color.rgb(252,214,146));
            b1.setTextSize(12);
            b1.setGravity(Gravity.CENTER);
            b1.setVisibility(View.INVISIBLE);

            b1.setId(R.id.ginkgo_video_values_strings_video_call);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), VideoMainActivity.class);

                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            contentView.addView(b1,btParams);



            //动态接受网络变化的广播接收器
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.uhutu.video.activity.call");

            broadcastReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean bFlag= intent.getStringExtra("type").equals("open");

                    findViewById(R.id.ginkgo_video_values_strings_video_call).setVisibility(bFlag?View.VISIBLE:View.INVISIBLE);
                }
            };



            registerReceiver(broadcastReceiver, intentFilter);


        };

        updateCheck.bundleSuccess(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new GlobalCheck().onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
