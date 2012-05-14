package com.feigdev.simplelist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class SimpleListViewActivity extends Activity {
	private ListAdapter listAdapter;
	private static final String TAG = "SimpleListViewActivity";
	private final String [][] list = {{"list 1 item 0","list 1 item 1","list 1 item 2","list 1 item 3","list 1 item 4"},
									  {"list 2 item 0","list 2 item 1","list 2 item 2","list 2 item 3","list 2 item 4"}};
	private int [] listViewArray = {R.id.list1_view, R.id.list2_view};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listAdapter = new ListAdapter();
        
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        
        TabSpec spec1=buildTabSpec(tabHost,"list 1",0);
        TabSpec spec2=buildTabSpec(tabHost,"list 2",1);
       
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        
        listViewBuild(0);
        listViewBuild(1);
    }
    
	private void toastText(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
    
	Handler handler = new Handler();

	private TabSpec buildTabSpec(TabHost tabHost, String name, int typeId){
		if (null == tabHost){
			Log.e(TAG,"tabHost is null");
			return null;
		}
		if (-1 >= typeId || typeId >= listViewArray.length){
			Log.e(TAG,"typeId is outside the bounds");
			return null;
		}
        TabSpec spec = tabHost.newTabSpec(name);
        spec.setContent(listViewArray[typeId]);
        TextView tv = new TextView(this);
        tv.setText(name);
        tv.setGravity(Gravity.CENTER);
        spec.setIndicator(tv);
        return spec;
    }
	
	private void listViewBuild(final int typeId){
		if (-1 >= typeId || typeId >= listViewArray.length){
			Log.e(TAG,"typeId is outside the bounds");
			return;
		}
		if (null == listAdapter.getAdapter(typeId)){
			listAdapter.setListView((ListView)findViewById(listViewArray[typeId]),typeId);
			listAdapter.setAdapter(new MessageAdapter(SimpleListViewActivity.this, typeId),typeId);
			
			if (null == listAdapter.getListView(typeId) || null == listAdapter.getAdapter(typeId)){
				Log.e(TAG,"Something went wrong while trying to create the ListView");
				return;
			}
			listAdapter.getListView(typeId).setAdapter(listAdapter.getAdapter(typeId));
			listAdapter.getListView(typeId).setClickable(true);
			listAdapter.getListView(typeId).setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	        	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
					handler.post(new Runnable(){
						public void run(){
							toastText(list[typeId][position]);
	        			}
					});
	        	}
	        });
		}
	}
	
	private class ListAdapter {
		private MessageAdapter [] listAdapters = {null, null}; 
		private ListView [] listViews = {null, null};
				
		public MessageAdapter getAdapter(int typeId){
			if (-1 < typeId && typeId < listAdapters.length){
				return listAdapters[typeId];
			}
			Log.e(TAG,"typeId is outside the bounds");
			return null;
		}
		public void setAdapter(MessageAdapter a, int typeId){
			if (-1 < typeId && typeId < listAdapters.length){
				listAdapters[typeId] = a;
			}
			else {
				Log.e(TAG,"typeId is outside the bounds");
			}
		}
		public ListView getListView(int typeId){
			if (-1 < typeId && typeId < listViews.length){
				return listViews[typeId];
			}
			Log.e(TAG,"typeId is outside the bounds");
			return null;
		}
		public void setListView(ListView l, int typeId){
			if (-1 < typeId && typeId < listViews.length){
				listViews[typeId] = l;
			}
			else {
				Log.e(TAG,"typeId is outside the bounds");
			}
		}
		
	}
    
	private class MessageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		protected TextView messageHolder;
		private int messageType;
		
		public MessageAdapter(Context context, int messageType) {
			mInflater = LayoutInflater.from(context);
			this.messageType = messageType;
		}
		
		@Override
		public int getCount() {
			return list[messageType].length;
		}
		 
		@Override
		public Object getItem(int position) {
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			messageHolder = (TextView) convertView.findViewById(R.id.item_name);
			convertView.setTag(messageHolder);
			messageHolder.setText(list[messageType][position]);
			return convertView;
		}

	}

}