package module.backmutpres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BackMutsPresenter {

	boolean fullInformation = false;
	
	
	public void setFullInformation(boolean fullInformation) {
		this.fullInformation = fullInformation;
	}
	/**
	 * 
	 * 将一个突变字符串按照是否有multiple hit 展示成两列。
	 * 
	 * @title present
	 * @createdDate 2021-04-01 08:16
	 * @lastModifiedDate 2021-04-01 08:16
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param mutString
	 * @param seperator
	 * @return void
	 */
	public String present(String mutString, String seperator) {

		final String blankElement = "                  ";
		String[] split = mutString.split(seperator, -1);

		List<Mutation0302> listOfMutations = new ArrayList<>();

		for (String str : split) {

			Mutation0302 parseMutation = parseMutation(str, true);
			listOfMutations.add(parseMutation);
		}

		StringBuilder stringBuilder = new StringBuilder();

		Map<Integer, Integer> position2countMap = new HashMap<>();

		for (Mutation0302 mutation : listOfMutations) {

			int position = mutation.getPosition();
			Integer integer = position2countMap.get(position);
			String mutStringTemp = null;
			if (fullInformation) {
				mutStringTemp = mutation.getFullInformation();
			}else {
				mutStringTemp = mutation.toString();
			}
			if (integer == null) {
				stringBuilder.append(mutStringTemp).append("\n");
				position2countMap.put(position, 1);
			} else {
				stringBuilder.append(StringUtils.repeat(blankElement, integer)).append(mutStringTemp).append("\n");
				position2countMap.put(position, ++integer);
			}
			
			
		}

		return stringBuilder.toString();

	}

	public String presentWithComma(String mutString) {
		return present(mutString, ",");
	}
	
	public static Mutation0302 parseMutation(String str, boolean zeroBased) {
		int strLength = str.length();

		int firstDigitIndex = -1; // 0-based, inclusive
		for (int i = 0; i < strLength; i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c)) {
				firstDigitIndex = i;
				break;
			}
		}

		int lastDigitIndex = -1; // 0-based, exclusive
		for (int i = firstDigitIndex + 1; i < strLength; i++) {
			char c = str.charAt(i);
			if (!Character.isDigit(c)) {
				lastDigitIndex = i;
				break;
			}
		}

		String ancestralAllele = str.substring(0, firstDigitIndex);
		String derivedAllele = str.substring(lastDigitIndex);

		int pos = -1;
		if (zeroBased) {
			pos = Integer.parseInt(str.substring(firstDigitIndex, lastDigitIndex));
		} else {
			pos = Integer.parseInt(str.substring(firstDigitIndex, lastDigitIndex)) - 1; // Transited as 0-based.
		}

		Mutation0302 mut = new Mutation0302(pos, ancestralAllele, derivedAllele);

		return mut;
	}

	
	public static void main(String[] args) {
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,A10323G,T...T11288-,A23063T,C23664T,A21801C,G10323A,T28282A,A28281T,G28280C,C5388A,A28271-,C21801A,C28977T,C25904T,A28881G,T25780G,T6954C";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C28977T,T28282A,A28281T,G28280C,A28111G,G28048T,C27972T,G24914C,T24506G,C23709T,C23271A,A23063T,T16176C,C15279T,C14676T,T6954C,C5986T,C5388A,C3267T,C913T,C23604A,TACATG21765-,TCTGGTTTT11288-,A28271-,C24795T,T8416C";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C28977T,T28282A,A28281T,G28280C,A28111G,G28048T,C27972T,G24914C,T24506G,C23709T,C23271A,A23063T,T16176C,C15279T,C14676T,T6954C,C5986T,C5388A,C3267T,C913T,C23604A,TACATG21765-,TCTGGTTTT11288-,A28271-,G25135T,A21137G,C6954T";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C23604A,C28977T,T28282A,A28281T,G28280C,A28111G,G28048T,C27972T,G24914C,C23271A,A23063T,T16176C,C15279T,C14676T,T6954C,C5986T,T24506G,C23709T,C5388A,C3267T,C913T,TACATG21765-,TCTGGTTTT11288-,A28271-,C2453T,C29743T,C28770T,C22712T,C18877T";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,A10323G,TCTGGTTTT11288-,A23063T,C23664T,C28977T,T28282A,A28281T,G28280C,A28271-,C23709T,A17615G,A2692T,TACATG21765-,C5388A,C27972T,C25904T,G17615A,G10323A,T2692A,A28111G,T23664C,T6954C,T25904C,C23271A,C913T,G24914C,G28111A,C5986T,G29734C,G28368A,T27972C,G25947T,A25253G,C24914G,T23709C,T23063A,C22591T,TTA21991-,C20759T,A20268G,C20032T,C19386T,G18028T,G15598A,C14697T,G14055C,G12988T,-11288TCTGGTTTT,C8802T,C8047T,C1469T";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C23604A,T24506G,C23709T,C5388A,C3267T,G28048T,G24914C,T6954C,C5986T,C913T,A28111G,C23271A,T16176C,C15279T,C14676T,C28977T,T28282A,A28281T,G28280C,C27972T,A23063T,T...G21765-,T...T11288-,A28271-,T15096C,A28095T,C5944T,G28884C,-21991TTA,C643T,C25603T,A6683G";
//		String string = "C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C23604A,T24506G,C23709T,C5388A,C3267T,G28048T,G24914C,T6954C,C5986T,C913T,A28111G,C23271A,T16176C,C15279T,C14676T,C28977T,T28282A,A28281T,G28280C,C27972T,A23063T,TACATG21765-,TCTGGTTTT11288-,A28271-,C2453T,G210T,C27575T,T9454C,C4893T,G1274A,C28883G,A28882G,A28881G,A28282T,T28281A,C28280G,-28271A";
		String string = "C3037T,A23403G,C14408T,C241T,C8782T,G29734C,C8874T,T3037C";
		
		String presentWithComma = new BackMutsPresenter().presentWithComma(string);
		
		System.out.println(presentWithComma);
	}
}
