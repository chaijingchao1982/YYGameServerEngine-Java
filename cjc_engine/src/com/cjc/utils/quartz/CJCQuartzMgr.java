package com.cjc.utils.quartz;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjc.utils.CJCExceptionUtil;
import com.cjc.utils.string.CJCStringUtil;

/**
 * @author chaijingchao
 * @date 2017-9
 */
@Component
public class CJCQuartzMgr implements InitializingBean {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static final String JOB_NAME = "quartz_job_name_";
	public static final String JOB_GROUP_NAME = "quartz_job_group_name_";
	public static final String TRIGGER_NAME = "quartz_trigger_name_";
	public static final String TRIGGER_GROUP = "quartz_trigger_group_";

	@Autowired
	private Scheduler mScheduler;

	private CJCQuartzMgr() {
	}

	@Override
	public void afterPropertiesSet() {
		//这里不需要了，用spring5.1.1后，qz更准了，而且也不用调mScheduler.start()了
		//		try {
		//			if (!mScheduler.isStarted()) {
		//				mScheduler.start();
		//			}
		//		} catch (SchedulerException e) {
		//			CJCExceptionUtil.log(e);
		//		}
	}

	/** 非spring创建--- */
	public static CJCQuartzMgr createWhitoutSpring() {
		CJCQuartzMgr ret = new CJCQuartzMgr();
		ret.initWhitoutSpring();
		return ret;
	}

	private void initWhitoutSpring() {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			mScheduler = sf.getScheduler();
			mScheduler.start(); //（不确定后面注释说的是否对）如果没这句，startNow第一个事件会走两次，startAt会走不起来
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/** ---非spring创建 */

	public JobDetail createJobDetail(final Class<? extends Job> clazz, final String jobName,
			final String jobGroupName) {
		try {
			JobDetail jd = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroupName).build();
			return jd;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return null;
	}

	public boolean startNowMisfire(final JobDetail job, final String triggerName, final String triggerGroup,
			final String rule, final Date startTime) {
		CronTriggerImpl ct = new CronTriggerImpl();
		ct.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(10))
				// 如果你没有调用 *startAt(..)*方法，那么当前时间（立刻激活）将被赋值。
				.startAt(startTime).build();
		((SimpleTriggerImpl) trigger)
				.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT);

		try {
			/*
			 * SimpleTrigger trigger = (SimpleTrigger)
			 * TriggerBuilder.newTrigger().withIdentity(triggerName,
			 * triggerGroup)
			 * .withSchedule(CronScheduleBuilder.cronSchedule(rule)).startNow().
			 * build(); ((SimpleTriggerImpl) trigger)
			 * .setMisfireInstruction(SimpleTrigger.
			 * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);//
			 * MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT
			 */

			// 把作业和触发器注册到任务调度中
			mScheduler.scheduleJob(job, trigger);
			return true;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	public boolean startNow(String rule, final Class<? extends Job> clazz) {
		final String tag = clazz.getSimpleName();
		final String jobName = CJCStringUtil.append(JOB_NAME, tag);
		final String jobGroupName = CJCStringUtil.append(JOB_GROUP_NAME, tag);
		final String triggerName = CJCStringUtil.append(TRIGGER_NAME, tag);
		final String triggerGroup = CJCStringUtil.append(TRIGGER_GROUP, tag);
		JobDetail job = createJobDetail(clazz, jobName, jobGroupName);
		return startNow(job, triggerName, triggerGroup, rule);
	}

	/**
	 * 马上开始任务
	 * !!!首次调用时间有可能不准，不一定多一次啊，看启动时间点，初始化可能需要2秒，1秒一次，那过了1秒，会补一次（在使用spring5.1.1以后这个问题没了）
	 * @param job
	 * @param triggerName
	 * @param triggerGroup
	 * @param rule
	 * @return
	 */
	private boolean startNow(final JobDetail job, final String triggerName, final String triggerGroup,
			final String rule) {
		try {
			// 定义调度触发规则
			// 使用cornTrigger规则 == 秒 分 小时 日期 月份 星期 年
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
					.withSchedule(CronScheduleBuilder.cronSchedule(rule)).startNow().build();

			// 把作业和触发器注册到任务调度中
			mScheduler.scheduleJob(job, trigger);
			return true;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	/**
	 * 在指定时间开始任务
	 * @param rule
	 * @param clazz
	 * @param startTime
	 * @return
	 */
	public boolean startAt(String rule, final Class<? extends Job> clazz, final Date startTime) {
		final String tag = clazz.getSimpleName();
		final String jobName = CJCStringUtil.append(JOB_NAME, tag);
		final String jobGroupName = CJCStringUtil.append(JOB_GROUP_NAME, tag);
		final String triggerName = CJCStringUtil.append(TRIGGER_NAME, tag);
		final String triggerGroup = CJCStringUtil.append(TRIGGER_GROUP, tag);
		JobDetail job = createJobDetail(clazz, jobName, jobGroupName);
		return startAt(job, triggerName, triggerGroup, rule, startTime);
	}

	/**
	 * 在指定时间开始任务
	 * @param job
	 * @param triggerName
	 * @param triggerGroup
	 * @param rule
	 * @param startTime
	 * @return
	 */
	private boolean startAt(final JobDetail job, final String triggerName, final String triggerGroup, final String rule,
			final Date startTime) {
		try {
			// 定义调度触发规则
			// 使用cornTrigger规则 == 秒 分 小时 日期 月份 星期 年
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
					.withSchedule(CronScheduleBuilder.cronSchedule(rule)).startAt(startTime).build();

			// 把作业和触发器注册到任务调度中
			mScheduler.scheduleJob(job, trigger);
			return true;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	public boolean isExists(JobKey key) {
		try {
			return mScheduler.checkExists(key);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	public boolean deleteJob(JobKey jobKey) {
		try {
			return mScheduler.deleteJob(jobKey);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	public static String makeRule(int second, int minute, int hour, int day, int month, int year) {
		return CJCStringUtil.append(Integer.toString(second), " ", Integer.toString(minute), " ",
				Integer.toString(hour), " ", Integer.toString(day), " ", Integer.toString(month), " ? ",
				Integer.toString(year));
	}
}