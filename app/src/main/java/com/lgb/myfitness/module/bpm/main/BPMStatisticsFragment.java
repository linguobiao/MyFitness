package com.lgb.myfitness.module.bpm.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lgb.mvp.SimpleBaseFragment;
import com.lgb.myfitness.R;
import com.lgb.myfitness.adapter.BPMAdapter;
import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.database.DatabaseProvider_bpm;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.helper.ChartHelper;
import com.lgb.myfitness.helper.ProfileHelper;
import com.lgb.myfitness.helper.ShowValueHelper;
import com.lgb.myfitness.module.bpm.BPMFragment;
import com.lgb.myfitness.module.bpm.BPMFragmentManager;
import com.lgb.myfitness.view.BPMLineValue;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class BPMStatisticsFragment extends SimpleBaseFragment{

	@BindView(R.id.button_back) ImageView button_back;
	@BindView(R.id.layout_chart_pressure) RelativeLayout layout_chart_pressure;
	@BindView(R.id.chart_pressure_linevalue) BPMLineValue lineValue_bpm;
	@BindView(R.id.list_value) ListView list_value;
	private BPMAdapter adapter_bpm;

	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	
	private int profileID;
	
	private List<BPM> bpm_desc;
	private List<BPM> bpm_asc;
	
	/**
	 * 起始点坐标值
	 */
	private float[] xArray ;

	@Override
	public void initView() {
		button_back.setOnClickListener(view -> BPMFragmentManager.getInstance().returnMainFragment(BPMFragment.class));
		list_value.setOnItemClickListener(myOnItemClickListener);
		list_value.setOnItemLongClickListener(myOnItemLongClickListener);

		profileID = ProfileHelper.initProfileID(getActivity());

		showChart();
		showList();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_bpm_home_statistics;
	}
	
	
	private OnTouchListener myOnTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				break;
			case MotionEvent.ACTION_MOVE:
				int xEvent = (int) event.getX();
				float x = ShowValueHelper.getLineXValue(xEvent, xArray);
				int index = ShowValueHelper.getXValueIndex(xEvent, xArray);
				
				Log.i(getTag(), xEvent + ", " + x + ", " + index);
				
				if (bpm_asc != null && bpm_asc.size() > index) {
					BPM bpm = bpm_asc.get(index);
					if (bpm != null) {
						int type = CalculateHelper.getBPMLevel(bpm.getSystolic(), bpm.getDiatolic());
						String value = null;
						String xLabel = null;
						String xLabel2 = null;

						value = CalculateHelper.buildBPMLevelText(type);
						
						String datetime = Global.sdf_4_1.format(bpm.getDatetime().getTime());
						String[] array_datetime = datetime.split(" ");
						xLabel = array_datetime[0];
						xLabel2 = array_datetime[1];
						
						lineValue_bpm.setDrawData(x, value, xLabel, xLabel2);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				lineValue_bpm.setDrawData(0, "", "", "");
				break;	
			}
			return true;
		}
	};
	
	private OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (bpm_desc != null && bpm_desc.size() > position) {
				BPM bpm = bpm_desc.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Global.KEY_BPM, bpm);
				BPMFragmentManager.getInstance().showFragment(BPMStatisticsDetailFragment.class, bundle);
			}
		}
	};

	private OnItemLongClickListener myOnItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if (bpm_desc != null && bpm_desc.size() > position) {
				BPM bpm = bpm_desc.get(position);
				
				showDeleteDialog(bpm);
			}
			return true;
		}
	};

	private void showChart() {
//		bpm_asc = DatabaseProvider_bpm.queryBPM_asc(getActivity(), profileID, 20);
		bpm_asc = DatabaseProvider_bpm.queryBPM_30Day_asc(getActivity(), profileID);
		
		//设置小图和大图数据
		mDataset = ChartHelper.setDataset_chart_bpm(getActivity(), bpm_asc, bpm_asc.size());
		mRenderer = ChartHelper.setRenderer_chart_bpm(getActivity(), bpm_asc.size());
		
		//设置大图样式	
		layout_chart_pressure.removeAllViews();
		mChartView = ChartFactory.getCubeLineChartView(getActivity(), mDataset,mRenderer, ChartHelper.SMOTHNESS);
		mChartView.setOnTouchListener(myOnTouchListener);
		
		layout_chart_pressure.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		

		xArray = ShowValueHelper.calculateXArray(getActivity(), bpm_asc.size());
	}
	
	
	private void showList() {
		bpm_desc = DatabaseProvider_bpm.queryBPM_desc(getActivity(), profileID, 20);
//		for (BPM bpm : bpm_desc) {
//			System.out.println(Global.sdf_4.format(bpm.getDatetime().getTime()) + ", " + bpm.getSystolic() + ", " + bpm.getDiatolic() + ", " + bpm.getHeartRate());
//		}
		
		adapter_bpm = new BPMAdapter(getActivity(), bpm_desc);
		list_value.setAdapter(adapter_bpm);
		
		adapter_bpm.notifyDataSetChanged();
	}
	
	
	private void showDeleteDialog(final BPM bpm) {
		final AlertDialog dialog_delete = new AlertDialog.Builder(getActivity()).create();
		 
		dialog_delete.setTitle(getString(R.string.Notice));
		dialog_delete.setMessage(getString(R.string.Delete_bpm));
		
		dialog_delete.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.Commit), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (bpm != null) {
					Calendar cal = bpm.getDatetime();
					if (cal != null) {
						DatabaseProvider_bpm.deleteBPM(getActivity(), profileID, cal);
					
						showChart();
						showList();
					}
				}
				
			}
		});
		dialog_delete.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog_delete.dismiss();
				
			}
		});
		
		dialog_delete.show();
	}
}
