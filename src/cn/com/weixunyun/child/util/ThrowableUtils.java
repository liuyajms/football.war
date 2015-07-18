/*
 * apage com.xuware.apage.util.ExceptionUtil
 * copyright xuware 2006-2007, all rights reserved.
 * created on 2008-5-22 ???04:18:47
 */
package cn.com.weixunyun.child.util;

import org.xml.sax.SAXException;

/**
 * <p>
 * copyright &copy; xuware 2006-2008, all rights reserved.
 * 
 * @author xu
 * @version $Id: ThrowableUtils.java,v 1.1 2014/08/22 02:05:05 liuxu Exp $
 * @since 1.0
 */
public class ThrowableUtils {
	public static Throwable getRootCause(Throwable t) {
		Throwable cause = null;
		if (t instanceof SAXException) {
			cause = ((SAXException) t).getException();
		} else if (t instanceof java.sql.SQLException) {
			cause = ((java.sql.SQLException) t).getNextException();
		} else {
			cause = t.getCause();
		}
		if (cause == null) {
			return t;
		} else {
			return getRootCause(cause);
		}
	}
}
