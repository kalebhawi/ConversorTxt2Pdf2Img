package br.com.estudos_conversao.conversor;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SchedulerFactory shedFact = new StdSchedulerFactory();
		try {
			Scheduler scheduler = shedFact.getScheduler();
			scheduler.start();
			JobDetail job = JobBuilder
					.newJob(ValidadorJob.class)
					.withIdentity("validadorJOB", "grupo01")
					.build();
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("validadorTRIGGER", "grupo01")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
					.build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
