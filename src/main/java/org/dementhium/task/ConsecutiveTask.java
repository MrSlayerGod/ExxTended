package org.dementhium.task;



/**
 * A task which executes a group of tasks in a guaranteed sequence.
 * @author Graham Edgecombe
 *
 */
public class ConsecutiveTask implements Task {

	/**
	 * The tasks.
	 */
	private final Task[] tasks;
	
	/**
	 * Creates the consecutive task.
	 * @param tasks The child tasks to execute.
	 */
	public ConsecutiveTask(Task... tasks) {
		this.tasks = tasks;
	}
	

	@Override
	public void execute() {
		for (Task task : tasks) {
			task.execute();
		}
	}

}
