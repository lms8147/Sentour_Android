package com.tou4u.sentour.contentviewer.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tou4u.sentour.contentviewer.main.network.NetworkUtil;
import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.R;
import com.tou4u.sentour.contentviewer.fragment.ContentListFragment;
import com.tou4u.sentour.data.GeoPosition;
import com.tou4u.sentour.data.WeatherData;
import com.tou4u.sentour.httpapi.WeatherAPI;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MainActivity extends AppCompatActivity implements ContentsUpdateExcuter.OnUpdateCallBack {


    private static final String TAG = "MainActivity";

    public static final String CONTENT_TYPE_FORMAT_SPOT = "   관광지   ";
    public static final String CONTENT_TYPE_FORMAT_CULTURE = "    문화    ";
    public static final String CONTENT_TYPE_FORMAT_FESTIVAL = "    축제    ";
    public static final String CONTENT_TYPE_FORMAT_COURSE = "  여행코스  ";
    public static final String CONTENT_TYPE_FORMAT_LEPORTS = "   레포츠   ";
    public static final String CONTENT_TYPE_FORMAT_SHOPPING = "    쇼핑    ";
    public static final String CONTENT_TYPE_FORMAT_RESTAURANT = "    음식    ";

    public static final int CONTENT_TYPE_CODE_SPOT = 12;
    public static final int CONTENT_TYPE_CODE_CULTURE = 14;
    public static final int CONTENT_TYPE_CODE_FESTIVAL = 15;
    public static final int CONTENT_TYPE_CODE_COURSE = 25;
    public static final int CONTENT_TYPE_CODE_LEPORTS = 28;
    public static final int CONTENT_TYPE_CODE_SHOPPING = 38;
    public static final int CONTENT_TYPE_CODE_RESTAURANT = 39;

    private static final int[] CONTENT_TYPE_CODE =
            new int[]{
                    CONTENT_TYPE_CODE_SPOT,
                    CONTENT_TYPE_CODE_CULTURE,
                    CONTENT_TYPE_CODE_FESTIVAL,
                    CONTENT_TYPE_CODE_COURSE,
                    CONTENT_TYPE_CODE_LEPORTS,
                    CONTENT_TYPE_CODE_SHOPPING,
                    CONTENT_TYPE_CODE_RESTAURANT};

    private static final String[] CONTENT_TYPE_FORMAT =
            new String[]{
                    CONTENT_TYPE_FORMAT_SPOT,
                    CONTENT_TYPE_FORMAT_CULTURE,
                    CONTENT_TYPE_FORMAT_FESTIVAL,
                    CONTENT_TYPE_FORMAT_COURSE,
                    CONTENT_TYPE_FORMAT_LEPORTS,
                    CONTENT_TYPE_FORMAT_SHOPPING,
                    CONTENT_TYPE_FORMAT_RESTAURANT};

    private static final int PLAY_SERVICES_REQUEST_ERROR = 0;

    /**
     * 위치 권한 요청 키
     */
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private ViewPager mViewPager;

    private GoogleApiClient mClient;

    private ConcurrentMap<Integer, ContentListFragment> mFragmentMap = new ConcurrentHashMap<>();
    private MainDataSubject mMainDataSubject;

    private WeatherAPI mWeatherAPI;

    private FloatingActionButton mHashChangeButton;
    private ContentTypeAdapter mContentTypeAdapter;
    private FloatingActionButton mRefreshButton;

    private AppCompatTextView mSkyTextView;
    private AppCompatTextView mTemTextView;

    private ContentsUpdateExcuter mContentsUpdateExcuter;
    private HashTagChanger mHashTagChanger;
    private InfoOpener mInfoOpener;
    private CopyrightOpener mCopyrightOpener;
    private WarningOpener warningOpener;

    public MainDataSubject getMainData() {
        return mMainDataSubject;
    }

    @Override
    protected void onDestroy() {
        mInfoOpener.dismissDialog();
        mCopyrightOpener.dismissDialog();
        mHashTagChanger.dismissDialog();
        warningOpener.dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onContentsUpdate(String lat, String lng, String hashTag, String sky, int currentPage) {
        ContentListFragment fragment = mFragmentMap.get(currentPage);
        fragment.updateUI(lat, lng, sky, hashTag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        mMainDataSubject.setCurrentPage(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Info) {
            mInfoOpener.showDialog();
            return true;
        } else if (id == R.id.action_copyright) {
            mCopyrightOpener.showDialog();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * GoogleAPI 사용가능한지 체크
     * 안드로이드 버전 UP 에따라 기존 라이브러리 deprecated 됨
     * 따라서 변경된 소스
     *
     * @return
     */
    private boolean checkPlayServices() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_REQUEST_ERROR).show();
            }

            return false;
        }

        return true;
    }
/*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        warningOpener = new WarningOpener(this);
        mInfoOpener = new InfoOpener(this);
        mCopyrightOpener = new CopyrightOpener(this);
        mMainDataSubject = new MainDataSubject();
        mContentsUpdateExcuter = new ContentsUpdateExcuter(this, this, mMainDataSubject);
        mHashTagChanger = new HashTagChanger(this, mMainDataSubject, (AppCompatTextView) findViewById(R.id.textView_tag));

        /**
         * com.google.android.gms.common.api 패키지에 있는 GoogleApiClient 생성
         * gradle 의 depencencies 에서 compile 'com.google.android.gms:play-services-location:7.3.0' 추가하면
         * com.google.android.gms.common.api 사용가능
         */
        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API) // LocationServices.API 사용하겠다고 설정
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() { //위치 서비스가 연결되거나 꺼질때 실행될 콜백
                    @Override
                    public void onConnected(Bundle bundle) {
                        // 연결시
                        updateGPSInformation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        // 연결 종료시
                    }
                })
                .build();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mContentTypeAdapter = new ContentTypeAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mContentTypeAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ContentListFragment fragment = mFragmentMap.get(position);
                if (fragment != null) {
                    mMainDataSubject.setCurrentPage(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSkyTextView = (AppCompatTextView) findViewById(R.id.textView_sky);
        mTemTextView = (AppCompatTextView) findViewById(R.id.textView_tem);

        mWeatherAPI = new WeatherAPI();

        mHashTagChanger.showDialog();
        warningOpener.showDialog();

        mHashChangeButton = (FloatingActionButton) findViewById(R.id.button_tag_change);
        mHashChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHashTagChanger.showCancelableDialog();

            }
        });

        mRefreshButton = (FloatingActionButton) findViewById(R.id.button_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPosition position = mMainDataSubject.getPosition();

                LocationRequest request = LocationRequest.create();
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                request.setNumUpdates(1);
                request.setInterval(0);

                if (!checkLocationAvailale()) {
                    Toast.makeText(MainActivity.this, "위치 정보 갱신 실패 : GPS 미연결", Toast.LENGTH_SHORT).show();

                    if (NetworkUtil.getInstance().isConnected(MainActivity.this) == NetworkUtil.CODE_CONNECT) {
                        if (position != null) {
                            new AsyncTask<GeoPosition, Void, WeatherData>() {

                                @Override
                                protected WeatherData doInBackground(GeoPosition... params) {
                                    WeatherData data = mWeatherAPI.getSkyData(params[0]);
                                    return data;
                                }

                                @Override
                                protected void onPostExecute(WeatherData weatherData) {
                                    super.onPostExecute(weatherData);
                                    boolean flag = true;
                                    if (weatherData != null) {
                                        mSkyTextView.setText(String.format("날씨 : %s", weatherData.getSkyName()));
                                        mTemTextView.setText(String.format("기온 : %s'", weatherData.getTemperature()));
                                        flag = !mMainDataSubject.setSky(weatherData.getSkyCode());
                                        Toast.makeText(MainActivity.this, "날씨 정보 갱신", Toast.LENGTH_SHORT).show();
                                    }
                                    if (flag) {
                                        mMainDataSubject.setChanged();
                                        Toast.makeText(MainActivity.this, "컨텐츠 정보 갱신", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.execute(position);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mClient, request, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    DebugUtil.logD(TAG, String.format("GPS UPDATE ( %s )", location.toString()));

                                    mMainDataSubject.setGeoPosition(new GeoPosition(location.getLatitude(), location.getLongitude()));
                                    Toast.makeText(MainActivity.this, "위치 정보 갱신", Toast.LENGTH_SHORT).show();
                                    if (NetworkUtil.getInstance().isConnected(MainActivity.this) == NetworkUtil.CODE_CONNECT) {
                                        new AsyncTask<GeoPosition, Void, WeatherData>() {

                                            @Override
                                            protected WeatherData doInBackground(GeoPosition... params) {
                                                WeatherData data = mWeatherAPI.getSkyData(params[0]);
                                                return data;
                                            }

                                            @Override
                                            protected void onPostExecute(WeatherData weatherData) {
                                                super.onPostExecute(weatherData);
                                                if (weatherData != null) {
                                                    mSkyTextView.setText(String.format("날씨 : %s", weatherData.getSkyName()));
                                                    mTemTextView.setText(String.format("기온 : %s'", weatherData.getTemperature()));
                                                    mMainDataSubject.setSky(weatherData.getSkyCode());
                                                    Toast.makeText(MainActivity.this, "날씨 정보 갱신", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }.execute(new GeoPosition(location));
                                    } else {
                                        Toast.makeText(MainActivity.this, "날씨 정보 갱신 실패 : 네트워크 미연결", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                }


            }
        });

    }


    class ContentTypeAdapter extends FragmentPagerAdapter {
        public ContentTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ContentListFragment fragment = ContentListFragment.newInstance(CONTENT_TYPE_CODE[position]);
            mFragmentMap.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT_TYPE_FORMAT[position % CONTENT_TYPE_FORMAT.length];
        }

        @Override
        public int getCount() {
            return CONTENT_TYPE_FORMAT.length;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    /**
     * 권한 요청 시 실행될 메소드
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    registLocationRequester();
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    new AlertDialog.Builder(this, R.style.DialogTheme)
                            .setTitle("권한 필수")
                            .setMessage("위치 정보 권한이 거부되어 실행 할 수 없습니다.")
                            .setCancelable(false)
                            .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
                return;
        }
    }

    private boolean checkLocationAvailale() {
        boolean availability = LocationServices.FusedLocationApi.getLocationAvailability(mClient).isLocationAvailable();
        return availability;
    }

    private void registLocationRequester() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(1000);
        request.setInterval(5000);

        if (!checkLocationAvailale()) {
            Toast.makeText(MainActivity.this, "위치 정보 갱신 실패 : GPS 미연결", Toast.LENGTH_SHORT).show();
        }


        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    int flag = 41;

                    @Override
                    public void onLocationChanged(Location location) {
                        DebugUtil.logD(TAG, String.format("GPS UPDATE ( %s )", location.toString()));

                        mMainDataSubject.setGeoPosition(new GeoPosition(location.getLatitude(), location.getLongitude()));
                        Toast.makeText(MainActivity.this, "위치 정보 갱신", Toast.LENGTH_SHORT).show();
                        flag++;

                        if (flag > 40) {
                            flag = 0;
                            if (NetworkUtil.getInstance().isConnected(MainActivity.this) == NetworkUtil.CODE_CONNECT) {
                                new AsyncTask<GeoPosition, Void, WeatherData>() {

                                    @Override
                                    protected WeatherData doInBackground(GeoPosition... params) {
                                        WeatherData data = mWeatherAPI.getSkyData(params[0]);
                                        return data;
                                    }

                                    @Override
                                    protected void onPostExecute(WeatherData weatherData) {
                                        super.onPostExecute(weatherData);
                                        if (weatherData != null) {
                                            mSkyTextView.setText(String.format("날씨 : %s", weatherData.getSkyName()));
                                            mTemTextView.setText(String.format("기온 : %s'", weatherData.getTemperature()));
                                            mMainDataSubject.setSky(weatherData.getSkyCode());
                                            Toast.makeText(MainActivity.this, "날씨 정보 갱신", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }.execute(new GeoPosition(location));
                            } else {
                                Toast.makeText(MainActivity.this, "날씨 정보 갱신 실패 : 네트워크 미연결", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

    }

    private void updateGPSInformation() {

        int findLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int access_coarse_location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        /**
         * 위치 정보 사용 권한 확인 -> 없으면 요청
         */
        if (findLocationPermission != PackageManager.PERMISSION_GRANTED && access_coarse_location != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            registLocationRequester();
        }

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


}
