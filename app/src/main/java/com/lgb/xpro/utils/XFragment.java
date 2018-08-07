package com.lgb.xpro.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment管理封装类
 * Created by linguobiao on 16/11/8.
 */
public class XFragment {

	private FragmentManager fMgr;

	private int id_layout_content;

	private List<XFragmentBean> tagList = new ArrayList<>();

	public XFragment(FragmentManager fMgr, int id_layout_content, List<XFragmentBean> tagList) {
		this.fMgr = fMgr;
		this.id_layout_content = id_layout_content;
		this.tagList.addAll(tagList);
	}

	/**
	 * 显示一个fragment
	 * @param tag	需要显示的fragment的标签
	 */
	public boolean showFragment(@NonNull String tag, Bundle bundle) {
		Fragment fragment = fMgr.findFragmentByTag(tag);
		if (fragment != null) {
			showFragment(fragment);
			return true;		// 已经创建过，返回
		}
		try {
			for (XFragmentBean bean : tagList) {
				if (tag.equalsIgnoreCase(bean.getParentTag())) {
					fragment = (Fragment) bean.getParentClass().newInstance();
					break;	// 属于一级fragment，停止遍历
				} else {
					Map<String, Class> sonMap = bean.getFragmentMap();
					if (sonMap != null) {
						for (String sonTag : sonMap.keySet()) {
							if (sonTag.equalsIgnoreCase(tag)) {
								fragment = (Fragment) sonMap.get(sonTag).newInstance();
								fragment.setArguments(bundle);
								break;	// 属于二级fragment，停止遍历
							}
						}
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		hideAllFragment();
		addFragment(fragment, tag);
		return false;
	}

	public void showFragment(@NonNull String tag) {showFragment(tag, null);}

	public void showFragment(@NonNull Class cls, Bundle bundle) {showFragment(cls.getName(), bundle);}

	public void showFragment(@NonNull Class cls) {showFragment(cls.getName(), null);}

	/**
	 * 显示一级fragment
	 * 类似于底部栏点击按键的平级切换
	 * @param mainTag	需要显示的一级fragment的标签
	 */
	public void showMainFragment(@NonNull String mainTag) {
		if (fMgr != null) {
			boolean hasSon = false;		// 用于判断该一级fragment中是否已经有二级
			for (XFragmentBean bean : tagList) {
				if (mainTag.equalsIgnoreCase(bean.getParentTag())) {
					Map<String, Class> sonMap = bean.getFragmentMap();
					if (sonMap != null) {
						for (String sonTag : sonMap.keySet()) {
							hasSon = showSon(sonTag);
							if (hasSon) break;		// 已经找到一个二级fragment，停止遍历
						}
					}
				}
			}

			if (!hasSon) {
				showFragment(mainTag);		// 没有二级fragemnt，直接显示一级fragment
			}
		}
	}

	public void showMainFragment(@NonNull Class cls) {showMainFragment(cls.getName());}

	/**
	 * 返回一级fragment
	 * 从二级fragment返回一级fragment
	 * @param mainTag
	 */
	public void returnMainFragment(@NonNull String mainTag) {

		for (XFragmentBean bean : tagList) {
			if (mainTag.equalsIgnoreCase(bean.getParentTag())) {
				Map<String, Class> sonTagList = bean.getFragmentMap();
				if (sonTagList != null) {
					for (String sonTag : sonTagList.keySet()) {
						removeFragment(sonTag);		// remove该一级fragment下的所有二级fragment
					}
				}
				break;
			}
		}

		showFragment(mainTag);		// 显示一级fragment
	}

	public void returnMainFragment(@NonNull Class cls) {returnMainFragment(cls.getName());}

	public boolean isVisible(@NonNull String tag) {
		Fragment fragment = fMgr.findFragmentByTag(tag);
		if (fragment != null) {
			return fragment.isVisible();
		}
		return false;
	}

	public boolean isVisible(@NonNull Class cls) {return isVisible(cls.getName());}

	/**
	 * 显示二级fragment
	 * @param tag	需要显示的二级fragment的标签
	 * @return
	 */
	private boolean showSon(String tag) {
		Fragment fragment = fMgr.findFragmentByTag(tag);
		if (fragment != null) {
			showFragment(fragment);
			return true;
		}
		return false;
	}

	/**
	 * 隐藏所有fragment
	 */
	private void hideAllFragment() {

		FragmentTransaction ft = fMgr.beginTransaction();

		for (XFragmentBean bean : tagList) {
			Fragment fragment = fMgr.findFragmentByTag(bean.getParentTag());
			if (fragment != null) {
				ft.hide(fragment);
			}
			Map<String, Class> sonTagList = bean.getFragmentMap();
			if (sonTagList != null) {
				for (String sonTag : sonTagList.keySet()) {
					Fragment fragmentSon = fMgr.findFragmentByTag(sonTag);
					if (fragmentSon != null) {
						ft.hide(fragmentSon);
					}
				}
			}
		}

		ft.commitAllowingStateLoss();
	}

	/**
	 * 添加fragment
	 * 
	 * @param fragment
	 * @param fragmentTag
	 */
	private void addFragment(Fragment fragment, String fragmentTag) {

		if (fMgr != null) {
			FragmentTransaction ft = fMgr.beginTransaction();
			if (fragment != null) {
				ft.add(id_layout_content, fragment, fragmentTag);
				ft.commitAllowingStateLoss();
			}
		}
	}

	/**
	 * 移除一个fragment
	 * 
	 * @param tag
	 */
	public void removeFragment(@NonNull String tag) {

		Fragment fragment = fMgr.findFragmentByTag(tag);
		if (fragment != null) {
			FragmentTransaction ft = fMgr.beginTransaction();

			ft.remove(fragment);
			ft.commitAllowingStateLoss();
		}
	}

	public void removeFragment(@NonNull Class cls) {removeFragment(cls.getName());}

	/**
	 * 显示fragment
	 * 
	 * @param fragment
	 */
	private void showFragment(Fragment fragment) {
		hideAllFragment();

		FragmentTransaction ft = fMgr.beginTransaction();
		if (fragment != null) {
			ft.show(fragment);
			ft.commitAllowingStateLoss();
		}
	}

	public static class XFragmentBean {
		private Class parentClass;
		private String parentTag;

		private Map<String, Class> fragmentMap;

		public XFragmentBean(@NonNull Class parentClass, @NonNull String parentTag) {
			this.parentClass = parentClass;
			this.parentTag = parentTag;
			fragmentMap = new HashMap<>();
		}

		public XFragmentBean(@NonNull Class parentClass) {
			this.parentClass = parentClass;
			this.parentTag = parentClass.getName();
			fragmentMap = new HashMap<>();
		}

		public void add(@NonNull Class sonCls, @NonNull String sonTag) {
			fragmentMap.put(sonTag, sonCls);
		}
		public void add(@NonNull Class sonCls) {
			fragmentMap.put(sonCls.getName(), sonCls);
		}

		public Class getParentClass() {
			return this.parentClass;
		}
		public String getParentTag() {
			return this.parentTag;
		}

		public Map<String, Class> getFragmentMap() {
			return fragmentMap;
		}

		public void setFragmentMap(Map<String, Class> fragmentMap) {
			this.fragmentMap = fragmentMap;
		}
	}

}
