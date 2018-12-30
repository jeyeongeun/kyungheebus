
public class Bookmark {
	String route_id = "";
	String route_nm = "";
	String direction = "";
	
	public Bookmark(){}
	public Bookmark(String id, String nm, String dir){
		route_id = id;
		route_nm = nm;
		direction = dir;
	}
	public void setRouteID(String id){route_id = id;}
	public void setRouteNM(String name){route_nm = name;}
	public void setDirection(String dir){direction = dir;}
	
	public String getRouteID(){return route_id;}
	public String getRouteNM(){return route_nm;}
	public String getDirection(){return direction;}
}
