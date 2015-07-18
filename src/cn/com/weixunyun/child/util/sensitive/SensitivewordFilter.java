package cn.com.weixunyun.child.util.sensitive;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.weixunyun.child.model.bean.Sensitive;
import cn.com.weixunyun.child.model.dao.SensitiveMapper;
import cn.com.weixunyun.child.model.service.ServiceFactory;

/***
 * 
 * @author Administrator
 *
 *
 *关键字树占用内存是以256的平方增长的 
	第一层 256 
	第二层 256 * 256 
	第三层 256 * 256 * 256 
	。。。 
	内存的开销是很大的 
	所以最好设定关键词的最大长度，以控制内存的开销 
	
 *测试结果 
	加载了16000个关键词，短的一个汉字 最长的有20个汉字的  
	共占用内存 190M吧 
	
	速度： 
	从数据库中取了 5w 条信息 需要10s左右 
	10w条信息 16s左右
 */
public class SensitivewordFilter {
	@SuppressWarnings("rawtypes")
	private static Map sensitiveWordMap = null;
	public static int minMatchTYpe = 1;      //最小匹配规则
	public static int maxMatchType = 2;      //最大匹配规则
	
	private final static  Logger logger = LoggerFactory.getLogger(SensitivewordFilter.class);
	/**
	 * 构造函数，初始化敏感词库
	 */
	static {
		
		refreshSensitiveWordMap();
	}
	
	/**
	 * 初始化敏感词库
	 */
	public static String refreshSensitiveWordMap(){
		long beginTime = System.currentTimeMillis();
		sensitiveWordMap = new SensitiveWordInit().initKeyWord();
		long endTime = System.currentTimeMillis();
		String n ="耗时："+(endTime - beginTime)+"毫秒";
		logger.debug("=========初始化敏感词库结束，耗时："+(endTime - beginTime)+"毫秒");
		return n;
	}
	
	/**
	 * 判断文字是否包含敏感字符
	 * @author chenming 
	 * @date 2014年4月20日 下午4:28:30
	 * @param txt  文字
	 * @param matchType  匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return 若包含返回true，否则返回false
	 * @version 1.0
	 */
	public static boolean isContaintSensitiveWord(String txt,int matchType){
		boolean flag = false;
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = SensitivewordFilter.CheckSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
			if(matchFlag > 0){    //大于0存在，返回true
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获取文字中的敏感词
	 * @author chenming 
	 * @date 2014年4月20日 下午5:10:52
	 * @param txt 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 1.0
	 */
	public static Set<String> getSensitiveWord(String txt , int matchType){
		Set<String> sensitiveWordList = new HashSet<String>();
		
		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}
		
		return sensitiveWordList;
	}
	
	/**
	 * 替换敏感字字符
	 * @author chenming 
	 * @date 2014年4月20日 下午5:12:07
	 * @param txt
	 * @param matchType
	 * @param replaceChar 替换字符，默认*
	 * @version 1.0
	 */
	public static String replaceSensitiveWord(String txt,int matchType,String replaceChar){
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}
		
		return resultTxt;
	}
	
	/**
	 * 替换敏感内容
	 * @param txt 传入内容
	 * @return  返回的替换值
	 */
	public static String replaceSensitiveWord(String txt){
//		U+1F604
		try {
//			String emoji = new String("\ue415".getBytes("utf-8"),"utf-8");
			String emoji = new String("☺".getBytes("utf-8"),"utf-8");
			return replaceSensitiveWord( txt,1, emoji);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		return replaceSensitiveWord( txt,1, "\\\\"+"ue415");
		return null;
	}
	
	
	/**
	 * 获取替换字符串
	 * @author chenming 
	 * @date 2014年4月20日 下午5:21:19
	 * @param replaceChar
	 * @param length
	 * @return
	 * @version 1.0
	 */
	private static String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
/*		
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
	*/	
		return resultReplace;
	}
	
	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @author chenming 
	 * @date 2014年4月20日 下午4:31:03
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes"})
	public static int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0;     //匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchFlag++;     //找到相应key，匹配标识+1 
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true   
					if(SensitivewordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else{     //不存在，直接返回
				break;
			}
		}
		if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词 
			matchFlag = 0;
		}
		return matchFlag;
	}
	
	public static void main(String[] args) throws SQLException {
//		SensitivewordFilter filter = new SensitivewordFilter();
		System.out.println("敏感词的数量：" + SensitivewordFilter.sensitiveWordMap.size());
		String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
						+ "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
						+ "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。@@@@@";
		string = "难过就三级片在某一个人的怀里";
		System.out.println("待检测语句字数：" + string.length());
		long beginTime = System.currentTimeMillis();
		Set<String> set = SensitivewordFilter.getSensitiveWord(string, 1);
		
		String rs = SensitivewordFilter.replaceSensitiveWord(string);
		
		System.out.println("替换后结果："+rs);
		System.out.println();
		long endTime = System.currentTimeMillis();
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		System.out.println("总共消耗时间为：" + (endTime - beginTime));
		
//		SqlSession session= ServiceFactory.getSession().openSession();
		
		Sensitive s = new Sensitive();
		s.setName(rs);
//		s.setId((long)111);
//		try {
////			session.getConnection().prepareStatement("insert into sensitive(id,name) values(1111111,'"+rs+"')").execute();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//		}finally{
//			session.getConnection().commit();
//			session.getConnection().close();
//		}
//		session.getMapper(SensitiveMapper.class).insert(s);
	}
}
