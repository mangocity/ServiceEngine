/**
 * 
 */
package com.mangocity.mbr.controller.expand;

import org.apache.log4j.Logger;

import com.mangocity.ce.bean.EngineBean;
import com.mangocity.ce.exception.ExceptionAbstract;
import com.mangocity.ce.util.CommonUtils;
import com.mangocity.mbr.book.ErrorCode;
import com.mangocity.mbr.controller.mbr.MbrCommService;
import com.mangocity.mbr.util.ErrorUtils;
import com.mangocity.mbr.util.ServerCall;

/**
 * @Package com.mangocity.mbr.controller.expand
 * @Description : 用户设备服务
 * @author YangJie
 * @email <a href='yangjie_software@163.com'>yangjie</a>
 * @date 2015-12-4
 */
public class UserDeviceService {
	private static final Logger log = Logger.getLogger(UserDeviceService.class);

	/**
	 * 添加用户设备
	 * 
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean addUserDevice(EngineBean pb) throws ExceptionAbstract {
		log.info("UserDeviceService addUserDevice begin()...params: " + pb.getHeadMap());
		String deviceId = (String) pb.getHead("deviceId");
		if (CommonUtils.isBlank(deviceId)) {
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID, "deviceId为必填选项");
		}
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean;
	}

	/**
	 * 更新用户设备
	 * 
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean updateUserDevice(EngineBean pb) throws ExceptionAbstract {
		log.info("UserDeviceService updateUserDevice begin()...params: " + pb.getHeadMap());
		EngineBean resultEngineBean = ServerCall.call(pb);
		String deviceId = (String) pb.getHead("deviceId");
		if (CommonUtils.isBlank(deviceId)) {
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID, "deviceId为必填选项");
		}
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean;
	}
	
	/**
	 * 查询用户设备信息
	 * 
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean queryUserDeviceByUserId(EngineBean pb) throws ExceptionAbstract {
		log.info("UserDeviceService queryUserDeviceByUserId begin()...params: " + pb.getHeadMap());
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean;
	}
}
