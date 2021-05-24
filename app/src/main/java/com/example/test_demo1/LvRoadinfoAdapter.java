package com.example.test_demo1;


/*
* adapter 适配器（历史数据中的每一条）
* */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LvRoadinfoAdapter extends BaseAdapter {
    private Context context;    //上下文信息---操作的源对象
    private List<Roadinfo> roadinfoList;    //用户信息数据集合
    private static final String TAG = "LvRoadinfoAdapter";



    public LvRoadinfoAdapter(Context context, List<Roadinfo> roadinfoList) {
        Log.d(TAG,"LvRoadinfoAdapter");
        this.context = context;
        this.roadinfoList = roadinfoList;
    }

    public LvRoadinfoAdapter() {
    }

    //放数据
    public void setRoadinfoList(List<Roadinfo> roadinfoList) {
        this.roadinfoList = roadinfoList;
    }

    @Override
    public int getCount() {
        return roadinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return roadinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if(convertView == null){
            //设置显示格式

            Log.d(TAG,"convertView ==null");

            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item, null);
            viewHolder = new ViewHolder();  //实例化

            viewHolder.tv_id = convertView.findViewById(R.id.tv_list_id);
            viewHolder.tv_fengsu = convertView.findViewById(R.id.tv_list_fengsu);
            viewHolder.tv_yanwu = convertView.findViewById(R.id.tv_list_yanwu);
            viewHolder.tv_yudi = convertView.findViewById(R.id.tv_list_yudi);
            viewHolder.tv_createDt = convertView.findViewById(R.id.tv_list_createDt);

            convertView.setTag(viewHolder);
        }else{

            Log.d(TAG,"convertView !=null");
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //进行数据填充
        Log.d(TAG,"data_load");
        Roadinfo item = roadinfoList.get(position);
        viewHolder.tv_id.setText("设备"+item.getId()+".");
        viewHolder.tv_fengsu.setText("风速："+item.getFengsu());
        viewHolder.tv_yudi.setText("降雨情况："+item.getJiangyu());
        viewHolder.tv_yanwu.setText("烟雾浓度："+item.getYanwu());
        viewHolder.tv_createDt.setText("数据采集时间为："+item.getCreateDt());

        Log.d(TAG,"lvRoadinfoAdapter_load_over"+convertView);

        return convertView;
    }


    private class ViewHolder{
        private TextView tv_id,tv_fengsu, tv_yudi, tv_yanwu, tv_createDt;
    }


}
