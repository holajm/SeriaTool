/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android_serialport_api.Nfc;

public class ConsoleActivity extends SerialPortActivity {

	EditText mReception;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);

//		setTitle("Loopback test");
		mReception = (EditText) findViewById(R.id.EditTextReception);

		EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
		Emission.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int i;
				CharSequence t = v.getText();
				char[] text = new char[t.length()];
				for (i=0; i<t.length(); i++) {
					text[i] = t.charAt(i);
				}
				try {
					mOutputStream.write(new String(text).getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		int cardtype,check;
		int[] cardnumber;

		Nfc nfc=new Nfc();
		byte[] frameHeader=new byte[2];
		System.arraycopy(buffer, 0, frameHeader, 0, 2);
		nfc.setFrameHeader(bytesToHexString(frameHeader));

		byte[] CardTypes=new byte[1];
		System.arraycopy(buffer, 2, CardTypes, 0, 1);
		cardtype=CardTypes[0] & 0xFF;
		nfc.setCardType(bytesToHexString(CardTypes));

		byte[] CardNumbers=new byte[size-4];
		System.arraycopy(buffer, 3, CardNumbers, 0, size-4);
		cardnumber=bytetoInteger(CardNumbers);
		nfc.setCardNumber(bytesToHexString(CardNumbers));

		byte[] checkStr=new byte[1];
		System.arraycopy(buffer, size-1, checkStr, 0, 1);
		check=checkStr[0] & 0xFF;
		nfc.setCheckStr(bytesToHexString(checkStr));


		boolean f=xor(cardtype,cardnumber,check);


		final String bb="帧头："+nfc.getFrameHeader()+"卡类型："+nfc.getCardType()+"卡号："+nfc.getCardNumber()+"校验字节"+nfc.getCheckStr()+"是否校验成功:"+f;


		runOnUiThread(new Runnable() {
			public void run() {

				if (mReception != null) {
					mReception.append(bb);
				}
			}
		});
	}
	/**
	 *  校验 异或 运算
	 * @param cardType 卡类型
	 * @param cardNumber 卡号
	 * @param check 校验
	 * @return
	 */
	public boolean xor(int cardType,int[] cardNumber,int check){
		System.out.println("=====cardType======="+cardType);


		for(int i=0;i<cardNumber.length;i++){
			cardType=cardType^cardNumber[i];
		}

		if(check==cardType){
			return true;
		}
		return false;
	}

	public int[] bytetoInteger(byte[] src){
		int[] ints=new int[src.length];
		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;

			ints[i]=v;
		}

		return ints;
	}


	public  String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
