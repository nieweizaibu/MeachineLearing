import java.text.DecimalFormat;
/**
 * 按照设定格式输出小数                   2016/7/25
 * @author nie
 *
 */
class OutputFormat {
	private static DecimalFormat fmt = new DecimalFormat("0.00000000 ");

	public static String formatOut(double x) {
		return fmt.format(x);
	}
}

