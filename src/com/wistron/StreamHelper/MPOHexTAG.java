package com.wistron.StreamHelper;

/**
 * JPEG&EXIF HEX标签
 * @author WH1107028 Cocoonshu
 * @date   2012-02-01 11:42:44
 */
public class MPOHexTAG {
	public    final static int    LITTLE_ENDIAN     = 0x49492A00; //小字节序:GO TO HELL!!!
	public    final static int    BIG_ENDIAN        = 0x4D4D002A; //大字节序
	
	protected final static byte[] TAG_SOI           = {(byte)0xFF, (byte)0xD8}; //JPEG标志：图像头
	protected final static byte[] TAG_APP0          = {(byte)0xFF, (byte)0xE0}; //JPEG标志：应用程序保留位 0
	protected final static byte[] TAG_APP1          = {(byte)0xFF, (byte)0xE1}; //JPEG标志：应用程序保留位 1
	protected final static byte[] TAG_APP2          = {(byte)0xFF, (byte)0xE2}; //JPEG标志：应用程序保留位 2
	protected final static byte[] TAG_APP3          = {(byte)0xFF, (byte)0xE3}; //JPEG标志：应用程序保留位 3
	protected final static byte[] TAG_APP4          = {(byte)0xFF, (byte)0xE4}; //JPEG标志：应用程序保留位 4
	protected final static byte[] TAG_APP5          = {(byte)0xFF, (byte)0xE5}; //JPEG标志：应用程序保留位 5
	protected final static byte[] TAG_APP6          = {(byte)0xFF, (byte)0xE6}; //JPEG标志：应用程序保留位 6
	protected final static byte[] TAG_APP7          = {(byte)0xFF, (byte)0xE7}; //JPEG标志：应用程序保留位 7
	protected final static byte[] TAG_APP8          = {(byte)0xFF, (byte)0xE8}; //JPEG标志：应用程序保留位 8
	protected final static byte[] TAG_APP9          = {(byte)0xFF, (byte)0xE9}; //JPEG标志：应用程序保留位 9
	protected final static byte[] TAG_APP10         = {(byte)0xFF, (byte)0xEA}; //JPEG标志：应用程序保留位 10
	protected final static byte[] TAG_APP11         = {(byte)0xFF, (byte)0xEB}; //JPEG标志：应用程序保留位 11
	protected final static byte[] TAG_APP12         = {(byte)0xFF, (byte)0xEC}; //JPEG标志：应用程序保留位 12
	protected final static byte[] TAG_APP13         = {(byte)0xFF, (byte)0xED}; //JPEG标志：应用程序保留位 13
	protected final static byte[] TAG_APP14         = {(byte)0xFF, (byte)0xEE}; //JPEG标志：应用程序保留位 14
	protected final static byte[] TAG_APP15         = {(byte)0xFF, (byte)0xFF}; //JPEG标志：应用程序保留位 15
	protected final static byte[] TAG_DQT           = {(byte)0xFF, (byte)0xDB}; //JPEG标志：定义量化表
	protected final static byte[] TAG_SOF0          = {(byte)0xFF, (byte)0xC0}; //JPEG标志：帧头
	protected final static byte[] TAG_DHT           = {(byte)0xFF, (byte)0xC4}; //JPEG标志：定义HUFFMAN表
	protected final static byte[] TAG_DRI           = {0}; //JPEG标志：定义重新开始间隔
	protected final static byte[] TAG_SOS           = {(byte)0xFF, (byte)0xDA}; //JPEG标志：扫描行头
	protected final static byte[] TAG_EOI           = {(byte)0xFF, (byte)0xD9}; //JPEG标志：图像尾
	protected final static byte[] TAG_MPFVER        = {(byte)0xB0, (byte)0x00}; //MPO 标志：MP Format Version 
	protected final static byte[] TAG_MPIMGCOUNT    = {(byte)0xB0, (byte)0x01}; //MPO 标志：MP Number of Image
	protected final static byte[] TAG_MPENTRY       = {(byte)0xB0, (byte)0x02}; //MPO 标志：MP Entry
	protected final static byte[] TAG_MPUIDLIST     = {(byte)0xB0, (byte)0x03}; //MPO 标志：MP Individual Image Unique ID list
	protected final static byte[] TAG_MPTOTALFRAMES = {(byte)0xB0, (byte)0x04}; //MPO 标志：MP Total Number of Captured Frames
	protected final static byte[] TAG_MPIMGNUM      = {(byte)0xB1, (byte)0x01}; //MPO 标志：MP Individual Image Number
	protected final static byte[] TAG_PANORIEN      = {(byte)0xB2, (byte)0x01}; //MPO 标志：Panorama Scanning Orientation
	protected final static byte[] TAG_PANHOZOLP     = {(byte)0xB2, (byte)0x02}; //MPO 标志：Panorama Horizontal Overlap
	protected final static byte[] TAG_PANVEROLP     = {(byte)0xB2, (byte)0x03}; //MPO 标志：Panorama Vertical Overlap
	protected final static byte[] TAG_BASEVPNUM     = {(byte)0xB2, (byte)0x04}; //MPO 标志：Base Viewpoint Number
	protected final static byte[] TAG_CONANGLE      = {(byte)0xB2, (byte)0x05}; //MPO 标志：Convergence Angle
	protected final static byte[] TAG_BASELINELEN   = {(byte)0xB2, (byte)0x06}; //MPO 标志：Baseline Length
	protected final static byte[] TAG_DIVANGLE      = {(byte)0xB2, (byte)0x07}; //MPO 标志：Divergence Angle
	protected final static byte[] TAG_HORAXISDIS    = {(byte)0xB2, (byte)0x08}; //MPO 标志：Horizontal Axis Distance
	protected final static byte[] TAG_VERAXISDIS    = {(byte)0xB2, (byte)0x09}; //MPO 标志：Vertical Axis Distance
	protected final static byte[] TAG_COLAXISDIS    = {(byte)0xB2, (byte)0x09}; //MPO 标志：Collimation Axis Distance
	protected final static byte[] TAG_YAWANGLE      = {(byte)0xB2, (byte)0x09}; //MPO 标志：Yaw Angle
	protected final static byte[] TAG_PITCHANGLE    = {(byte)0xB2, (byte)0x09}; //MPO 标志：Pitch Angle
	protected final static byte[] TAG_ROLLANGLE     = {(byte)0xB2, (byte)0x09}; //MPO 标志：Roll Angle
	
	protected final static byte[] TAG_BIG_ORDER     = {(byte)0x49, (byte)0x49, (byte)0x2A, (byte)0x00}; //JPEG标记：大字节序
	protected final static byte[] TAG_LITTLE_ORDER  = {(byte)0x4D, (byte)0x4D, (byte)0x00, (byte)0x2A}; //JPEG标记：小字节序
	
	protected final static int    TAG_BYTE          = 0x0001; //Exif标志：数据类型BYTE      - An 8-bit unsigned integer
	protected final static int    TAG_ASCII         = 0x0002; //Exif标志：数据类型ASCII     - An 8-bit byte containing one 7-bit ASCII codethe final byte is terminated with NULL
	protected final static int    TAG_SHORT         = 0x0003; //Exif标志：数据类型SHORT     - A 16-bit unsigned integer
	protected final static int    TAG_LONG          = 0x0004; //Exif标志：数据类型LONG      - A 32-bit unsigned integer
	protected final static int    TAG_RATIONAL      = 0x0005; //Exif标志：数据类型RATIONAL  - 2 LONGs. 1st LONG is the numerator and 2rd LONG expresses the denominator
	protected final static int    TAG_SBYTE         = 0x0006; //Exif标志：数据类型SBYTE     - An 8-bit signed (twos-complement) integer
	protected final static int    TAG_UNDEFINED     = 0x0007; //Exif标志：数据类型UNDEFINED - An 8-bit byte that can take any value depending on the field definition
	protected final static int    TAG_SSHORT        = 0x0008; //Exif标志：数据类型SSHORT    - A 16-bit (2-byte) signed (twos-complement) integer
	protected final static int    TAG_SLONG         = 0x0009; //Exif标志：数据类型SLONG     - A 32-bit signed integer
	protected final static int    TAG_SRATIONAL     = 0x000A; //Exif标志：数据类型SRATIONAL - 2 SLONGs. 1st SLONG is the numerator and 2rd SLONG is the denominator	
	protected final static int    TAG_FLOAT         = 0x000B; //Exif标志：数据类型FLOAT     - Single precision (4-byte) IEEE format
	protected final static int    TAG_DOUBLE        = 0x000C; //Exif标志：数据类型DOUBLE　　 - Double precision (8-byte) IEEE format
	
