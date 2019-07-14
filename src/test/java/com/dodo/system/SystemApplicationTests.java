package com.dodo.system;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemApplicationTests {

	@Test
	public void contextLoads() {

		String start = "2019-07-18";
		String end = "2019-08-05";
		int myDay = 15;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate = formatter.parse(start);
			Date endDate = formatter.parse(end);

			// 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
			long diff = endDate.getTime() - beginDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.println("날짜차이=" + diffDays);

			if(myDay < diffDays){
				System.out.println("으추으추");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}


	}
}
