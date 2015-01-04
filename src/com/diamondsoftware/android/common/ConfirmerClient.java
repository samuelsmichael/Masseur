package com.diamondsoftware.android.common;

import java.util.ArrayList;

public interface ConfirmerClient {
	void heresYourAnswer(boolean saidYes, ArrayList<Object> otherData);
}