	protected final static byte[] TAG_GPSLATREF     = {(byte)0x00, (byte)0x01}; //Exif标志：图片信息GPSLatitudeRef
	protected final static byte[] TAG_GPSLAT        = {(byte)0x00, (byte)0x02}; //Exif标志：图片信息GPSLatitude
	protected final static byte[] TAG_GPSLONREF     = {(byte)0x00, (byte)0x03}; //Exif标志：图片信息GPSLongitudeRef
	protected final static byte[] TAG_GPSLON        = {(byte)0x00, (byte)0x04}; //Exif标志：图片信息GPSLongitude
	protected final static byte[] TAG_GPSALTREF     = {(byte)0x00, (byte)0x05}; //Exif标志：图片信息GPSAltitudeRef
	protected final static byte[] TAG_GPSALT        = {(byte)0x00, (byte)0x06}; //Exif标志：图片信息GPSAltitude
	protected final static byte[] TAG_GPSTIME       = {(byte)0x00, (byte)0x07}; //Exif标志：图片信息GPSTimeStamp
	protected final static byte[] TAG_GPSSATELLITES = {(byte)0x00, (byte)0x08}; //Exif标志：图片信息GPSSatellites
	protected final static byte[] TAG_GPSSTATUS     = {(byte)0x00, (byte)0x09}; //Exif标志：图片信息GPSStatus
	protected final static byte[] TAG_GPSSPEEDREF   = {(byte)0x00, (byte)0x0C}; //Exif标志：图片信息GPSSpeedRef
	protected final static byte[] TAG_GPSSPEED      = {(byte)0x00, (byte)0x0D}; //Exif标志：图片信息GPSSpeed
	protected final static byte[] TAG_GPSTRACKREF   = {(byte)0x00, (byte)0x0E}; //Exif标志：图片信息GPSTrackRef
	protected final static byte[] TAG_GPSTRACK      = {(byte)0x00, (byte)0x0F}; //Exif标志：图片信息GPSTrack
	protected final static byte[] TAG_GPSDATE       = {(byte)0x00, (byte)0x1D}; //Exif标志：图片信息GPSDateStamp
	protected final static byte[] TAG_IMAGEWIDTH    = {(byte)0x01, (byte)0x00}; //Exif标志：图片信息ImageWidth
	protected final static byte[] TAG_IMAGEHEIGHT   = {(byte)0x01, (byte)0x01}; //Exif标志：图片信息ImageHeight
	protected final static byte[] TAG_BITSPERSAMPLE = {(byte)0x01, (byte)0x02}; //Exif标志：图片信息BitsPerSample
	protected final static byte[] TAG_COMPRESSION   = {(byte)0x01, (byte)0x03}; //Exif标志：图片信息Compression
	protected final static byte[] TAG_PIP           = {(byte)0x01, (byte)0x06}; //Exif标志：图片信息PhotometricInterpretation
	protected final static byte[] TAG_MAKE          = {(byte)0x01, (byte)0x0F}; //Exif标志：图片信息Make
	protected final static byte[] TAG_IDP           = {(byte)0x01, (byte)0x0E}; //Exif标志：图片信息ImageDescription
	protected final static byte[] TAG_MODEL         = {(byte)0x01, (byte)0x10}; //Exif标志：图片信息Model
	protected final static byte[] TAG_STRIPOFFSET   = {(byte)0x01, (byte)0x11}; //Exif标志：图片信息StripOffsets
	protected final static byte[] TAG_ORIENTATION   = {(byte)0x01, (byte)0x12}; //Exif标志：图片信息Orientation
	protected final static byte[] TAG_SAMPLEPERPIX  = {(byte)0x01, (byte)0x15}; //Exif标志：图片信息SamplesPerPixel
	protected final static byte[] TAG_ROWSPERSTRIP  = {(byte)0x01, (byte)0x16}; //Exif标志：图片信息RowsPerStrip
	protected final static byte[] TAG_STRIPBYTELEN  = {(byte)0x01, (byte)0x17}; //Exif标志：图片信息StripByteCounts
	protected final static byte[] TAG_XRES          = {(byte)0x01, (byte)0x1A}; //Exif标志：图片信息XResolution
	protected final static byte[] TAG_YRES          = {(byte)0x01, (byte)0x1B}; //Exif标志：图片信息YResolution
	protected final static byte[] TAG_PLANARCONFIG  = {(byte)0x01, (byte)0x1C}; //Exif标志：图片信息PlanarConfiguration
	protected final static byte[] TAG_RES_UNIT      = {(byte)0x01, (byte)0x28}; //Exif标志：图片信息ResolutionUnit
	protected final static byte[] TAG_SOFTWARE      = {(byte)0x01, (byte)0x31}; //Exif标志：图片信息Software
	protected final static byte[] TAG_DATETIME      = {(byte)0x01, (byte)0x32}; //Exif标志：图片信息DateTime
	protected final static byte[] TAG_ARTIST        = {(byte)0x01, (byte)0x3B}; //Exif标志：图片信息Artist
	protected final static byte[] TAG_HOSTCOMPUTER  = {(byte)0x01, (byte)0x3C}; //Exif标志：图片信息HostComputer
	protected final static byte[] TAG_ICGFORMAT     = {(byte)0x02, (byte)0x01}; //Exif标志：图片信息JPEGInterchangeFormat
	protected final static byte[] TAG_ICGFORMATLEN  = {(byte)0x02, (byte)0x02}; //Exif标志：图片信息JPEGInterchangeFormatLength
	protected final static byte[] TAG_YCBCRPOS      = {(byte)0x02, (byte)0x13}; //Exif标志：图片信息YCbCrPositioning
	protected final static byte[] TAG_EXIF_IFD_PTR  = {(byte)0x69, (byte)0x87}; //Exif标志：图片信息Exif IFD Pointer
	protected final static byte[] TAG_EXPTIME       = {(byte)0x82, (byte)0x9A}; //Exif标志：图片信息ExposureTime
	protected final static byte[] TAG_FNUMBER       = {(byte)0x82, (byte)0x9D}; //Exif标志：图片信息ImageDescription
	protected final static byte[] TAG_GPS_IFD_PTR   = {(byte)0x88, (byte)0x25}; //Exif标志：图片信息GPS IFD Pointer
	protected final static byte[] TAG_ISOSPEED      = {(byte)0x88, (byte)0x27}; //Exif标志：图片信息ISOSpeedRatings
	protected final static byte[] TAG_EXIFVER       = {(byte)0x90, (byte)0x00}; //Exif标志：图片信息ExifVersion
	protected final static byte[] TAG_DATETIMEORG   = {(byte)0x90, (byte)0x03}; //Exif标志：图片信息DateTimeOriginal
	protected final static byte[] TAG_DATETIMEDIG   = {(byte)0x90, (byte)0x04}; //Exif标志：图片信息DateTimeDigitized
	protected final static byte[] TAG_CPTCONFIG     = {(byte)0x91, (byte)0x01}; //Exif标志：图片信息ComponentsConfiguration
	protected final static byte[] TAG_CPSBITSPERPIX = {(byte)0x91, (byte)0x02}; //Exif标志：图片信息CompressedBitsPerPixel
	protected final static byte[] TAG_SHUTTERSPEED  = {(byte)0x92, (byte)0x01}; //Exif标志：图片信息ShutterSpeedValue
	protected final static byte[] TAG_APERTURE      = {(byte)0x92, (byte)0x02}; //Exif标志：图片信息ApertureValue
	protected final static byte[] TAG_BRIGHTNESS    = {(byte)0x92, (byte)0x03}; //Exif标志：图片信息BrightnessValue
	protected final static byte[] TAG_EXPOSUREBIAS  = {(byte)0x92, (byte)0x04}; //Exif标志：图片信息ExposureBiasValue
	protected final static byte[] TAG_MAXAPERRATIO  = {(byte)0x92, (byte)0x05}; //Exif标志：图片信息MaxApertureRatioValue
	protected final static byte[] TAG_SUBDISTANCE   = {(byte)0x92, (byte)0x06}; //Exif标志：图片信息SubjectDistance
	protected final static byte[] TAG_METERMODE     = {(byte)0x92, (byte)0x07}; //Exif标志：图片信息MeteringMode
	protected final static byte[] TAG_LIGHTSOURCE   = {(byte)0x92, (byte)0x08}; //Exif标志：图片信息LightSource
	protected final static byte[] TAG_FLASH         = {(byte)0x92, (byte)0x09}; //Exif标志：图片信息Flash
	protected final static byte[] TAG_FOCALLENGTH   = {(byte)0x92, (byte)0x0A}; //Exif标志：图片信息FocalLength
	protected final static byte[] TAG_USERCOMMENTS  = {(byte)0x92, (byte)0x86}; //Exif标志：图片信息UserComments
	protected final static byte[] TAG_SUBSECTIME    = {(byte)0x92, (byte)0x90}; //Exif标志：图片信息SubSecTime
	protected final static byte[] TAG_SUBSECTIMEORG = {(byte)0x92, (byte)0x91}; //Exif标志：图片信息SubSecTimeOriginal
	protected final static byte[] TAG_SUBSECTIMEDIG = {(byte)0x92, (byte)0x92}; //Exif标志：图片信息SubSecTimeDigitized
	protected final static byte[] TAG_COPYRIGHT     = {(byte)0x98, (byte)0x82}; //Exif标志：图片信息CopyRight
	protected final static byte[] TAG_FLASHPIXVER   = {(byte)0xA0, (byte)0x00}; //Exif标志：图片信息FlashPixVersion
	protected final static byte[] TAG_COLORSPACE    = {(byte)0xA0, (byte)0x01}; //Exif标志：图片信息SubSecTime
	protected final static byte[] TAG_PIXXDIM       = {(byte)0xA0, (byte)0x02}; //Exif标志：图片信息Pixel X Dimension
	protected final static byte[] TAG_PIXYDIM       = {(byte)0xA0, (byte)0x03}; //Exif标志：图片信息Pixel Y Dimension
	protected final static byte[] TAG_ITP_IFD_PTR   = {(byte)0xA0, (byte)0x05}; //Exif标志：图片信息Interoperability IFD Pointer
	protected final static byte[] TAG_SENSINGMETHOD = {(byte)0xA2, (byte)0x17}; //Exif标志：图片信息Sensing Method
	protected final static byte[] TAG_FILESOURCE    = {(byte)0xA3, (byte)0x00}; //Exif标志：图片信息File Source
	protected final static byte[] TAG_SCENETYPE     = {(byte)0xA3, (byte)0x01}; //Exif标志：图片信息Scene Type
	protected final static byte[] TAG_EXPOSUREMODE  = {(byte)0xA4, (byte)0x02}; //Exif标志：图片信息ExposureMode
	protected final static byte[] TAG_WHITEBALANCE  = {(byte)0xA4, (byte)0x03}; //Exif标志：图片信息WhiteBalance
	protected final static byte[] TAG_SATURATION    = {(byte)0xA4, (byte)0x09}; //Exif标志：图片信息Saturation
	protected final static byte[] TAG_SHARPNESS     = {(byte)0xA4, (byte)0x0A}; //Exif标志：图片信息Sharpness
	
