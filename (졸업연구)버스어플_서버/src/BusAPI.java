import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

public class BusAPI {
	private Vector<Route> all_route = new Vector<Route>();
	private Vector<Station> all_station = new Vector<Station>();
	private Vector<RouteStation> all_routestation = new Vector<RouteStation>();
	
	public BusAPI(){
		initiateBusRoute();
		initiateBusStation();
		initiateBusRStation();
	}
	public void initiateBusRoute(){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select route_id, route_nm, route_tp, st_sta_id, st_sta_nm, st_sta_no, ed_sta_id, ed_sta_nm, ed_sta_no, up_first_time, up_last_time, down_first_time, down_last_time, peek_alloc, npeek_alloc from route");
			try{
				while(rs.next()){
					String route_id = rs.getString("route_id");
					String route_nm = rs.getString("route_nm");
					String route_tp = rs.getString("route_tp");
					String st_sta_id = rs.getString("st_sta_id");
					String st_sta_nm = rs.getString("st_sta_nm");
					String st_sta_no = rs.getString("st_sta_no");
					String ed_sta_id = rs.getString("ed_sta_id");
					String ed_sta_nm = rs.getString("ed_sta_nm");
					String ed_sta_no = rs.getString("ed_sta_no");
					String up_first_time = rs.getString("up_first_time");
					String up_last_time = rs.getString("up_last_time");
					String down_first_time = rs.getString("down_first_time");
					String down_last_time = rs.getString("down_last_time");
					int peek_alloc = rs.getInt("peek_alloc");
					int npeek_alloc = rs.getInt("npeek_alloc");
					
					Route newroute = new Route(route_id, route_nm, route_tp, st_sta_id, st_sta_nm, st_sta_no, ed_sta_id, ed_sta_nm, ed_sta_no, up_first_time, up_last_time, down_first_time, down_last_time, peek_alloc, npeek_alloc);
					all_route.add(newroute);
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
	}
	public void initiateBusStation(){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select station_id, station_nm, x, y, region_name from station");
			try{
				while(rs.next()){
					String station_id = rs.getString("station_id");
					String station_nm = rs.getString("station_nm");
					String x = rs.getString("x");
					String y = rs.getString("y");
					String region_name = rs.getString("region_name");
					
					Station newstation = new Station(station_id, station_nm, x, y, region_name);
					all_station.add(newstation);
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
	}
	public void initiateBusRStation(){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select route_id, station_id, updown, sta_order, route_nm, station_nm from routestation");
			try{
				while(rs.next()){
					String route_id = rs.getString("route_id");
					String station_id = rs.getString("station_id");
					String updown = rs.getString("updown");
					int sta_order = rs.getInt("sta_order");
					String route_nm = rs.getString("route_nm");
					String station_nm = rs.getString("station_nm");
					
					RouteStation newrs = new RouteStation(route_id, station_id, updown, sta_order, route_nm, station_nm);
					all_routestation.add(newrs);
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
	}
	
	//버스 시간표 검색
	public String searchBustime(String text){
		String result = "bustimeResult";
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select route_nm, st_sta_nm, ed_sta_nm from route where route_nm LIKE '%" + text + "%'");
			try{
				while(rs.next()){
					String route_nm = rs.getString("route_nm");
					String st_sta_nm = rs.getString("st_sta_nm");
					String ed_sta_nm = rs.getString("ed_sta_nm");
					
					result = result + "|" + route_nm + "|<<" + st_sta_nm + ">>~<<" + ed_sta_nm + ">>";
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
		
		return result;
	}
	
	//버스 정보
	public String busTimeInfo(String text){
		String result = "currentBusTimeInfo";
		
		for(int i = 0; i < all_route.size(); i++){
			if(all_route.elementAt(i).getRouteNM().equals(text)){
				String up_first_time = all_route.elementAt(i).getUpFirstTime();
				String up_last_time = all_route.elementAt(i).getUpLastTime();
				int peek_alloc = all_route.elementAt(i).getPeekAlloc();
				int npeek_alloc = all_route.elementAt(i).getNPeekAlloc();
				result = result + "|" + up_first_time + "|" + up_last_time + "|" + peek_alloc + "|" + npeek_alloc ;
			}
		}
		
		return result;
	}
	
	//노선 정보 검색
	public String searchRouteResult(String text){
		String result = "routeSearchResult";
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select route_nm, st_sta_nm, ed_sta_nm from route where route_nm LIKE '%" + text + "%'");
			try{
				while(rs.next()){
					String route_nm = rs.getString("route_nm");
					String st_sta_nm = rs.getString("st_sta_nm");
					String ed_sta_nm = rs.getString("ed_sta_nm");
					
					result = result + "|" + route_nm + "|<<" + st_sta_nm + ">>~<<" + ed_sta_nm + ">>";
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
		
		return result;
	}
	
	//정류소 정보 검색
	public String searchStationResult(String text){
		String result = "stationSearchResult";
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select station_id, station_nm from station where station_nm LIKE '%" + text + "%'");
			try{
				while(rs.next()){
					String station_id = rs.getString("station_id");
					String station_nm = rs.getString("station_nm");
					String default_text = ">>경희대학교 방면";
					
					result = result + "|" + station_id + "|" + station_nm + "|" + default_text;
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
		
		return result;
	}
	
	//노선 위치
	public String routePosition(String route_name, String bookmark_info) throws IOException{
		//currentResult|노선 id|9번 영통역|>>경희대학교 방면|x|y|남은 좌석 수|true
		//맨 뒤에 toggle값은 bookmark에서 확인해보고 붙여야 함.(일단 빼고)
		String route_id = "", stationID = "", stationNM = "", x = "", y = "", direction = "", bookmark = "false";
		String result = "currentResult";
		int remainSeatCount = 0;
		
        ///////////////////////////////////////////////////////////////////////////////////////////
		for(int i = 0; i < all_route.size(); i++){
			if(all_route.elementAt(i).getRouteNM().equals(route_name)){
				route_id = all_route.elementAt(i).getRouteID();
				direction = ">>" + all_route.elementAt(i).getStStaNM();
			}
		}
		/////////////////////////////////북마크 체크/////////////////////////////////////////////////
		StringTokenizer t = new StringTokenizer(bookmark_info, "|");
		String protocol = t.nextToken();
		while(t.hasMoreElements()){
			if(route_name.equals(t.nextToken())){
				bookmark = "true";
			}
		}
		
		////////////////////////////////현재 위치 API값 받아오기////////////////////////////////////////
		StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/buslocationservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=M41JaRzOkoBYMF6MLAl5zCLF%2BOqERx0Y0RKGnYWrmQx31QlfZfg9%2FsmL3Yxn47NQdmhhkO%2F7sF5I9RBlcUaPeg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(route_id, "UTF-8")); /*노선 ID*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        
		/////////////////////////////////결과 분석/////////////////////////////////////////////////
        StringTokenizer tt = new StringTokenizer(sb.toString(), "<>");
        while(tt.hasMoreElements()){
        	String message = tt.nextToken();
        	if(message.equals("busLocationList")){
        		remainSeatCount = 0;
        		stationID = "";
        	}
        	if(message.equals("remainSeatCnt")){
        		remainSeatCount = Integer.parseInt(tt.nextToken());
        	}
        	else if(message.equals("stationId")){
        		stationID = tt.nextToken();
        		
        		for(int i = 0; i < all_station.size(); i++){
        			if(all_station.elementAt(i).getStationID().equals(stationID)){
        				stationNM = all_station.elementAt(i).getStationNM();
        				x = all_station.elementAt(i).getX();
        				y = all_station.elementAt(i).getY();
        				
        				result = result + "|" + route_id + "|" + route_name + "|" + stationNM + "|" + route_name + "번 " + stationNM + "역" + "|" + direction + "|" 
            					+ x + "|" + y + "|" + remainSeatCount + "|" + bookmark;
        			}
        		}
        	}
        }
		return result;
	}
	
	//여기서 bookmark 목록 확인해서 보냄.
	public String stationPosition(String station_id, String station_name, String bookmark_info) throws IOException{
		System.out.println("위치 함수 시작!");
		String result = "currentResult";
		Vector<String> route_id = new Vector<String>();
		Vector<StationPos> all_stationpos = new Vector<StationPos>();
		
		int first_locationNo = 0; 
		String first_locationStationID = "", first_locationTime = "", first_x = "", first_y = "";
		int first_seat = 0;
		int second_locationNo = 0;
		String second_locationStationID = "", second_locationTime = "", second_x = "", second_y = "";
		int second_seat = 0, sta_order = 0;
		String tt_route_id = "";
		String api_result = "";
		
		
		for(int i = 0; i < all_routestation.size(); i++){
			if(all_routestation.elementAt(i).getStationID().equals(station_id)){
				String temp = all_routestation.elementAt(i).getRouteID();
				route_id.add(temp);
			}
		}
		///////////////////////////////////모든 ROUTE_ID 뽑아내기///////////////////////////////
		int count = route_id.size();
		String temp_route_id = "";
		for(int i = 0; i < count; i++){
			temp_route_id = route_id.get(i);
			StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busarrivalservice"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=M41JaRzOkoBYMF6MLAl5zCLF%2BOqERx0Y0RKGnYWrmQx31QlfZfg9%2FsmL3Yxn47NQdmhhkO%2F7sF5I9RBlcUaPeg%3D%3D"); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(station_id, "UTF-8")); /*정류소 ID*/
			urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(temp_route_id, "UTF-8")); /*노선 ID*/
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();
			api_result = api_result + sb.toString();
		}
		System.out.println(api_result);
		
		///////////////////////////////////API 요청//////////////////////////////////////////
        StringTokenizer tt = new StringTokenizer(api_result, "<>");
        while(tt.hasMoreElements()){
        	String message = tt.nextToken();
        	System.out.println("메세지 : " + message);
        	if(message.equals("busArrivalItem")){
        		first_locationNo = 0;
        		first_locationStationID = "";
        		first_locationTime = "";
        		first_x = "";
        		first_y = "";
        		first_seat = 0;
        		second_locationNo = 0;
        		second_locationStationID = "";
        		second_locationTime = "";
        		second_x = "";
        		second_y = "";
        		second_seat = 0;
        	}
        	else if(message.equals("locationNo1")){
        		String temp = tt.nextToken();
        		if(temp.equals("/locationNo1")){}
        		else
        			first_locationNo = Integer.parseInt(temp);
        	}
        	else if(message.equals("locationNo2")){
        		String temp = tt.nextToken();
        		if(temp.equals("/locationNo2")){}
        		else
        			second_locationNo = Integer.parseInt(temp);
        	}
        	else if(message.equals("predictTime1")){
        		first_locationTime = tt.nextToken();
        	}
        	else if(message.equals("predictTime2")){
        		second_locationTime = tt.nextToken();
        	}
        	else if(message.equals("remainSeatCnt1")){
        		String temp = tt.nextToken();
        		if(temp.equals("/remainSeatCnt1")){}
        		else
        			first_seat = Integer.parseInt(temp);
        	}
        	else if(message.equals("remainSeatCnt2")){
        		String temp = tt.nextToken();
        		if(temp.equals("/remainSeatCnt2")){}
        		else
        			second_seat = Integer.parseInt(temp);
        	}
        	else if(message.equals("routeId")){
        		tt_route_id = tt.nextToken();
        	}
        	else if(message.equals("staOrder")){
        		String temp = tt.nextToken();
        		if(temp.equals("/staOrder")){}
        		else
        			sta_order = Integer.parseInt(temp);
        	}
        	else if(message.equals("/busArrivalItem")){
        		//stationID, X, Y좌표 계산.
        		//현재 정류장의 위치 받아오기. => 몇 역전이라는 정보를 이용해 위치 계산 => 그 위치에 해당하는 stationid받아오기
        		//=> station db에서 x, y좌표값 받아오기 => station vector에 저장.
        		int temp_pos = sta_order - first_locationNo;
        		int temp_pos2 = sta_order - second_locationNo;
        		//ex) route_id = 200000103, station_id = 203000114
        		String temp_stationID = "", temp_stationID2 = "", temp_stationNM = "", temp_stationNM2 = "";
        		
        		for(int i = 0; i < all_routestation.size(); i++){
        			if(all_routestation.elementAt(i).getRouteID().equals(tt_route_id) && all_routestation.elementAt(i).getStaOrder() == temp_pos){
        				temp_stationID = all_routestation.elementAt(i).getStationID();
        			}
        		}
        		
        		for(int i = 0; i < all_routestation.size(); i++){
        			if(all_routestation.elementAt(i).getRouteID().equals(tt_route_id) && all_routestation.elementAt(i).getStaOrder() == temp_pos2){
        				temp_stationID2 = all_routestation.elementAt(i).getStationID();
        			}
        		}
        		///////////////////////////station_id값 불러오기////////////////////////////////////////////
        		for(int i = 0; i < all_station.size(); i++){
        			if(all_station.elementAt(i).getStationID().equals(temp_stationID)){
        				temp_stationNM = all_station.elementAt(i).getStationNM();
        				first_x = all_station.elementAt(i).getX();
        				first_y = all_station.elementAt(i).getY();
        			}
        			
        			if(all_station.elementAt(i).getStationID().equals(temp_stationID2)){
        				temp_stationNM2 = all_station.elementAt(i).getStationNM();
        				second_x = all_station.elementAt(i).getX();
        				second_y = all_station.elementAt(i).getY();
        			}
        		}
        		
        		//String r_id, String s_id, int no, String l_id, String l_nm, String time, int seat, String _x, String _y
        		if(first_locationNo == 0){}
        		else{
        			StationPos new_station = new StationPos(tt_route_id, station_id, first_locationNo, temp_stationID, temp_stationNM, first_locationTime, first_seat, first_x, first_y);
        			all_stationpos.add(new_station);
        		}
        		if(second_locationNo == 0){}
        		else{
        			StationPos new_station2 = new StationPos(tt_route_id, station_id, second_locationNo, temp_stationID2, temp_stationNM2, second_locationTime, second_seat, second_x, second_y);
        			all_stationpos.add(new_station2);
        		}
        	}
        }
        
		///////////////////////////////////Tokenizer로 분류하여 변수들 저장///////////////////////
        //currentResult|노선 id|9번 영통역|경희대학교 방면|x|y|남은 좌석 수|true
        for(int i = 0; i < all_stationpos.size(); i++){
        	String for_route_id = all_stationpos.elementAt(i).getRouteID();
        	String direction = "", route_nm = "", bookmark = "false";
        	
        	for(int j = 0; j < all_route.size(); j++){
        		if(all_route.elementAt(i).getRouteID().equals(for_route_id)){
        			route_nm = all_route.elementAt(i).getRouteNM();
        			direction = ">>" + all_route.elementAt(i).getStStaNM();
        		}
        	}
        
        	StringTokenizer t = new StringTokenizer(bookmark_info, "|");
			String protocol = t.nextToken();
			while(t.hasMoreElements()){
				if(route_nm.equals(t.nextToken())){
					bookmark = "true";
				}
			}
        	//currentResult|200000103|9|경희대학교|9번 경희대학교역|경희대학교 방면|127.0776333|37.2477833|-1|true
        	String position = route_nm + "번 " + all_stationpos.elementAt(i).getLocatioNo() + "역 전 (" + all_stationpos.elementAt(i).getLocationTime() + "분)";
        	result = result + "|" + all_stationpos.elementAt(i).getRouteID() + "|" + route_nm + "|" + all_stationpos.elementAt(i).getLocationStationNM() + "|" + position + "|" + direction + "|" + all_stationpos.elementAt(i).getX() + "|"
        			 + all_stationpos.elementAt(i).getY() + "|" + all_stationpos.elementAt(i).getRemainSeatCnt() + "|" + bookmark;
        } 
        System.out.println(result);
        return result;
	}
}
