package module.remnant.datapanel.data.validation;//package egps.remnant.vcfoperator.datapanel.data.validation;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.List;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//import egps.remnant.vcfoperator.datapanel.data.DataFormat;
//import egps.remnant.vcfoperator.datapanel.data.DataFormatError;
//
///**
// * 
// * Copyright © 2018 Chinese Academy of Sciences. All rights reserved.
// *
// * 
// * 
// * @Title: ValidateProteomics.java
// * 
// * @Prject: eGPS_beta_v1.0
// * 
// * @Package: egps.data
// * 
// * @author: mhl
// * 
// * @date: 2018/05/22 14:45:51
// * 
// * @version: V1.0
// */
//public class ValidateSimulator implements FormatDetect,CommonValidate {
//	private File inputFile;
//
//	public ValidateSimulator(File file) {
//		this.inputFile = file;
//	}
//
//	@Override
//	public DataforamtInfo getFileFormat() {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(inputFile));
//
//			StringBuffer sb = new StringBuffer();
//			String readLine = null;
//			while ((readLine = br.readLine()) != null) {
//				sb.append(readLine);
//			}
//			
//
//			JSONArray jsonArray = JSONArray.parseArray(sb.toString());
//
//			for (int i = 0; i < jsonArray.size(); i++) {
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//				String string = jsonObject.toString();
//
//				jsonObject.getString("id");
//				jsonObject.getString("type");
//				jsonObject.getString("name");
//				jsonObject.getString("N0");
//				jsonObject.getString("N1");
//				jsonObject.getString("startTime");
//				jsonObject.getString("endTime");
//
//				if (string.contains("parent")) {
//					jsonObject.getString("parent");
//				}
//				if (string.contains("K")) {
//					jsonObject.getString("K");
//
//				}
//				if (string.contains("T0")) {
//					jsonObject.getString("T0");
//				}
//
//			}
//			
//			br.close();
//		} catch (Exception e) {
//			return new DataforamtInfo(false,DataFormatError.SIMULATOREERROR);
//		}
//		return new DataforamtInfo(true,DataFormat.SIMULATOR);
//	}
//
//	@Override
//	public boolean detectFormat(List<String> strings) {
//		if (strings.size() > 1) {
//			return false;
//		}
//		String firString = strings.get(0);
//		if (!firString.startsWith("[{\"id\":")) {
//			return false;
//		}
//		
//		if (firString.endsWith("}]")) {
//			return false;
//		}
//		
//		return true;
//	}
//}
