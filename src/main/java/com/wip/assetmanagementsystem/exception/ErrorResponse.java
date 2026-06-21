package com.wip.assetmanagementsystem.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private String path;
    public ErrorResponse() {}

    
    public ErrorResponse(int statusCode, String message,String error, String path) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
    

   