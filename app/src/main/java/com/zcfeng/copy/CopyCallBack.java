package com.zcfeng.copy;
public interface CopyCallBack {
	void ShowProcess(String filename, float percent);
	void ShowCheckProcess(String filename, float percent);
	void ShowCheckResult(String filename, Boolean res);
	void ShowFileName(String name);
	void showFilesSize(double size);
	void OnFinish();
	void OnError(String error);
	void ShowStata(int stata);
	void ShowTotalProcess(int d);
}
