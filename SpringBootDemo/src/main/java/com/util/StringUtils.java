package com.util;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

public static final char DEFAULT_DELIMITER = ',';
	
	public static boolean checkNull(Object obj){
		return obj == null;
	}
	
	public static boolean checkNull(String a){
		return a == null || a.trim().length() == 0;
	}
	
	/**
	 * 判断字符串去掉首尾是否为空
	 * @param str
	 * @return 空返回true
	 */
	public static boolean isStrTrimNull(final String str) {
		return str == null || str.length() == 0 || str.trim().length() == 0
				|| str.equalsIgnoreCase("null");
	}

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return 空返回true
	 */
	public static boolean isNull(final String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 * @return 空返回true
	 */
	public static  boolean isNotNull(final String str) {
		return !isNull(str);
	}

	/**
	 * 判断字符串是否不为为空
	 * 
	 * @param str
	 * @return 空返回true
	 */
	public static  boolean isNotStrTrimNull(final String str) {
		return !isStrTrimNull(str);
	}

	/**
	 * 判断字段串 s是否在字段串strList中
	 * 
	 * @param strList
	 *            可理解为源字符串
	 * @param s
	 *            想要在源字符串中查找的字符串
	 * @return 如果存在返回true,如果不存在返回false
	 */
	public static  boolean isStrIn(String strList, String s) {
		return isStrIn(strList, s, ',');
	}

	/**
	 * 判断字段串 s是否在字段串strList中
	 * 
	 * @param strList
	 *            可理解为源字符串
	 * @param s
	 *            想要在源字符串中查找的字符串
	 * @param delimiter
	 *            分隔符
	 * @return 如果存在返回true,如果不存在返回false
	 */
	public static boolean isStrIn(String strList, String s, char delimiter) {
		return indexOf(strList, delimiter, s, false, false) >= 0;
	}
	
	/**
	 * 返回子串在源字符串中的索引，如果找到则返回非负的索引，否则返回-1
	 * @param str 源字符串
	 * @param delimiter 分隔符
	 * @param sub 子串
	 * @param trim 删除前后导空白字符
	 * @param ignoreCase 忽略大小写
	 * @return
	 */
	public static final int indexOf(String str, char delimiter, String sub, boolean trim, boolean ignoreCase)
	{
		if (str == null || sub == null)
			return -1;
		int p0 = 0;
		final int n = str.length();
		final int cmpLen = sub.length();
		int j = 0;
		for (int i = 0; i <= n; i++)
		{
			if (i == n || str.charAt(i) == delimiter)
			{
				if (trim)
				{
					if (ignoreCase ? str.substring(p0, i).trim().equalsIgnoreCase(sub) : str.substring(p0, i).trim().equals(sub))
						return j;
				} else
				{
					if (cmpLen == i - p0 && str.regionMatches(ignoreCase, p0, sub, 0, cmpLen))
						return j;
				}
				p0 = i + 1;
				j++;
			}
		}
		return -1;
	}
	
	/**
	* @Title: indexOf
	* @Description: 查找子串在在字符串数组的位置
	* @param @param a 原字符串
	* @param @param s 目标子串
	* @return int    
	 */
	public static  int indexOf(String a[],String s)
    {
        if( a==null )
            return -1;
        for(int i=0;i<a.length;i++)
        {
            if( s==a[i] || (s!=null && s.equals(a[i])) )
                return i;
        }
        return -1;
    }

	/**
	* @title: splitStringByRegex
	* @description: 根据正则表达式分割字符串
	* @param str 需要分割的字符串
	* @param regex 正则表达式
	* @return
	 */
	public static final String[] splitStringByRegex(String str,String regex){
		if (str == null)
			return null;
		if(isStrTrimNull(regex)){
			return new String[]{str};
		}
		return str.split(regex);
	}
	/**
	* @Title: splitString
	* @Description: 指定字符串的起始位置开始分割字符串
	* @param @param str
	* @param @param istart 需要分割的开始索引
	* @param @param delimiter 分割字符
	* @return String[]  字符串数组
	 */
	public static final String[] splitString(String str, int istart,char delimiter) {
		if (str == null)
			return null;
		int sl = str.length();
		int n = 0;
		for (int i = istart; i < sl; i++){
			if (str.charAt(i) == delimiter)
				n++;
		}
		String[] sa = new String[n + 1];
		int i = istart, j = 0;
		for (; i < sl;) {
			int iend = str.indexOf(delimiter, i);
			if (iend < 0)
				break;
			sa[j++] = str.substring(i, iend);
			i = iend + 1;
		}
		sa[j++] = str.substring(i);
		return sa;
	}

	/**
	* @Title: splitString
	* @Description: 
	* @param @param str
	* @param @param delimiter
	* @param @return    
	* @return String[]    字符串数组
	* @throws
	 */
	public static final String[] splitString(String str, char delimiter) {
		if (delimiter == 0)
			return str == null ? null : new String[] { str };
		return splitString(str, 0, delimiter);
	}

	/**
	* @title: splitNumber
	* @description:使用默认的分隔符分割金额 
	* @param srcStr
	* @return
	 */
	public static final String splitNumber(final String srcStr){
		return splitNumber(srcStr,DEFAULT_DELIMITER);
	}
	/**
	* @title: splitNumber
	* @description: 
	* @param src  需要分割的数据字符串
	* @param delimiter 分割符
	* @return
	 */
	public static final String splitNumber(final String srcStr,char delimiter){
		if (isStrTrimNull(srcStr))
			return null;
		int dot = srcStr.indexOf(".");
		boolean hasdot = dot>0?true:false;
		String integer_Part = hasdot?srcStr.substring(0,dot):srcStr;
		StringBuilder sb = new StringBuilder(integer_Part);
		int len = integer_Part.length();
		if(len>3){
			integer_Part = sb.reverse().toString();
			sb = new StringBuilder();
			int lastIndex = 0;
			for(int i=0 ; i<len;i=i+3){
				//1111000000
				lastIndex = i+3;
				if(lastIndex>=len){
					sb.append(integer_Part.subSequence(i, len));
					break;
				}
				sb.append(integer_Part.subSequence(i, lastIndex));
				sb.append(delimiter);
			}
			sb = sb.reverse();
		}
		if(hasdot){
			//有小数部分
			sb.append(".");
			String srcdotSubStr = srcStr.substring(dot+1, srcStr.length());
			int dotsublen = srcdotSubStr.length();
			int endIndex = 0;
			for(int d=0;d<dotsublen;d=d+3){
				endIndex = d+3;
				if(endIndex>=dotsublen){
					sb.append(srcdotSubStr.subSequence(d, dotsublen));
					break;
				}
				sb.append(srcdotSubStr.subSequence(d, endIndex));
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}


	/**
	* @Title: removeExistString
	* @Description: 去掉不需要的字符串
	* @param	resource 源字符串
	* @param ext 需要移除的字符串
	* @return String[]   移除后的字符串数据
	* @throws
	 */
	public static final String[] removeExistString(String[] resource,String[] ext){
		if(resource  == null ) return null;
		if(ext == null) return resource;
		String result = "";
		String s = toString(ext);
		for(String originalStr : resource){
			if(isNotStrTrimNull(originalStr)){
				String t = originalStr.trim();
				if(isStrIn(s, t)){
					continue;
				}
				result += result.length()>0?","+t:t;
			}
		}
		return splitString(result,',');
	}
	
	
	/**
	 * 注：该功能同subSplitString(String str, char delimiter, int index)
	 * 将一个字符串以某字符作为分隔符进行分隔后取其中某一段.
	 * 示例1：String subtext = Utilities.subString("AAAA,BBBB,CCCC,DDDDD",',',2); 结果：subtext 为 "CCCC"
	 * @param str 被分隔的字符串
	 * @param delimiter 分隔符
	 * @param index 分隔后的子串数组下标
	 * @return 第 index 个子串
	 */
	public static final String subString(String str, char delimiter, int index)
	{
		return subSplitString(str, 0, delimiter, index);
	}
	
	/**
	 * 对字符串进行二次拆分得到一个字符串二维数组
	 * 示例：
	 * String[][] ss = StrUtil.splitString("name=tom;age=18;score=100", ';', '=');
	 * for (String[] s1 : ss)
	 * {
	 * System.out.print(Arrays.toString(s1));
	 * }
	 * 结果：[name, tom][age, 18][score, 100]
	 * @param str 要进行拆分的字符串
	 * @param delimiter1 分隔符1
	 * @param delimiter2 分隔符2
	 * @return 拆分完成的字符串二维数组
	 */
	public static final String[][] splitString(String str, char delimiter1, char delimiter2)
	{
		String[] a1 = splitString(str, delimiter1);
		if (a1 == null)
			return null;
		String a2[][] = new String[a1.length][];
		for (int i = 0; i < a1.length; i++)
		{
			a2[i] = splitString(a1[i], delimiter2);
		}
		return a2;
	}
	
	/**
	 * 将一个字符串以某字符作为分隔符进行分隔后取其中某一段.
	 * 示例1：String subtext = Utilities.splitString("AAAA,BBBB,CCCC,DDDDD",0,',',2); 结果： "CCCC"
	 * @param str 被分隔的字符串
	 * @param istart 开始位置
	 * @param delimiter 分隔符
	 * @param index 分隔后的子串数组下标
	 * @return 第 index 个子串
	 */
	public static final String subSplitString(String str, int istart, char delimiter, int index)
	{
		if (str == null)
			return null;
		int sl = str.length();
		int i = istart, j = 0;
		for (; i < sl;)
		{
			int iend = str.indexOf(delimiter, i);
			if (iend < 0)
				break;
			if (j++ == index)
				return str.substring(i, iend);
			// System.out.println(sa[j-1]);
			i = iend + 1;
		}
		return j == index ? str.substring(i) : null;
	}
	/**
	 * 将一个字符串以某字符作为分隔符进行分隔后取其中某一段.
	 * 示例1：String subtext = StrUtil.splitString("AAAA,BBBB,CCCC,DDDDD",',',2); 结果：subtext 为 "CCCC"
	 * @param str 被分隔的字符串
	 * @param delimiter 分隔符
	 * @param index 分隔后的子串数组下标
	 * @return 第 index 个子串
	 */
	public static final String subSplitString(String str, char delimiter, int index)
	{
		return subSplitString(str, 0, delimiter, index);
	}
	
	/**
	 * 取字符串交集
	 * 
	 * @param srcStr
	 *            源字符串集
	 * @param subStr
	 *            源字符串集
	 * @param del
	 *            分隔符
	 * @return
	 */
	public static String getInterSet(String srcStr, String subStr, char del) {
		if (srcStr == null || subStr == null)
			return null;
		String[] srces = splitString(srcStr, del);
		StringBuilder sb = new StringBuilder();
		for (String src : srces) {
			if (src == null || src.length() == 0)
				continue;
			if (!isStrIn(subStr, src))
				continue;
			if (sb.length() > 0)
				sb.append(del);
			sb.append(src);
		}
		if (sb.length() > 0)
			return sb.toString();
		return null;
	}

	/**
	 * 返回两个字符串的差集【srcStr-subStr】，默认使用逗号分隔. 测试1:
	 * 源：System.err.println(getDiffset("1,2,11,1,,", "1")); 终端：2,11
	 * 
	 * @param srcStr
	 *            源字符串
	 * @param subStr
	 *            被减字符串
	 * @return 差集
	 */
	public static String getDiffset(String srcStr, String subStr) {
		if (srcStr == null || subStr == null)
			return srcStr;
		StringBuilder sb = new StringBuilder();
		for (String src : srcStr.split(",")) {
			if (src.length() == 0 || isStrIn(subStr, src))
				continue;
			if (sb.length() > 0)
				sb.append(',');
			sb.append(src);
		}
		return sb.toString();
	}

	/**
	 * 将集合collection中的所有元素转换成以逗号分隔的字符串返回。
	 * 
	 * @param collection
	 *            想要转换的源数据集合
	 * @return 转化完成的以逗号分隔的字符串
	 */
	public static String toString(Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			return toString(collection.toArray());
		}
		return null;
	}

	/**
	 * 将一维数组vs转换成逗号分隔的字符串返回。
	 * 
	 * @param vs
	 *            数组
	 * @return
	 */
	public static String toString(Object[] vs) {
		return toString(vs, ',');
	}

	/**
	 * 将一维数组vs转换成指定分隔符分隔的字符串返回。 注意：vs中的null会被过滤。
	 * 
	 * @param vs
	 *            一维数组
	 * @return 如果vs为null，或长度为零，或元素全部为空则返回null；否则返回逗号分隔的字符串。
	 */
	public static String toString(Object[] vs, char deli) {
		if (vs != null && vs.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (Object v : vs) {
				if (v == null)
					continue;
				if (sb.length() > 0)
					sb.append(deli);
				sb.append(v);
			}
			if (sb.length() > 0)
				return sb.toString();
		}
		return null;
	}
	
	/**
	* @Title: toLowerCaseFirstOne
	* @Description:首字母转小写 
	* @return String    
	 */
    public static String toLowerCaseFirstOne(String s)
    {
    	if(isNull(s)) 
    		return null;
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    /**
    * @Title: toUpperCaseFirstOne
    * @Description: 首字母转大写
    * @return String    
     */
    public static String toUpperCaseFirstOne(String s)
    {
    	if(isNull(s)) 
    		return null;
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    
	
	/**
	 * 
	* @Title: isChineseChar
	* @Description: 
	* @return boolean    
	 */
	public static boolean isChineseChar(String str)
	{
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
	}
	
	/**
	 * 
	* @Title: isInteger
	* @Description: 判断字符串是否是整数
	* @author ningquan
	* @param str
	* @return boolean    
	 */
	public static boolean isInteger(String str)
	{  
		Pattern pattern = Pattern.compile("(-)?[0-9]+");   
		Matcher matcher = pattern.matcher((CharSequence) str);   
		boolean result = matcher.matches();
		return result;
	}
	
	/**
	 * 判断是否是浮点数
	 * @author ningquan
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str){
		Pattern pattern = Pattern.compile("(-)?[0-9]+.[0-9]+");   
		Matcher matcher = pattern.matcher((CharSequence) str);   
		boolean result = matcher.matches();
		return result;
	}
	
	/**
	 * 判断字符串是否是数字
	 * @author ningquan
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		return isInteger(str) || isDouble(str);
	}

	/**
	 * 检查tokenList中的字段是否包含在content中，默认tokenList分隔符是逗号（,）,可以自己传参数
	 * @param tokenList
	 * @param content
	 * @return
	 */
	public static boolean isContainIn(String tokenList, String content) {
		return isContainIn(tokenList, content, DEFAULT_DELIMITER);
	}
	
	/**
	 * 检查tokenList中的字段是否包含在content中，默认tokenList分隔符是逗号（,）,可以自己传参数
	 * @param tokenList
	 * @param content
	 * @param c
	 * @return
	 */
	public static boolean isContainIn(String tokenList, String content, char c) {
		if(isNull(tokenList) || isNull(content)){
			return false;
		}
		String[] arr_ = null;
		if(tokenList.indexOf(c)>=0){
			arr_ = splitString(tokenList, c);
		}else {
			arr_ = new String[]{tokenList};
		}
		for (String searchK : arr_) {
			if(isStrTrimNull(searchK)){
				continue;
			}
			if(content.indexOf(searchK)>=0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过正则表达式替换字符串
	 * @author ningquan
	 * @param content 需要替换的内容
	 * @param replaceMsgRegex 替换规则
	 * @param index 需要替换的索引值，default为0
	 * @param args 参数值
	 * @return
	 */
	public static String replaceByRegex(String content, String replaceMsgRegex,int index,String ... args) {
		if(isStrTrimNull(content) || args == null || isStrTrimNull(replaceMsgRegex)){
			return content;
		}
		Pattern pattern = Pattern.compile(replaceMsgRegex);
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
			String argIndex = matcher.group(index);
			String val_current = "";
			if(isNotStrTrimNull(argIndex) && isNumber(argIndex)){
				Integer indexOf = Integer.valueOf(argIndex);
				if(args.length<indexOf){
					break;
				}
				val_current = args[indexOf];
			}
			matcher.appendReplacement(sb,val_current);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
}
