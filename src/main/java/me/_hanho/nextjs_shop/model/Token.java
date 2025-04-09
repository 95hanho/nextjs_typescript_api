package me._hanho.nextjs_shop.model;

public class Token {

	private int token_id;
	private String connect_ip;
	private String connect_agent;
	private String refresh_token;
	private String id;
	
	public Token() {
		// TODO Auto-generated constructor stub
	}

	public Token(int token_id, String connect_ip, String connect_agent, String refresh_token, String id) {
		super();
		this.token_id = token_id;
		this.connect_ip = connect_ip;
		this.connect_agent = connect_agent;
		this.refresh_token = refresh_token;
		this.id = id;
	}

	public Token(String connect_ip, String connect_agent, String refresh_token, String id) {
		super();
		this.connect_ip = connect_ip;
		this.connect_agent = connect_agent;
		this.refresh_token = refresh_token;
		this.id = id;
	}
	
	public Token(String connect_ip, String connect_agent, String refresh_token) {
		super();
		this.connect_ip = connect_ip;
		this.connect_agent = connect_agent;
		this.refresh_token = refresh_token;
	}

	public int getToken_id() {
		return token_id;
	}

	public void setToken_id(int token_id) {
		this.token_id = token_id;
	}

	public String getConnect_ip() {
		return connect_ip;
	}

	public void setConnect_ip(String connect_ip) {
		this.connect_ip = connect_ip;
	}

	public String getConnect_agent() {
		return connect_agent;
	}

	public void setConnect_agent(String connect_agent) {
		this.connect_agent = connect_agent;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Token [token_id=" + token_id + ", connect_ip=" + connect_ip + ", connect_agent=" + connect_agent
				+ ", refresh_token=" + refresh_token + ", id=" + id + "]";
	}
}
