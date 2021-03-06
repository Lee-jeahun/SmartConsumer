package team10.ldcc.com.smartconsumer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.laboratory.ldcc.wave.wave.WaveInfo;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zhangjingjing.autoscrolbanner.CycleViewpager;
import com.zhangjingjing.autoscrolbanner.PagerIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import team10.ldcc.com.smartconsumer.Data.Cart;
import team10.ldcc.com.smartconsumer.Data.Product;
import team10.ldcc.com.smartconsumer.common.Util;
import team10.ldcc.com.smartconsumer.retrofitService.NetworkService;

public class MainActivity extends AppCompatActivity {
    private ApplicationController application;
    private NetworkService networkService;
    private final String TAG = "MainActivity";
    private Toolbar toolbar;
    private LayoutInflater inflater;
    private FrameLayout frame;
    private DialogProductDetail dialogProductDetail;
    private DialogProductDetail2 dialogProductDetail2;
    private DialogProductNumberPicker dialogProductNumberPicker;
    private DatePickerDialog dialogDatePicker;
    private DialogProducDelivery dialogProducDelivery;

    private String product_code;
    private String product_delivery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = ApplicationController.getInstance();
        application.buildNetworkService();
        networkService = ApplicationController.getInstance().getContentService();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        getSupportActionBar().setTitle("");


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frame = (FrameLayout) findViewById(R.id.contentContainer);
        changeView(0);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                switch (tabId) {
                    case R.id.tab_home:
                        changeView(0);
                        break;
                    case R.id.tab_cart:
                        changeView(1);
                        break;
                    case R.id.tab_recipe:
                        changeView(2);
                        break;
                    case R.id.tab_setting:
                        changeView(3);
                        break;

                }
//                if (tabId == R.id.tab_home) {
//                    // The tab with id R.id.tab_favorites was selected,
//                    // change your content accordingly.
//                    Log.e("abc", "abc");
//                }

            }
        });



    }

    private int cuurrentIndex = 0;
    private int beforeIndex = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                changeView(beforeIndex);
                break;
            case R.id.action_barcodeScanner:


                //barcodeScanner("9421023610112","8");

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener click_add_cart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogProducDelivery = new DialogProducDelivery(MainActivity.this);
            dialogProducDelivery.show();
