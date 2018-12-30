
public class Route {
	String route_id = "";
	String route_nm = "";
	String route_tp = "";
	String st_sta_id = "";
	String st_sta_nm = "";
	String st_sta_no = "";
	String ed_sta_id = "";
	String ed_sta_nm = "";
	String ed_sta_no = "";
	String up_first_time = "";
	String up_last_time = "";
	String down_first_time = "";
	String down_last_time = "";
	int peek_alloc = 0;
	int npeek_alloc = 0;
	
	public Route(){}
	public Route(String r_id, String r_nm, String r_tp, String s_id, String s_nm, String s_no, String e_id, String e_nm, String e_no,
			String uf_time, String ul_time, String df_time, String dl_time, int peek, int npeek){
		route_id = r_id;
		route_nm = r_nm;
		route_tp = r_tp;
		st_sta_id = s_id;
		st_sta_nm = s_nm;
		st_sta_no = s_no;
		ed_sta_id = e_id;
		ed_sta_nm = e_nm;
		ed_sta_no = e_no;
		up_first_time = uf_time;
		up_last_time = ul_time;
		down_first_time = df_time;
		down_last_time = dl_time;
		peek_alloc = peek;
		npeek_alloc = npeek;
	}
	
	public String getRouteID(){return route_id;}
	public String getRouteNM(){return route_nm;}
	public String getRouteTP(){return route_tp;}
	public String getStStaID(){return st_sta_id;}
	public String getStStaNM(){return st_sta_nm;}
	public String getStStaNO(){return st_sta_no;}
	public String getEdStaID(){return ed_sta_id;}
	public String getEdStaNM(){return ed_sta_nm;}
	public String getEdStaNo(){return ed_sta_no;}
	public String getUpFirstTime(){return up_first_time;}
	public String getUpLastTime(){return up_last_time;}
	public String getDownFirstTime(){return down_first_time;}
	public String getDownLastTime(){return down_last_time;}
	public int getPeekAlloc(){return peek_alloc;}
	public int getNPeekAlloc(){return npeek_alloc;}
	
	public void setRouteID(String id){route_id = id;}
	public void setRouteNM(String nm){route_nm = nm;}
	public void setRouteTP(String tp){route_tp = tp;}
	public void setStStaID(String id){st_sta_id = id;}
	public void setStStaNM(String nm){st_sta_nm = nm;}
	public void setStStaNO(String no){st_sta_no = no;}
	public void setEdStaID(String id){ed_sta_id = id;}
	public void setEdStaNM(String nm){ed_sta_nm = nm;}
	public void setEdStaNO(String no){ed_sta_no = no;}
	public void setUpFirstTime(String time){up_first_time = time;}
	public void setUpLastTime(String time){up_last_time = time;}
	public void setDownFirstTime(String time){down_first_time = time;}
	public void setDownLastTime(String time){down_last_time = time;}
	public void setPeekAlloc(int _alloc){peek_alloc = _alloc;}
	public void setNPeekAlloc(int _alloc){npeek_alloc = _alloc;}
}
