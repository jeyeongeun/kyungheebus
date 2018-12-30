
public class RoutePos {
	String direction = "";
	int order = 0;
	String route_nm = "";
	String station_nm = "";
	
	public void setDirection(String dir){direction = dir;}
	public void setOrder(int _order){order = _order;}
	public void setRouteNM(String name){route_nm = name;}
	public void setStationNM(String name){station_nm = name;}
	public void setInfo(String dir, int _order, String r_name, String s_name){
		direction = dir;
		order = _order;
		route_nm = r_name;
		station_nm = s_name;
	}
	
	public String getDirection(){return direction;}
	public int getOrder(){return order;}
	public String getRouteNM(){return route_nm;}
	public String getStationNM(){return station_nm;}
}
