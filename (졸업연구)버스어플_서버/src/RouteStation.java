
public class RouteStation {
	String route_id = "";
	String station_id = "";
	String updown = "";
	int sta_order = 0;
	String route_nm = "";
	String station_nm = "";
	
	public RouteStation(){}
	public RouteStation(String r_id, String s_id, String up, int order, String r_nm, String s_nm){
		route_id = r_id;
		station_id = s_id;
		updown = up;
		sta_order = order;
		route_nm = r_nm;
		station_nm = s_nm;
	}
	
	public String getRouteID(){return route_id;}
	public String getStationID(){return station_id;}
	public String getUpdown(){return updown;}
	public int getStaOrder(){return sta_order;}
	public String getRouteNM(){return route_nm;}
	public String getStationNM(){return station_nm;}
	
	public void setRouteID(String id){route_id = id;}
	public void setStationID(String id){station_id = id;}
	public void setUpdown(String up){updown = up;}
	public void setStaOrder(int order){sta_order = order;}
	public void setRouteNM(String nm){route_nm = nm;}
	public void setStationNM(String nm){station_nm = nm;}	
}
