package com.wistron.StreamHelper;

public class JPEGExif {
	
	public String mArtist              = ""; //作者
	public String mTitle               = ""; //标题
	public String mComment             = ""; //注释
	public String mVersion             = "0220"; //Exif 信息版本
	public String mCameraMake          = ""; //DC 制造商
	public String mCameraModel         = ""; //DC 型号
	public String mCameraSoftware      = ""; //JPEG 创建软件
	public String mDateTime            = ""; //JPEG 文件创建日期
	public String mDateTimeDigitized   = ""; //JPEG 文件修改日期
	public int    mImageWidth          = -1;  //图片宽度
	public int    mImageHeight         = -1;  //图片高度
	public int    mOrientation         =  1;  //拍摄方向
	public int    mFlashUsed           = -1;  //闪光灯模式
	public float  mXResolution         = -1f; //水平解析度
	public float  mYResolution         = -1f; //垂直解析度
	public float  mFocalLength         = -1f; //焦距
	public float  mExposureTime        = -1f; //曝光时间
	public float  mApertureFNumber     = -1f; //光圈数
	public float  mExposureBias        = -1f; //曝光补偿
	public float  mBrightness          = -1f; //亮度
	public int    mWhiteBalance        = -1;  //白平衡
	public float  mLatitude[]          = {-1f,-1f,-1f}; //纬度
	public float  mLongitude[]         = {-1f,-1f,-1f}; //经度
}
