package script01;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import bpmn.task.service.java.JavaTask02;


public class Scp01 {
	private static String filename = "D:\\workspace\\eclipse\\process02"
			+"\\src\\main\\resources\\diagrams\\Script01.bpmn";
	public static ProcessEngine getProcessEngine(){
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		return processEngine;
	}
	public static String getContent(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter something:");
		String content = scanner.nextLine();
		scanner.close();
		return content;
	}
	public static void main(String[] args) throws IOException {
		ProcessEngine processEngine = getProcessEngine();// get process engine
		// get process services
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		//deploy
		repositoryService.createDeployment()
			.addInputStream("script01.bpmn20.xml", new FileInputStream(filename))
			.deploy();
		
		String content = getContent();
		Map<String, Object> varibleMap = new HashMap<String, Object>();
		varibleMap.put("content", content);
		varibleMap.put("name", "Private Blockchain");
		JavaTask02 jk = new JavaTask02();
		varibleMap.put("JavaTask02", jk);
		//start the process
		ProcessInstance instance = runtimeService
				.startProcessInstanceByKey("myProcess", varibleMap);
		
		//get varible in java service
		Date executeTime = (Date)runtimeService
				.getVariable(instance.getId(), "executionTime");
		System.out.println("Execution Time: " + executeTime);
		
		String formatTime = (String)runtimeService
				.getVariable(instance.getId(), "formatTime");
		System.out.println("Format Time: " + formatTime);
		System.out.println("processID: " + instance.getId());
		System.out.println("Current task: " + instance.getActivityId());
		
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery()
				.taskDefinitionKey("lignManualTask").list();
		for (Task task : tasks){
			System.out.println("Tasks:\ntaskID - " + task.getId()
			+ " taskName - " + task.getName());
			// claim task
			taskService.claim(task.getId(), "lign");
		}
		// user's claimed task
		tasks = taskService.createTaskQuery().taskAssignee("lign").list();
		System.out.println("Task for lign:");
		for (Task task : tasks){
			System.out.println(task.getName());
			taskService.complete(task.getId());
		}
		
		// check if the process is end
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstance historicProcessInstance =
				historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(instance.getId())
				.singleResult();
		System.out.println("Process instance " + instance.getProcessDefinitionId()
				+ "\nBegin time -- " + historicProcessInstance.getStartTime()
				+ "\nEnd time -- " + historicProcessInstance.getEndTime());
	}
}
