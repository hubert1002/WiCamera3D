package com.wistron.WiViewer;


import com.wistron.swpc.wicamera3dii.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

class VideoFrameAdapter  extends BaseAdapter
{
	Bitmap[]  mBitmaps;
	int count;
	Context m_context;
	 private LayoutInflater inflater ;  
	boolean isBase;
	public void resetBitmap()
	{
		if(mBitmaps!=null)
		{
			for (int i = 0; i < mBitmaps.length; i++)
			{
				if(mBitmaps[i]!=null&&!mBitmaps[i].isRecycled())
				{
					mBitmaps[i].recycle();
					mBitmaps[i]=null;
				}
			}
		}
		notifyDataSetChanged();
	}
 public VideoFrameAdapter(Context context, int num,boolean isBase )
   {
	 m_context=context;
	 this.mBitmaps=new Bitmap[num];
	 this.count=num;
	 this.isBase=isBase;
	 inflater=LayoutInflater.from(context);
    }
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		ImageView imageView = new ImageView(m_context);
		if (convertView == null)
		{
			if (isBase)
			{
				convertView = inflater.inflate(R.layout.grid_item_base, null);
			} else
			{
				convertView = inflater.inflate(R.layout.grid_item_sub, null);
			}
			if (position == 0)
			{
				LinearLayout layout = (LinearLayout) convertView
						.findViewById(R.id.grid_linearlayout);
				layout.setPadding(1, 1, 1, 1);
			}
			imageView = (ImageView) convertView.findViewById(R.id.item_image);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			convertView.setTag(imageView);

		} else
		{
			imageView = (ImageView) convertView.getTag();
		}
		if (mBitmaps[position] != null)
		{
			imageView.setImageBitmap(mBitmaps[position]);
		}
		return convertView;
		
//      ImageView mImageView;
//		if (convertView == null)
//		{
//			mImageView=new ImageView(m_context);
//			mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			if(isBase)
//			{
//				mImageView.setLayoutParams(new GridView.LayoutParams(83, 53));
//			}
//			else
//			{
//				mImageView.setLayoutParams(new GridView.LayoutParams(72, 48));
//			}
//		}else {
//			mImageView=(ImageView) convertView;
//		}
//		if(mBitmaps[position]!=null)
//		{
//			mImageView.setImageBitmap(mBitmaps[position]);
//		}
//		return mImageView;
		
//        ImageView mImageView;
//		if (convertView == null)
//		{
//			mImageView=new ImageView(m_context);
//			mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			if(isBase)
//			{
//				mImageView.setLayoutParams(new GridView.LayoutParams(83, 53));
//			}
//			else
//			{
//				mImageView.setLayoutParams(new GridView.LayoutParams(72, 48));
//			}
//		}else {
//			mImageView=(ImageView) convertView;
//		}
//		if(mBitmaps[position]!=null)
//		{
//			mImageView.setImageBitmap(mBitmaps[position]);
//		}
//		return mImageView;
	}}