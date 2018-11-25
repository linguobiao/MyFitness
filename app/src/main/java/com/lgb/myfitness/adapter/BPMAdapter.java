package com.lgb.myfitness.adapter;

import java.util.List;

import com.lgb.myfitness.been.BPM;
import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.CalculateHelper;
import com.lgb.myfitness.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BPMAdapter extends BaseAdapter{

	private Context mContext;
	private List<BPM> bpmList;
	
	public BPMAdapter(Context context, List<BPM> bpmList) {
		
		mContext = context;
		this.bpmList = bpmList;
	}
	
	@Override
	public int getCount() {
		if (bpmList != null) {
			return bpmList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (bpmList != null) {
			return bpmList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 按当前所需的样式，确定new的布局
			convertView = inflater.inflate(R.layout.item_bmp, parent, false);
			
			holder.image_bpm_low = convertView.findViewById(R.id.image_bpm_low);
//			holder.image_bpm_opti = convertView.findViewById(R.id.image_bpm_opti);
//			holder.image_bpm_norm = convertView.findViewById(R.id.image_bpm_norm);
			holder.image_bpm_mild = convertView.findViewById(R.id.image_bpm_mild);
			holder.image_bpm_midd = convertView.findViewById(R.id.image_bpm_midd);
//			holder.image_bpm_high = convertView.findViewById(R.id.image_bpm_high);
			
			holder.text_bpm = (TextView) convertView.findViewById(R.id.text_bpm);
			
			holder.text_value_low = (TextView) convertView.findViewById(R.id.text_low_value);
//			holder.text_value_opti = (TextView) convertView.findViewById(R.id.text_opti_value);
//			holder.text_value_norm = (TextView) convertView.findViewById(R.id.text_norm_value);
			holder.text_value_mild = (TextView) convertView.findViewById(R.id.text_mild_value);
			holder.text_value_midd = (TextView) convertView.findViewById(R.id.text_midd_value);
//			holder.text_value_high = (TextView) convertView.findViewById(R.id.text_high_value);
			
			holder.text_date_low = (TextView) convertView.findViewById(R.id.text_low_date);
//			holder.text_date_opti = (TextView) convertView.findViewById(R.id.text_opti_date);
//			holder.text_date_norm = (TextView) convertView.findViewById(R.id.text_norm_date);
			holder.text_date_mild = (TextView) convertView.findViewById(R.id.text_mild_date);
			holder.text_date_midd = (TextView) convertView.findViewById(R.id.text_midd_date);
//			holder.text_date_high = (TextView) convertView.findViewById(R.id.text_high_date);

			holder.tv_bpm_low = (TextView) convertView.findViewById(R.id.tv_bpm_low);
			holder.tv_bpm_mild = (TextView) convertView.findViewById(R.id.tv_bpm_mild);
			holder.tv_bpm_midd = (TextView) convertView.findViewById(R.id.tv_bpm_midd);

			 convertView.setTag(holder);
			 
		} else {
			holder = (ViewHolder) convertView.getTag();  
		}
		
		if (bpmList != null && bpmList.size() > position) {
			BPM bpm = bpmList.get(position);
			
			int type = CalculateHelper.getBPMLevel(bpm.getSystolic(), bpm.getDiatolic());
			int color = CalculateHelper.getBPMLevelColor(type);
			String level = CalculateHelper.buildBPMLevelText(type);

			holder.image_bpm_low.setVisibility(color == Global.TYPE_BPM_LEVEL_BLUE ? View.VISIBLE : View.INVISIBLE);
			holder.image_bpm_mild.setVisibility(color == Global.TYPE_BPM_LEVEL_ORANGE ? View.VISIBLE : View.INVISIBLE);
			holder.image_bpm_midd.setVisibility(color == Global.TYPE_BPM_LEVEL_RED ? View.VISIBLE : View.INVISIBLE);

			holder.text_value_low.setVisibility(color == Global.TYPE_BPM_LEVEL_BLUE ? View.VISIBLE : View.INVISIBLE);
			holder.text_value_mild.setVisibility(color == Global.TYPE_BPM_LEVEL_ORANGE ? View.VISIBLE : View.INVISIBLE);
			holder.text_value_midd.setVisibility(color == Global.TYPE_BPM_LEVEL_RED ? View.VISIBLE : View.INVISIBLE);

			holder.text_date_low.setVisibility(color == Global.TYPE_BPM_LEVEL_BLUE ? View.VISIBLE : View.INVISIBLE);
			holder.text_date_mild.setVisibility(color == Global.TYPE_BPM_LEVEL_ORANGE ? View.VISIBLE : View.INVISIBLE);
			holder.text_date_midd.setVisibility(color == Global.TYPE_BPM_LEVEL_RED ? View.VISIBLE : View.INVISIBLE);

			holder.text_value_low.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
			holder.text_date_low.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
			holder.tv_bpm_low.setText(level);

			holder.text_value_mild.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
			holder.text_date_mild.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
			holder.tv_bpm_mild.setText(level);

			holder.text_value_midd.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
			holder.text_date_midd.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
			holder.tv_bpm_midd.setText(level);

//			holder.image_bpm_low.setVisibility(type == Global.TYPE_BPM_LEVEL_1 ? View.VISIBLE : View.INVISIBLE);
//			holder.image_bpm_opti.setVisibility(type == Global.TYPE_BPM_LEVEL_2 ? View.VISIBLE : View.INVISIBLE);
//			holder.image_bpm_norm.setVisibility(type == Global.TYPE_BPM_LEVEL_3 ? View.VISIBLE : View.INVISIBLE);
//			holder.image_bpm_mild.setVisibility(type == Global.TYPE_BPM_LEVEL_4 ? View.VISIBLE : View.INVISIBLE);
//			holder.image_bpm_midd.setVisibility(type == Global.TYPE_BPM_LEVEL_5 ? View.VISIBLE : View.INVISIBLE);
//			holder.image_bpm_high.setVisibility(type == Global.TYPE_BPM_LEVEL_6 ? View.VISIBLE : View.INVISIBLE);
//
//			holder.text_value_low.setVisibility(type == Global.TYPE_BPM_LEVEL_1 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_value_opti.setVisibility(type == Global.TYPE_BPM_LEVEL_2 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_value_norm.setVisibility(type == Global.TYPE_BPM_LEVEL_3 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_value_mild.setVisibility(type == Global.TYPE_BPM_LEVEL_4 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_value_midd.setVisibility(type == Global.TYPE_BPM_LEVEL_5 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_value_high.setVisibility(type == Global.TYPE_BPM_LEVEL_6 ? View.VISIBLE : View.INVISIBLE);
//
//			holder.text_date_low.setVisibility(type == Global.TYPE_BPM_LEVEL_1 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_date_opti.setVisibility(type == Global.TYPE_BPM_LEVEL_2 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_date_norm.setVisibility(type == Global.TYPE_BPM_LEVEL_3 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_date_mild.setVisibility(type == Global.TYPE_BPM_LEVEL_4 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_date_midd.setVisibility(type == Global.TYPE_BPM_LEVEL_5 ? View.VISIBLE : View.INVISIBLE);
//			holder.text_date_high.setVisibility(type == Global.TYPE_BPM_LEVEL_6 ? View.VISIBLE : View.INVISIBLE);
//
//			holder.text_value_low.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_low.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
//
//			holder.text_value_opti.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_opti.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
//
//			holder.text_value_norm.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_norm.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
//
//			holder.text_value_mild.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_mild.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
//
//			holder.text_value_midd.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_midd.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));
//
//			holder.text_value_high.setText(bpm.getSystolic() + "/" + bpm.getDiatolic() + "mmHg");
//			holder.text_date_high.setText(Global.sdf_4_1.format(bpm.getDatetime().getTime()));

			
			holder.text_bpm.setText(String.valueOf(bpm.getHeartRate()) + mContext.getString(R.string.ppm));
		}
		
		
		return convertView;
	}
	
	
	public class ViewHolder {
		View image_bpm_low;
		View image_bpm_opti;
		View image_bpm_norm;
		View image_bpm_mild;
		View image_bpm_midd;
		View image_bpm_high;
		
		TextView text_bpm;
		
		TextView text_value_low;
		TextView text_value_opti;
		TextView text_value_norm;
		TextView text_value_mild;
		TextView text_value_midd;
		TextView text_value_high;
		
		TextView text_date_low;
		TextView text_date_opti;
		TextView text_date_norm;
		TextView text_date_mild;
		TextView text_date_midd;
		TextView text_date_high;

		TextView tv_bpm_low;
		TextView tv_bpm_mild;
		TextView tv_bpm_midd;
	}

}
