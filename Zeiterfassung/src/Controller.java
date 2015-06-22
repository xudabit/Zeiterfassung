
public class Controller {
	private static Controller singleton = null;
	
	public static Controller getController() {
		if(singleton == null)
			singleton = new Controller();
		return singleton;
	}
	
	private Controller(){}
}
