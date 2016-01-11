package bpmn.task.service.java;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class JavaTask01 implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("execution ID: " + execution.getId());
		String content = (String)execution.getVariable("content");
		System.out.println("received content: " + content);
		execution.setVariable("executionTime", new Date());
		System.out.println(this.someLogic());
	}
	public String someLogic(){
		return "\nSome User defined logic.\n";
	}
}