	//GPS 高度
	public    final static int    GPS_ABOVE_SEA_LEVEL                      = 0;
	public    final static int    GPS_BELOW_SEA_LEVEL                      = 1;
	
	//拍摄方向
	public    final static int    ORIENTATION_TOPLEFT                      = 1; //0
	public    final static int    ORIENTATION_TOPRIGHT                     = 2; //0'
	public    final static int    ORIENTATION_BOTRIGHT                     = 3; //180
	public    final static int    ORIENTATION_BOTLEFT                      = 4; //180'
	public    final static int    ORIENTATION_LEFTTOP                      = 5; //270'
	public    final static int    ORIENTATION_RIGHTTOP                     = 6; //270
	public    final static int    ORIENTATION_RIGHTBOT                     = 7; //90'
	public    final static int    ORIENTATION_LEFTBOT                      = 8; //90

	//闪光灯模式 
	public    final static int    FLASH_UNFIRED                            = 0x00; //Flash didn't fire
	public    final static int    FLASH_FIRED                              = 0x01; //Flash fired
	public    final static int    FLASH_UNDETECTED                         = 0x05; //Strobe return light not detected
	public    final static int    FLASH_DETECTED                           = 0x07; //Strobe return light detected
	public    final static int    FLASH_COMPULSORY_FIRED                   = 0x09; //Flash fired, compulsory flash mode
	public    final static int    FLASH_COMPULSORY_FIRED_UNDETECTED        = 0x0D; //Flash fired, compulsory flash mode, return light not detected
	public    final static int    FLASH_COMPULSORY_FIRED_DETECTED          = 0x0F; //Flash fired, compulsory flash mode, return light detected
	public    final static int    FLASH_COMPULSORY_UNFIRED                 = 0x10; //Flash did not fire, compulsory flash mode
	public    final static int    FLASH_AUTO_UNFIRED                       = 0x18; //Flash did not fire, auto mode
	public    final static int    FLASH_AUTO_FIRED                         = 0x19; //Flash fired, auto mode
	public    final static int    FLASH_AUTO_FIRED_UNDETECTED              = 0x1D; //Flash fired, auto mode, return light not detected
	public    final static int    FLASH_AUTO_FIRED_DETECTED                = 0x1F; //Flash fired, auto mode, return light detected
	public    final static int    FLASH_NO_FUNCTION                        = 0x20; //No flash function
	public    final static int    FLASH_REDEYE_FIRED                       = 0x41; //Flash fired, red-eye reduction mode
	public    final static int    FLASH_REDEYE_FIRED_UNDETECTED            = 0x45; //Flash fired, red-eye reduction mode, return light not detected
	public    final static int    FLASH_REDEYE_FIRED_DETECTED              = 0x47; //Flash fired, red-eye reduction mode, return light detected
	public    final static int    FLASH_REDEYE_COMPULSORY_FIRED            = 0x49; //Flash fired, compulsory flash mode, red-eye reduction mode
	public    final static int    FLASH_REDEYE_COMPULSORY_FIRED_UNDETECTED = 0x4D; //Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected
	public    final static int    FLASH_REDEYE_COMPULSORY_FIRED_DETECTED   = 0x4F; //Flash fired, compulsory flash mode, red-eye reduction mode, return light detected
	public    final static int    FLASH_REDEYE_AUTO                        = 0x59; //Flash fired, auto mode, red-eye reduction mode
	public    final static int    FLASH_REDEYE_AUTO_UNDETECTED             = 0x5D; //Flash fired, auto mode, return light not detected, red-eye reduction mode
	public    final static int    FLASH_REDEYE_AUTO_DETECTED               = 0x5F; //Flash fired, auto mode, return light detected, red-eye reduction mode 
	
	//白平衡
	public    final static int    WHITE_BALANCE_AUTO                       = 0; //Auto white balance
	public    final static int    WHITE_BALANCE_MANUAL                     = 1; //Manual white balance 
	
	/**
	 * 2D图片
	 */
	public    final static int    TID_2DIMAGE                              = 0xFFAA; // 自定义标志：2D图片
	/**
	 * 3D图片
	 */
	public    final static int    TID_3DIMAGE                              = 0xFFAB; // 自定义标志：3D图片
	/**
	 * 全景图片
	 */
	public    final static int    TID_PANORAMIC                            = 0xFFAC; // 自定义标志：全景图片
	/**
	 * GPS Version ID
	 */
	public    final static int    TID_GPSVERSIONID                         = 0x0000;// Exif标志：图片信息GPSVersionID
	/**
	 * GPS Latitude Reference 
	 */
	public    final static int    TID_GPSLATREF                            = 0x0001; // Exif标志：图片信息GPSLatitudeRef
	/**
	 * GPS Latitude
	 */
	public    final static int    TID_GPSLAT                               = 0x0002; // Exif标志：图片信息GPSLatitude
	/**
	 * GPS Longitude Reference
	 */
	public    final static int    TID_GPSLONREF                            = 0x0003; // Exif标志：图片信息GPSLongitudeRef
	/**
	 * GPS Longitude
	 */
	public    final static int    TID_GPSLON                               = 0x0004; // Exif标志：图片信息GPSLongitude
	/**
	 * GPS Altitude Reference
	 */
	public    final static int    TID_GPSALTREF                            = 0x0005; // Exif标志：图片信息GPSAltitudeRef
	/**
	 * GPS Altitude
	 */
	public    final static int    TID_GPSALT                               = 0x0006; // Exif标志：图片信息GPSAltitude
	/**
	 * GPS Time Stamp
	 */
	public    final static int    TID_GPSTIME                              = 0x0007; // Exif标志：图片信息GPSTimeStamp
	/**
	 * GPS Satellites
	 */
	public    final static int    TID_GPSSATELLITES                        = 0x0008; // Exif标志：图片信息GPSSatellites
	/**
	 * GPS Status
	 */
	public    final static int    TID_GPSSTATUS                            = 0x0009; // Exif标志：图片信息GPSStatus
	/**
	 * GPS Measure Mode
	 */
	public    final static int    TID_GPSMEASUREMODE                       = 0x000A;// Exif标志：图片信息GPSMEASUREMODE
	/**
	 * Gps Dop
	 */
	public    final static int    TID_GPSDOP                               = 0X000B;// Exif标志：图片信息GPSDOP
	/**
	 * GPSSpeed Reference
	 */
	public    final static int    TID_GPSSPEEDREF                          = 0x000C; // Exif标志：图片信息GPSSpeedRef
	/**
	 * GPS Speed
	 */
	public    final static int    TID_GPSSPEED                             = 0x000D; // Exif标志：图片信息GPSSpeed
	/**
	 * GPTI Dack Reference
	 */
	public    final static int    TID_GPTIDACKREF                          = 0x000E; // Exif标志：图片信息GPTIDackRef
	/**
	 * GPTI Dack
	 */
	public    final static int    TID_GPTIDACK                             = 0x000F; // Exif标志：图片信息GPTIDack
	/**
	 * GPS Image Direction Ref
	 */
	public    final static int    TID_GPSIMGDIRECTIONREF                   = 0X0010;// Exif标志：图片信息GPSImgDirectionRef
	/**
	 * GPS Image Direction
	 */
	public    final static int    TID_GPSIMGDIRECTION                      = 0X0011;// Exif标志：图片信息GPSImgDirection
	/**
	 * GPS Map Datum
	 */
	public    final static int    TID_GPSMAPDATUM                          = 0X0012;// Exif标志：图片信息GPSMapDatum
	/**
	 * GPS DestLatitude Ref
	 */
	public    final static int    TID_GPSDESTLATITUDEREF = 0X0013;// Exif标志：图片信息GPSDestLatitudeRef
	/**
	 * GPS Dest Latitude
	 */
	public    final static int    TID_GPSDESTLATITUDE = 0X0014;// Exif标志：图片信息GPSDestLatitude
	/**
	 * GPS Dest Longitude Ref
	 */
	public    final static int    TID_GPSDESTLONGITUDEREF = 0X0015;// Exif标志：图片信息GPSDestLongitudeRef
	/**
	 * GPS Dest Longitude
	 */
	public    final static int    TID_GPSDESTLONGITUDE = 0X0016;// Exif标志：图片信息GPSDestLongitude
	/**
	 * GPS Dest Bearing Ref
	 */
	public    final static int    TID_GPSDESTBEARINGREF = 0X0017;// Exif标志：图片信息GPSDestBearingRef
	/**
	 * GPS Dest Bearing
	 */
	public    final static int    TID_GPSDESTBEARING = 0X0018;// Exif标志：图片信息GPSDestBearing
	/**
	 * GPS Dest Distance Ref
	 */
	public    final static int    TID_GPSDESTDISTANCEREF = 0X0019;// Exif标志：图片信息GPSDestDistanceRef
	/**
	 * GPS Dest Distance
	 */
	public    final static int    TID_GPSDESTDISTANCE = 0X001A;// Exif标志：图片信息GPSDestDistance
	/**
	 * GPS Processing Method
	 */
	public    final static int    TID_GPSPROCESSINGMETHOD = 0X001B;// Exif标志：图片信息GPSProcessingMethod
	/**
	 * GPS Area Information
	 */
	public    final static int    TID_GPSAREAINFORMATION = 0X001C;// Exif标志：图片信息GPSAreaInformation
	/**
	 * GPS Date Stamp
	 */
	public    final static int    TID_GPSDATE = 0x001D; // Exif标志：图片信息GPSDateStamp
	/**
	 * GPS Differential
	 */
	public    final static int    TID_GPSDIFFERENTIAL = 0x001E; // Exif标志：图片信息GPSDifferential
	/**
	 * New Subfile Type
	 */
	public    final static int    TID_NEWSUBFILETYPE = 0x00FE; // Exif标志：图片信息NewSubfileType
	/**
	 * Subfile Type
	 */
	public    final static int    TID_SUBFILETYPE = 0x00FF; // Exif标志：图片信息Subfile Type
	/**
	 * Image Width
	 */
	public    final static int    TID_IMAGEWIDTH = 0x0100; // Exif标志：图片信息ImageWidth
	/**
	 * Image Height
	 */
	public    final static int    TID_IMAGEHEIGHT = 0x0101; // Exif标志：图片信息ImageHeight
	/**
	 * Bits Per Sample
	 */
	public    final static int    TID_BITSPERSAMPLE = 0x0102; // Exif标志：图片信息BitsPerSample
	/**
	 * Compression
	 */
	public    final static int    TID_COMPRESSION = 0x0103; // Exif标志：图片信息Compression

