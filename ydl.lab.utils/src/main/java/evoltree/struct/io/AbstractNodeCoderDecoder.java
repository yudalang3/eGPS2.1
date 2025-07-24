package evoltree.struct.io;

import evoltree.struct.EvolNode;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * For the tree coder the codeNode method will be invoked.
 * For the tree decoder the parseNode and createNode methods will be invoked.
 *
 * For the real world utility, the leaf and internal node coder and/or decoder can also be the same class.
 * @author yudalang
 * @param <S>
 */
public abstract class AbstractNodeCoderDecoder<S extends EvolNode> {

	protected static final char COLON = ':';
	protected static final char COMMA = ',';
	NumberFormat numberFormat = NumberFormat.getNumberInstance();

	public AbstractNodeCoderDecoder() {

		numberFormat.setMaximumFractionDigits(6);// 设置保留小数位
		numberFormat.setRoundingMode(RoundingMode.HALF_UP); // 设置舍入模式
	}

	public abstract void parseNode(S node, String str);

	public abstract String codeNode(S node);

	/**
	 * 派生类需要实现这个方法去创建对象，不要用反射，那个太慢了。
	 * 
	 * @return the concrete node
	 */
	public abstract S createNode();

	/**
	 * https://www.baeldung.com/java-double-to-string
	 * 
	 * @param value
	 * @return the string representation of the double value
	 */
	protected String convertRate2Decimal(double value) {
		return numberFormat.format(value);
	}

}
