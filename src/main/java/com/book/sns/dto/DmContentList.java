package com.book.sns.dto;

public class DmContentList {
	
	private int dmNum;
	private String dmUserId;
	private String toId;
    private String dmContent;
    private String dmRegdate;
	
    public DmContentList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DmContentList(int dmNum, String dmUserId, String toId, String dmContent, String dmRegdate) {
		super();
		this.dmNum = dmNum;
		this.dmUserId = dmUserId;
		this.toId = toId;
		this.dmContent = dmContent;
		this.dmRegdate = dmRegdate;
	}

	public int getDmNum() {
		return dmNum;
	}

	public void setDmNum(int dmNum) {
		this.dmNum = dmNum;
	}

	public String getDmUserId() {
		return dmUserId;
	}

	public void setDmUserId(String dmUserId) {
		this.dmUserId = dmUserId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getDmContent() {
		return dmContent;
	}

	public void setDmContent(String dmContent) {
		this.dmContent = dmContent;
	}

	public String getDmRegdate() {
		return dmRegdate;
	}

	public void setDmRegdate(String dmRegdate) {
		this.dmRegdate = dmRegdate;
	}

	@Override
	public String toString() {
		return "DmContentList [dmNum=" + dmNum + ", dmUserId=" + dmUserId + ", toId=" + toId + ", dmContent="
				+ dmContent + ", dmRegdate=" + dmRegdate + "]";
	}
	
    
}