	/**
	 * Photometric Interpretation
	 */
	public    final static int    TID_PIP = 0x0106; // Exif标志：图片信息PhotometricInterpretation
	/**
	 * Threshholding
	 */
	public    final static int    TID_THRESHHOLDING = 0x0107; // Exif标志：图片信息Threshholding
	/**
	 * CellWidth
	 */
	public    final static int    TID_CellWidth = 0x0108; // Exif标志：图片信息CellWidth
	/**
	 * Cell Length
	 */
	public    final static int    TID_CellLength = 0x0109; // Exif标志：图片信息CellLength
	/**
	 * Fill Order
	 */
	public    final static int    TID_FillOrder = 0x010A; // Exif标志：图片信息FillOrder
	/**
	 *Document Name
	 */
	public    final static int    TID_DOCUMENTNAME = 0x010D; // Exif标志：图片信息DocumentName
	/**
	 * Image Description
	 */
	public    final static int    TID_IMAGEDESCRIPTION = 0x010E; // Exif标志：图片信息ImageDescription
	/**
	 * Make
	 */
	public    final static int    TID_MAKE = 0x010F; // Exif标志：图片信息Make
	/**
	 * Model
	 */
	public    final static int    TID_MODEL = 0x0110; // Exif标志：图片信息Model
	/**
	 * TIDip Offsets
	 */
	public    final static int    TID_TIDIPOFFSET = 0x0111; // Exif标志：图片信息TIDipOffsets
	/**
	 * Orientation
	 */
	public    final static int    TID_ORIENTATION = 0x0112; // Exif标志：图片信息Orientation
	/**
	 * Samples Per Pixel
	 */
	public    final static int    TID_SAMPLEPERPIX = 0x0115; // Exif标志：图片信息SamplesPerPixel
	/**
	 * Rows Per TIDip
	 */
	public    final static int    TID_ROWSPERTIDIP = 0x0116; // Exif标志：图片信息RowsPerTIDip
	/**
	 * TIDip Byte Counts
	 */
	public    final static int    TID_TIDIPBYTELEN = 0x0117; // Exif标志：图片信息TIDipByteCounts
	/**
	 * Min Sample Value
	 */
	public    final static int    TID_MINSAMPLEVALUE = 0x0118; // Exif标志：图片信息MinSampleValue
	/**
	 * Max Sample Value
	 */
	public    final static int    TID_MAXSAMPLEVALUE = 0x0119; // Exif标志：图片信息MaxSampleValue
	/**
	 * X Resolution
	 */
	public    final static int    TID_XRES = 0x011A; // Exif标志：图片信息XResolution
	/**
	 * Y Resolution
	 */
	public    final static int    TID_YRES = 0x011B; // Exif标志：图片信息YResolution
	/**
	 * Planar Configuration
	 */
	public    final static int    TID_PLANARCONFIG = 0x011C; // Exif标志：图片信息PlanarConfiguration
	/**
	 * Page Name
	 */
	public    final static int    TID_PAGENAME = 0x011D; // Exif标志：图片信息PageName
	/**
	 * XPosition
	 */
	public    final static int    TID_XPOSITION = 0x011E; // Exif标志：图片信息XPosition
	/**
	 * YPosition
	 */
	public    final static int    TID_YPOSITION = 0x011F; // Exif标志：图片信息YPosition
	/**
	 * Free Offsets
	 */
	public    final static int    TID_FREEOFFSETS = 0x0120; // Exif标志：图片信息FreeOffsets
	/**
	 * Free Byte Counts
	 */
	public    final static int    TID_FREEBYTECOUNTS = 0x0121; // Exif标志：图片信息FreeByteCounts
	/**
	 * Gray Response Unit
	 */
	public    final static int    TID_GRAYRESPONSEUNIT = 0x0122; // Exif标志：图片信息GrayResponseUnit
	/**
	 * Gray Response Curve
	 */
	public    final static int    TID_GRAYRESPONSECURVE = 0x0123; // Exif标志：图片信息GrayResponseCurve
	/**
	 * T4Options
	 */
	public    final static int    TID_T4OPTIONS = 0x0124; // Exif标志：图片信息T4Options
	/**
	 * T6Options
	 */
	public    final static int    TID_T6OPTIONS = 0x0125; // Exif标志：图片信息T6Options
	/**
	 * Resolution Unit
	 */
	public    final static int    TID_RES_UNIT = 0x0128; // Exif标志：图片信息ResolutionUnit
	/**
	 * Page Number
	 */
	public    final static int    TID_PAGENUMBER = 0x0129; // Exif标志：图片信息PageNumber
	/**
	 * Transfer Function
	 */
	public    final static int    TID_TRANSFERFUNCTION = 0x012D; // Exif标志：图片信息TransferFunction
	/**
	 * Software
	 */
	public    final static int    TID_SOFTWARE = 0x0131; // Exif标志：图片信息Software
	/**
	 * Date Time
	 */
	public    final static int    TID_DATETIME = 0x0132; // Exif标志：图片信息DateTime
	/**
	 * Artist
	 */
	public    final static int    TID_ARTIST = 0x013B; // Exif标志：图片信息Artist
	/**
	 * Host Computer
	 */
	public    final static int    TID_HOSTCOMPUTER = 0x013C; // Exif标志：图片信息HostComputer
	/**
	 *Predictor
	 */
	public    final static int    TID_PREDICTOR = 0x013D; // Exif标志：图片信息Predictor
	/**
	 *White Point
	 */
	public    final static int    TID_WHITEPOINT= 0x013E; // Exif标志：图片信息WhitePoint
	/**
	 *Primary Chromaticities
	 */
	public    final static int    TID_PRIMARYCHROMATICITIES = 0x013F; // Exif标志：图片信息PrimaryChromaticities
	/**
	 * Color Map
	 */
	public    final static int    TID_COLORMAP = 0x0140; // Exif标志：图片信息ColorMap
	/**
	 * Halftone Hints
	 */
	public    final static int    TID_HALFTONEHINTS = 0x0141; // Exif标志：图片信息HalftoneHints
	/**
	 *Tile Width
	 */
	public    final static int    TID_TILEWIDTH = 0x0142; // Exif标志：图片信息TileWidth
	/**
	 * Tile Length
	 */
	public    final static int    TID_TILELENGTH = 0x0143; // Exif标志：图片信息TileLength
	/**
	 * Tile Offsets
	 */
	public    final static int    TID_TILEOFFSETS = 0x0144; // Exif标志：图片信息TileOffsets
	/**
	 * Tile Byte Counts
	 */
	public    final static int    TID_TILEBYTECOUNTS= 0x0145; // Exif标志：图片信息TileByteCounts
	/**
	 *Bad Fax Lines
	 */
	public    final static int    TID_BADFAXLINES = 0x0146; // Exif标志：图片信息BadFaxLines
	/**
	 * Clean Fax Data
	 */
	public    final static int    TID_CLEANFAXDATA = 0x0147; // Exif标志：图片信息CleanFaxData
	/**
	 * Consecutive Bad Fax Lines
	 */
	public    final static int    TID_CONSECUTIVEBADFAXLINES= 0x0148; // Exif标志：图片信息ConsecutiveBadFaxLines
	/**
	 * SubIFDs
	 */
	public    final static int    TID_SUBIFDS = 0x014A; // Exif标志：图片信息SubIFDs
	/**
	 *InkSet
	 */
	public    final static int    TID_INKSET = 0x014C; // Exif标志：图片信息InkSet
	/**
	 *Ink Names
	 */
	public    final static int    TID_INKNAMES = 0x014D; // Exif标志：图片信息InkNames
	/**
	 * Number Of Inks
	 */
	public    final static int    TID_NUMBEROFINKS = 0x014E; // Exif标志：图片信息NumberOfInks
	/**
	 *Dot Range
	 */
	public    final static int    TID_DOTRANGE = 0x0150; // Exif标志：图片信息Dot Range
	/**
	 *Target Printer
	 */
	public    final static int    TID_TARGETPRINTER = 0x0151; // Exif标志：图片信息TargetPrinter
	/**
	 * Extra Samples
	 */
	public    final static int    TID_EXTRASAMPLES = 0x0152; // Exif标志：图片信息ExtraSamples
	/**
	 * Sample Format
	 */
	public    final static int    TID_SAMPLEFORMAT = 0x0153; // Exif标志：图片信息SampleFormat
	/**
	 * SMin Sample Value
	 */
	public    final static int    TID_SMINSAMPLEVALUE = 0x0154; // Exif标志：图片信息SMinSampleValue
	/**
	 * SMax ample Value
	 */
	public    final static int    TID_SMAXSAMPLEVALUE = 0x0155; // Exif标志：图片信息SMaxSampleValue
	/**
	 * Transfer Range
	 */
	public    final static int    TID_TRANSFERRANGE = 0x0156; // Exif标志：图片信息TransferRange
	/**
	 * Clip Path
	 */
	public    final static int    TID_CLIPPATH = 0x0157; // Exif标志：图片信息ClipPath
	/**
	 * XClip Path Units
	 */
	public    final static int    TID_XCLIPPATHUNITS = 0x0158; // Exif标志：图片信息XClipPathUnits
	/**
	 * YClip Path Units
	 */
	public    final static int    TID_YCLIPPATHUNITS = 0x0159; // Exif标志：图片信息YClipPathUnits
	/**
	 * Indexed
	 */
	public    final static int    TID_INDEXED = 0x015A; // Exif标志：图片信息Indexed
	/**
	 * JPEG Tables
	 */
	public    final static int    TID_JPEGTABLES= 0x015B; // Exif标志：图片信息JPEGTables
	/**
	 * OPI Proxy
	 */
	public    final static int    TID_OPIPROXY = 0x015F; // Exif标志：图片信息OPIProxy
	/**
	 * Global Parameters IFD
	 */
	public    final static int    TID_GLOBALPARAMETERSIFD = 0x0190; // Exif标志：图片信息GlobalParametersIFD
	/**
	 * Profile Type
	 */
	public    final static int    TID_PROFILETYPE = 0x0191; // Exif标志：图片信息ProfileType
	/**
	 * Fax Profile
	 */
	public    final static int    TID_FAXPROFILE = 0x0192; // Exif标志：图片信息FaxProfile
	/**
	 * Coding Methods
	 */
	public    final static int    TID_CODINGMETHODS = 0x0193; // Exif标志：图片信息CodingMethods
	/**
	 * Version Year
	 */
	public    final static int    TID_VERSIONYEAR= 0x0194; // Exif标志：图片信息VersionYear
	/**
	 * Mode Number
	 */
	public    final static int    TID_MODENUMBER = 0x0195; // Exif标志：图片信息ModeNumber
	/**
	 * Decode
	 */
	public    final static int    TID_DECODE = 0x01B1; // Exif标志：图片信息Decode
	/**
	 * Default Image Color
	 */
	public    final static int    TID_DEFAULTIMAGECOLOR = 0x01B2; // Exif标志：图片信息DefaultImageColor
	/**
	 * JPEG Proc
	 */
	public    final static int    TID_JPEGPROC = 0x0200; // Exif标志：图片信息JPEGProc
	/**
	 * JPEG Interchange Format
	 */
	public    final static int    TID_ICGFORMAT = 0x0201; // Exif标志：图片信息JPEGInterchangeFormat
	/**
	 * JPEG Interchange Format Length
	 */
	public    final static int    TID_ICGFORMATLEN = 0x0202; // Exif标志：图片信息JPEGInterchangeFormatLength
	/**
	 * JPEG Restart Interval
	 */
	public    final static int    TID_JPEGRESTARTINTERVAL = 0x0203; // Exif标志：图片信息JPEGRestartInterval
	/**
	 * JPEG Lossless Predictors
	 */
	public    final static int    TID_JPEGLOSSLESSPREDICTORS = 0x0205; // Exif标志：图片信息JPEGLosslessPredictors
	/**
	 * JPEG Point Transforms
	 */
	public    final static int    TID_JPEGPOINTTRANSFORMS = 0x0206; // Exif标志：图片信息JPEGPointTransforms
	/**
	 * JPEG QTables
	 */
	public    final static int    TID_JPEGQTABLES = 0x0207; // Exif标志：图片信息JPEGQTables
	/**
	 * JPEG DCTables
	 */
	public    final static int    TID_JPEGDCTABLES = 0x0208; // Exif标志：图片信息JPEGLosslessPredictors
	/**
	 * JPE GACTables
	 */
	public    final static int    TID_JPEGACTABLES = 0x0209; // Exif标志：图片信息JPEGACTables
	/**
	 * YCb Cr Coefficients
	 */
	public    final static int    TID_YCBCRCOEFFICIENTS = 0x0211; // Exif标志：图片信息YCbCrCoefficients
	/**
	 * YCb Cr Sub Sampling
	 */
	public    final static int    TID_YCBCRSUBSAMPLING = 0x0212; // Exif标志：图片信息YCbCrSubSampling
	/**
	 * YCbCr Positioning
	 */
	public    final static int    TID_YCBCRPOS = 0x0213; // Exif标志：图片信息YCbCrPositioning
	/**
	 * Reference Black White
	 */
	public    final static int    TID_REFERENCEBLACKWHITE = 0x0214; // Exif标志：图片信息ReferenceBlackWhite
	/**
	 * Strip Row Counts
	 */
	public    final static int    TID_STRIPROWCOUNTS = 0x022F; // Exif标志：图片信息StripRowCounts
	/**
	 * XMP
	 */
	public    final static int    TID_XMP = 0x02BC; // Exif标志：图片信息XMP
	/**
	 *ImageID
	 */
	public    final static int    TID_ImageID = 0x800D; // Exif标志：图片信息ImageID
	/**
	 * Wang Annotation
	 */
	public    final static int    TID_WANGANNOTATION = 0x80A4; // Exif标志：图片信息Wang
	/**
	 * Copyright
	 */
	public    final static int    TID_COPYRIGHT = 0x8298; // Exif标志：图片信息Copyright
	/**
	 * ExposureTime
	 */
	public    final static int    TID_EXPTIME = 0x829A; // Exif标志：图片信息ExposureTime
	/**
	 * Image Description
	 */
	public    final static int    TID_FNUMBER = 0x829D; // Exif标志：图片信息ImageDescription
	/**
	 * MD FileTag
	 */
	public    final static int    TID_MDFILETAG = 0x82A5; // Exif标志：图片信息MD FileTag
	/**
	 * MD ScalePixel
	 */
	public    final static int    TID_MDSCALEPIXEL = 0x82A6; // Exif标志：图片信息MD ScalePixel
	/**
	 * MD ColorTable
	 */
	public    final static int    TID_MDCOLORTABLE = 0x82A7; // Exif标志：图片信息MD ColorTable
	/**
	 * MD LabName
	 */
	public    final static int    TID_MDLABNAME = 0x82A8; // Exif标志：图片信息MD LabName
	/**
	 * MD SampleInfo
	 */
	public    final static int    TID_MDSAMPLEINFO = 0x82A9; // Exif标志：图片信息MD SampleInfo
	/**
	 * MD PrepDate
	 */
	public    final static int    TID_MDPREPDATE = 0x82AA; // Exif标志：图片信息MD PrepDate
	/**
	 * MD PrepTime
	 */
	public    final static int    TID_MDPREPTIME = 0x82AB; // Exif标志：图片信息MD PrepTime
	/**
	 * MD FileUnits
	 */
	public    final static int    TID_MDFILEUNITS = 0x82AC; // Exif标志：图片信息MD FileUnits
	/**
	 * Model Pixel Scale Tag
	 */
	public    final static int    TID_MODELPIXELSCALETAG = 0x830E; // Exif标志：图片信息ModelPixelScaleTag
	/**
	 * IPTC
	 */
	public    final static int    TID_IPTC = 0x83BB; // Exif标志：图片信息IPTC
	/**
	 * INGR Packet Data Tag
	 */
	public    final static int    TID_INGRPACKETDATATAG = 0x847E; // Exif标志：图片信息INGR Packet Data Tag
	/**
	 * INGR Flag Registers
	 */
	public    final static int    TID_INGRFLAGREGISTERS = 0x847F; // Exif标志：图片信息INGR Flag Registers
	/**
	 * IrasB Transformation Matrix
	 */
	public    final static int    TID_IRASBTRANSFORMATIONMATRIX = 0x8480; // Exif标志：图片信息IrasB Transformation Matrix
	/**
	 * Model Tiepoint Tag
	 */
	public    final static int    TID_MODELTIEPOINTTAG = 0x8482; // Exif标志：图片信息ModelTiepointTag
	/**
	 * Model Transformation Tag
	 */
	public    final static int    TID_MODELTRANSFORMATIONTAG = 0x85D8; // Exif标志：图片信息ModelTransformationTag
	/**
	 * Photoshop
	 */
	public    final static int    TID_PHOTOSHOP = 0x8649; // Exif标志：图片信息Photoshop
	/**
	 * Exif IFD Pointer
	 */
	protected final static int    TID_EXIF_IFD_PTR = 0x8769; // Exif标志：图片信息Exif IFD Pointer
	/**
	 * ICC Profile
	 */
	protected final static int    TID_ICCPROFILE = 0x8773; // Exif标志：图片信息ICC Profile
	/**
	 * Image Layer
	 */
	protected final static int    TID_IMAGELAYER = 0x87AC; // Exif标志：图片信息ImageLayer
	/**
	 * Geo Key Directory Tag
	 */
	protected final static int    TID_GEOKEYDIRECTORYTAG = 0x87AF; // Exif标志：图片信息GeoKeyDirectoryTag
	/**
	 * Geo Double Params Tag
	 */
	protected final static int    TID_GEODOUBLEPARAMSTAG = 0x87B0; // Exif标志：图片信息GeoDoubleParamsTag
	/**
	 * GeoAsciiParamsTag
	 */
	protected final static int    TID_GEOASCIIPARAMSTAG = 0x87B1; // Exif标志：图片信息GeoAsciiParamsTag

