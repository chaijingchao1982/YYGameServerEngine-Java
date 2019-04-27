package com.cjc.frame.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * @Description: Qz注入工厂（给qz job进行注入）
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCQzJobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory mBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		mBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}