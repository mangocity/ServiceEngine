/**
 * 
 */
package com.mangocity.mbr.controller.mbr;

import java.text.ParseException;

import org.apache.log4j.Logger;

import com.mangocity.ce.bean.EngineBean;
import com.mangocity.ce.book.SysBook;
import com.mangocity.ce.exception.ExceptionAbstract;
import com.mangocity.ce.util.AssertUtils;
import com.mangocity.ce.util.CommonUtils;
import com.mangocity.mbr.book.ErrorCode;
import com.mangocity.mbr.controller.mbrship.MbrShipService;
import com.mangocity.mbr.controller.point.PointService;
import com.mangocity.mbr.factory.ApplicationContext;
import com.mangocity.mbr.factory.BeanFactory;
import com.mangocity.mbr.util.ErrorUtils;
import com.mangocity.mbr.util.RegexUtils;
import com.mangocity.mbr.util.ServerCall;

/**   
 * @Package com.mangocity.mbr.controller.mbr 
 * @Description :  通用会员服务
 * @author YangJie
 * @email <a href='yangjie_software@163.com'>yangjie</a>
 * @date 2015-11-3
 */
public class MbrCommService {
	private static final Logger log = Logger.getLogger(MbrCommService.class);
	
	/**工厂实例*/
	private BeanFactory beanFactory = new ApplicationContext();
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	/**
	 * 根据mbrId查询绑定手机号
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean queryMobileNoByMbrId(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService queryMobileNoByMbrId begin()...params: " + pb.getHeadMap());
		Long mbrId = CommonUtils.objectToLong(pb.getHead("mbrId"), -1L);
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean ;
	}
	
	/**
	 * 根据loginName查询绑定手机号
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean queryMobileNoByLoginName(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService queryMobileNoByLoginName begin()...params: " + pb.getHeadMap());
		MbrService mbrService = beanFactory.getBean("MbrService", MbrService.class);
		pb.setCommand("queryRegisterByLoginNameAndPassword");
		EngineBean eb = mbrService.queryRegisterByLoginNameAndPassword(pb);
		Long mbrId = CommonUtils.objectToLong(eb.getBody("mbrId"), -1L);
		if(!"00000".equals(eb.getResultCode()) || mbrId == -1L){
			return eb;
		}
		pb.setHead("mbrId", mbrId);
		pb.setCommand("queryMobileNoByMbrId");
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean ;
	}
	
	/**
	 * 修改会员信息
	 * {"countryCd":"国家编码","mbrNetName":"网络用名","familyName":"中文姓","name":"中文名","firstName":"英文名",
	 * "middleName":"英文中间名","lastName":"英文姓","gender":"姓别","birthday":"生日","position":"职位","certNo":"","certType":""}
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean updateMbrInfo(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService updateMbrInfo begin()...params: " + pb.getHeadMap());
		String certType = (String) pb.getHead("certType");
		String certNo = (String) pb.getHead("certNo");
		String birthday = (String) pb.getHead("birthday");
		String gender = (String) pb.getHead("gender");
		if(CommonUtils.isNotBlank(certType) && CommonUtils.isBlank(certNo)){
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID, "证件号必填");
		}
		if(CommonUtils.isNotBlank(certNo) && CommonUtils.isBlank(certType)){
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID, "证件类型必填");
		}
		
		if(CommonUtils.isNotBlank(birthday)){
			try {
				pb.setHead("birthday", CommonUtils.parseDate(birthday, "yyyy-MM-dd"));
			} catch (ParseException e) {
			}
		}
		pb.setHead("gender",CommonUtils.isBlank(gender)?"99":gender);
		
		if("11".equals(certType)){//这里只校验了身份证
			if(!RegexUtils.isValidIDCard(certNo)){
				return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID, "身份证不合法");
			}
		}
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean.getBodyMap());
		return resultEngineBean ;
	}
	
	/**
	 * 修改用户的Attribute属性，用户芒果网和万里通积分兑换授权的时候使用
	 * @param headMap
	 * @return
	 */
	public EngineBean updateMbrAttribute(EngineBean pb) throws ExceptionAbstract{
		log.info("DeliverAddressService createAddress begin()...");
		AssertUtils.assertNull(pb, "EngineBean can't be null.");
		Long mbrId = CommonUtils.objectToLong(pb.getHead("mbrId"), -1L);
		String attribute=(String)pb.getHead("attribute");
		if (-1L == mbrId) {
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID,
					"mbrId不能为空或非法数字");
		}
		if (CommonUtils.isBlank(attribute)) {
			return ErrorUtils.error(pb, ErrorCode.ERROR_REQUEST_PARAM_INVALID,
					"attribute不能为空");
		}
		EngineBean resultEngineBean = ServerCall.call(pb);
		return resultEngineBean;
	}
	
	/**
	 * 根据mbrId查询会员信息
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean queryMbrByMbrId(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService queryMbrByMbrId begin()...params: " + pb.getHeadMap());
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean ;
	}
	
	
	/**
	 * 根据会籍编码查询会员信息
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean queryMbrInfoByMbrshipCd(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService queryMbrInfoByMbrshipCd begin()...params: " + pb.getHeadMap());
		//step 1: 根据会籍编码查询mbrId
		pb.setCommand("queryMbrShipByMbrshipCd");
		MbrShipService mbrShipService = beanFactory.getBean("MbrShipService", MbrShipService.class);
		EngineBean resultEngineBean = mbrShipService.queryMbrShipByMbrshipCd(pb);
		if(!SysBook.SUCCESS.equals(resultEngineBean.getResultCode())){
			return resultEngineBean;
		}
		
		//step 2: 根据mbrId查询会员信息
		Long mbrId = CommonUtils.objectToLong( resultEngineBean.getBody("MBR_ID"), -1L);
		pb.setHead("mbrId", mbrId);
		pb.setCommand("getPersonInfo");
		resultEngineBean = ServerCall.call(pb);
		
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean ;
	}
	
	/**
	 * 是否为集团会员
	 * @param pb
	 * @return
	 * @throws ExceptionAbstract
	 */
	public EngineBean isCrmMbr(EngineBean pb) throws ExceptionAbstract {
		log.info("MbrCommService isCrmMbr begin()...params: " + pb.getHeadMap());
		EngineBean resultEngineBean = ServerCall.call(pb);
		log.info("resultEngineBean: " + resultEngineBean);
		return resultEngineBean ;
	}
}