	/**
	 * Exposure Program
	 */
	public    final static int    TID_EXPOSUREPROGRAM = 0x8822; // Exif标志：图片信息ExposureProgram
	/**
	 * Spectral Sensitivity
	 */
	public    final static int    TID_SPECTRALSENSITIVITY = 0x8824; // Exif标志：图片信息SpectralSensitivity
	/**
	 * GPSIFDPointer
	 */
	protected final static int    TID_GPS_IFD_PTR = 0x8825; // Exif标志：图片信息GPS IFD Pointer
	/**
	 * ISO Speed Ratings
	 */
	public    final static int    TID_ISOSPEED = 0x8827; // Exif标志：图片信息ISOSpeedRatings
	/**
	 * OECF
	 */
	public    final static int    TID_OECF = 0x8828; // Exif标志：图片信息OECF
	/**
	 * HylaFAX FaxRecvParams
	 */
	public    final static int    TID_HYLAFAXFAXRECVPARAMS = 0x885C; // Exif标志：图片信息HylaFAX FaxRecvParams
	/**
	 * HylaFAX FaxSubAddress
	 */
	public    final static int    TID_HYLAFAXFAXSUBADDRESS = 0x885D; // Exif标志：图片信息HylaFAX FaxSubAddress
	/**
	 * HylaFAX FaxRecvTime
	 */
	public    final static int    TID_HYLAFAXFAXRECVTIME = 0x885E; // Exif标志：图片信息HylaFAX FaxRecvTime
	/**
	 * Exif Version
	 */
	public    final static int    TID_EXIFVER = 0x9000; // Exif标志：图片信息ExifVersion
	/**
	 * Date Time Original
	 */
	public    final static int    TID_DATETIMEORG = 0x9003; // Exif标志：图片信息DateTimeOriginal
	/**
	 * Date Time Digitized
	 */
	public    final static int    TID_DATETIMEDIG = 0x9004; // Exif标志：图片信息DateTimeDigitized
	/**
	 * Components Configuration
	 */
	public    final static int    TID_CPTCONFIG = 0x9101; // Exif标志：图片信息ComponentsConfiguration
	/**
	 * Compressed Bits Per Pixel
	 */
	public    final static int    TID_CPSBITSPERPIX = 0x9102; // Exif标志：图片信息CompressedBitsPerPixel
	/**
	 * Shutter Speed Value
	 */
	public    final static int    TID_SHUTTERSPEED = 0x9201; // Exif标志：图片信息ShutterSpeedValue
	/**
	 * Aperture Value
	 */
	public    final static int    TID_APERTURE = 0x9202; // Exif标志：图片信息ApertureValue
	/**
	 * Brightness Value
	 */
	public    final static int    TID_BRIGHTNESS = 0x9203; // Exif标志：图片信息BrightnessValue
	/**
	 * Exposure Bias Value
	 */
	public    final static int    TID_EXPOSUREBIAS = 0x9204; // Exif标志：图片信息ExposureBiasValue
	/**
	 * Max Aperture Ratio Value
	 */
	public    final static int    TID_MAXAPERRATIO = 0x9205; // Exif标志：图片信息MaxApertureRatioValue
	/**
	 * Subject Distance
	 */
	public    final static int    TID_SUBDISTANCE = 0x9206; // Exif标志：图片信息SubjectDistance
	/**
	 * Metering Mode
	 */
	public    final static int    TID_METERMODE = 0x9207; // Exif标志：图片信息MeteringMode
	/**
	 * Light Source
	 */
	public    final static int    TID_LIGHTSOURCE = 0x9208; // Exif标志：图片信息LightSource
	/**
	 * Flash
	 */
	public    final static int    TID_FLASH = 0x9209; // Exif标志：图片信息Flash
	/**
	 * Focal Length
	 */
	public    final static int    TID_FOCALLENGTH = 0x920A; // Exif标志：图片信息FocalLength
	/**
	 * Subject Area
	 */
	public    final static int    TID_SUBJECTAREA = 0x9214; // Exif标志：图片信息SubjectArea
	/**
	 * Maker Note
	 */
	public    final static int    TID_MAKERNOTE = 0x927C; // Exif标志：图片信息MakerNote
	/**
	 * User Comments
	 */
	public    final static int    TID_USERCOMMENTS = 0x9286; // Exif标志：图片信息UserComments
	/**
	 * Sub Sec Time
	 */
	public    final static int    TID_SUBSECTIME = 0x9290; // Exif标志：图片信息SubSecTime
	/**
	 * Sub Sec Time Original
	 */
	public    final static int    TID_SUBSECTIMEORG = 0x9291; // Exif标志：图片信息SubSecTimeOriginal
	/**
	 * Sub Sec Time Digitized
	 */
	public    final static int    TID_SUBSECTIMEDIG = 0x9292; // Exif标志：图片信息SubSecTimeDigitized
	/**
	 * ImageSourceData
	 */
	public    final static int    TID_IMAGESOURCEDATA = 0x935C; // Exif标志：图片信息ImageSourceData
	/**
	 * Flash Pix Version
	 */
	public    final static int    TID_FLASHPIXVER = 0xA000; // Exif标志：图片信息FlashPixVersion
	/**
	 * Sub Sec Time
	 */
	public    final static int    TID_COLORSPACE = 0xA001; // Exif标志：图片信息SubSecTime
	/**
	 * Pixel X Dimension
	 */
	public    final static int    TID_PIXXDIM = 0xA002; // Exif标志：图片信息Pixel X Dimension
	/**
	 * Pixel Y Dimension
	 */
	public    final static int    TID_PIXYDIM = 0xA003; // Exif标志：图片信息Pixel Y Dimension
	/**
	 * Related Sound File
	 */
	public    final static int    TID_RELATEDSOUNDFILE = 0xA004; // Exif标志：图片信息RelatedSoundFile
	/**
	 * Interoperability IFD Pointer
	 */
	protected final static int    TID_ITP_IFD_PTR = 0xA005; // Exif标志：图片信息Interoperability
	/**
	 * Flash Energy
	 */
	protected final static int    TID_FLASHENERGY = 0xA20B; // Exif标志：图片信息FlashEnergy
	/**
	 * Spatial Frequency Response
	 */
	protected final static int    TID_SFR = 0xA20C; // Exif标志：图片信息SpatialFrequencyResponse
	/**
	 * Focal PlaneX Resolution
	 */
	protected final static int    TID_FPXR = 0xA20E; // Exif标志：图片信息FocalPlaneXResolution
	/**
	 * Focal lane YResolution
	 */
	protected final static int    TID_FPYR = 0xA20F; // Exif标志：图片信息FocalPlaneYResolution
	/**
	 * Focal Plane Resolution Unit
	 */
	protected final static int    TID_FPRU = 0xA210; // Exif标志：图片信息FocalPlaneResolutionUnit
	/**
	 * Subject Location
	 */
	protected final static int    TID_SUBJECTLOCATION = 0xA214; // Exif标志：图片信息SubjectLocation
	/**
	 * Exposure Index
	 */
	protected final static int    TID_EXPOSUREINDEX = 0xA215; // Exif标志：图片信息ExposureIndex IFD Pointer
	/**
	 * Sensing Method
	 */
	public    final static int    TID_SENSINGMETHOD = 0xA217; // Exif标志：图片信息Sensing Method
	/**
	 * Sensing Method
	 */
	public    final static int    TID_FILESOURCE = 0xA300; // Exif标志：图片信息File Source
	/**
	 * Scene Type
	 */
	public    final static int    TID_SCENETYPE = 0xA301; // Exif标志：图片信息Scene Type
	/**
	 * CFA Pattern
	 */
	public    final static int    TID_CFAPattern = 0xA302; // Exif标志：图片信息CFAPattern
	/**
	 * Custom Rendered
	 */
	public    final static int    TID_CustomRendered = 0xA401; // Exif标志：图片信息CustomRendered
	/**
	 * Exposure Mode
	 */
	public    final static int    TID_EXPOSUREMODE = 0xA402; // Exif标志：图片信息ExposureMode
	/**
	 * White Balance
	 */
	public    final static int    TID_WHITEBALANCE = 0xA403; // Exif标志：图片信息WhiteBalance
	/**
	 * Digital Zoom Ratio
	 */
	public    final static int    TID_DIGITALZOOMRATIO = 0xA404; // Exif标志：图片信息DigitalZoomRatio
	/**
	 * Focal Length In35mmFilm
	 */
	public    final static int    TID_FOCALLENGTHIN35MMFILM = 0xA405; // Exif标志：图片信息FocalLengthIn35mmFilm
	/**
	 * Scene Capture Type
	 */
	public    final static int    TID_SCENECAPTURETYPE = 0xA406; // Exif标志：图片信息SceneCaptureType
	/**
	 * Gain Control
	 */
	public    final static int    TID_GAINCONTROL = 0xA407; // Exif标志：图片信息GainControl
	/**
	 * Contrast
	 */
	public    final static int    TID_CONTRAST = 0xA408; // Exif标志：图片信息Contrast
	/**
	 * Saturation
	 */
	public    final static int    TID_SATURATION = 0xA409; // Exif标志：图片信息Saturation
	/**
	 * Sharpness
	 */
	public    final static int    TID_SHARPNESS = 0xA40A; // Exif标志：图片信息Sharpness
	/**
	 * Device Setting Description
	 */
	public    final static int    TID_DEVICESETTINGDESCRIPTION = 0xA40B; // Exif标志：图片信息DeviceSettingDescription
	/**
	 * Subject Distance Range
	 */
	public    final static int    TID_SUBJECTDISTANCERANGE = 0xA40C; // Exif标志：图片信息SubjectDistanceRange
	/**
	 * Image Unique ID
	 */
	public    final static int    TID_IMAGEUNIQUEID = 0xA420; // Exif标志：图片信息ImageUniqueID
	/**
	 * Gdalmeta Data
	 */
	public    final static int    TID_GDALMETADATA = 0xA480; // Exif标志：图片信息 Gdalmeta Data
	/**
	 * Gdal No Data
	 */
	public    final static int    TID_GDALNODATA = 0xA481; // Exif标志：图片信息GDAL_NODATA
	/**
	 * Oce Scanjob Description
	 */
	public    final static int    TID_OCESCANJOBDESCRIPTION = 0xC427; // Exif标志：图片信息Oce Scanjob Description
	/**
	 * Oce Application Selector
	 */
	public    final static int    TID_OCEAPPLICATIONSELECTOR = 0xC428; // Exif标志：图片信息Oce Application Selector
	/**
	 * Oce Identification Number
	 */
	public    final static int    TID_OCEIDENTIFICATIONNUMBER = 0xC429; // Exif标志：图片信息Oce Identification Number
	/**
	 * Oce ImageLogic Characteristics
	 */
	public    final static int    TID_OCEIMAGELOGICCHARACTERISTICS = 0xC42A; // Exif标志：图片信息Oce ImageLogic Characteristics
	/**
	 * DNGVersion
	 */
	public    final static int    TID_DNGVERSION = 0xC612; // Exif标志：图片信息DNGVersion
	/**
	 * DNG Backward Version
	 */
	public    final static int    TID_DNGBACKWARDVERSION = 0xC613; // Exif标志：图片信息DNGBackwardVersion
	/**
	 * Unique Camera Model
	 */
	public    final static int    TID_UNIQUECAMERAMODEL = 0xC614; // Exif标志：图片信息UniqueCameraModel
	/**
	 * Localized Camera Model
	 */
	public    final static int    TID_LOCALIZEDCAMERAMODEL = 0xC615; // Exif标志：图片信息LocalizedCameraModel
	/**
	 * CFAPlane Color
	 */
	public    final static int    TID_CFAPLANECOLOR = 0xC616; // Exif标志：图片信息CFAPlaneColor
	/**
	 * CFA Layout
	 */
	public    final static int    TID_CFALAYOUT = 0xC617; // Exif标志：图片信息CFALayout
	/**
	 * Linearization Table
	 */
	public    final static int    TID_LINEARIZATIONTABLE = 0xC618; // Exif标志：图片信息LinearizationTable
	/**
	 * Black Level Repeat Dim
	 */
	public    final static int    TID_BLACKLEVELREPEATDIM = 0xC619; // Exif标志：图片信息BlackLevelRepeatDim
	/**
	 * Black Level
	 */
	public    final static int    TID_BLACKLEVEL = 0xC61A; // Exif标志：图片信息BlackLevel
	/**
	 * Black Level Delta H
	 */
	public    final static int    TID_BLACKLEVELDELTAH = 0xC61B; // Exif标志：图片信息BlackLevelDeltaH
	/**
	 * Black Level Delta V
	 */
	public    final static int    TID_BLACKLEVELDELTAV = 0xC61C; // Exif标志：图片信息BlackLevelDeltaV
	/**
	 * White Level
	 */
	public    final static int    TID_WHITELEVEL = 0xC61D; // Exif标志：图片信息WhiteLevel
	/**
	 * Default Scale
	 */
	public    final static int    TID_DEFAULTSCALE = 0xC61E; // Exif标志：图片信息DefaultScale
	/**
	 * Default Crop Origin
	 */
	public    final static int    TID_DEFAULTCROPORIGIN = 0xC61F; // Exif标志：图片信息DefaultCropOrigin
	/**
	 * Default Crop Size
	 */
	public    final static int    TID_DEFAULTCROPSIZE = 0xC620; // Exif标志：图片信息DefaultCropSize
	/**
	 * Color Matrix1
	 */
	public    final static int    TID_COLORMATRIX1 = 0xC621; // Exif标志：图片信息ColorMatrix1
	/**
	 * Color Matrix2
	 */
	public    final static int    TID_COLORMATRIX2 = 0xC622; // Exif标志：图片信息ColorMatrix2
	/**
	 * Camera Calibration 1
	 */
	public    final static int    TID_CAMERACALIBRATION1 = 0xC623; // Exif标志：图片信息CameraCalibration1
	/**
	 * Camera Calibration 2
	 */
	public    final static int    TID_CAMERACALIBRATION2 = 0xC624; // Exif标志：图片信息CameraCalibration2
	/**
	 * Reduction Matrix 1
	 */
	public    final static int    TID_REDUCTIONMATRIX1 = 0xC625; // Exif标志：图片信息ReductionMatrix1
	/**
	 * Reduction Matrix 2
	 */
	public    final static int    TID_REDUCTIONMATRIX2 = 0xC626; // Exif标志：图片信息ReductionMatrix2
	/**
	 * Analog Balance
	 */
	public    final static int    TID_ANALOGBALANCE = 0xC627; // Exif标志：图片信息AnalogBalance
	/**
	 * As Shot Neutral
	 */
	public    final static int    TID_ASSHOTNEUTRAL = 0xC628; // Exif标志：图片信息AsShotNeutral
	/**
	 * As Shot White XY
	 */
	public    final static int    TID_ASSHOTWHITEXY = 0xC629; // Exif标志：图片信息AsShotWhiteXY
	/**
	 * Baseline Exposure
	 */
	public    final static int    TID_BASELINEEXPOSURE = 0xC62A; // Exif标志：图片信息BaselineExposure
	/**
	 * Baseline Noise
	 */
	public    final static int    TID_BASELINENOISE = 0xC62B; // Exif标志：图片信息BaselineNoise
	/**
	 * Baseline Sharpness
	 */
	public    final static int    TID_BASELINESHARPNESS = 0xC62C; // Exif标志：图片信息BaselineSharpness
	/**
	 * Bayer Green Split
	 */
	public    final static int    TID_BAYERGREENSPLIT = 0xC62D; // Exif标志：图片信息BayerGreenSplit
	/**
	 * Linear Response Limit
	 */
	public    final static int    TID_LINEARRESPONSELIMIT = 0xC62E; // Exif标志：图片信息LinearResponseLimit
	/**
	 * Camera Serial Number
	 */
	public    final static int    TID_CAMERASERIALNUMBER = 0xC62F; // Exif标志：图片信息CameraSerialNumber
	/**
	 * Lens Info
	 */
	public    final static int    TID_LENSINFO = 0xC630; // Exif标志：图片信息LensInfo
	/**
	 * Chroma Blur Radius
	 */
	public    final static int    TID_CHROMABLURRADIUS = 0xC631; // Exif标志：图片信息ChromaBlurRadius
	/**
	 * Anti Alias Strength
	 */
	public    final static int    TID_ANTIALIASSTRENGTH = 0xC632; // Exif标志：图片信息AntiAliasStrength
	/**
	 * DNG Private Data
	 */
	public    final static int    TID_DNGPRIVATEDATA = 0xC634; // Exif标志：图片信息DNGPrivateData
	/**
	 * Maker Note Safety
	 */
	public    final static int    TID_MAKERNOTESAFETY = 0xC635; // Exif标志：图片信息MakerNoteSafety
	/**
	 * Calibration Illuminant 1
	 */
	public    final static int    TID_CALIBRATIONILLUMINANT1 = 0xC65A; // Exif标志：图片信息CalibrationIlluminant1
	/**
	 * Calibration Illuminant2
	 */
	public    final static int    TID_CALIBRATIONILLUMINANT2 = 0xC65B; // Exif标志：图片信息CalibrationIlluminant2
	/**
	 * Best Quality Scale
	 */
	public    final static int    TID_BESTQUALITYSCALE = 0xC65C; // Exif标志：图片信息BestQualityScale
	/**
	 * Alias Layer Metadata
	 */
	public    final static int    TID_ALIASLAYERMETADATA = 0xC660; // Exif标志：图片信息Alias Layer Metadata
	/**
	 * Image size
	 */
	public    final static int    TID_IMAGESIZE = 0xFE00; // Exif标志：图片信息Image size
	
