package com.vunke.test;

import com.jfinal.core.JFinal;

import java.text.ParseException;
import java.util.Random;

public class TestMain {


	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) throws ParseException {
		JFinal.start("C:\\Users\\Administrator\\Desktop\\onlyWeb\\webShareHome\\ShareHome\\WebRoot", 8080, "/ShareHome", 5);
	//	System.out.println(System.currentTimeMillis());

	   /* Date date = DateUtils.parseDate("2016-6-2.19.21. 50. 0", new String[]{"yyyy-MM-dd.HH.mm.ss.SS"});
        System.out.println(date.toString());*/
    }


	//生成随机数字和字母,
	public String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		//参数length，表示生成几位随机数
		for(int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			//输出字母还是数字
			if( "char".equalsIgnoreCase(charOrNum) ) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char)(random.nextInt(26) + temp);
			} else if( "num".equalsIgnoreCase(charOrNum) ) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
	
}
