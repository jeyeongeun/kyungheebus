
public class Station {
	String station_id = "";
	String station_nm = "";
	String x = "";
	String y = "";
	String region_name = "";
	
	public Station(){}
	public Station(String _id, String _nm, String _x, String _y, String _name){
		station_id = _id;
		station_nm = _nm;
		x = _x;
		y = _y;
		region_name = _name;
	}
	
	public String getStationID(){return station_id;}
	public String getStationNM(){return station_nm;}
	public String getX(){return x;}
	public String getY(){return y;}
	public String getRegionName(){return region_name;}
	
	public void setStationID(String id){station_id = id;}
	public void setStationNM(String nm){station_nm = nm;}
	public void setX(String _x){x = _x;}
	public void setY(String _y){y = _y;}
	public void setRegionName(String name){region_name = name;}
}
