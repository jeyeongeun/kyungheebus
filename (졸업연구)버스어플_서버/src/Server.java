import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;


public class Server {
	private int port = 5589;
	private ServerSocket server_socket;
	private Socket socket;
	private Vector<Bookmark> all_bookmark = new Vector<Bookmark>();
	private String bookmark_info = "bookmark_info";
	BusAPI busapi = new BusAPI();
	
	public Server(){
		initiateBookmark();
		startNetwork();
		connect();
	}
	
	private void startNetwork(){
		try{
			server_socket = new ServerSocket(port);
			System.out.println("네트워크 연결중...");
			connect();
		} catch(IOException e){
			System.out.println("네트워크 연결에 실패하였습니다.");
		} catch(Exception e){
			System.out.println("네트워크 연결에 실패하였습니다.");
		}
	}
	
	private void connect(){
		Thread th = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
					System.out.println("접속 시도중....");
					socket = server_socket.accept();
					System.out.println("성공!!");
					
					ServerThread servThread = new ServerThread(socket);
					servThread.start();
					} catch(IOException e){
						System.out.println("접속에 실패하였습니다.");
					}
				}
			}
		});
		th.start();
	}
	
	public void initiateRoute(){}
	public void initiateRouteLine(){}
	public void initiateRouteStation(){}
	public void initiateStation(){}
	public void initiateBookmark(){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("DB CONNECTION");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
			System.out.println("DB CONNECTION SUCCESS!");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select route_id, route_nm, direction from bookmark");
			try{
				while(rs.next()){
					String route_id = rs.getString("route_id");
					String route_nm = rs.getString("route_nm");
					String direction = rs.getString("direction");
					
					Bookmark newmark = new Bookmark();
					newmark.setRouteID(route_id);
					newmark.setRouteNM(route_nm);
					newmark.setDirection(direction);
					all_bookmark.add(newmark);
					
					bookmark_info = bookmark_info + "|" + route_nm;
				}
			}catch(Exception ex){}
			
		}catch(ClassNotFoundException cnfe){
			System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
		}catch(SQLException se){
			System.out.println(se.getMessage());
		}
	}
	
	public void initiate(){
		initiateRoute();
		initiateRouteLine();
		initiateRouteStation();
		initiateStation();
		initiateBookmark();
	}
	
	class ServerThread extends Thread{
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket thread_socket;
		Thread thread;
		private StringBuffer buf = new StringBuffer(4096);
		
		public ServerThread(){
			this.thread_socket = new Socket();
			thread = this;
			setStream();
		}
		public ServerThread(Socket newsock){
			this.thread_socket = newsock;
			thread = this;
			setStream();
		}
		
		private void setStream(){
			try{
				is = thread_socket.getInputStream();
				dis = new DataInputStream(is);
				os = thread_socket.getOutputStream();
				dos = new DataOutputStream(os);
			} catch(IOException e){
				System.out.println("스트림 설정에 문제가 발생했습니다.\n");
			}
		}
		
		public void run() {
			try{
				Thread currentThread = Thread.currentThread();
				
				while(currentThread == thread){
					String msg = dis.readUTF();
					System.out.println("received message : " + msg);
					inmessage(msg);
				}
			}catch(IOException e){
				System.out.println("Fail");
			}	
		}
		
		private void inmessage(String str) throws IOException{
			StringTokenizer st = new StringTokenizer(str, "|");
			String protocol = st.nextToken();
			
			if(protocol.equals("routeSearch"))
				getRouteSearch(str);
			else if(protocol.equals("stationSearch"))
				getStationSearch(str);
			else if(protocol.equals("routeCurrentPosition"))
				getRouteCurrentPosition(str);
			else if(protocol.equals("stationCurrentPosition"))
				getStationCurrentPosition(str);
			else if(protocol.equals("bustimeSearch"))
				getBusTimeSearch(str);
			else if(protocol.equals("bustimeInfo"))
				getCurrentBusTime(str);
			else if(protocol.equals("addBookMark"))
				getAddBookmark(str);
			else if(protocol.equals("deleteBookMark"))
				getDeleteBookmark(str);
			else if(protocol.equals("requestAllRoute"))
				getAllRouteInfo(str);
			else if(protocol.equals("requestAllBookmark"))
				getAllBookmark(str);			
		}
		
		// 노선 검색 결과 요청
		// C->S : routeSearch|버스 이름
		// S->C : routeSearchResult|버스 이름|<<첫 정류장>>~<<마지막 정류장>>|...
		public void getRouteSearch(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String text = t.nextToken();
			String result = busapi.searchRouteResult(text);
			
			System.out.println(result);
			sendMessage(result);
		}
		////////////////id도 다 같이 보내줘야 함.(중복 대비)//////////
		// 정류소 검색 결과 요청
		// C->S : stationSearch|정류소 이름
		// S->C : stationSearchResult|정류소 번호|정류소 이름|>> 방향|... 
		public void getStationSearch(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String text = t.nextToken();
			String result = busapi.searchStationResult(text);
			
			System.out.println(result);
			sendMessage(result);
		}
		
		//
		// C->S : routeCurrentPosition|버스 이름
		// S->C : currentResult|노선 id|9번 전전역(5분)|경희대학교 방면|true|x|y
		public void getRouteCurrentPosition(String str) throws IOException{
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String text = t.nextToken();
			String result = busapi.routePosition(text, bookmark_info);
			
			System.out.println(result);
			sendMessage(result);
		}
		
		//
		// C->S : stationCurrentPosition|정류소 번호|정류소 이름
		// S->C : currentResult|노선 id|9번 4전역(11분)|경희대학교 방면|true|x|y
		public void getStationCurrentPosition(String str) throws IOException{
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String id = t.nextToken();
			String name = t.nextToken();
			String result = busapi.stationPosition(id,name,bookmark_info);
			
			System.out.println(result);
			sendMessage(result);
		}
		
		// C->S : bustimeSearch|버스 이름
		// S->C : bustimeResult|9|<<경희대학교>>~<<수원역>>|1112|<<경희대학교>>~<<수원역>>
		public void getBusTimeSearch(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String text = t.nextToken();
			String result = busapi.searchBustime(text);
			
			System.out.println(result);
			sendMessage(result);
		}
		
		// C->S : bustimeInfo|버스 이름
		// S->C : currentBusTimeInfo|8:00|23:00|10|13
		public void getCurrentBusTime(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String text = t.nextToken();
			String result = busapi.busTimeInfo(text);
			
			System.out.println(result);
			sendMessage(result);
		}
		
		//C->S : addBookMark|노선 아이디|방향
		//S->C : bookMark|노선 번호|방향|...
		public void getAddBookmark(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String route_id = t.nextToken();
			String direction = t.nextToken();
			String route_nm = "";
			String result = "bookMark";
			
			//////////////////////////////////노선 번호 받아오기//////////////////////////////////////////////////////
			Connection connection = null;
			Statement stmt = null;
			
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("DB CONNECTION");
				
				connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
				System.out.println("DB CONNECTION SUCCESS!");
				
				stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("select route_nm from route where route_id = '" + route_id + "'");
				try{
					while(rs.next()){
						route_nm = rs.getString("route_nm");
					}
				}catch(Exception ex){}
				
			}catch(ClassNotFoundException cnfe){
				System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			
			Bookmark newmark = new Bookmark();
			newmark.setRouteID(route_id);
			newmark.setRouteNM(route_nm);
			newmark.setDirection(direction);
			all_bookmark.add(newmark);
			
			//DB에도 추가.
			//234000016/1112/<<경희대차고지>>~<<동부지방법원앞>>
			/*
			Statement stmt2 = null;
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("DB CONNECTION");
				
				connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
				System.out.println("DB CONNECTION SUCCESS!");
				//stmt.executeQuery(query);
				stmt2 = connection.createStatement();
				stmt2.executeQuery("insert into bookmark values('" + route_id + "','" + route_nm + "','" + direction + "')");
			}catch(ClassNotFoundException cnfe){
				System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}*/
			
			
			//////////////////////////////모든 북마크 정보 갱신을 위해 전달/////////////////////////////////////////////
			bookmark_info = "bookmark_info";
			for(int i = 0; i < all_bookmark.size(); i++){
				result = result + "|" + all_bookmark.elementAt(i).getRouteNM() + "|>>" + all_bookmark.elementAt(i).getDirection();
				bookmark_info = bookmark_info + "|" + all_bookmark.elementAt(i).getRouteNM();
			}
			
			System.out.println(result);
			sendMessage(result);
		}
		
		//C->S : deleteBookMark|노선 아이디
		//S->C : bookMark|노선 번호|방향|...
		public void getDeleteBookmark(String str){
			StringTokenizer t = new StringTokenizer(str, "|");
			String protocol = t.nextToken();
			String route_id = t.nextToken();
			String result = "bookMark";
			
			for(int i = 0; i < all_bookmark.size(); i++){
				if(all_bookmark.elementAt(i).getRouteID().equals(route_id)){
					all_bookmark.remove(i);
				}
			}
			
			//DB 삭제
			/*
			Connection connection = null;
			Statement stmt = null;
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("DB CONNECTION");
				
				connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
				System.out.println("DB CONNECTION SUCCESS!");
				stmt = connection.createStatement();
				stmt.executeQuery("delete from bookmark where route_id='" + route_id + "'");
			}catch(ClassNotFoundException cnfe){
				System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}*/
			bookmark_info = "bookmark_info";
			for(int i = 0; i < all_bookmark.size(); i++){
				result = result + "|" + all_bookmark.elementAt(i).getRouteNM() + "|>>" + all_bookmark.elementAt(i).getDirection();
				bookmark_info = bookmark_info + "|" + all_bookmark.elementAt(i).getRouteNM();
			}
			
			System.out.println(result);
			sendMessage(result);
		}
		
		//C->S : requestAllRoute
		//S->C : allRouteInfo|노선 번호|정류소 이름|....
		public void getAllRouteInfo(String str){
			Vector<RoutePos> all_route = new Vector<RoutePos>();			//모든 노선 정보
			Vector<String> all_route_nm = new Vector<String>();		//모든 노선이름 정보.
			boolean check = false;
			String result = "allRouteInfo";
			
			////////////////////////////////////////DB 로딩 및 세팅///////////////////////////////////////////////
			Connection conn = null;
			Statement stmt = null;
			
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("DB CONNECTION");
				
				conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
				System.out.println("DB CONNECTION SUCCESS!");
				
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select updown, sta_order, route_nm, station_nm from routestation");
				try{
					while(rs.next()){
						String updown = rs.getString("updown");
						int sta_order = rs.getInt("sta_order");
						String route_nm = rs.getString("route_nm");
						String station_nm = rs.getString("station_nm");
						check = false;
						
						for(int i = 0; i < all_route_nm.size(); i++){
							if(all_route_nm.elementAt(i).equals(route_nm)){
								check = true;
							}
						}
						
						if(check == false){
							all_route_nm.add(route_nm);
						}
						
						//if(updown.equals("정")){
							RoutePos newroute = new RoutePos();
							newroute.setInfo(updown, sta_order, route_nm, station_nm);
							all_route.add(newroute);
						//}
					}
				}catch(Exception ex){}
				
			}catch(ClassNotFoundException cnfe){
				System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			
			for(int i = 0; i < all_route_nm.size(); i++){
				System.out.println(all_route_nm.elementAt(i));
			}
			System.out.println("크기 : " + all_route.size());
			////////////////////////////////////////////////////////////////////////////////////////////////////
			//ed_sta_nm
			String temp_nm = "";
			for(int i = 0; i < all_route_nm.size(); i++){
				//끝 정거장 받아오기.
				try{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					System.out.println("DB CONNECTION");
					
					conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","hr","hr");
					System.out.println("DB CONNECTION SUCCESS!");
					
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select ed_sta_nm from route where route_nm='" + all_route_nm.elementAt(i)+"'");
					try{
						while(rs.next()){
							temp_nm = rs.getString("ed_sta_nm");
						}
					}catch(Exception ex){}
					
				}catch(ClassNotFoundException cnfe){
					System.out.println("DB CONNECTION FAIL." + cnfe.getMessage());
				}catch(SQLException se){
					System.out.println(se.getMessage());
				}
				for(int j = 0; j < all_route.size(); j++){
					for(int k = 0; k < all_route.size(); k++){
					//System.out.println("노선 번호 : " + all_route.elementAt(j).getRouteNM() + "순서 : " + all_route.elementAt(j).getOrder());
					if(all_route.elementAt(k).getRouteNM().equals(all_route_nm.elementAt(i)) && all_route.elementAt(k).getOrder() == j+1){
						result = result + "|" + all_route.elementAt(k).getRouteNM() + "|" + all_route.elementAt(k).getStationNM();
						}
					}
				}
				//result = result + "|" + all_route_nm.elementAt(i) + "|" + temp_nm;
			}
			System.out.println(result);
			sendMessage(result);
		}
		
		// C->S : requestAllBookmark
		// S->C : bookMark|노선 번호|방향|...
		public void getAllBookmark(String str){
			String result = "bookMark";
			for(int i = 0; i < all_bookmark.size(); i++){
				result = result + "|" + all_bookmark.elementAt(i).getRouteNM() + "|>>" + all_bookmark.elementAt(i).getDirection();
			}
			System.out.println(result);
			sendMessage(result);
		}
		
		private void sendMessage(String msg){
			try{
				dos.writeUTF(msg);
				dos.flush();
				System.out.println("Server side sending.");
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	} //ServerThread
	
	public static void main(String[] args) {
		new Server();
	}
	
}
