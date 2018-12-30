
public class StationPos {
	private String route_id = "";		//노선 번호
	private String station_id = "";		//현재 사용자의 정류소 ID
	private int location_no = 0;	//몇 역 전
	private String location_station_id = "";	//location_no의 정류소 ID
	private String location_station_nm = "";	//정류소 이름
	private String location_time = "";	//예상 도착 시간
	private int remainSeatCnt = 0;		//남은 좌석수(시내 버스면 -1, 좌석 버스만 제대로 뜸)
	private String x = "";				//정류소의 X좌표
	private String y = "";				//정류소의 Y좌표
	
	public StationPos(){}
	public StationPos(String r_id, String s_id, int no, String l_id, String l_nm, String time, int seat, String _x, String _y){
		route_id = r_id;
		station_id = s_id;
		location_no = no;
		location_station_id = l_id;
		location_station_nm = l_nm;
		location_time = time;
		remainSeatCnt = seat;
		x = _x;
		y = _y;
	}
	public String getRouteID(){return route_id;}
	public String getStationID(){return station_id;}
	public int getLocatioNo(){return location_no;}
	public String getLocationStationID(){return location_station_id;}
	public String getLocationStationNM(){return location_station_nm;}
	public String getLocationTime(){return location_time;}
	public int getRemainSeatCnt(){return remainSeatCnt;}
	public String getX(){return x;}
	public String getY(){return y;}
	
	public void setRouteID(String text){route_id = text;}
	public void setStationID(String text){station_id = text;}
	public void setLocationNo(int text){location_no = text;}
	public void setLocationStationID(String text){location_station_id = text;}
	public void setLocationStationNM(String text){location_station_nm = text;}
	public void setLocationTime(String text){location_time = text;}
	public void setRemainSeatCnt(int text){remainSeatCnt = text;}
	public void setX(String text){x = text;}
	public void setY(String text){y = text;}
}
