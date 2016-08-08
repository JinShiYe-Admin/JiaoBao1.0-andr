package com.jsy_jiaobao.main.appcenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.appcenter.sign.LocationActivity;
import com.jsy_jiaobao.main.appcenter.sign.ShowPopup;
import com.jsy_jiaobao.main.appcenter.workplan.WorkPlanActivity;
import com.jsy_jiaobao.main.system.LoginActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppCenterActivity extends BaseActivity {

    @ViewInject(R.id.appcenter_gridview)
    GridView gridView;
    @ViewInject(R.id.base_appcenter_layout)
    LinearLayout layout_ui;
    private TextView title;
    private Context mContext;
    private Intent intent = new Intent();
    private String[] items = {"签到", "日程"};
    private int[] drawables = {R.drawable.icon_signin, R.drawable.icon_workplan};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ui_appcenter);
        mContext = this;
        ViewUtils.inject(this);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_titile);
        title = (TextView) findViewById(R.id.actionbar_title);

        initParentView();
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("item_image", drawables[i]);
            map.put("item_name", items[i]);
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(mContext, data, R.layout.appcenter_gridview_item, new String[]{"item_image", "item_name"}, new int[]{R.id.item_image, R.id.item_name});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                switch (position) {
                    case 0:
                        intent.setClass(mContext, LocationActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(mContext, WorkPlanActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mContext, GalleryActivity.class);
                        startActivity(intent);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initParentView() {
        getSupportActionBar().setTitle(getResources().getString(R.string.function_appcenter));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBusUtil.register(this);
        setTitleText();
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);

        super.onPause();
    }

    private void setTitleText() {
        String nick = sp.getString("UserName", "");
        String unit = sp.getString("UnitName", "");
        title.setText(unit + ":" + nick);
    }

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_work_change://
                setTitleText();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        SubMenu sub_search = menu.addSubMenu("搜索").setIcon(R.drawable.abs__ic_search);
//        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

//        SubMenu sub_more = menu.addSubMenu("更多").setIcon(R.drawable.top_btn_more);
//        sub_more.add(0, 1001, 0, "更多_1");
//        sub_more.add(0, 1002, 0, "更多_2");
//        sub_more.add(0, 1003, 0, "更多_3");
//        sub_more.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        SubMenu sub_menu = menu.addSubMenu("系统").setIcon(R.drawable.top_btn_menu);
        sub_menu.add(1, 1011, 0, getResources().getString(R.string.function_changeunit));
        sub_menu.add(1, 1012, 0, getResources().getString(R.string.function_changeuser));
        sub_menu.add(1, 1013, 0, getResources().getString(R.string.function_exit));
        sub_menu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1000:

                break;
            case 1001:
                break;
            case 1002:

                break;
            case 1003:

                break;
            case 1011:
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                ShowPopup showPopup = new ShowPopup(mContext);
                showPopup.showPop(layout_ui, statusBarHeight + title.getBottom() + 2, Constant.listUserIdentity, null);

                break;
            case 1012:
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
            case 1013:
                JSYApplication.getInstance().onTerminate();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return false;
    }

//	@Override
//	public void onAttachedToWindow() {
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//		super.onAttachedToWindow();
//	}
}