//            dialogDatePicker = new DatePickerDialog(MainActivity.this, listener, 2017, 6, 31);
//            dialogDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
//            dialogDatePicker.show();



            //            dialogProductNumberPicker = new DialogProductNumberPicker(MainActivity.this);
            //            dialogProductNumberPicker.show();
        }
    };





    public void addCart(){
        Cart cart = new Cart();
        cart.setUser_code(ApplicationController.getInstance().getUser().getUser_code());
        cart.setProductDetail_code(product_code);
        cart.setCart_count("1");
        cart.setCart_delivery(product_delivery);

        Log.e("abc",""+cart.toString());
        Call<Cart> call = networkService.addCart(cart);

        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.isSuccessful()){
                    dialogDatePicker.dismiss();
                    dialogProductDetail.dismiss();
                    Toast.makeText(getApplicationContext(), "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }





    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
        product_delivery = year+"."+(monthOfYear+1)+"."+dayOfMonth;
        addCart();
    }
};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barcodescanner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, ">>> requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            barcodeScanner(result.getContents(),"8");
        }
    }

    private void barcodeScanner(String barcode, String shopcode){

        Call<Product> call = networkService.barcodeScanner(barcode, shopcode);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
//                    Product product = new Product("5","샤프란 케어", "섬유탈취제 샤프란 케어 은은한 향" ,"8801051262995", "/images/product/01.jpeg");
                    product_code = response.body().getProduct_code();

//                    dialogProductDetail = new DialogProductDetail(MainActivity.this,response.body(), click_add_cart);
//                    dialogProductDetail.show();

                    Log.e(TAG,"bs : "+response.body());

                    dialogProductDetail2 = new DialogProductDetail2(MainActivity.this,response.body());
                    dialogProductDetail2.show();




//                    Util.alertMsg(MainActivity.this, response.body().toString(), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
                } else {
                    Log.e("MainActivity", "fail : " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }

    // home page
    private List<Product> productList;
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter adapter;
    private NestedScrollView myScrollingContent;
    private View view_home;
    private CycleViewpager mAutoVp;
    private PagerIndicator mIndicator;
    private List<String> imgUrls;



    private void selectTop10(){
        Call<List<Product>> call = networkService.selectTop10();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productList = new ArrayList<>();
                    productList = response.body();
                    Log.e(TAG,"list : "+productList.size());
                    recyclerView = (RecyclerView) view_home.findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    adapter = new ProductRecyclerAdapter(productList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }

    // cart page
    private void deleteCart(String cart_code){
        Call<Product> call = networkService.deleteCart(cart_code);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    selectCart();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }

    private void paymentCart(String cart_code){
        Call<Product> call = networkService.paymentCart(cart_code);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "결제되었습니다.", Toast.LENGTH_SHORT).show();
                    selectCart();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }

    private void addCount(String productDetail_code, String productDetail_count){
        Call<Product> call = networkService.addCount(productDetail_code, productDetail_count);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }


    private void selectCart(){
        Call<List<Product>> call = networkService.selectCart(ApplicationController.getInstance().getUser().getUser_code());
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                cartList = new ArrayList<Product>();
                cartList = response.body();
                for(Product c : cartList){
                    Log.e(TAG,"cartList :"+c.toString());
                }
                recyclerView_cart = (RecyclerView) view_cart.findViewById(R.id.recyclerView_cart);
                recyclerView_cart.setHasFixedSize(true);

                recyclerView_cart.setNestedScrollingEnabled(false);
                LinearLayoutManager mLayoutManager_cart = new LinearLayoutManager(MainActivity.this);
                recyclerView_cart.setLayoutManager(mLayoutManager_cart);
                adapter_cart = new CartRecyclerAdapter(cartList, MainActivity.this);
                recyclerView_cart.setAdapter(adapter_cart);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("error", "서버 onFailure : " + t.getMessage());
            }
        });
    }
    private List<Product> cartList;
    private RecyclerView recyclerView_cart;
    private CartRecyclerAdapter adapter_cart;
    private View view_cart;
    private Button btn_select;
    private Button btn_delete;
    private Button btn_payment;
    // setting page
    private Button button_logout;
    private Button button_modify;

    private void changeView(int index) {
        // LayoutInflater 초기화.
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (frame.getChildCount() > 0) { // FrameLayout에서 뷰 삭제.
            frame.removeViewAt(0);
        } // XML에 작성된 레이아웃을 View 객체로 변환.
        View view = null;
        switch (index) {
            case 0:
                view = inflater.inflate(R.layout.activity_home, frame, false);
                Log.e(TAG, "" + 0);
//                productList = new ArrayList<>();
                imgUrls = new ArrayList<>();
                imgUrls.add("http://52.78.34.142:3000/images/home/banner1.png");
                imgUrls.add("http://52.78.34.142:3000/images/home/banner2.png");
                imgUrls.add("http://52.78.34.142:3000/images/home/banner3.png");
                mAutoVp = (CycleViewpager) view.findViewById(R.id.vp_auto);
                mIndicator = (PagerIndicator) view.findViewById(R.id.pi_indicator);
                MyPagerAdapter adaptermy = new MyPagerAdapter(this, imgUrls);
                mAutoVp.setAdapter(adaptermy);
                mAutoVp.setOffscreenPageLimit(3);
                mAutoVp.startAutoScroll();
                mIndicator.setViewpager(mAutoVp);


                myScrollingContent = (NestedScrollView) view.findViewById(R.id.myScrollingContent);
                view_home = view;
                myScrollingContent.scrollTo(0, 0);
                selectTop10();
                break;
            case 1:
                view = inflater.inflate(R.layout.activity_cart, frame, false);
                view_cart = view;
                selectCart();
                btn_select = (Button) view.findViewById(R.id.btn_select);
                btn_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                btn_delete = (Button) view.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(Product p : adapter_cart.returnList()){
                            if(p.getProduct_check()!=null && p.getProduct_check()){
                                deleteCart(p.getCart_code());
                            }
                        }
                    }
                });
                btn_payment = (Button) view.findViewById(R.id.btn_payment);
                btn_payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(Product p : adapter_cart.returnList()){
                            if(p.getProduct_check()!=null && p.getProduct_check()){
                                paymentCart(p.getCart_code());
                                addCount(p.getProductDetail_code(),p.getProduct_count());
                            }
                        }

                    }
                });
                Log.e(TAG,""+1);
                break;
            case 2:
                view = inflater.inflate(R.layout.activity_recipe, frame, false);
                Log.e(TAG,""+2);
                break;
            case 3:
                view = inflater.inflate(R.layout.activity_setting, frame, false);
                Log.e(TAG,""+3);

                button_logout = (Button) view.findViewById(R.id.button_logout);
                button_logout.setOnClickListener(click_logout);

                button_modify = (Button) view.findViewById(R.id.button_modify);
                button_modify.setOnClickListener(click_modify_myinfo);
                break;
            case 4:
                beforeIndex = 3;
                view = inflater.inflate(R.layout.activity_modify_myinfo, frame, false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                getSupportActionBar().setDisplayShowHomeEnabled(false);
                Log.e(TAG,""+3);

        } // FrameLayout에 뷰 추가.
        if (view != null) {
            frame.addView(view);
        }
    }


    View.OnClickListener click_modify_myinfo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            //startActivityForResult(intent, 0);
            changeView(4);
        }
    };

    View.OnClickListener click_logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ApplicationController.getInstance().setUser(null);
            ApplicationController.getInstance().setLogin(false);
            Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            WaveInfo.getInstance().remove(getApplicationContext());
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };



    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<String> mUrls;
        public MyPagerAdapter(Context context,List<String> imgUrls) {
            this.mContext = context;
            this.mUrls = imgUrls;
        }


        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(mUrls.get(position)).into(imageView);
            ViewGroup par = (ViewGroup) imageView.getParent();
            if(par!=null){
                par.removeView(imageView);
            }
            container.addView(imageView, lp);
            return imageView;
        }
    }
}
