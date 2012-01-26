package ai.fsm;

public class FSMTest {
	
	public static int globalNum = 0;
	
	static class MyCondition extends Condition {
		
		public int num;
		
		public MyCondition(int num) {
			this.num = num;
		}

		@Override
		public boolean test() {
			return num == globalNum;
		}
		
	}
	
	static class MyAction extends Action {
		public String actionText;
		
		public MyAction(String actionText) {
			this.actionText = actionText;
		}
		
		@Override
		public void act() {
			System.out.println("Action: " + actionText);
		}
	}
	
	static class MyTransition extends Transition {
		public String transitionText;
		
		@Override
		public Action getActions() {
			return new MyAction(transitionText);
		}
	}
	
	static class MyState extends StateMachineState {
		public String entryText;
		public String exitText;
		public String text;
		
		@Override 
		public Action getEntryActions() {
			return new MyAction(entryText);
		}
		
		@Override
		public Action getActions() {
			return new MyAction(text);
		}
		
		@Override 
		public Action getExitActions() {
			return new MyAction(exitText);
		}
	}
	
	public static void main(String[] args) {
		
		StateMachine sm = new StateMachine();
		
		MyState state1 = new MyState();
		state1.entryText = "enterying state 1";
		state1.text = "in state 1";
		state1.exitText = "exiting state 1";
		
		MyState state2 = new MyState();
		state2.entryText = "enterying state 2";
		state2.text = "in state 2";
		state2.exitText = "exiting state 2";
		
		MyState state3 = new MyState();
		state3.entryText = "enterying state 3";
		state3.text = "in state 3";
		state3.exitText = "exiting state 3";
		
		MyTransition t1 = new MyTransition();
		t1.transitionText = "tran 1";
		t1.next = null;
		t1.condition = new MyCondition(2);
		t1.target = state2;
		
		MyTransition t2 = new MyTransition();
		t2.transitionText = "tran 2";
		t2.next = null;
		t2.condition = new MyCondition(3);
		t2.target = state3;
		
		MyTransition t3 = new MyTransition();
		t3.transitionText = "tran 3";
		t3.next = null;
		t3.condition = new MyCondition(1);
		t3.target = state1;
		
		state1.firstTransition = t1;
		state2.firstTransition = t2;
		state3.firstTransition = t3;
		
		sm.initialState = state1;
		sm.currentState = sm.initialState;
		
		Action actions;
		
		actions = sm.update();
		act(actions);
		
		globalNum = 2;
		
		actions = sm.update();
		act(actions);
		
		actions = sm.update();
		act(actions);
		
		globalNum = 3;
		
		actions = sm.update();
		act(actions);
		
		actions = sm.update();
		act(actions);
		
		globalNum = 1;
		
		actions = sm.update();
		act(actions);
		
		actions = sm.update();
		act(actions);
	}
	
	public static void act(Action actions) {
		while (actions != null) {
			actions.act();
			actions = actions.next;
		}
	}

}