	public final static int[]  TID_LINK  = {
		TID_GPSVERSIONID,
		TID_GPSLATREF,
		TID_GPSLAT,
		TID_GPSLONREF,
		TID_GPSLON,
		TID_GPSALTREF,
		TID_GPSALT,
		TID_GPSTIME,
		TID_GPSSATELLITES,
		TID_GPSSTATUS,
		TID_GPSMEASUREMODE,
		TID_GPSDOP,
		TID_GPSSPEEDREF,
		TID_GPSSPEED,
		TID_GPTIDACKREF,
		TID_GPTIDACK,
		TID_GPSIMGDIRECTIONREF,
		TID_GPSIMGDIRECTION,
		TID_GPSMAPDATUM,
		TID_GPSDESTLATITUDEREF,
		TID_GPSDESTLATITUDE,
		TID_GPSDESTLONGITUDEREF,
		TID_GPSDESTLONGITUDE,
		TID_GPSDESTBEARINGREF,
		TID_GPSDESTBEARING,
		TID_GPSDESTDISTANCEREF,
		TID_GPSDESTDISTANCE,
		TID_GPSPROCESSINGMETHOD,
		TID_GPSAREAINFORMATION,
		TID_GPSDATE,
		TID_GPSDIFFERENTIAL,
		TID_NEWSUBFILETYPE,
		TID_SUBFILETYPE,
		TID_IMAGEWIDTH,
		TID_IMAGEHEIGHT,
		TID_BITSPERSAMPLE,
		TID_COMPRESSION,
		TID_PIP,
		TID_THRESHHOLDING,
		TID_CellWidth,
		TID_CellLength,
		TID_FillOrder,
		TID_DOCUMENTNAME,
		TID_IMAGEDESCRIPTION,
		TID_MAKE,
		TID_MODEL,
		TID_TIDIPOFFSET,
		TID_ORIENTATION,
		TID_SAMPLEPERPIX,
		TID_ROWSPERTIDIP,
		TID_TIDIPBYTELEN,
		TID_MINSAMPLEVALUE,
		TID_MAXSAMPLEVALUE,
		TID_XRES,
		TID_YRES,
		TID_PLANARCONFIG,
		TID_PAGENAME,
		TID_XPOSITION,
		TID_YPOSITION,
		TID_FREEOFFSETS,
		TID_FREEBYTECOUNTS,
		TID_GRAYRESPONSEUNIT,
		TID_GRAYRESPONSECURVE,
		TID_T4OPTIONS,
		TID_T6OPTIONS,
		TID_RES_UNIT,
		TID_PAGENUMBER,
		TID_TRANSFERFUNCTION,
		TID_SOFTWARE,
		TID_DATETIME,
		TID_ARTIST,
		TID_HOSTCOMPUTER,
		TID_PREDICTOR,
		TID_WHITEPOINT,
		TID_PRIMARYCHROMATICITIES,
		TID_COLORMAP,
		TID_HALFTONEHINTS,
		TID_TILEWIDTH,
		TID_TILELENGTH,
		TID_TILEOFFSETS,
		TID_TILEBYTECOUNTS,
		TID_BADFAXLINES,
		TID_CLEANFAXDATA,
		TID_CONSECUTIVEBADFAXLINES,
		TID_SUBIFDS,
		TID_INKSET,
		TID_INKNAMES,
		TID_NUMBEROFINKS,
		TID_DOTRANGE,
		TID_TARGETPRINTER,
		TID_EXTRASAMPLES,
		TID_SAMPLEFORMAT,
		TID_SMINSAMPLEVALUE,
		TID_SMAXSAMPLEVALUE,
		TID_TRANSFERRANGE,
		TID_CLIPPATH,
		TID_XCLIPPATHUNITS,
		TID_YCLIPPATHUNITS,
		TID_INDEXED,
		TID_JPEGTABLES,
		TID_OPIPROXY,
		TID_GLOBALPARAMETERSIFD,
		TID_PROFILETYPE,
		TID_FAXPROFILE,
		TID_CODINGMETHODS,
		TID_VERSIONYEAR,
		TID_MODENUMBER,
		TID_DECODE,
		TID_DEFAULTIMAGECOLOR,
		TID_JPEGPROC,
		TID_ICGFORMAT,
		TID_ICGFORMATLEN,
		TID_JPEGRESTARTINTERVAL,
		TID_JPEGLOSSLESSPREDICTORS,
		TID_JPEGPOINTTRANSFORMS,
		TID_JPEGQTABLES,
		TID_JPEGDCTABLES,
		TID_JPEGACTABLES,
		TID_YCBCRCOEFFICIENTS,
		TID_YCBCRSUBSAMPLING,
		TID_YCBCRPOS,
		TID_REFERENCEBLACKWHITE,	 
		TID_STRIPROWCOUNTS,
		TID_XMP,
		TID_ImageID,
		TID_WANGANNOTATION,
		TID_COPYRIGHT,
		TID_EXPTIME,
		TID_FNUMBER,
		TID_MDFILETAG,
		TID_MDSCALEPIXEL,
		TID_MDCOLORTABLE,
		TID_MDLABNAME,
		TID_MDSAMPLEINFO,
		TID_MDPREPDATE,
		TID_MDPREPTIME,
		TID_MDFILEUNITS,
		TID_MODELPIXELSCALETAG,
		TID_IPTC,
		TID_INGRPACKETDATATAG,
		TID_INGRFLAGREGISTERS,
		TID_IRASBTRANSFORMATIONMATRIX,
		TID_MODELTIEPOINTTAG,
		TID_MODELTRANSFORMATIONTAG,
		TID_PHOTOSHOP,
		TID_EXIF_IFD_PTR,
		TID_ICCPROFILE,
		TID_IMAGELAYER,
		TID_GEOKEYDIRECTORYTAG,
		TID_GEODOUBLEPARAMSTAG,
		TID_GEOASCIIPARAMSTAG,
		TID_EXPOSUREPROGRAM,
		TID_SPECTRALSENSITIVITY,
		TID_GPS_IFD_PTR,
		TID_ISOSPEED,
		TID_OECF,
		TID_HYLAFAXFAXRECVPARAMS,
		TID_HYLAFAXFAXSUBADDRESS,
		TID_HYLAFAXFAXRECVTIME,
		TID_EXIFVER,
		TID_DATETIMEORG,
		TID_DATETIMEDIG,
		TID_CPTCONFIG,
		TID_CPSBITSPERPIX,
		TID_SHUTTERSPEED,
		TID_APERTURE,
		TID_BRIGHTNESS,
		TID_EXPOSUREBIAS,
		TID_MAXAPERRATIO,
		TID_SUBDISTANCE,
		TID_METERMODE,
		TID_LIGHTSOURCE,
		TID_FLASH,
		TID_FOCALLENGTH,
		TID_SUBJECTAREA,
		TID_MAKERNOTE,
		TID_USERCOMMENTS,
		TID_SUBSECTIME,
		TID_SUBSECTIMEORG,
		TID_SUBSECTIMEDIG,
		TID_IMAGESOURCEDATA,
		TID_FLASHPIXVER,
		TID_COLORSPACE,
		TID_PIXXDIM,
		TID_PIXYDIM,
		TID_RELATEDSOUNDFILE,
		TID_ITP_IFD_PTR,
		TID_FLASHENERGY,
		TID_SFR,
		TID_FPXR,
		TID_FPYR,
		TID_FPRU,
		TID_SUBJECTLOCATION,
		TID_EXPOSUREINDEX,
		TID_SENSINGMETHOD,
		TID_FILESOURCE,
		TID_SCENETYPE,
		TID_CFAPattern,
		TID_CustomRendered,
		TID_EXPOSUREMODE,
		TID_WHITEBALANCE,
		TID_DIGITALZOOMRATIO,
		TID_FOCALLENGTHIN35MMFILM,
		TID_SCENECAPTURETYPE,
		TID_GAINCONTROL,
		TID_CONTRAST,
		TID_SATURATION,
		TID_SHARPNESS,
		TID_DEVICESETTINGDESCRIPTION,
		TID_SUBJECTDISTANCERANGE,
		TID_IMAGEUNIQUEID,
		TID_GDALMETADATA,
		TID_GDALNODATA,
		TID_OCESCANJOBDESCRIPTION,
		TID_OCEAPPLICATIONSELECTOR,
		TID_OCEIDENTIFICATIONNUMBER,
		TID_OCEIMAGELOGICCHARACTERISTICS,
		TID_DNGVERSION,
		TID_DNGBACKWARDVERSION,
		TID_UNIQUECAMERAMODEL,
		TID_LOCALIZEDCAMERAMODEL,
		TID_CFAPLANECOLOR,
		TID_CFALAYOUT,
		TID_LINEARIZATIONTABLE,
		TID_BLACKLEVELREPEATDIM,
		TID_BLACKLEVEL,
		TID_BLACKLEVELDELTAH,
		TID_BLACKLEVELDELTAV,
		TID_WHITELEVEL,
		TID_DEFAULTSCALE,
		TID_DEFAULTCROPORIGIN,
		TID_DEFAULTCROPSIZE,
		TID_COLORMATRIX1,
		TID_COLORMATRIX2,
		TID_CAMERACALIBRATION1,
		TID_CAMERACALIBRATION2,
		TID_REDUCTIONMATRIX1,
		TID_REDUCTIONMATRIX2,
		TID_ANALOGBALANCE,
		TID_ASSHOTNEUTRAL,
		TID_ASSHOTWHITEXY,
		TID_BASELINEEXPOSURE,
		TID_BASELINENOISE,
		TID_BASELINESHARPNESS,
		TID_BAYERGREENSPLIT,
		TID_LINEARRESPONSELIMIT,
		TID_CAMERASERIALNUMBER,
		TID_LENSINFO,
		TID_CHROMABLURRADIUS,
		TID_ANTIALIASSTRENGTH,
		TID_DNGPRIVATEDATA,
		TID_MAKERNOTESAFETY,
		TID_CALIBRATIONILLUMINANT1,
		TID_CALIBRATIONILLUMINANT2,
		TID_BESTQUALITYSCALE,
		TID_ALIASLAYERMETADATA,
		TID_IMAGESIZE
	};
}
