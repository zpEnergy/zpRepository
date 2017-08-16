package com.zpinfo.wechat.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理工具类 Created by zpinfo on 2017/08/15.
 */
public class MessageHandlerUtil {

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return Map
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		System.out.println("MessageHandlerUtil.parseXml：获取输入流");
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		System.out.println("MessageHandlerUtil.parseXml：遍历所有子节点");
		for (Element e : elementList) {
			System.out.println(e.getName() + "|" + e.getText());
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	/**
	 * 根据消息类型 构造返回消息
	 * 
	 * @param map
	 * @return String
	 */
	//
	public static String buildXml(Map<String, String> map) {
		String result;
		String msgType = map.get("MsgType").toString();
		System.out.println("MessageHandlerUtil.buildXml:MsgType:" + msgType);
		String content = map.get("Content").toString();
		System.out.println("MessageHandlerUtil.buildXml:Content:" + content);
		if (msgType.toUpperCase().equals("TEXT")) {
			result = buildTextMessage(map, "朝蓬信息欢迎您!" + "您刚才发送过来的消息是：\r\n"
					+ content);
		} else {
			// 发送方帐号
			String fromUserName = map.get("FromUserName");
			System.out.println("MessageHandlerUtil.buildXml:fromUserName"
					+ fromUserName);
			// 开发者微信号
			String toUserName = map.get("ToUserName");
			System.out.println("MessageHandlerUtil.buildXml:toUserName"
					+ toUserName);
			result = String.format("<xml>"
					+ "<ToUserName><![CDATA[%s]]></ToUserName>"
					+ "<FromUserName><![CDATA[%s]]></FromUserName>"
					+ "<CreateTime>%s</CreateTime>"
					+ "<MsgType><![CDATA[text]]></MsgType>"
					+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
					fromUserName, toUserName, getUtcTime(),
					"请回复如下关键词：\n文本\n图片\n语音\n视频\n音乐\n图文");
		}
		System.out.println("MessageHandlerUtil.buildXml:result" + result);
		return result;
	}

	/**
	 * 构造文本消息
	 * 
	 * @param map
	 * @param content
	 * @return String
	 */
	private static String buildTextMessage(Map<String, String> map,
			String content) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		System.out.println("MessageHandlerUtil.buildTextMessage:fromUserName:"
				+ fromUserName);
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		System.out.println("MessageHandlerUtil.buildTextMessage:toUserName:"
				+ toUserName);
		/**
		 * 文本消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime>
		 * <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[this is a
		 * test]]></Content> <MsgId>1234567890123456</MsgId> </xml>
		 */
		return String.format("<xml>"
				+ "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[%s]]></Content>" + "</xml>", fromUserName,
				toUserName, getUtcTime(), content);
	}

	/**
	 * 获取UTC格式的时间
	 * 
	 * @param
	 * @return String
	 */
	private static String getUtcTime() {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
		String nowTime = df.format(dt);
		long dd = (long) 0;
		try {
			dd = df.parse(nowTime).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(dd);
	}
}