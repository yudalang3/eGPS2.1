package module.datetimecalculator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.CharMatcher;

import egps2.frame.gui.comp.SimplestWorkBench;
import egps2.EGPSProperties;

@SuppressWarnings("serial")
public class WorkBanch4presenter extends SimplestWorkBench {

	private final char dateSplitter = '-';

	private GuiMain guiMain;

	public WorkBanch4presenter(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	protected Pair<String, String> giveExample() {
		StringBuilder sBuilder = EGPSProperties.getSpecificationHeader();
		sBuilder.append("# format startDate[Tab]endDate[Tab]passedDays\n");
		sBuilder.append("# please keep the TWO tab key\n");
		sBuilder.append("2019-10\t2019-12\t\n");
		sBuilder.append("2019-10-01\t2019-12-30\t\n");
		sBuilder.append("2020-01-05\t\t8\n");
		sBuilder.append("2020-01-05\t\t-5");

		String left = sBuilder.toString();
		sBuilder.setLength(0);
		sBuilder.append("# Start date\tEnd date\t passed days\n");
		sBuilder.append("2019-10	2019-12	2\n");
		sBuilder.append("2019-10-01	2019-12-30	90\n");
		sBuilder.append("2020-01-05	2020-01-13	8\n");
		sBuilder.append("2020-01-05	2019-12-31	-5\n");

		return Pair.of(left, sBuilder.toString());
	}

	@Override
	protected void handle(List<String> inputStrings) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("# Start date\tEnd date\t passed days\n");

		for (String string : inputStrings) {

			String[] split = string.split("\t", -2);
			if (split.length < 3) {
				sBuilder.append("E r r o r\n");
			} else {
				sBuilder.append(handleDate(split));
			}
		}

		sBuilder.deleteCharAt(sBuilder.length() - 1);

		bottomTextarea.setText(sBuilder.toString());
	}

	private String handleDate(String[] split) {
		String startDateString = split[0];
		String endDateString = split[1];
		String passedDaysString = split[2];

		if (startDateString.isEmpty()) {
			return "E r r o r";
		}
		
		DateTimeFormatter dateTimeFormatter = null;
		int passedTime = 0;
		Temporal startDate = null;
		Temporal endDate = null;
		ChronoUnit chronoUnit = null;
		boolean isYYYYMM = true;

		String dateStr = startDateString.isEmpty() ? endDateString : startDateString;
		int countIn = CharMatcher.is(dateSplitter).countIn(dateStr);
		if (countIn == 1) {
			// 2019-10
			dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
			startDate = YearMonth.parse(startDateString, dateTimeFormatter);
			chronoUnit = ChronoUnit.MONTHS;
			guiMain.recordYYYYMMformat();
		}else {
			// 2019-10-15
			dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			startDate = LocalDate.parse(startDateString, dateTimeFormatter);
			chronoUnit = ChronoUnit.DAYS;
			isYYYYMM = false;
		}
		
		
		if (endDateString.isEmpty()) {
			if (passedDaysString.isEmpty()) {
				return "E r r o r";
			} else {
				passedTime = Integer.parseInt(passedDaysString);
				endDate = startDate.plus(passedTime,chronoUnit);
				guiMain.recordTheDatetimeAfterDuration();
			}
		} else {
			if (passedDaysString.isEmpty()) {
				if (isYYYYMM) {
					endDate = YearMonth.parse(endDateString, dateTimeFormatter);
				}else {
					endDate = LocalDate.parse(endDateString, dateTimeFormatter);
				}
				passedTime = (int) chronoUnit.between(startDate, endDate);
				guiMain.recordComputEelapsedTime();
			} else {
				return "E r r o r";
			}
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(dateTimeFormatter.format(startDate)).append("\t");
		sBuilder.append(dateTimeFormatter.format(endDate)).append("\t");
		sBuilder.append(String.valueOf(passedTime)).append("\n");

		return sBuilder.toString();

		
	}


}
