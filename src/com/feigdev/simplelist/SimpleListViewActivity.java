package com.feigdev.simplelist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
	private int typeId;
	private final String [] list1 = {"list 1 item 1","list 1 item 1","list 1 item 2","list 1 item 3","list 1 item 4"};
	private final String [] list2 = {"list 2 item 1","list 2 item 1","list 2 item 2","list 2 item 3","list 2 item 4"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listAdapter = new ListAdapter();
        
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        
        TabSpec spec1=tabHost.newTabSpec("list 1");
        spec1.setContent(R.id.list1_view);
        TextView tv1 = new TextView(this);
        tv1.setText("list 1");
        tv1.setGravity(Gravity.CENTER);
        spec1.setIndicator(tv1);
        
        TabSpec spec2=tabHost.newTabSpec("list 2");
        spec2.setContent(R.id.list2_view);
        TextView tv2 = new TextView(this);
        tv2.setText("list 2");
        tv2.setGravity(Gravity.CENTER);
        spec2.setIndicator(tv2);
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        
        listViewRebuild(0);
        listViewRebuild(1);
    }
    
	private void toastText(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
    
	Handler handler = new Handler();
	
	private void listViewRebuild(final int typeId){
		if (listAdapter.getAdapter(typeId) == null){
			switch (typeId){
			case 0:
				listAdapter.setListView((ListView)findViewById(R.id.list1_view),typeId);
				break;
			case 1:
				listAdapter.setListView((ListView)findViewById(R.id.list2_view),typeId);
				break;
			}
			listAdapter.setAdapter(new MessageAdapter(SimpleListViewActivity.this, typeId),typeId);
			listAdapter.getListView(typeId).setAdapter(listAdapter.getAdapter(typeId));
			listAdapter.getListView(typeId).setClickable(true);
			listAdapter.getListView(typeId).setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        	  @Override
	        	  public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
	        		  handler.post(new Runnable(){
	        				public void run(){
	        					switch (typeId){
	        					case 0:
	        						toastText(list1[position]);
	        						break;
	        					case 1:
	        						toastText(list2[position]);
	        						break;
	        					}
	        				}
	        			});
	        	  }
	        	});
		}
		else {
			listAdapter.getAdapter(typeId).notifyDataSetChanged();
			listAdapter.getAdapter(typeId).notifyDataSetInvalidated();
			listAdapter.getListView(typeId).invalidateViews();
		}
	}
	
	private class ListAdapter {
		private MessageAdapter list1Adapter; 
		private MessageAdapter list2Adapter; 
		private ListView list1View;
		private ListView list2View;
		
		public ListAdapter(){
			list1Adapter = null;
			list2Adapter = null;
			list1View = null;
			list2View = null;
		}
		
		public MessageAdapter getAdapter(int typeId){
			switch (typeId){
			case 0:
				return list1Adapter;
			case 1:
				return list2Adapter;
			}
			return null;
		}
		public void setAdapter(MessageAdapter a, int typeId){
			switch (typeId){
			case 0:
				list1Adapter = a;
				break;
			case 1:
				list2Adapter = a;
				break;
			}
		}
		public ListView getListView(int typeId){
			switch (typeId){
			case 0:
				return list1View;
			case 1:
				return list1View;
			}
			return null;
		}
		public void setListView(ListView l, int typeId){
			switch (typeId){
			case 0:
				list1View = l;
				break;
			case 1:
				list1View = l;
				break;
			}
		}
		
	}
    
	private class MessageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		protected MessageViewHolder messageHolder;
		private int messageType;
		
		public MessageAdapter(Context context, int messageType) {
			mInflater = LayoutInflater.from(context);
			this.messageType = messageType;
		}
		
		@Override
		public int getCount() {
			switch (messageType){
			case 0:
				return list1.length;
			case 1:
				return list2.length;
			}
			return 0;
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
			
			messageHolder = new MessageViewHolder();
			messageHolder.name = (TextView) convertView.findViewById(R.id.item_name);
			
			convertView.setTag(messageHolder);
			
			switch (messageType){
			case 0:
				messageHolder.name.setText(list1[position]);
				break;
			case 1:
				messageHolder.name.setText(list2[position]);
				break;
			}
			
			return convertView;
		}

	}

	
	protected class MessageViewHolder {
		TextView name;
	}
}