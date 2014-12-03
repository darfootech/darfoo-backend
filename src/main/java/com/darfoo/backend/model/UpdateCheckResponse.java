package com.darfoo.backend.model;

public class UpdateCheckResponse {
	
	int videoUpdate;//若值被置为1，则表明要更新的video对象不存在于数据库中
	int musicUpdate;//若值被置为1，则表明要更新的music对象不存在于数据库中
	int educationUpdate;//若值被置为1，则表明要更新的education对象不存在于数据库中
	int imageUpdate;
	//int imageRepeat;
	int authorUpdate;
	//int authorRepeat;
	
	public UpdateCheckResponse(){
		this.videoUpdate = 0;
		this.musicUpdate = 0;
		this.authorUpdate = 0;
		this.imageUpdate = 0;
	}
	
	public int getEducationUpdate() {
		return educationUpdate;
	}

	public void setEducationUpdate(int educationUpdate) {
		this.educationUpdate = educationUpdate;
	}

	public int getVideoUpdate() {
		return videoUpdate;
	}
	public void setVideoUpdate(int videoUpdate) {
		this.videoUpdate = videoUpdate;
	}
	public int getImageUpdate() {
		return imageUpdate;
	}
	public void setImageUpdate(int imageUpdate) {
		this.imageUpdate = imageUpdate;
	}
	public int getAuthorUpdate() {
		return authorUpdate;
	}
	public void setAuthorUpdate(int authorUpdate) {
		this.authorUpdate = authorUpdate;
	}
	
	public int getMusicUpdate() {
		return musicUpdate;
	}

	public void setMusicUpdate(int musicUpdate) {
		this.musicUpdate = musicUpdate;
	}

	/**
	 * 是否准备好更新
	 * @return true 准备好更新  false 没有准备好更新
	 * **/
	public boolean updateIsReady(){
		boolean isReady = true;
		if(videoUpdate==1||musicUpdate==1||educationUpdate==1||imageUpdate==1||authorUpdate==1){
			isReady = false;
		}
		return isReady;
	}
	
}
