package com.jsy.xuezhuli.utils;
public class Maps {
	 private static  double EARTH_RADIUS = 6378137;//一般的认为，地球的赤道半径是6378137米
	 
	 static double DEF_PI = 3.14159265359; // PI
	 static double DEF_2PI= 6.28318530712; // 2*PI
	 static double DEF_PI180= 0.01745329252; // PI/180.0
	 static double DEF_R =6370693.5; // radius of earth
	 
	 private static double rad(double d)
	 {
	    return d * Math.PI / 180.0;
	 }
	 
	 /**
	  * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	  * @param lng1
	  * @param lat1
	  * @param lng2
	  * @param lat2
	  * @return
	  */
	 public static double getDistatce(double lng1, double lat1, double lng2, double lat2)
	  {
		 double radLat1 = rad(lat1);
		 double radLat2 = rad(lat2);
		 double a = radLat1 - radLat2;
		 double b = rad(lng1) - rad(lng2);
		 double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
		 Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		 s = s * EARTH_RADIUS;
		 s = Math.round(s * 10000) / 10000;
		 return s;
	  }
	 
	 public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
	{
		 	double ew1, ns1, ew2, ns2;
			double distance;
			// 角度转换为弧度
			ew1 = lon1 * DEF_PI180;
			ns1 = lat1 * DEF_PI180;
			ew2 = lon2 * DEF_PI180;
			ns2 = lat2 * DEF_PI180;
			// 求大圆劣弧与球心所夹的角(弧度)
	 		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
	 		// 调整到[-1..1]范围内，避免溢出
			if (distance > 1.0)
	 		     distance = 1.0;
			else if (distance < -1.0)
	 		      distance = -1.0;
	 		// 求大圆劣弧长度
			distance = DEF_R * Math.acos(distance);
			return distance/1000;
		}
	 
	 public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
	 	{
	 		double ew1, ns1, ew2, ns2;
	 	double dx, dy, dew;
	 		double distance;
	 		// 角度转换为弧度
	 		ew1 = lon1 * DEF_PI180;
	 		ns1 = lat1 * DEF_PI180;
	 		ew2 = lon2 * DEF_PI180;
	 		ns2 = lat2 * DEF_PI180;
	 		// 经度差
	 		dew = ew1 - ew2;
	 		// 若跨东经和西经180 度，进行调整
	 		if (dew > DEF_PI)
	 		dew = DEF_2PI - dew;
	 		else if (dew < -DEF_PI)
	 		dew = DEF_2PI + dew;
	 		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
	 		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
	 		// 勾股定理求斜边长
	 		distance = Math.sqrt(dx * dx + dy * dy);
	 		return distance;
	 }
	 
	 public static void main(String[] args) {
	  System.out.println(Maps.getDistatce(116.357428,39.97923,39.94923,116.437428));
	  System.out.println(Maps.GetLongDistance(117.12,36.68,36.1917,177.1193));
	 }
}
