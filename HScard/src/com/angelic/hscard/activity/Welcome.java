package com.angelic.hscard.activity;

import java.util.ArrayList;
import java.util.List;

import com.angelic.hscard.R;
import com.angelic.hscard.adapter.LeftMenuAdapter;
import com.angelic.hscard.adapter.MyPagerAdapter;
import com.angelic.hscard.anim.SafeAnimator;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.model.HsLeftMenu;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Welcome extends ActivityGroup {

	Context context;

	// 查询条件
	private HsCard searchCards;
	private HsCard searchTemp;
	//
	private Button btn_jumpToView;
	// 底部导航栏
	LocalActivityManager manager;
	private final int VP_INDEX_CT = 0;// CardTool
	private final int VP_INDEX_CG = 1;// CardGroup
	private int currentIndex = 0;// 当前的选项卡
	private RadioGroup rg_menu;// 底部的menu
	private ViewPager viewPager;
	private ArrayList<View> views;// Tab页面列表
	// 左侧导航栏
	private DrawerLayout mDrawerLayout;
	private ListView leftView;// 左侧的Menu
	private Button btnLeftView;
	List<HsLeftMenu> leftMenuList;
	LeftMenuAdapter leftAdapter;
	// 多级标题栏
	private ViewAnimator barSwitcher;
	private HorizontalScrollView scrMain, scrCost, scrAtta, scrHp;

	enum TopBarMode {
		Main, Cost, Atta, Hp
	};

	private TopBarMode topBarMode = TopBarMode.Main;
	private SafeAnimator safeAnimator;
	private Button btnCost, btnAtta, btnHp;
	private RelativeLayout costRl;
	private PopupWindow popCost;
	// 卡牌搜索弹出层
	private PopupWindow popSearch;
	private Button openPopSearch;
	private List<CheckBox> tempTypeBox;
	private List<CheckBox> tempRaceBox;
	private List<CheckBox> tempSkillBox;
	private EditText searchFuzzy;
	private int currentSearch = 0;
	private LinearLayout searchDetailLl, searchFuzzyLl;
	// 卡牌稀有度弹出层(下拉菜单)
	private PopupWindow popLevel;
	private RadioButton openPopLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = this;
		manager = getLocalActivityManager();

		btn_jumpToView = (Button) findViewById(R.id.jumptoview);
		btn_jumpToView.setOnClickListener(onClick);
		initListview();
		initMenu();
		initViewPager();
		initTitleBar();
		// 卡牌搜索弹出层初始化
		openPopSearch = (Button) findViewById(R.id.btn_openSearch);
		openPopSearch.setOnClickListener(onClick);
		// 卡牌稀有度弹出层初始化
		openPopLevel = (RadioButton) findViewById(R.id.btn_openLevel);
		openPopLevel.setOnClickListener(onClick);
	}

	/**
	 * 卡牌稀有度弹出层初始化
	 * 
	 * @Title initPopLevel
	 * @Description 
	 */
	public void initPopLevel() {
		// 获取自定义布局文件pop_level.xml的视图
		final View levelView = getLayoutInflater().inflate(R.layout.pop_level,
				null, false);

		// 创建PopupWindow实例
		popLevel = new PopupWindow(levelView, openPopLevel.getWidth(),
				openPopLevel.getHeight() * 6 + 6);
		// 设置动画效果 [R.style.AnimationFade 是事先定义好的]
		popLevel.setAnimationStyle(R.style.AnimationFade);
		// 使其聚集
		popLevel.setFocusable(true);
		// 设置允许在外点击消失
		popLevel.setOutsideTouchable(true);
		popLevel.setBackgroundDrawable(new BitmapDrawable());
		// 选择事件
		setCheckedLevel(levelView);
		((RadioGroup) levelView.findViewById(R.id.rg_level))
				.setOnCheckedChangeListener(searchChecked);

		// 显示位置
		popLevel.showAsDropDown(openPopLevel, 0, 0);
	}

	/**
	 * 设置PopLevel弹出层RadioButton的值
	 * @Title setCheckedLevel
	 * @Description TODO
	 * @param v
	 */
	private void setCheckedLevel(View v){
		if(openPopLevel.getText().equals("稀有度")){
			((RadioButton) v.findViewById(R.id.rg_level_all)).setChecked(true);
		}
		if(openPopLevel.getText().equals("基本")){
			((RadioButton) v.findViewById(R.id.rg_level_0)).setChecked(true);
		}
		if(openPopLevel.getText().equals("普通")){
			((RadioButton) v.findViewById(R.id.rg_level_1)).setChecked(true);
		}
		if(openPopLevel.getText().equals("精良")){
			((RadioButton) v.findViewById(R.id.rg_level_2)).setChecked(true);
		}
		if(openPopLevel.getText().equals("史诗")){
			((RadioButton) v.findViewById(R.id.rg_level_3)).setChecked(true);
		}
		if(openPopLevel.getText().equals("传说")){
			((RadioButton) v.findViewById(R.id.rg_level_4)).setChecked(true);
		}
	}
	/**
	 * 卡牌搜索弹出层初始化
	 * 
	 * @Title initPopSearch
	 * @Description
	 */
	public void initPopSearch() {
		// 获取自定义布局文件pop_search.xml的视图
		final View customView = getLayoutInflater().inflate(
				R.layout.pop_search, null, false);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 创建PopupWindow实例,屏幕宽,3/4屏幕高
		popSearch = new PopupWindow(customView, windowManager
				.getDefaultDisplay().getWidth(), windowManager
				.getDefaultDisplay().getHeight() * 3 / 4);
		// 设置动画效果 [R.style.AnimationFade 是事先定义好的]
		popSearch.setAnimationStyle(R.style.AnimationFade);

		// 使其聚集
		popSearch.setFocusable(true);
		// 设置允许在外点击消失
		popSearch.setOutsideTouchable(true);
		popSearch.setBackgroundDrawable(new BitmapDrawable());

		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popSearch.getWidth() / 2;

		// Search Pop弹出层右上角关闭按钮,关闭事件
		customView.findViewById(R.id.btn_search_close).setOnClickListener(
				onClick);
		// 查询事件
		customView.findViewById(R.id.btn_search).setOnClickListener(onClick);
		// 清空事件
		customView.findViewById(R.id.btn_search_canel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						clearSearchOption(customView);
					}
				});
		// 选择事件
		((RadioGroup) customView.findViewById(R.id.rg_search_title))
				.setOnCheckedChangeListener(searchChecked);
		searchDetailLl = (LinearLayout) customView
				.findViewById(R.id.search_detail_ll);
		searchFuzzyLl = (LinearLayout) customView
				.findViewById(R.id.search_fuzzy_ll);
		searchFuzzyLl.setVisibility(View.GONE);
		// 模糊查询条件
		searchFuzzy = (EditText) customView.findViewById(R.id.search_fuzzy);
		// checkbox选择事件
		if (tempTypeBox == null) {
			tempTypeBox = new ArrayList<CheckBox>();
		}
		if (tempRaceBox == null) {
			tempRaceBox = new ArrayList<CheckBox>();
		}
		if (tempSkillBox == null) {
			tempSkillBox = new ArrayList<CheckBox>();
		}
		initSetCheckBox(customView);
		((CheckBox) customView.findViewById(R.id.search_type_1))
				.setOnCheckedChangeListener(checkType);
		((CheckBox) customView.findViewById(R.id.search_type_2))
				.setOnCheckedChangeListener(checkType);
		((CheckBox) customView.findViewById(R.id.search_type_3))
				.setOnCheckedChangeListener(checkType);
		((CheckBox) customView.findViewById(R.id.search_race_1))
				.setOnCheckedChangeListener(checkRace);
		((CheckBox) customView.findViewById(R.id.search_race_2))
				.setOnCheckedChangeListener(checkRace);
		((CheckBox) customView.findViewById(R.id.search_race_3))
				.setOnCheckedChangeListener(checkRace);
		((CheckBox) customView.findViewById(R.id.search_race_4))
				.setOnCheckedChangeListener(checkRace);
		((CheckBox) customView.findViewById(R.id.search_race_5))
				.setOnCheckedChangeListener(checkRace);
		((CheckBox) customView.findViewById(R.id.search_skill_1))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_2))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_3))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_4))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_5))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_6))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_7))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_8))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_9))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_10))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_11))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_12))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_13))
				.setOnCheckedChangeListener(checkSkill);
		((CheckBox) customView.findViewById(R.id.search_skill_14))
				.setOnCheckedChangeListener(checkSkill);

		// 显示位置
		popSearch.showAsDropDown(scrMain, xPos, 0);

	}

	/**
	 * 设置PopSearch弹出层CheckBox的值
	 * @Title initSetCheckBox
	 * @Description TODO
	 * @param v
	 */
	private void initSetCheckBox(View v) {
		if (tempTypeBox != null) {
			for (CheckBox cb : tempTypeBox) {
				if (cb.isChecked()) {
					((CheckBox) v.findViewById(cb.getId())).setChecked(true);
				}

			}
		}
		if (tempRaceBox != null) {
			for (CheckBox cb : tempRaceBox) {
				if (cb.isChecked()) {
					((CheckBox) v.findViewById(cb.getId())).setChecked(true);
				}

			}
		}
		if (tempSkillBox != null) {
			for (CheckBox cb : tempSkillBox) {
				if (cb.isChecked()) {
					((CheckBox) v.findViewById(cb.getId())).setChecked(true);
				}

			}
		}
	}

	/**
	 * 条件搜索弹出层中清空事件
	 * 
	 * @Title clearSearchOption
	 * @Description
	 * @param v
	 */
	private void clearSearchOption(View v) {
		if (tempTypeBox != null) {
			for (CheckBox cb : tempTypeBox) {
				((CheckBox) v.findViewById(cb.getId())).setChecked(false);
			}
		}
		if (tempRaceBox != null) {
			for (CheckBox cb : tempRaceBox) {
				((CheckBox) v.findViewById(cb.getId())).setChecked(false);
			}
		}
		if (tempSkillBox != null) {
			for (CheckBox cb : tempSkillBox) {
				((CheckBox) v.findViewById(cb.getId())).setChecked(false);
			}
		}
		searchFuzzy.setText("");
	}

	/**
	 * 重置事件中,重置弹出层中的数据
	 * 
	 * @Title resetSearchOption
	 * @Description TODO
	 */
	private void resetSearchOption() {
		if (tempTypeBox != null) {
			for (CheckBox cb : tempTypeBox) {
				cb.setChecked(false);
			}
		}
		if (tempRaceBox != null) {
			for (CheckBox cb : tempRaceBox) {
				cb.setChecked(false);
			}
		}
		if (tempSkillBox != null) {
			for (CheckBox cb : tempSkillBox) {
				cb.setChecked(false);
			}
		}
		searchFuzzy.setText("");
	}

	/**
	 * 多级标题栏初始化
	 * 
	 * @Title initTitleBar
	 * @Description
	 */
	public void initTitleBar() {
		// 多级标题栏初始化
		barSwitcher = (ViewAnimator) findViewById(R.id.title_bar);
		scrMain = (HorizontalScrollView) findViewById(R.id.title_bar_main);
		scrCost = (HorizontalScrollView) findViewById(R.id.title_bar_cost);
		scrAtta = (HorizontalScrollView) findViewById(R.id.title_bar_atta);
		scrHp = (HorizontalScrollView) findViewById(R.id.title_bar_hp);
		btnCost = (Button) findViewById(R.id.btn_tocost);
		btnAtta = (Button) findViewById(R.id.btn_toatta);
		btnHp = (Button) findViewById(R.id.btn_tohp);
		// 多级标题中返回按钮初始化
		findViewById(R.id.title_bar_cost_back).setOnClickListener(onClick);
		findViewById(R.id.title_bar_atta_back).setOnClickListener(onClick);
		findViewById(R.id.title_bar_hp_back).setOnClickListener(onClick);
		// TODO 多级标题栏主页面的点击事件
		btnCost.setOnClickListener(onClick);
		btnAtta.setOnClickListener(onClick);
		btnHp.setOnClickListener(onClick);
		btnCost.setOnLongClickListener(onLongClick);
		btnAtta.setOnLongClickListener(onLongClick);
		btnHp.setOnLongClickListener(onLongClick);
		// 费用,攻击,血量等查询条件的长点击事件
		costRl = (RelativeLayout)findViewById(R.id.rl_cost);
		findViewById(R.id.btn_toatta).setVisibility(View.GONE);
		findViewById(R.id.btn_tohp).setVisibility(View.GONE);
		// 多级标题栏导航中的RadioButton点击事件
		((RadioGroup) findViewById(R.id.rg_cost))
				.setOnCheckedChangeListener(costChecked);
		((RadioGroup) findViewById(R.id.rg_atta))
				.setOnCheckedChangeListener(attaChecked);
		((RadioGroup) findViewById(R.id.rg_hp))
				.setOnCheckedChangeListener(hpChecked);

		safeAnimator = new SafeAnimator();
	}
	
	// TODO 初始化花费,攻击,血量弹出层
	private void initPopCost(){
		final View costView = getLayoutInflater().inflate(R.layout.pop_cost,
				null, false);

		// 创建PopupWindow实例
		popCost = new PopupWindow(costView, btnCost.getWidth(),
				btnCost.getHeight()*3+5);
		// 设置动画效果 [R.style.AnimationFade 是事先定义好的]
		popCost.setAnimationStyle(R.style.AnimationFade);
		// 使其聚集
		popCost.setFocusable(true);
		// 设置允许在外点击消失
		popCost.setOutsideTouchable(true);
		popCost.setBackgroundDrawable(new BitmapDrawable());
		// RadioButton赋值
		((RadioButton) costView.findViewById(R.id.rg_tocost_cost)).setText(btnCost.getText());
		((RadioButton) costView.findViewById(R.id.rg_tocost_atta)).setText(btnAtta.getText());
		((RadioButton) costView.findViewById(R.id.rg_tocost_hp)).setText(btnHp.getText());
		// RadioButton点击事件
		((RadioGroup) costView.findViewById(R.id.rg_tocost))
				.setOnCheckedChangeListener(searchChecked);
		// 显示位置
		popCost.showAsDropDown(costRl, 0, 0);
	}

	/**
	 * 初始化左侧导航栏
	 * 
	 * @Title initListview
	 * @Description
	 */
	@SuppressLint("NewApi")
	public void initListview() {
		// 初始化左侧导航栏
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// 设置一个定制的影子覆盖打开抽屉时的主要内容
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		btnLeftView = (Button) findViewById(R.id.btn_left);
		// 按钮按下，将抽屉打开
		btnLeftView.setOnClickListener(onClick);
		leftView = (ListView) findViewById(R.id.left_drawer);
		leftView.setOverScrollMode(View.OVER_SCROLL_NEVER);

		leftMenuList = new ArrayList<HsLeftMenu>();
		HsLeftMenu left1 = new HsLeftMenu();
		HsLeftMenu left2 = new HsLeftMenu();
		HsLeftMenu left3 = new HsLeftMenu();
		HsLeftMenu left4 = new HsLeftMenu();
		left1.setName("中立");
		left1.setCount(200);
		left1.setIsShow(false);
		left2.setName("牧师");
		left2.setCount(20);
		left2.setIsShow(false);
		left3.setName("法师");
		left3.setCount(20);
		left3.setIsShow(false);
		left4.setName("萨满");
		left4.setCount(20);
		left4.setIsShow(false);
		leftMenuList.add(left1);
		leftMenuList.add(left2);
		leftMenuList.add(left3);
		leftMenuList.add(left4);
		setLeftItemList(leftMenuList);
	}

	/**
	 * 设置左侧导航条的List
	 * 
	 * @Title setLeftItemList
	 * @Description
	 * @param leftlist
	 */
	private void setLeftItemList(List<HsLeftMenu> leftlist) {
		// 设置左侧导航条的List
		leftAdapter = new LeftMenuAdapter(leftlist, this);
		leftView.setAdapter(leftAdapter);
		leftView.setOnItemClickListener(leftItemClick);
	}

	/**
	 * 底部导航初始化
	 * 
	 * @Title initMenu
	 * @Description RadioGroup初始化,为Menu
	 */
	private void initMenu() {
		// 底部导航初始化
		rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
		rg_menu.setOnCheckedChangeListener(changeMenu);
	}

	/**
	 * 底部导航的页卡初始化
	 * 
	 * @Title initViewPager
	 * @Description 初始化ViewPager页卡
	 */
	private void initViewPager() {
		// 底部导航的页卡初始化
		viewPager = (ViewPager) findViewById(R.id.navi_view_pager);
		views = new ArrayList<View>();

		Intent intent = new Intent(context, CardTool.class);
		Intent intent2 = new Intent(context, CardGroup.class);
		views.add(getView("CardTool", intent));
		views.add(getView("CardGroup", intent2));
		viewPager.setAdapter(new MyPagerAdapter(views));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(0);
	}

	/**
	 * 左侧导航栏监听事件
	 */
	private OnItemClickListener leftItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			HsLeftMenu leftMenu = leftMenuList.get(position);
			String occName = leftMenu.getName();
			leftAdapter.setSelectRadioButton(position);
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			Toast.makeText(context, "item" + occName, Toast.LENGTH_SHORT)
					.show();
		}
	};

	private OnCheckedChangeListener searchChecked = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rg_precise:
				searchDetailLl.setVisibility(View.VISIBLE);
				searchFuzzyLl.setVisibility(View.GONE);
				currentSearch = 0;
				break;
			case R.id.rg_fuzzy:
				searchFuzzyLl.setVisibility(View.VISIBLE);
				searchDetailLl.setVisibility(View.GONE);
				currentSearch = 1;
				break;
			// TODO 稀有度查询
			case R.id.rg_level_all:
				openPopLevel.setText("稀有度");
				openPopLevel.setBackgroundResource(R.drawable.btn_level);
				closePopLevel();
				break;
			case R.id.rg_level_0:
				openPopLevel.setText("基本");
				openPopLevel.setBackgroundResource(R.drawable.btn_level);
				closePopLevel();
				break;
			case R.id.rg_level_1:
				openPopLevel.setText("普通");
				openPopLevel.setBackgroundResource(R.drawable.btn_level_nomal);
				closePopLevel();
				break;
			case R.id.rg_level_2:
				openPopLevel.setText("精良");
				openPopLevel.setBackgroundResource(R.drawable.btn_level_excellent);
				closePopLevel();
				break;
			case R.id.rg_level_3:
				openPopLevel.setText("史诗");
				openPopLevel.setBackgroundResource(R.drawable.btn_level_epic);
				closePopLevel();
				break;
			case R.id.rg_level_4:
				openPopLevel.setText("传说");
				openPopLevel.setBackgroundResource(R.drawable.btn_level_rumorous);
				closePopLevel();
				break;
			case R.id.rg_tocost_cost:
				btnCost.setVisibility(View.VISIBLE);
				btnAtta.setVisibility(View.GONE);
				btnHp.setVisibility(View.GONE);
				closePop(popCost);
				break;
			case R.id.rg_tocost_atta:
				btnCost.setVisibility(View.GONE);
				btnAtta.setVisibility(View.VISIBLE);
				btnHp.setVisibility(View.GONE);
				closePop(popCost);
				break;
			case R.id.rg_tocost_hp:
				btnCost.setVisibility(View.GONE);
				btnAtta.setVisibility(View.GONE);
				btnHp.setVisibility(View.VISIBLE);
				closePop(popCost);
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 关闭PopCost弹出层
	 * @Title clossPopCost
	 * @Description TODO
	 */
	private void clossPopCost(){
		if (popCost != null && popCost.isShowing()) {
			popCost.dismiss();
			popCost = null;
		}
	}
	private void closePop(PopupWindow pop){
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			pop = null;
		}
	}
	/**
	 * 关闭PopLevel弹出层
	 * @Title closePopLevel
	 * @Description 
	 */
	private void closePopLevel(){
		if (popLevel != null && popLevel.isShowing()) {
			popLevel.dismiss();
			popLevel = null;
		}
	}
	/**
	 * 条件搜索弹出层中卡牌类型checkbox的点击事件
	 */
	public android.widget.CompoundButton.OnCheckedChangeListener checkType = new android.widget.CompoundButton.OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// 判断tempTypeBox中是否存在此CompoundButton,0:没有,1:有
			int isHave = isHaveCheck(buttonView);
			if (isChecked) {
				if (searchTemp == null) {
					searchTemp = new HsCard();
				}
				// 如果存在 ,则改变CompoundButton的状态
				if (isHave == 1) {
					for (CheckBox cb : tempTypeBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(true);
						}
					}
				} else {// 如果不存在,将此CompoundButton添加进tempTypeBox
					tempTypeBox.add((CheckBox) buttonView);
				}
				// 获取查询条件
				setTempSearch("type");

			} else {
				if (isHave == 1) {
					for (CheckBox cb : tempTypeBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(false);
						}
					}
				}
				// 获取查询条件
				setTempSearch("type");
			}
		}

	};

	/**
	 * 条件搜索弹出层中卡牌技能checkbox的点击事件
	 */
	private android.widget.CompoundButton.OnCheckedChangeListener checkSkill = new android.widget.CompoundButton.OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// 判断tempTypeBox中是否存在此CompoundButton,0:没有,1:有
			int isHave = isHaveCheck(buttonView);
			if (isChecked) {
				if (searchTemp == null) {
					searchTemp = new HsCard();
				}
				// 如果存在 ,则改变CompoundButton的状态
				if (isHave == 1) {
					for (CheckBox cb : tempSkillBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(true);
						}
					}
				} else {// 如果不存在,将此CompoundButton添加进tempTypeBox
					tempSkillBox.add((CheckBox) buttonView);
				}
				// 获取查询条件
				setTempSearch("skill");

			} else {
				if (isHave == 1) {
					for (CheckBox cb : tempSkillBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(false);
						}
					}
				}
				// 获取查询条件
				setTempSearch("skill");
			}
		}

	};

	/**
	 * 条件搜索弹出层中卡牌种族checkbox的点击事件
	 */
	private android.widget.CompoundButton.OnCheckedChangeListener checkRace = new android.widget.CompoundButton.OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// 判断tempTypeBox中是否存在此CompoundButton,0:没有,1:有
			int isHave = isHaveCheck(buttonView);
			if (isChecked) {
				if (searchTemp == null) {
					searchTemp = new HsCard();
				}
				// 如果存在 ,则改变CompoundButton的状态
				if (isHave == 1) {
					for (CheckBox cb : tempRaceBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(true);
						}
					}
				} else {// 如果不存在,将此CompoundButton添加进tempTypeBox
					tempRaceBox.add((CheckBox) buttonView);
				}
				// 获取查询条件
				setTempSearch("race");

			} else {
				if (isHave == 1) {
					for (CheckBox cb : tempRaceBox) {
						if (cb.getId() == buttonView.getId()) {
							cb.setChecked(false);
						}
					}
				}
				// 获取查询条件
				setTempSearch("race");
			}
		}

	};

	/**
	 * 将搜索条件弹出层中所选择的条件,生成searchTemp的属性,即查询条件
	 * 
	 * @Title setTempSearch
	 * @Description 
	 * @param list
	 * @param listType
	 */
	private void setTempSearch(String listType) {
		if (listType.equals("type")) {
			StringBuffer typeTemp = new StringBuffer();
			typeTemp.append("");
			int tPoint = 0;
			for (CheckBox cb : tempTypeBox) {
				if (cb.isChecked()) {
					if (tPoint == 0) {
						typeTemp.append(cb.getText());
						tPoint = 1;
					} else {
						typeTemp.append("," + cb.getText());
					}
				}
			}
			searchTemp.setType(typeTemp.toString());
		} else if (listType.equals("race")) {
			StringBuffer raceTemp = new StringBuffer();
			raceTemp.append("");
			int tPoint = 0;
			for (CheckBox cb : tempRaceBox) {
				if (cb.isChecked()) {
					if (tPoint == 0) {
						raceTemp.append(cb.getText());
						tPoint = 1;
					} else {
						raceTemp.append("," + cb.getText());
					}
				}
			}
			searchTemp.setRace(raceTemp.toString());
		} else if (listType.equals("skill")) {
			StringBuffer skillTemp = new StringBuffer();
			skillTemp.append("");
			int tPoint = 0;
			for (CheckBox cb : tempSkillBox) {
				if (cb.isChecked()) {
					if (tPoint == 0) {
						skillTemp.append(cb.getText());
						tPoint = 1;
					} else {
						skillTemp.append("," + cb.getText());
					}
				}
			}
			searchTemp.setSkill(skillTemp.toString());
		}
	}

	/**
	 * 判断触发事件的CheckBox是否已经存在于用于生成条件的CheckBox List
	 * 
	 * @Title isHaveCheck
	 * @Description 
	 * @param buttonView
	 * @return
	 */
	private int isHaveCheck(CompoundButton buttonView) {
		if (tempTypeBox != null) {
			for (CheckBox cb : tempTypeBox) {
				if (cb.getId() == buttonView.getId()) {
					return 1;
				}
			}
		}
		if (tempRaceBox != null) {
			for (CheckBox cb : tempRaceBox) {
				if (cb.getId() == buttonView.getId()) {
					return 1;
				}
			}
		}
		if (tempSkillBox != null) {
			for (CheckBox cb : tempSkillBox) {
				if (cb.getId() == buttonView.getId()) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 监听底部导航点击事件
	 */
	private OnCheckedChangeListener changeMenu = new OnCheckedChangeListener() {
		// 监听底部导航点击事件
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int cur = VP_INDEX_CT;
			switch (checkedId) {
			case R.id.btn_cardtool:
				cur = VP_INDEX_CT;
				currentIndex = 0;
				break;
			case R.id.btn_cardgroup:
				cur = VP_INDEX_CG;
				currentIndex = 1;
				break;
			}
			if (viewPager.getCurrentItem() != cur) {
				viewPager.setCurrentItem(cur);
			}
		}
	};

	/**
	 * 监听ViewPage滑动事件
	 * 
	 * @classname MyOnPageChangeListener
	 * @description
	 * @author By Chestnut(lishq)
	 * @date Jan 4, 2014 7:00:31 PM
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 页面滑动后，更改当前页面的序号currentIndex，并更改RadioGroup的选中状态
		 */
		@Override
		public void onPageSelected(int arg0) {
			// 页面滑动后，更改当前页面的序号currentIndex，并更改RadioGroup的选中状态
			switch (arg0) {
			case 0:
				rg_menu.check(R.id.btn_cardtool);
				break;
			case 1:
				rg_menu.check(R.id.btn_cardgroup);
				break;
			}
			currentIndex = arg0;
		}

	}

	private void toScrCost() {
		if (TopBarMode.Cost != topBarMode) {
			topBarMode = TopBarMode.Cost;
			barSwitcher.setDisplayedChild(topBarMode.ordinal());
			safeAnimator.startVisibleAnimator(this, scrCost,
					R.anim.slide_top_in);
			safeAnimator.startInvisibleAnimator(this, scrMain,
					R.anim.slide_bottom_out);
		}
	}

	private void toScrAtta() {
		if (TopBarMode.Atta != topBarMode) {
			topBarMode = TopBarMode.Atta;
			barSwitcher.setDisplayedChild(topBarMode.ordinal());
			safeAnimator.startVisibleAnimator(this, scrAtta,
					R.anim.slide_top_in);
			safeAnimator.startInvisibleAnimator(this, scrMain,
					R.anim.slide_bottom_out);
		}
	}

	private void toScrHp() {
		if (TopBarMode.Hp != topBarMode) {
			topBarMode = TopBarMode.Hp;
			barSwitcher.setDisplayedChild(topBarMode.ordinal());
			safeAnimator.startVisibleAnimator(this, scrHp, R.anim.slide_top_in);
			safeAnimator.startInvisibleAnimator(this, scrMain,
					R.anim.slide_bottom_out);
		}
	}

	/**
	 * 返回主标题栏
	 * 
	 * @Title backToMain
	 * @Description
	 * @param mode
	 */
	private void backToMain(TopBarMode mode) {
		// 返回主标题栏
		if (TopBarMode.Main != topBarMode) {
			topBarMode = TopBarMode.Main;
			barSwitcher.setDisplayedChild(topBarMode.ordinal());
			if (TopBarMode.Cost == mode) {
				safeAnimator.startVisibleAnimator(this, scrMain,
						R.anim.slide_bottom_in);
				safeAnimator.startInvisibleAnimator(this, scrCost,
						R.anim.slide_top_out);
			} else if (TopBarMode.Atta == mode) {
				safeAnimator.startVisibleAnimator(this, scrMain,
						R.anim.slide_bottom_in);
				safeAnimator.startInvisibleAnimator(this, scrAtta,
						R.anim.slide_top_out);
			} else if (TopBarMode.Hp == mode) {
				safeAnimator.startVisibleAnimator(this, scrMain,
						R.anim.slide_bottom_in);
				safeAnimator.startInvisibleAnimator(this, scrHp,
						R.anim.slide_top_out);
			} else {
				safeAnimator.startVisibleAnimator(this, scrMain,
						android.R.anim.fade_in);
			}
		}
	}

	/**
	 * 多级标题栏导航中的RadioButton点击事件
	 */
	private OnCheckedChangeListener costChecked = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// 多级标题栏导航中的RadioButton点击事件
			switch (checkedId) {
			case R.id.title_bar_cost_all:
				btnCost.setText("");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_0:
				btnCost.setText("0");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_1:
				btnCost.setText("1");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_2:
				btnCost.setText("2");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_3:
				btnCost.setText("3");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_4:
				btnCost.setText("4");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_5:
				btnCost.setText("5");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_6:
				btnCost.setText("6");
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_cost_7:
				btnCost.setText("7+");
				backToMain(TopBarMode.Cost);
				break;
			}
		}
	};
	/**
	 * 多级标题栏导航中的RadioButton点击事件
	 */
	private OnCheckedChangeListener attaChecked = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// 多级标题栏导航中的RadioButton点击事件
			switch (checkedId) {
			case R.id.title_bar_atta_all:
				btnAtta.setText("");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_0:
				btnAtta.setText("0");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_1:
				btnAtta.setText("1");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_2:
				btnAtta.setText("2");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_3:
				btnAtta.setText("3");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_4:
				btnAtta.setText("4");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_5:
				btnAtta.setText("5");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_6:
				btnAtta.setText("6");
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_atta_7:
				btnAtta.setText("7+");
				backToMain(TopBarMode.Atta);
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 多级标题栏导航中的RadioButton点击事件
	 */
	private OnCheckedChangeListener hpChecked = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// 多级标题栏导航中的RadioButton点击事件
			switch (checkedId) {
			case R.id.title_bar_hp_all:
				btnHp.setText("");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_1:
				btnHp.setText("1");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_2:
				btnHp.setText("2");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_3:
				btnHp.setText("3");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_4:
				btnHp.setText("4");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_5:
				btnHp.setText("5");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_6:
				btnHp.setText("6");
				backToMain(TopBarMode.Hp);
				break;
			case R.id.title_bar_hp_7:
				btnHp.setText("7+");
				backToMain(TopBarMode.Hp);
				break;

			default:
				break;
			}
		}
	};

	private View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.jumptoview:
				Intent intent = new Intent(Welcome.this, Viewpager.class);
				intent.putExtra("normalName", "Alexstrasza");
				intent.putExtra("goldenName", "g-Alexstrasza");
				startActivity(intent);
				break;
			// TODO 按钮按下,将抽屉打开,(初始化)
			case R.id.btn_left:

				mDrawerLayout.openDrawer(Gravity.LEFT);
				break;
			// 多级标题栏导航中的返回事件
			case R.id.title_bar_cost_back:
				backToMain(TopBarMode.Cost);
				break;
			case R.id.title_bar_atta_back:
				backToMain(TopBarMode.Atta);
				break;
			case R.id.title_bar_hp_back:
				backToMain(TopBarMode.Hp);
				break;
			// 多级标题栏主页面的点击事件
			case R.id.btn_tocost:
				toScrCost();
				break;
			case R.id.btn_toatta:
				toScrAtta();
				break;
			case R.id.btn_tohp:
				toScrHp();
				break;
			case R.id.btn_openLevel:
				if (popLevel != null && popLevel.isShowing()) {
					popLevel.dismiss();
					return;
				} else {
					initPopLevel();
				}
				break;
			// 卡牌搜索弹出层初始化
			case R.id.btn_openSearch:
				if (popSearch != null && popSearch.isShowing()) {
					popSearch.dismiss();
					return;
				} else {
					initPopSearch();
				}
				break;
			// Search Pop弹出层右上角关闭按钮,关闭事件
			case R.id.btn_search_close:
				if (popSearch != null && popSearch.isShowing()) {
					popSearch.dismiss();
					popSearch = null;
				}
				break;
			case R.id.btn_search:
				// TODO 模糊查询
				searchFuzzy.getText();
				if (popSearch != null && popSearch.isShowing()) {
					popSearch.dismiss();
					popSearch = null;
				}
				break;
			default:
				break;
			}
		}

	};
	private OnLongClickListener onLongClick = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
				if (popCost != null && popCost.isShowing()) {
					popCost.dismiss();
					return false;
				} else {
					initPopCost();
				}
			return false;
		}
	};

	/**
	 * @Title getView
	 * @Description 通过activity获取视图
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}
}
